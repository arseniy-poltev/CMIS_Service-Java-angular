package com.phenix.fileshare.cmis.utils;

public class GlobalConstants {
    public final static String MSG_CMIS_SERVER_NOT_STARTED = "CMIS server is not started!";
//    private final static String SERVER_URL = "http://ec2-52-15-198-149.us-east-2.compute.amazonaws.com/Phenix-share";
//    public final static String FRONTEND_URL = "http://backend.synchronecs.com";
    //public final static String FRONTEND_URL = "http://mypmpnow.com";
    private final static String APPLICATION_CONTEXT = "/Phenix-share/";


    //private final static String APPLICATION_CONTEXT = "/";
    public final static String FRONTEND_URL = "http://localhost";
    //private final static String SERVER_URL = "http://localhost:8080/Phenix-share";
    private final static String SERVER_URL = "http://localhost:8080" + APPLICATION_CONTEXT;

    public final static String CMIS_SERVER_URL = SERVER_URL + "atom";
    public final static String USER_MANAGER_REGISTER_URL = APPLICATION_CONTEXT + "usermanage/register";
    public final static String USER_MANAGER_LOGIN_URL = APPLICATION_CONTEXT + "usermanage/login";
    public final static String USER_MANAGER_LOGOUT_URL = APPLICATION_CONTEXT + "usermanage/logout";
    public final static String USER_MANAGER_DUPLICATE_MSG = "Duplicate User";
    public final static String USER_MANAGER_REGISTER_SUCCESS_MSG = "You are registered successfully";
    public final static String USER_MANAGER_DUPLICATE_FOLDER = "Duplicate Folder";
    public final static String USER_MANAGER_PASSWORD_INVALID = "Password is incorrect";
    public final static String USER_MANAGER_LOGIN_SUCCESS_MSG = "Login success";
    public final static String USER_MANAGER_LOGOUT_SUCCESS_MSG = "Logout success";
    public final static String USER_MANAGER_DONT_EXIST = "User doesn't exist";
    public final static String FILE_MANAGER_MSG_1 = "Access denied!";
    public final static String ADMIN_GET_REPO_INFO_URL = APPLICATION_CONTEXT + "admin/get_info";
    public final static String ADMIN_START_SERVICE_URL = APPLICATION_CONTEXT + "admin/server_start";
    public final static String ADMIN_STOP_SERVICE_URL = APPLICATION_CONTEXT + "admin/server_stop";
    public final static String ADMIN_NO_PARAMETER_MSG = "There isn't any parameters";

    public static final String SERVICES_FACTORY = "org.apache.chemistry.opencmis.servicesfactory";
    public static final String CMIS_SERVICE_ROOT_IDENTIFIER = "cmis_root";

    public static final int UPLOAD_FILE_MAX_SIZE = 1000;
}
