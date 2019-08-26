package com.phenix.fileshare.cmis.experimental;

import javax.servlet.*;

/**
 * ServletContextAttributeListener interface implementation listener class.
 */
public class ImplServletContextAttributeListener implements ServletContextAttributeListener {

    public void attributeAdded(ServletContextAttributeEvent event) {
        // Write to console
        System.out.println("Within the attributeAdded() method");
        // Get attribute name and value and write to console
        String attrName = event.getName();
        DataSource ds = (DataSource) event.getValue();
        System.out.println("Context attribute added. Name: " + attrName + " Value: "
                + ds.getFileName());
    }

    public void attributeRemoved(ServletContextAttributeEvent event) {
        // Write to console
        System.out.println("Within the attributeRemoved() method");
        // Get attribute name and value
        String attrName = event.getName();
        DataSource ds = (DataSource) event.getValue();
        System.out.println("Context attribute removed. Name: " + attrName + " Value: "
                + ds.getFileName());
    }

    public void attributeReplaced(ServletContextAttributeEvent event) {
        // Write to console
        System.out.println("Within the attributeReplaced() method");
        // Get attribute name and value
        String attrName = event.getName();
        DataSource ds = (DataSource) event.getValue();
        System.out.println("Context attribute replaced. Name: " + attrName + " Old value: "
                + ds.getFileName());
    }

}
