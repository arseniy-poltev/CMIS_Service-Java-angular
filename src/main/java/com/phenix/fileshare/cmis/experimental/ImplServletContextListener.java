package com.phenix.fileshare.cmis.experimental;

import javax.servlet.*;

/**
 * ServerContextListener interface implementation listener class.
 */
public class ImplServletContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        // Write to console
        System.out.println("Within the contextInitialized() method");
        // Get ServletContext using event
        ServletContext sc = event.getServletContext();
        // Get data source intital parameter using ServletContext
        String fileName = sc.getInitParameter("datasource");
        // Create an arbitrary DataSource source object
        DataSource ds = new DataSource(fileName);
        // Set ServletContext attribute
        sc.setAttribute("datasource", ds);
    }

    public void contextDestroyed(ServletContextEvent event) {
        // Write to console
        System.out.println("Within the contextDestroyed() method");
    }
}

