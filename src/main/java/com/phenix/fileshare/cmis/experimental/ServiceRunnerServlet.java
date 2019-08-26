package com.phenix.fileshare.cmis.experimental;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.chemistry.opencmis.commons.impl.ClassLoaderUtil;
import org.apache.chemistry.opencmis.commons.impl.IOUtils;
import org.apache.chemistry.opencmis.commons.server.CmisServiceFactory;
import org.apache.chemistry.opencmis.server.impl.CmisRepositoryContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phenix.fileshare.cmis.server.FileShareCMISServiceFactory;
import com.phenix.fileshare.cmis.utils.CMISUtil;
import com.phenix.fileshare.cmis.utils.GlobalConstants;

import net.minidev.json.JSONObject;

public class ServiceRunnerServlet extends BaseServlet {

    private static final Logger LOG = LoggerFactory.getLogger(CmisRepositoryContextListener.class.getName());

    private static final String CONFIG_INIT_PARAM = "org.apache.chemistry.opencmis.REPOSITORY_CONFIG_FILE";
    private static final String CONFIG_FILENAME = "/repository.properties";
    private static final String PROPERTY_CLASS = "class";
    private static final String MSG_SERVER_START = "CMIS server is started";
    private static final String MSG_SERVER_STOP = "CMIS server is stopped";
    private static final String CONFIG_SERVER_URL = "repository.test";


    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setAccessControlHeaders(req, resp);
    }
    private String errorMsg = "";
    private Map<String, String> getInitParameter(){
        String path = HelloWorldServlet.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = "";
        try{
            decodedPath = java.net.URLDecoder.decode(path, "UTF-8");

        }catch(Exception e){
            LOG.warn("Cannot load property file: " + e, e);
            return null;
        }
        File f = new File(decodedPath + CONFIG_FILENAME);
        InputStream stream = null;
        try {
            stream = new FileInputStream( f );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //InputStream stream = this.getClass().getResourceAsStream();

        if (stream == null) {
            return null;
        }

        Properties props = new Properties();
        try {
            props.load(stream);
        } catch (IOException e) {
            return null;
        } finally {
            IOUtils.closeQuietly(stream);
        }
        Map<String, String> params = new HashMap<String, String>();

        for (Enumeration<?> e = props.propertyNames(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            String value = props.getProperty(key);
            params.put(key, value);
        }
        return params;
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setAccessControlHeaders(request, response);
        HttpSession httpSession = request.getSession();
        String email = (String)httpSession.getAttribute("email");
        String password = (String)httpSession.getAttribute("password");

        if(httpSession.getAttribute("type") != "admin"){
            return;
        }
        FileShareCMISServiceFactory service = (FileShareCMISServiceFactory) getServletContext().getAttribute(GlobalConstants.SERVICES_FACTORY);
        String requestURI = request.getRequestURI();
        if(requestURI.equals(GlobalConstants.ADMIN_GET_REPO_INFO_URL)){
            Map<String,String> parameters = getInitParameter();
            if(parameters == null){
                setError(GlobalConstants.ADMIN_NO_PARAMETER_MSG,response);
                return;
            }
            String repURL = parameters.get(CONFIG_SERVER_URL);
            if(service == null || service.getThreadLocalService() == null)
                setSuccess(repURL,response);
            else
                setSuccess(repURL,response,MSG_SERVER_START);
        }else if(requestURI.equals(GlobalConstants.ADMIN_START_SERVICE_URL)){
            JSONObject params = getPayloadFromRequest(request,response);
            if(params == null){
                setError(GlobalConstants.ADMIN_NO_PARAMETER_MSG,response);
                return;
            }
            String repURL = params.getAsString("repURL");
            if(repURL == null){
                setError(GlobalConstants.ADMIN_NO_PARAMETER_MSG,response);
                return;
            }
            if(startService(repURL)){
                System.out.println("CMIS server started!");
                FileShareCMISServiceFactory cmisFactory = (FileShareCMISServiceFactory) getServletContext().getAttribute(GlobalConstants.SERVICES_FACTORY);
                if(cmisFactory.getThreadLocalService() == null){
                    setError(GlobalConstants.MSG_CMIS_SERVER_NOT_STARTED,response);
                    return;
                }
                setOpenCMISSession(request,CMISUtil.createOpenCMISSession(email,password));
                setSuccess(MSG_SERVER_START,response);
            }else{
                // setError("Cannot start the server!",response);
                setError(errorMsg,response);
            }
        }else if(requestURI.equals(GlobalConstants.ADMIN_STOP_SERVICE_URL)){
            stopCMISServer();
            //httpSession.invalidate();
            setSuccess(MSG_SERVER_STOP,response);
        }
        /*
        switch (requestURI) {
            case GlobalConstants.ADMIN_GET_REPO_INFO_URL:{
                Map<String,String> parameters = getInitParameter();
                if(parameters == null){
                    setError(GlobalConstants.ADMIN_NO_PARAMETER_MSG,response);
                    return;
                }
                String repURL = parameters.get(CONFIG_SERVER_URL);
                if(service == null || service.getThreadLocalService() == null)
                    setSuccess(repURL,response);
                else
                    setSuccess(repURL,response,MSG_SERVER_START);
                break;
            }
            case GlobalConstants.ADMIN_START_SERVICE_URL:{
                JSONObject params = getPayloadFromRequest(request,response);
                if(params == null){
                    setError(GlobalConstants.ADMIN_NO_PARAMETER_MSG,response);
                    return;
                }
                String repURL = params.getAsString("repURL");
                if(repURL == null){
                    setError(GlobalConstants.ADMIN_NO_PARAMETER_MSG,response);
                    return;
                }
                if(startService(repURL)){
                    System.out.println("CMIS server started!");
                    FileShareCMISServiceFactory cmisFactory = (FileShareCMISServiceFactory) getServletContext().getAttribute(GlobalConstants.SERVICES_FACTORY);
                    if(cmisFactory.getThreadLocalService() == null){
                        setError(GlobalConstants.MSG_CMIS_SERVER_NOT_STARTED,response);
                        return;
                    }
                    setOpenCMISSession(request,CMISUtil.createOpenCMISSession(email,password));
                    setSuccess(MSG_SERVER_START,response);
                }else{
                   // setError("Cannot start the server!",response);
                    setError(errorMsg,response);
                }
                break;
            }
            case GlobalConstants.ADMIN_STOP_SERVICE_URL:{
                stopCMISServer();
                //httpSession.invalidate();
                setSuccess(MSG_SERVER_STOP,response);
                break;
            }
            default:
                break;
        }
        */
    }

    private void stopCMISServer(){
        CmisServiceFactory factory = (CmisServiceFactory) getServletContext().getAttribute(GlobalConstants.SERVICES_FACTORY);
        if (factory != null) {
            try {
                factory.destroy();
             //   getServletContext().setAttribute(GlobalConstants.SERVICES_FACTORY, null);
            } catch (Exception e) {
                LOG.error("Service factory couldn't be destroyed: " + e.toString(), e);
            }
        }
    }
    private boolean startService(String repURL) throws IOException {
        String path = ServiceRunnerServlet.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = java.net.URLDecoder.decode(path, "UTF-8");
        File f = new File(decodedPath + CONFIG_FILENAME);
        OutputStream outputStream = new FileOutputStream( f );
        Properties props = new Properties();
        props.setProperty("class","com.phenix.fileshare.cmis.server.FileShareCMISServiceFactory");
        props.setProperty("repository.test",repURL);
        props.store(outputStream,"Framework Entry Point");
        return startCMISServer();
    }

    private boolean startCMISServer(){
        CmisServiceFactory factory = null;

        try {
            factory = createServiceFactory();

        } catch (Exception e) {
            errorMsg = "Service factory couldn't be created: " + e.getMessage();
            LOG.error("Service factory couldn't be created: " + e.toString(), e);
            return false;
        }
        //HibernateUtil.buildSessionFactory();

        getServletContext().setAttribute(GlobalConstants.SERVICES_FACTORY, factory);
        return factory != null;
    }

    private CmisServiceFactory createServiceFactory() throws IOException {
        // load properties
        String path = ServiceRunnerServlet.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = java.net.URLDecoder.decode(path, "UTF-8");
        File f = new File(decodedPath + CONFIG_FILENAME);
        InputStream stream = new FileInputStream(f);
        //InputStream stream = this.getClass().getResourceAsStream(CONFIG_FILENAME);

        if (stream == null) {
            LOG.warn("Cannot find configuration!");
            errorMsg = "Cannot find configuration!";
            return null;
        }

        Properties props = new Properties();
        try {
            props.load(stream);
        } catch (IOException e) {
            LOG.warn("Cannot load configuration: " + e, e);
            errorMsg = "Cannot load configuration: " + e.getMessage();
            return null;
        } finally {
            IOUtils.closeQuietly(stream);
        }

        // get 'class' property
        String className = props.getProperty(PROPERTY_CLASS);
        if (className == null) {
            LOG.warn("Configuration doesn't contain the property 'class'!");
            errorMsg = "Configuration doesn't contain the property 'class'!";
            return null;
        }

        // create a factory instance
        //***************************************************************
        //***************************************************************
        Object object = getServletContext().getAttribute(GlobalConstants.SERVICES_FACTORY);
        //***************************************************************
        //***************************************************************

        if(object == null){
            try {
                object = ClassLoaderUtil.loadClass(className).newInstance();
            } catch (Exception e) {
                LOG.warn("Could not create a services factory instance: " + e, e);
                errorMsg = "Could not create a services factory instance: " + e.getMessage();
                return null;
            }
        }

        if (!(object instanceof CmisServiceFactory)) {
            LOG.warn("The provided class is not an instance of CmisServiceFactory!");
        }

        CmisServiceFactory factory = (CmisServiceFactory) object;

        // initialize factory instance
        Map<String, String> parameters = new HashMap<String, String>();

        for (Enumeration<?> e = props.propertyNames(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            String value = props.getProperty(key);
            parameters.put(key, value);
        }

        String root = parameters.get("repository.test");
        getServletContext().setAttribute(GlobalConstants.CMIS_SERVICE_ROOT_IDENTIFIER, root);

        factory.init(parameters);
        errorMsg = "Initialized Services Factory:" + factory.getClass().getName();
        LOG.info("Initialized Services Factory: " + factory.getClass().getName());
        return factory;
    }


}
