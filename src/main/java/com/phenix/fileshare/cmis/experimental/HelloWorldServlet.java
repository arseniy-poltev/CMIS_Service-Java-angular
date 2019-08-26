package com.phenix.fileshare.cmis.experimental;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.phenix.fileshare.cmis.model.Property;
//import com.phenix.fileshare.cmis.model.Role;
//import com.phenix.fileshare.cmis.model.User;
import com.phenix.fileshare.cmis.model.Document;
import com.phenix.fileshare.cmis.model.User;
import com.phenix.fileshare.cmis.model.UserProperty;
import com.phenix.fileshare.cmis.utils.HibernateUtil;


/**
 * HelloWorldServlet class
 */
public class HelloWorldServlet extends HttpServlet {

    private final static Logger LOG = LoggerFactory.getLogger(HelloWorldServlet.class.getName());
    private static final String CONFIG_FILENAME = "/repository.properties";
    private static final String CONFIG_SERVER_URL = "repository.test";
    private static final String PREFIX_LOGIN = "login.";
    private Map<String, String> parameters;


    private static Session sessionObj;

    private static SessionFactory buildSessionFactory() {
        // Creating Configuration Instance & Passing Hibernate Configuration File
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private void ShareDocument(Long docId,Long toId){
        Session session = HibernateUtil.getDBSession();
        try{
            session.beginTransaction();
            Document sharingDoc = session.get(Document.class,docId);
            UserProperty prop = session.get(UserProperty.class,5L);
            User user = session.get(User.class,toId);
            sharingDoc.getShared_users().add(user);
            sharingDoc.setName("Renamed!");
            session.update(sharingDoc);
            session.getTransaction().commit();
            //send email to user.email
            sendEmail(user.getUserName());
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("Hibernate Util...");
        HibernateUtil.buildSessionFactory();
        // Create a Hibernate Search wrapper around the vanilla Hibernate session


        //------------------Sharing logic--------------

        //------------------Indexing&Searching function
        /*
        Session session = HibernateUtil.getDBSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);
        try {
            fullTextSession.createIndexer().startAndWait();
            fullTextSession.beginTransaction();
            // Create a Hibernate Search QueryBuilder for the appropriate Lucene index (i.e. the index for "App" in this case)
            QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity( Document.class ).get();
            org.apache.lucene.search.Query luceneQuery = null;
//            luceneQuery = queryBuilder
//                    .keyword()
//                    .fuzzy()
//                    .withThreshold(.8f)
//                    .withPrefixLength(1)
//                    .onField("name")
//                    .matching("New Document")
//                    .createQuery();
            luceneQuery = queryBuilder
                    .keyword()
                    .wildcard()
                    .onField("name")
                    .matching("*test*")
                    .createQuery();
            FullTextQuery hibernateQuery = fullTextSession.createFullTextQuery(luceneQuery, Document.class);
            int resultSize = hibernateQuery.getResultSize();
            List list = hibernateQuery.list();
            int x = 3;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        //-----------------Adding documents---------------------------
//        // Begin a transaction.  This may not be strictly necessary, but is a good practice in general.


//        session.beginTransaction();
//        Role role1 = new Role("Administrator1","Admin1");
//        User user = new User("test10","test10",0L,role1);
//        Document doc = new Document("0s293827",0,"New Document",user);
//        session.save(role1);
//        session.save(user);
//        session.save(doc);
//        session.getTransaction().commit();

//        try{
//            sessionObj = buildSessionFactory().openSession();
//            sessionObj.beginTransaction();
//            Property prop1 = new Property("Set Parameter","User can set service parameters");
//            Property prop2 = new Property("Start Server","User can start CMIS service");
//            Property prop3 = new Property("Stop Server","User can stop CMIS service");
//            sessionObj.save(prop1);
//            sessionObj.save(prop2);
//            sessionObj.save(prop3);
//
//            Role role1 = new Role("Administrator","Admin");
//            Role role2 = new Role("Manager","Manager");
//            Role role3 = new Role("User","User");
//            Role role4 = new Role("Guest","Guest");
//
//            role1.getProperties().add(prop1);
//            role1.getProperties().add(prop2);
//            role2.getProperties().add(prop2);
//            role2.getProperties().add(prop3);
//            role3.getProperties().add(prop1);
//            role4.getProperties().add(prop2);
//
//
//            sessionObj.save(role1);
//            sessionObj.save(role2);
//            sessionObj.save(role3);
//            sessionObj.save(role4);
//
//            User user1 = new User("test1","password1",0L,role1);
//            User user2 = new User("test2","password2",0L,role2);
//            User user3 = new User("test3","password3",0L,role3);
//            User user4 = new User("test4","password4",0L,role1);
//
//            user1.getProperties().add(prop1);
//            user1.getProperties().add(prop2);
//            user2.getProperties().add(prop1);
//            user3.getProperties().add(prop3);
//            user4.getProperties().add(prop1);
//
//            sessionObj.save(user1);
//            sessionObj.save(user2);
//            sessionObj.save(user3);
//            sessionObj.save(user4);
//            sessionObj.getTransaction().commit();
//        }catch(Exception sqlException) {
//            if(sessionObj == null)
//                return;
//            if(null != sessionObj.getTransaction()) {
//                System.out.println("\n.......Transaction Is Being Rolled Back.......");
//                sessionObj.getTransaction().rollback();
//            }
//            sqlException.printStackTrace();
//        } finally {
//            if(sessionObj != null) {
//                sessionObj.close();
//            }
//        }

    }
    private void sendEmail(String email){

    }

    private void setAccessControlHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        //resp.setHeader("Access-Control-Allow-Methods", "POST");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

//        response.addHeader("Access-Control-Allow-Origin", "*");
//        response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(HttpServletResponse.SC_OK);
//        PrintWriter out = response.getWriter();
//        out.print(request.getHeader("Origin"));
//        out.flush();
//        out.close();

//        ShareDocument(2L,10L);
//
//
        /*
        parameters = new HashMap<String, String>();
        getInitParameter();
        String requestURI = request.getRequestURI();
        System.out.println(requestURI);

        List<String> keys = new ArrayList<String>(parameters.keySet());
        Collections.sort(keys);

        Vector<UserModel> users = new Vector<UserModel>();
        for (String key : keys) {
            if (key.startsWith(PREFIX_LOGIN)) {
                String usernameAndPassword = parameters.get(key);
                if (usernameAndPassword == null)
                    continue;
                String username = usernameAndPassword;
                String password = "";
                int x = usernameAndPassword.indexOf(':');
                if (x > -1) {
                    username = usernameAndPassword.substring(0, x);
                    password = usernameAndPassword.substring(x + 1);
                }
                users.add(new UserModel(username,password));
            }
        }

        String repURL = parameters.get(CONFIG_SERVER_URL);
        request.setAttribute("repURL", repURL);
        request.setAttribute("users",users);
        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
        dispatcher.forward(request, response);
        */
    }
}
