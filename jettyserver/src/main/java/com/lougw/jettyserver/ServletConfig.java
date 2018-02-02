package com.lougw.jettyserver;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ServletConfig {
    public static void config(ServletContextHandler handler) {
        handler.addServlet(new ServletHolder(new LinkStatusServlet()), "/index");
        handler.addServlet(new ServletHolder(new ControllerServlet()), "/controller");
    }
}
