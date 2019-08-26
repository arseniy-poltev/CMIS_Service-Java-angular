package com.phenix.fileshare.cmis.utils;

import com.phenix.fileshare.cmis.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class HibernateUtil{
    private static Session DBSession;
    private static SessionFactory sessionFactory;

    public static void buildSessionFactory() {
        // Creating Configuration Instance & Passing Hibernate Configuration File
        try {
            sessionFactory =  new Configuration().configure().buildSessionFactory();
            DBSession = sessionFactory.openSession();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getDBSession() {
//        if(sessionFactory == null)
//            buildSessionFactory();
//        if(DBSession.isOpen())
//            DBSession.close();
        DBSession = sessionFactory.openSession();

        return DBSession;
    }

    public static User getUserByEmail(Session session, String email){
        return session.byNaturalId(User.class)
                .using("email",email)
                .load();
    }
    @SuppressWarnings("unchecked")
    public static List<User> getAllUsers(Session session){
        return (List<User>)session.createQuery("from User").list();
    }
}