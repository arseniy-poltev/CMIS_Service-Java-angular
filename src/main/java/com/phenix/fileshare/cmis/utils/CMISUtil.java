package com.phenix.fileshare.cmis.utils;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import java.util.HashMap;
import java.util.Map;

public class CMISUtil {
    /**
     * Creates a new OpenCMIS session with the provided username and
     * password.
     */
    private static SessionFactory factory;
    private static Map<String, String> parameter;

    private static void buildSessionFactory(String email,
                                           String password){
        parameter = new HashMap<String, String>();

        parameter.put(SessionParameter.USER, email);
        parameter.put(SessionParameter.PASSWORD, password);

        parameter.put(SessionParameter.ATOMPUB_URL,
                GlobalConstants.CMIS_SERVER_URL);
        parameter.put(SessionParameter.BINDING_TYPE,
                BindingType.ATOMPUB.value());
        parameter.put(SessionParameter.REPOSITORY_ID, email);

        factory = SessionFactoryImpl.newInstance();
    }
    public static Session createOpenCMISSession(String email,
                                                String password) {

        buildSessionFactory(email,password);
        return factory.createSession(parameter);
    }
}
