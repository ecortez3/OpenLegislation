package gov.nysenate.openleg.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Basically the web.xml in programmatic form.
 */
public class WebInitializer implements WebApplicationInitializer
{
    protected static String DISPATCHER_SERVLET_NAME = "legislation";

    /**
     * Bootstraps the web application. Automatically invoked by Spring during startup.
     * @param servletContext
     * @throws ServletException
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        /** Create the root Spring application context. */
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();

        /** Manage the lifecycle of the root application context. */
        servletContext.addListener(new ContextLoaderListener(rootContext));

        /** The dispatcher servlet has it's own application context in which it can override
         * beans from the parent root context. */
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.setServletContext(servletContext);
        dispatcherContext.setParent(rootContext);
        dispatcherContext.register(WebApplicationConfig.class);

        /** Register the dispatcher servlet which basically serves as the front controller for Spring.
         * The servlet has to be mapped to the root path "/". */
        ServletRegistration.Dynamic dispatcher;
        dispatcher = servletContext.addServlet(DISPATCHER_SERVLET_NAME, new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}