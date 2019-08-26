package com.phenix.fileshare.cmis.experimental;

import com.phenix.fileshare.cmis.utils.CMISHelper;
import com.phenix.fileshare.cmis.utils.GlobalConstants;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;

import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

abstract public class BaseServlet extends HttpServlet {
    static final String HTTP_SESSION_SESSION = "session";
    static final String HTTP_SESSION_DOC_TYPES = "doctypes";
    static final String HTTP_SESSION_FOLDER_TYPES = "foldertypes";


    void setAccessControlHeaders(HttpServletRequest request, HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", GlobalConstants.FRONTEND_URL);
        resp.setHeader("Access-Control-Allow-Methods", "GET, PUT, POST, OPTIONS, DELETE");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, authorization, X-Requested-With");
        resp.setHeader("Access-Control-Max-Age", "86400");
        resp.setHeader("Access-Control-Allow-Credentials","true");
//        HttpSession session = request.getSession();
//        if (request.getParameter("JSESSIONID") != null) {
//            Cookie userCookie = new Cookie("JSESSIONID", request.getParameter("JSESSIONID"));
//            resp.addCookie(userCookie);
//        } else {
//            String sessionId = session.getId();
//            Cookie userCookie = new Cookie("JSESSIONID", sessionId);
//            resp.addCookie(userCookie);
//        }
    }

    void setError(Throwable t, HttpServletResponse response) throws IOException {
        try {
            // { "result": { "success": false, "error": "message" } }
            JSONObject responseJsonObject = error(t.getMessage());
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(responseJsonObject);
            out.flush();
        } catch (IOException ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
    void setError(String msg, HttpServletResponse response) throws IOException {
        try {
            // { "result": { "success": false, "error": "message" } }
            JSONObject responseJsonObject = error(msg);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(responseJsonObject);
            out.flush();
        } catch (IOException ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
    void setSuccess(String msg, HttpServletResponse response , String ... other) throws IOException {
        try {
            // { "result": { "success": false, "error": "message" } }
            JSONObject responseJsonObject = success(msg, other);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(responseJsonObject);
            out.flush();
        } catch (IOException ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
    protected JSONObject error(String msg) {
        // { "result": { "success": false, "error": "msg" } }
        JSONObject result = new JSONObject();
        result.put("success", false);
        result.put("error", msg);
        JSONObject json = new JSONObject();
        json.put("result", result);
        return json;
    }
    protected JSONObject success(String msg, String ... other) {
        // { "result": { "success": true, "error": null } }
        JSONObject result = new JSONObject();
        result.put("success", true);
        result.put("error", null);
        result.put("message",msg);
        result.put("other",other.length > 0 ? other[0] : null);
        JSONObject json = new JSONObject();
        json.put("result", result);
        return json;
    }
    void setOpenCMISSession(HttpServletRequest request,
                            Session session) {

        HttpSession httpSession = request.getSession();
//        if(session == null){
//            httpSession.setAttribute(HTTP_SESSION_SESSION, null);
//            httpSession.setAttribute(HTTP_SESSION_DOC_TYPES, null);
//            httpSession.setAttribute(HTTP_SESSION_FOLDER_TYPES, null);
//            return;
//        }
        httpSession.setAttribute(HTTP_SESSION_SESSION, session);
        // get all creatable document types and
        // store them in the HTTP session
        List<ObjectType> documentTypes = CMISHelper.getCreatableTypes(
                session, BaseTypeId.CMIS_DOCUMENT.value());

        httpSession.setAttribute(HTTP_SESSION_DOC_TYPES, documentTypes);

        // get all creatable folder types and
        // store them in the HTTP session
        List<ObjectType> folderTypes = CMISHelper.getCreatableTypes(
                session, BaseTypeId.CMIS_FOLDER.value());

        httpSession.setAttribute(HTTP_SESSION_FOLDER_TYPES, folderTypes);
    }
    Session getOpenCMISSession(HttpServletRequest request){
        HttpSession httpSession = request.getSession();
        if (httpSession != null) {
            return (Session) httpSession.getAttribute(HTTP_SESSION_SESSION);
        }
        return null;
    }
    JSONObject getPayloadFromRequest(HttpServletRequest request,HttpServletResponse response) throws IOException{
        //get payload data from request
        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        try {
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
            setError(e.getMessage(),response);
            return null;
        }
        return JSONValue.parse(sb.toString(), JSONObject.class);
    }
}
