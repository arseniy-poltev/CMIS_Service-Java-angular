package com.phenix.fileshare.cmis.experimental;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import com.phenix.fileshare.cmis.model.User;
import com.phenix.fileshare.cmis.server.FileShareCMISServiceFactory;
import com.phenix.fileshare.cmis.server.FileShareRepository;
import com.phenix.fileshare.cmis.utils.CMISUtil;
import com.phenix.fileshare.cmis.utils.GlobalConstants;
import com.phenix.fileshare.cmis.utils.HibernateUtil;

import net.minidev.json.JSONObject;


public class UserManagementServlet extends BaseServlet {
    private FileShareCMISServiceFactory cmisFactory;

    @Override
    public void init() throws ServletException {
        System.out.println("UserManagementServlet");
        super.init();
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setAccessControlHeaders(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setAccessControlHeaders(request, response);
        String requestURI = request.getRequestURI();
        System.out.println(requestURI);
        System.out.println(GlobalConstants.USER_MANAGER_LOGIN_URL);
        if(requestURI.equals(GlobalConstants.USER_MANAGER_REGISTER_URL)){
            registerUser(request,response);
        }else if(requestURI.equals(GlobalConstants.USER_MANAGER_LOGIN_URL)){
            loginUser(request,response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setAccessControlHeaders(request, response);
        String requestURI = request.getRequestURI();
        System.out.println(requestURI);
        if(requestURI.equals(GlobalConstants.USER_MANAGER_LOGOUT_URL)){
            HttpSession httpSession = request.getSession();
            httpSession.invalidate();
            setSuccess(GlobalConstants.USER_MANAGER_LOGOUT_SUCCESS_MSG, response);
        }
    }

    @PersistenceContext
    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //check cmis server is started
        cmisFactory = (FileShareCMISServiceFactory) getServletContext().getAttribute(GlobalConstants.SERVICES_FACTORY);
        if(cmisFactory == null){
            setError(GlobalConstants.MSG_CMIS_SERVER_NOT_STARTED,response);
            return;
        }

        JSONObject params = getPayloadFromRequest(request,response);
        if(params == null)
            return;

        //check duplication
        Session dbSession = HibernateUtil.getDBSession();
        String email = params.getAsString("email");
        User user = HibernateUtil.getUserByEmail(dbSession, email);
        if(user != null){
            setError(GlobalConstants.USER_MANAGER_DUPLICATE_MSG,response);
            return;
        }

        //register user
        User regUser = new User(params.getAsString("username"),
                params.getAsString("email"),params.getAsString("password"));
        dbSession.beginTransaction();
        dbSession.save(regUser);
        dbSession.getTransaction().commit();

        //create repository
        //create folder for repository
        String cmisRoot = (String)getServletContext().getAttribute(GlobalConstants.CMIS_SERVICE_ROOT_IDENTIFIER);
        try {
            Path path = Paths.get(cmisRoot, email);
            Files.createDirectories(path);
        } catch (FileAlreadyExistsException ex) {
            setError(GlobalConstants.USER_MANAGER_DUPLICATE_FOLDER,response);
            return;
        } catch (IOException e) {
            setError(e.getMessage(),response);
            return;
        }

        FileShareRepository fsr = new FileShareRepository(email, cmisRoot, cmisFactory.getTypeManager(),false);
        fsr.setUserReadWrite(email);
        cmisFactory.getRepositoryManager().addRepository(fsr);

        setSuccess(GlobalConstants.USER_MANAGER_REGISTER_SUCCESS_MSG,response);
    }



    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws IOException{


        JSONObject params = getPayloadFromRequest(request,response);
        if(params == null)
            return;

        String password = params.getAsString("password");
        String email = params.getAsString("email");

        Session dbSession = HibernateUtil.getDBSession();
        User user = HibernateUtil.getUserByEmail(dbSession, email);
        if(user == null){
            setError(GlobalConstants.USER_MANAGER_DONT_EXIST,response);
            return;
        }
        if(password == null || !password.equals(user.getPassword())){
            setError(GlobalConstants.USER_MANAGER_PASSWORD_INVALID,response);
            return;
        }

        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute("email",email);
        httpSession.setAttribute("password",password);
        cmisFactory = (FileShareCMISServiceFactory) getServletContext().getAttribute(GlobalConstants.SERVICES_FACTORY);
        if(user.getRole() != null && user.getRole().getRoleName().equals("Administrator")){
            httpSession.setAttribute("type","admin");
            if(cmisFactory != null && cmisFactory.getThreadLocalService() != null){
                setOpenCMISSession(request,CMISUtil.createOpenCMISSession(email,password));
            }
            setSuccess(GlobalConstants.USER_MANAGER_LOGIN_SUCCESS_MSG,response,"administrator");
        }else{
            httpSession.setAttribute("type","user");

            if(cmisFactory == null || cmisFactory.getThreadLocalService() == null){
                setError(GlobalConstants.MSG_CMIS_SERVER_NOT_STARTED,response);
                return;
            }

            setOpenCMISSession(request,CMISUtil.createOpenCMISSession(email,password));
            setSuccess(GlobalConstants.USER_MANAGER_LOGIN_SUCCESS_MSG,response);
        }
        //if(getOpenCMISSession(request) == null){

        //}
    }
}
