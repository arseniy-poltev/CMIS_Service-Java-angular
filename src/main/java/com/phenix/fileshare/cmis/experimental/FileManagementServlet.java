package com.phenix.fileshare.cmis.experimental;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.FileableCmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.runtime.OperationContextImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisNameConstraintViolationException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phenix.fileshare.cmis.server.FileShareCMISServiceFactory;
import com.phenix.fileshare.cmis.utils.CMISException;
import com.phenix.fileshare.cmis.utils.CMISHelper;
import com.phenix.fileshare.cmis.utils.GlobalConstants;
import com.phenix.fileshare.cmis.utils.TikaProperties;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class FileManagementServlet extends BaseServlet {


    private static final String PARAM_PARENT_PATH = "parentpath";
    private static final String PARAM_PARENT_ID = "parentid";
    private static final String PARAM_TYPE_ID = "typeid";

    private String DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss z"; // (Wed, 4 Jul 2001 12:08:56)
    private static final Logger LOG = LoggerFactory.getLogger(FileManagementServlet.class);

    private Map<Mode, Boolean> enabledAction = null;
    private FileShareCMISServiceFactory factory;
    private static final OperationContext BROWSE_OPERATION_CONTEXT = new OperationContextImpl();
    enum Mode {
        list, rename, move, copy, remove, edit, getContent, createFolder, changePermissions, compress, extract, upload
    }
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setAccessControlHeaders(req, resp);
    }
    @Override
    public void init() throws ServletException {
        super.init();

        if (getInitParameter("enabled.action") == null) {
            enabledAction = java.util.Collections.EMPTY_MAP;
        } else {
            final String enabledActions = getInitParameter("enabled.action").toLowerCase();
            Pattern movePattern = Pattern.compile("\\bmove\\b");
            enabledAction = new HashMap<Mode, Boolean>();
            enabledAction.put(Mode.rename, enabledActions.contains("rename"));
            enabledAction.put(Mode.move, movePattern.matcher(enabledActions).find());
            enabledAction.put(Mode.remove, enabledActions.contains("remove"));
            enabledAction.put(Mode.edit, enabledActions.contains("edit"));
            enabledAction.put(Mode.getContent, enabledActions.contains("getcontent"));
            enabledAction.put(Mode.createFolder, enabledActions.contains("createfolder"));
            enabledAction.put(Mode.changePermissions, enabledActions.contains("changepermissions"));
            enabledAction.put(Mode.compress, enabledActions.contains("compress"));
            enabledAction.put(Mode.extract, enabledActions.contains("extract"));
            enabledAction.put(Mode.copy, enabledActions.contains("copy"));
            enabledAction.put(Mode.upload, enabledActions.contains("upload"));
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setAccessControlHeaders(request, response);
        factory = (FileShareCMISServiceFactory) getServletContext().getAttribute(GlobalConstants.SERVICES_FACTORY);
        if(factory == null || factory.getThreadLocalService() == null){
            setError(GlobalConstants.MSG_CMIS_SERVER_NOT_STARTED,response);
            return;
        }
        //************test login CMIS server***************
        Session cmisSession = getOpenCMISSession(request);
        if(cmisSession == null){
            setError(GlobalConstants.FILE_MANAGER_MSG_1,response);
            return;
        }

        //*************************************************
        try {
            // if request contains multipart-form-data
            if (ServletFileUpload.isMultipartContent(request)) {
                if (isSupportFeature(Mode.upload)) {
                    uploadFile(request, response);
                } else {
                    setError(new IllegalAccessError(notSupportFeature(Mode.upload).getAsString("error")), response);
                }
            } // all other post request has jspn params in body
            else {
                fileOperation(request, response);
            }
        } catch(CmisNameConstraintViolationException e){
            setError("Duplicate file!",response);
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
            setError(ex, response);
        } catch (CMISException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        setAccessControlHeaders(request, response);
        factory = (FileShareCMISServiceFactory) getServletContext().getAttribute(GlobalConstants.SERVICES_FACTORY);
        if(factory == null || factory.getThreadLocalService() == null){
            setError(GlobalConstants.MSG_CMIS_SERVER_NOT_STARTED,response);
            return;
        }

        //************test login CMIS server***************
        Session cmisSession = getOpenCMISSession(request);
        if(cmisSession == null){
            setError(GlobalConstants.FILE_MANAGER_MSG_1,response);
            return;
        }
        String action = request.getParameter("action");
        if ("download".equals(action)) {
            String path = request.getParameter("path");
            // fetch the document object
            CmisObject cmisObject = null;
            try {
                cmisObject = cmisSession.getObjectByPath(path,
                        CMISHelper.LIGHT_OPERATION_CONTEXT);
            } catch (CmisObjectNotFoundException onfe) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Document not found!");
                return;
            } catch (CmisBaseException cbe) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Error: " + cbe.getMessage());
                return;
            }

            if (!(cmisObject instanceof Document)) {
                // object is not a document -> no content
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Object is not a document!");
                return;
            }

            Document doc = (Document) cmisObject;
            ContentStream contentStream = doc.getContentStream();

            if (contentStream == null) {
                // document has no content
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Document has no content!");
                return;
            }

            InputStream in = contentStream.getStream();

            try {
                // set MIME type
                String mimeType = contentStream.getMimeType();
                if (mimeType == null || mimeType.length() == 0) {
                    // if the repository didn't send a MIME type,
                    // use a generic one
                    mimeType = "application/octet-stream";
                }

                response.setContentType(mimeType);

                // if the 'save' parameter is set, ask the browser to open a
                // download dialog by setting a Content-Disposition attachment
                // header
                //if (Boolean.parseBoolean(save)) {
                if(true){
                    // set filename
                    String filename = contentStream.getFileName();
                    if (filename == null) {
                        // if the repository didn't send a filename, use the
                        // document name
                        filename = doc.getName();
                    }

                    // !!! A real application should implement RFC 2231 !!!
                    // this is good enough for a demo
                    response.setHeader("Content-Disposition",
                            "attachment; filename=\"" + filename + "\"");
                } else {
                    response.setHeader("Content-Disposition", "inline");
                }

                // push out content
                OutputStream out = response.getOutputStream();


                byte[] buffer = new byte[64 * 1024];
                int b;
                while ((b = in.read(buffer)) > -1) {
                    out.write(buffer, 0, b);
                }

                out.flush();
            } finally {
                // VERY IMPORTANT:
                // always close the content stream
                in.close();
            }
        }
    }

    private void fileOperation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject responseJsonObject = null;
        Session cmisSession = getOpenCMISSession(request);
        if(cmisSession == null){
            setError(GlobalConstants.FILE_MANAGER_MSG_1,response);
            return;
        }

        try {
            // legge il parametro json
            StringBuilder sb = new StringBuilder();
            BufferedReader br = request.getReader();
            //try {
                String str;
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
            //}
            JSONObject params = JSONValue.parse(sb.toString(), JSONObject.class);
            // legge mode e chiama il metodo aapropriato
            Mode mode = Mode.valueOf(params.getAsString("action"));
            switch (mode) {
                case createFolder:
                    responseJsonObject = isSupportFeature(mode) ? createFolder(cmisSession, params) : notSupportFeature(mode);
                    break;
                case changePermissions:
                    //responseJsonObject = isSupportFeature(mode) ? changePermissions(params) : notSupportFeature(mode);
                    break;
                case compress:
                    //responseJsonObject = isSupportFeature(mode) ? compress(params) : notSupportFeature(mode);
                    break;
                case copy:
                    responseJsonObject = isSupportFeature(mode) ? copy(cmisSession, params) : notSupportFeature(mode);
                    break;
                case remove:
                    responseJsonObject = isSupportFeature(mode) ? remove(cmisSession, params) : notSupportFeature(mode);
                    break;
                case getContent:
                    responseJsonObject = isSupportFeature(mode) ? getContent(cmisSession, params) : notSupportFeature(mode);
                    break;
                case edit:
                    responseJsonObject = isSupportFeature(mode) ? editContent(cmisSession, params) : notSupportFeature(mode);
                    break;
                case extract:
                    //responseJsonObject = isSupportFeature(mode) ? extract(params) : notSupportFeature(mode);
                    break;
                case list:
                    responseJsonObject = list(cmisSession, params);
                    break;
                case rename:
                    responseJsonObject = isSupportFeature(mode) ? rename(cmisSession, params) : notSupportFeature(mode);
                    break;
                case move:
                    responseJsonObject = isSupportFeature(mode) ? move(cmisSession, params) : notSupportFeature(mode);
                    break;
                default:
                    throw new ServletException("not implemented");
            }
            if (responseJsonObject == null) {
                responseJsonObject = error("generic error : responseJsonObject is null");
            }
        } catch (ServletException e) {
            responseJsonObject = error(e.getMessage());
        }catch(IOException e){

        }
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(responseJsonObject);
        out.flush();
    }
    private JSONObject editContent(Session cmisSession, JSONObject params) throws ServletException {
        String path = params.getAsString("item");
        LOG.debug("edit content from: {}", path);
        CmisObject cmisObject = null;
        try {
            cmisObject = cmisSession.getObjectByPath(path,
                    CMISHelper.LIGHT_OPERATION_CONTEXT);
            if (!(cmisObject instanceof Document)) {
                return error("Object is not a document!");
            }
            String content = params.getAsString("content");

            Document doc = (Document) cmisObject;
            ContentStream contentStream = doc.getContentStream();


            if (contentStream == null) {
                return error("Content stream null");
            }
            ContentStream newStream = prepareContentStream(cmisSession,content,contentStream.getMimeType());
            doc.setContentStream(newStream,true);

        }catch (CmisObjectNotFoundException onfe) {
            return error("Object doesn't exist!");
        } catch (CmisNameConstraintViolationException ncve) {
            return error("The new name is invalid or \"\n" +
                    "                    + \"an object with this name \"\n" +
                    "                    + \"already exists in this folder!");
        } catch (CmisBaseException cbe) {
            return error("Rename failed: ");
        }
        return success("Change content success!");
    }
    private JSONObject getContent(Session cmisSession, JSONObject params) throws ServletException {
        String path = params.getAsString("item");
        LOG.debug("get content from: {}", path);
        String result = "";
        CmisObject cmisObject = null;
        try {
            cmisObject = cmisSession.getObjectByPath(path,
                    CMISHelper.LIGHT_OPERATION_CONTEXT);
            if (!(cmisObject instanceof Document)) {
                return error("Object is not a document!");
            }

            Document doc = (Document) cmisObject;
            ContentStream contentStream = doc.getContentStream();

            if (contentStream == null) {
                return error("Content stream null");
            }

            InputStream in = contentStream.getStream();
            String mimeType = contentStream.getMimeType();

            result = IOUtils.toString(in, StandardCharsets.UTF_8);
            in.close();

        }catch (CmisObjectNotFoundException onfe) {
            return error("Object doesn't exist!");
        } catch (CmisNameConstraintViolationException ncve) {
            return error("The new name is invalid or \"\n" +
                    "                    + \"an object with this name \"\n" +
                    "                    + \"already exists in this folder!");
        } catch (CmisBaseException cbe) {
            return error("Rename failed: ");
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject json = new JSONObject();
        json.put("result", result);
        return json;
    }
    private JSONObject copy(Session cmisSession, JSONObject params) throws ServletException {
        JSONArray paths = ((JSONArray) params.get("items"));
        String newFileName = params.getAsString("singleFilename");
        String newPath = params.getAsString("newPath");
        for (Object obj : paths) {
            String path = obj.toString();
            CmisObject cmisObject = null;
            try {
                cmisObject = CMISHelper.getCmisObjectByPath(cmisSession, path,
                        CMISHelper.LIGHT_OPERATION_CONTEXT, "object");

                if (!(cmisObject instanceof FileableCmisObject)) {
                    return error("Object is not fileable!");
                }
                if(cmisObject instanceof Document){
                    Folder targetFolder = CMISHelper.getFolderByPath(cmisSession, newPath,
                            CMISHelper.LIGHT_OPERATION_CONTEXT, "target folder");
                    ((Document) cmisObject).copy(targetFolder);
                }
            } catch (CMISException e) {
                e.printStackTrace();
                return error(e.getMessage());
            }
        }
        return success("Copy success");
    }

    private JSONObject remove(Session cmisSession, JSONObject params) throws ServletException {
        JSONArray items = ((JSONArray) params.get("items"));
        for (Object obj : items) {
            String path = obj.toString();
            CmisObject cmisObject = null;
            try {
                cmisObject = CMISHelper.getCmisObjectByPath(cmisSession, path,
                        CMISHelper.LIGHT_OPERATION_CONTEXT, "object");

                if (!(cmisObject instanceof FileableCmisObject)) {
                    return error("Object is not fileable!");
                }
                String parentId = null;
                if (cmisObject instanceof FileableCmisObject) {
                    List<Folder> parents = ((FileableCmisObject) cmisObject)
                            .getParents();
                    if (parents.size() > 0) {
                        parentId = parents.get(0).getId();
                    }
                }

                if (cmisObject instanceof Folder) {
                    List<String> failedToDelete = ((Folder) cmisObject)
                            .deleteTree(true, UnfileObject.DELETE, true);

                    if (failedToDelete != null && !failedToDelete.isEmpty()) {
                        return error("Deletion fail!");
                    }
                } else {
                    cmisObject.delete();
                }


            } catch (CMISException e) {
                e.printStackTrace();
                return error(e.getMessage());
            }
        }
        return success("Delete success");
    }
    private JSONObject move(Session cmisSession, JSONObject params) throws ServletException {
        JSONArray items = ((JSONArray) params.get("items"));
        String newpath = params.getAsString("newPath");
        for (Object obj : items) {
            String path = obj.toString();
            CmisObject cmisObject = null;
            try {
                cmisObject = CMISHelper.getCmisObjectByPath(cmisSession, path,
                        CMISHelper.LIGHT_OPERATION_CONTEXT, "object");

                if (!(cmisObject instanceof FileableCmisObject)) {
                    return error("Object is not fileable!");
                }

                FileableCmisObject fileableCmisObject = (FileableCmisObject) cmisObject;
                // fetch the target folder
                Folder targetFolder = CMISHelper.getFolderByPath(cmisSession, newpath,
                        CMISHelper.LIGHT_OPERATION_CONTEXT, "target folder");
                Folder source = ((FileableCmisObject) cmisObject).getParents().get(0);
                // move it!
                fileableCmisObject.move(source, targetFolder);

            } catch(CmisBaseException e){
                e.printStackTrace();
                return error(e.getMessage());
            }catch(CMISException e){
                e.printStackTrace();
                return error(e.getMessage());
            }
        }
        return success("Move success");
    }
    private JSONObject createFolder(Session cmisSession, JSONObject params) throws ServletException {

            //Path path = Paths.get(REPOSITORY_BASE_PATH, params.getAsString("newPath"));
        String path = params.getAsString("newPath");
        LOG.debug("createFolder path: {} name: {}", path);
        int x = path.lastIndexOf('/');
        Folder parent = null;
        if(x == 0){
            parent = cmisSession.getRootFolder();
        }else{
            parent = (Folder)cmisSession.getObjectByPath(path.substring(0, x));
        }

        if(parent == null){
            return error("Root folder doesnt exist!");
        }

        // create folder
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
        properties.put(PropertyIds.NAME, path.substring(x + 1));


        parent.createFolder(properties, null, null, null,
                CMISHelper.LIGHT_OPERATION_CONTEXT);

        return success("New folder is created!");
    }

    private JSONObject rename(Session cmisSession, JSONObject params) throws ServletException {
        String path = params.getAsString("item");
        String newName = params.getAsString("newItemName");
        LOG.debug("rename from: {} to: {}", path, newName);
        CmisObject cmisObject = null;

        // rename the object
        try {
            // update the cmis:name property
            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(PropertyIds.NAME, newName);

            cmisObject = CMISHelper.getCmisObjectByPath(cmisSession, path,
                    CMISHelper.LIGHT_OPERATION_CONTEXT, "object");
            CmisObject newObject = cmisObject.updateProperties(properties);

        } catch (CmisNameConstraintViolationException ncve) {
            return error("The new name is invalid or \"\n" +
                    "                    + \"an object with this name \"\n" +
                    "                    + \"already exists in this folder!");
        } catch (CmisBaseException onfe) {
            return error("Rename failed");
        } catch (CMISException e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
        return success("Rename success");
    }


    private JSONObject list(Session cmisSession, JSONObject params) throws ServletException {
        try {
            boolean onlyFolders = "true".equalsIgnoreCase(params.getAsString("onlyFolders"));
            String path = params.getAsString("path");
            LOG.debug("list path: Paths.get('{}', '{}'), onlyFolders: {}", null, path, onlyFolders);
            SimpleDateFormat dt = new SimpleDateFormat(DATE_FORMAT);
            List<JSONObject> resultList = new ArrayList<JSONObject>();
            //Folder appRootFolder = (Folder)cmisSession.getObject("@root@");
            Folder appRootFolder = (Folder)cmisSession.getObjectByPath(path);

            for (CmisObject child : appRootFolder.getChildren(
                    BROWSE_OPERATION_CONTEXT).getPage()) {
                //children.add(child);
                JSONObject el = new JSONObject();
                el.put("name", child.getName());
                el.put("rights", null);

                el.put("date", dt.format(child.getCreationDate().getTime()));
                el.put("size", child.getPropertyValue("cmis:contentStreamLength"));
                el.put("type", child instanceof Folder ? "dir" : "file");
                resultList.add(el);
            }

            JSONObject json = new JSONObject();
            json.put("result", resultList);
            return json;
        } catch (Exception e) {
            LOG.error("list:" + e.getMessage(), e);
            return error(e.getMessage());
        }
    }
    private List<ObjectType> getCreatableDocumentTypes(
            HttpServletRequest request, HttpServletResponse response) {
        List<ObjectType> types = null;

        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            types = (List<ObjectType>) httpSession.getAttribute(HTTP_SESSION_DOC_TYPES);
        }

        return types;
    }

    private ContentStream prepareContentStream(Session session,
                                                 File contentFile, ObjectType docType, Map<String, Object> properties)
            throws Exception {

        //ObjectType docType = session.getTypeDefinition(typeId);
        TikaProperties tikaProperties = new TikaProperties(contentFile);
        tikaProperties.setDocumentType(docType);
        tikaProperties.enrichProperties(session, properties);

        String name = (String) properties.get(PropertyIds.NAME);
        long size = contentFile.length();
        String mimeType = tikaProperties.getMIMEType();
        InputStream stream = new BufferedInputStream(new FileInputStream(
                contentFile));
        return session.getObjectFactory().createContentStream(name, size,
                mimeType, stream);
    }
    private ContentStream prepareContentStream(Session session, String content,String mimeType){
        long size = content.length();
        InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        return session.getObjectFactory().createContentStream(null,size,mimeType,stream);
    }
    private void uploadFile(HttpServletRequest request, HttpServletResponse response) throws CMISException , ServletException, IOException {
        // URL: $config.uploadUrl, Method: POST, Content-Type: multipart/form-data
        // Unlimited file upload, each item will be enumerated as file-1, file-2, etc.
        // [$config.uploadUrl]?destination=/public_html/image.jpg&file-1={..}&file-2={...}
        Session cmisSession = getOpenCMISSession(request);
        if(cmisSession == null){
            setError(GlobalConstants.FILE_MANAGER_MSG_1,response);
            return;
        }

        if (isSupportFeature(Mode.upload)) {
            LOG.debug("upload now");
            Map<String, Object> properties = new HashMap<String, Object>();
            File uploadedFile = null;
            String parentId = null;
            String parentPath = null;
            ObjectId newId = null;
            try {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setSizeMax(GlobalConstants.UPLOAD_FILE_MAX_SIZE * 1024 * 1024);

                @SuppressWarnings("unchecked")
                List<FileItem> items = upload.parseRequest(request);

                for (FileItem item : items) {
                    if (item.isFormField()) {
                        String name = item.getFieldName();
                        String str = item.getString();
                        if (PARAM_PARENT_ID.equalsIgnoreCase(name)) {
                            parentId = item.getString();
                        } else if (PARAM_PARENT_PATH.equalsIgnoreCase(name)) {
                            parentPath = item.getString();
                        } else if (PARAM_TYPE_ID.equalsIgnoreCase(name)) {
                            properties.put(PropertyIds.OBJECT_TYPE_ID,
                                    item.getString());
                        }
                    } else {
                        String name = item.getName();
                        if (name == null) {
                            name = "file";
                        } else {
                            // if the browser provided a path instead of a file name,
                            // strip off the path
                            int x = name.lastIndexOf('/');
                            if (x > -1) {
                                name = name.substring(x + 1);
                            }
                            x = name.lastIndexOf('\\');
                            if (x > -1) {
                                name = name.substring(x + 1);
                            }

                            name = name.trim();
                            if (name.length() == 0) {
                                name = "file";
                            }
                        }

                        properties.put(PropertyIds.NAME, name);

                        uploadedFile = File.createTempFile("blend", "tmp");
                        item.write(uploadedFile);
                    }
                }
            } catch (FileUploadException e) {
                LOG.error("Cannot parse multipart request: DiskFileItemFactory.parseRequest", e);
                throw new ServletException("Cannot parse multipart request: DiskFileItemFactory.parseRequest", e);
            } catch (IOException e) {
                LOG.error("Cannot parse multipart request: item.getInputStream");
                throw new ServletException("Cannot parse multipart request: item.getInputStream", e);
            } catch (Exception e) {
                LOG.error("Cannot write file", e);
                throw new ServletException("Cannot write file", e);
            }
            if (uploadedFile == null) {
                throw new ServletException(notSupportFeature(Mode.upload).getAsString("No content"));
            }

            try {
                // prepare the content stream
                ContentStream contentStream = null;
                try {
                    String objectTypeId = (String) properties
                            .get(PropertyIds.OBJECT_TYPE_ID);
                    List<ObjectType> types = getCreatableDocumentTypes(request,response);
                    contentStream = prepareContentStream(cmisSession, uploadedFile,
                            getCreatableDocumentTypes(request,response).get(0), properties);
                } catch (Exception e) {
                    throw new ServletException(notSupportFeature(Mode.upload).getAsString("Upload failed") + e.getMessage());
                }

                // find the parent folder
                // (we don't deal with unfiled documents here)
                Folder parent = null;
                if (parentId != null) {
                    parent = CMISHelper.getFolder(cmisSession, parentId,
                            CMISHelper.LIGHT_OPERATION_CONTEXT, "parent folder");
                } else {
                    parent = CMISHelper.getFolderByPath(cmisSession, parentPath,
                            CMISHelper.LIGHT_OPERATION_CONTEXT, "parent folder");
                }

                // create the document
                try {
                    newId = cmisSession.createDocument(properties, parent,
                            contentStream, null);
                } catch (CmisBaseException cbe) {
                    throw new ServletException(notSupportFeature(Mode.upload).getAsString("error"));
                } finally {
                    try {
                        contentStream.getStream().close();
                        JSONObject responseJsonObject = null;
                        //responseJsonObject = this.success(responseJsonObject);
                        responseJsonObject = success("success");
                        response.setContentType("application/json;charset=UTF-8");
                        PrintWriter out = response.getWriter();
                        out.print(responseJsonObject);
                        out.flush();
                    } catch (IOException ioe) {
                        // ignore
                    }
                }
            } finally {
                // delete temp file
                uploadedFile.delete();
            }

        } else {
            throw new ServletException(notSupportFeature(Mode.upload).getAsString("error"));
        }
    }



    private boolean isSupportFeature(Mode mode) {
        LOG.debug("check spport {}", mode);
        return Boolean.TRUE.equals(enabledAction.get(mode));
    }
    private JSONObject notSupportFeature(Mode mode) throws ServletException {
        return error("This implementation not support " + mode + " feature");
    }

}
