package com.phenix.fileshare.cmis.server;

import com.phenix.fileshare.cmis.model.User;
import com.phenix.fileshare.cmis.utils.HibernateUtil;
import org.apache.chemistry.opencmis.commons.impl.server.AbstractServiceFactory;
import org.apache.chemistry.opencmis.commons.server.CallContext;
import org.apache.chemistry.opencmis.commons.server.CmisService;
import org.apache.chemistry.opencmis.commons.server.MutableCallContext;
import org.apache.chemistry.opencmis.server.support.wrapper.CallContextAwareCmisService;
import org.apache.chemistry.opencmis.server.support.wrapper.CmisServiceWrapperManager;
import org.apache.chemistry.opencmis.server.support.wrapper.ConformanceCmisServiceWrapper;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * FileShare Service Factory.
 */
public class FileShareCMISServiceFactory extends AbstractServiceFactory {

    private static final Logger LOG = LoggerFactory.getLogger(FileShareCMISServiceFactory.class);

    private static final String PREFIX_LOGIN = "login.";
    private static final String PREFIX_REPOSITORY = "repository.";
    private static final String SUFFIX_READWRITE = ".readwrite";
    private static final String SUFFIX_READONLY = ".readonly";

    /** Default maxItems value for getTypeChildren()}. */
    private static final BigInteger DEFAULT_MAX_ITEMS_TYPES = BigInteger.valueOf(50);

    /** Default depth value for getTypeDescendants(). */
    private static final BigInteger DEFAULT_DEPTH_TYPES = BigInteger.valueOf(-1);

    /**
     * Default maxItems value for getChildren() and other methods returning
     * lists of objects.
     */
    private static final BigInteger DEFAULT_MAX_ITEMS_OBJECTS = BigInteger.valueOf(200);

    /** Default depth value for getDescendants(). */
    private static final BigInteger DEFAULT_DEPTH_OBJECTS = BigInteger.valueOf(10);

    /** Each thread gets its own {@link FileShareCMISService} instance. */
    // old threadLocalService
    //private ThreadLocal<CmisServiceWrapper<FileShareCMISService>> threadLocalService = new ThreadLocal<CmisServiceWrapper<FileShareCMISService>>();
    // new CallContextAware threadLocalService
    private ThreadLocal<CallContextAwareCmisService> threadLocalService;
    // new wrapperManager
    private CmisServiceWrapperManager wrapperManager;

    private FileShareRepositoryManager repositoryManager;
    private FileShareUserManager userManager;
    private FileShareTypeManager typeManager;

    @Override
    public void init(Map<String, String> parameters) {

        threadLocalService = new ThreadLocal<CallContextAwareCmisService>();
        // New for Chameleon **
        wrapperManager = new CmisServiceWrapperManager();
        wrapperManager.addWrappersFromServiceFactoryParameters(parameters);
        wrapperManager.addOuterWrapper(ConformanceCmisServiceWrapper.class, DEFAULT_MAX_ITEMS_TYPES,
                DEFAULT_DEPTH_TYPES, DEFAULT_MAX_ITEMS_OBJECTS, DEFAULT_DEPTH_OBJECTS);
        // *******

        repositoryManager = new FileShareRepositoryManager();
        userManager = new FileShareUserManager();
        typeManager = new FileShareTypeManager();

        readConfiguration(parameters);
    }

    @Override
    public void destroy() {
        threadLocalService = null;
    }

    public FileShareTypeManager getTypeManager() {
        return typeManager;
    }

    public FileShareRepositoryManager getRepositoryManager() {
        return repositoryManager;
    }

    public ThreadLocal<CallContextAwareCmisService> getThreadLocalService() {
        return threadLocalService;
    }

    public void setThreadLocalService(ThreadLocal<CallContextAwareCmisService> threadLocalService) {
        this.threadLocalService = threadLocalService;
    }

    @Override
    public CmisService getService(CallContext context) {
        // authenticate the user
        // if the authentication fails, authenticate() throws a
        // CmisPermissionDeniedException
        userManager.authenticate(context);

        // get service object for this thread
        // New for Chameleon **
        if(threadLocalService == null)
            return null;
        CallContextAwareCmisService service = threadLocalService.get();
        if (service == null) {
            FileShareCMISService fileShareService = new FileShareCMISService(repositoryManager);
            // wrap it with the chain of wrappers
            service = (CallContextAwareCmisService) wrapperManager.wrap(fileShareService);
            threadLocalService.set(service);
        }

        // Stash any object into the call context and then pass it to our service
        // so that it can be shared with any extensions.
        // Here is where you would put in a reference to a native api object if needed.
        MutableCallContext mcc = (MutableCallContext)context;
        mcc.put("foo","bar");

        service.setCallContext(context);
        // ******

        return service;
    }

    // ---- helpers ----

    /**
     * Reads the configuration and sets up the repositories, logins, and type
     * definitions.
     */
    private void readConfiguration(Map<String, String> parameters) {
        List<String> keys = new ArrayList<String>(parameters.keySet());
        Collections.sort(keys);

        for (String key : keys) {
            if (key.startsWith(PREFIX_LOGIN)) {
                // get logins
                String usernameAndPassword = parameters.get(key);
                if (usernameAndPassword == null) {
                    continue;
                }

                String username = usernameAndPassword;
                String password = "";

                int x = usernameAndPassword.indexOf(':');
                if (x > -1) {
                    username = usernameAndPassword.substring(0, x);
                    password = usernameAndPassword.substring(x + 1);
                }

                LOG.info("Adding login '{}'.", username);

                userManager.addLogin(username, password);
            } else if (key.startsWith(PREFIX_REPOSITORY)) {
                // configure repositories
                String repositoryId = key.substring(PREFIX_REPOSITORY.length()).trim();
                int x = repositoryId.lastIndexOf('.');
                if (x > 0) {
                    repositoryId = repositoryId.substring(0, x);
                }

                if (repositoryId.length() == 0) {
                    throw new IllegalArgumentException("No repository id!");
                }

                if (key.endsWith(SUFFIX_READWRITE)) {
                    // read-write users
                    //FileShareRepository fsr = repositoryManager.getRepository(repositoryId);
                    //for (String user : split(parameters.get(key))) {
                    //    fsr.setUserReadWrite(user);
                    //}
                } else if (key.endsWith(SUFFIX_READONLY)) {
                    // read-only users
                    //FileShareRepository fsr = repositoryManager.getRepository(repositoryId);
                    //for (String user : split(parameters.get(key))) {
                    //   fsr.setUserReadOnly(user);
                    //}
                } else {
                    // new repository
                    String root = parameters.get(key);

                    LOG.info("Adding repository '{}': {}", repositoryId, root);
                    Session session = HibernateUtil.getDBSession();
                    List<User> users = HibernateUtil.getAllUsers(session);
                    for(User user:users){
                        String email = user.getEmail();
                        if(email.equals(""))
                            continue;
                        FileShareRepository fsr;
                        if(user.getRole() != null && user.getRole().getRoleName().equals("Administrator")){
                            fsr = new FileShareRepository(email, root, typeManager,true);
                        }else{
                            fsr = new FileShareRepository(email, root, typeManager,false);
                        }
                        fsr.setUserReadWrite(email);
                        repositoryManager.addRepository(fsr);
                    }


                    //FileShareRepository fsr = new FileShareRepository(repositoryId, root, typeManager);
                    //*************************Suju************************************
//                    String[] roots = new String[3];
//                    roots[0] = "C:\\";
//                    roots[1] = "D:\\";
//                    roots[2] = "E:\\";
//
//                    for(int i = 0 ; i < 3 ; i++){
//                        FileShareRepository fsr = new FileShareRepository(repositoryId+i, roots[i], typeManager);
//                        fsr.setUserReadWrite("test"+i);
//                        repositoryManager.addRepository(fsr);
//                    }
                    //*****************************************************************
                    //repositoryManager.addRepository(fsr);
                }
            }
        }
    }

    /**
     * Splits a string by comma.
     */
    private List<String> split(String csl) {
        if (csl == null) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<String>();
        for (String s : csl.split(",")) {
            result.add(s.trim());
        }

        return result;
    }
}
