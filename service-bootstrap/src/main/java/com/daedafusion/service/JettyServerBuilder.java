package com.daedafusion.service;

import com.daedafusion.crypto.Crypto;
import com.daedafusion.crypto.CryptoFactory;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.security.KeyStore;
import java.util.EnumSet;
import java.util.EventListener;

/**
 * Created by mphilpot on 7/24/14.
 */
public class JettyServerBuilder
{
    private static final Logger log = Logger.getLogger(JettyServerBuilder.class);

    private Server server;

    private ServletContextHandler context;

    public JettyServerBuilder()
    {
        server = new Server();
        server.setStopAtShutdown(true);
        
    }

    public JettyServerBuilder newServletContext(String path)
    {
        context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(path);

        return this;
    }

    public JettyServerBuilder newServletContext()
    {
        return newServletContext("/");
    }

    public JettyServerBuilder addJerseyApplication(Class clazz, String path)
    {
        ServletHolder h = new ServletHolder(new ServletContainer());
        h.setInitParameter("javax.ws.rs.Application", clazz.getName());

        context.addServlet(h, path);

        return this;
    }

    public ServletHolder newServletHolder(String path)
    {
        ServletHolder h = new ServletHolder(new ServletContainer());

        context.addServlet(h, path);

        return h;
    }

    public JettyServerBuilder addJerseyApplication(Class clazz)
    {
        return addJerseyApplication(clazz, "/*");
    }

    public JettyServerBuilder addListener(EventListener listener)
    {
        context.addEventListener(listener);

        return this;
    }

    public JettyServerBuilder setErrorHandler(ErrorHandler eh)
    {
        context.setErrorHandler(eh);

        return this;
    }

    public JettyServerBuilder addFilter(Filter filter, String path)
    {
        FilterHolder fh = new FilterHolder();
        fh.setFilter(filter);

        context.addFilter(fh, path, EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));

        return this;
    }

    public JettyServerBuilder addFilter(Filter filter)
    {
        return addFilter(filter, "/*");
    }

    public JettyServerBuilder addDefaultConnector(int port)
    {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        return this;
    }

    public JettyServerBuilder addSslConnector(int port, boolean enableMutualAuth)
    {
        HttpConfiguration httpsConfig = new HttpConfiguration();
        httpsConfig.setSecureScheme("https");
        httpsConfig.setSecurePort(port);
        httpsConfig.addCustomizer(new SecureRequestCustomizer());

        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setNeedClientAuth(enableMutualAuth);

        try
        {
            KeyStore keyStore = CryptoFactory.getInstance().getKeyStore();

            sslContextFactory.setTrustStore(keyStore);
            sslContextFactory.setTrustStorePassword(Crypto.getProperty(Crypto.KEYSTORE_PASSWORD));

            sslContextFactory.setKeyStore(keyStore);
            sslContextFactory.setKeyStorePassword(Crypto.getProperty(Crypto.KEYSTORE_PASSWORD));
            sslContextFactory.setKeyManagerPassword(Crypto.getProperty(Crypto.PROTECTION_PASSWORD));
        }
        catch (Exception e)
        {
            log.error("", e);
        }

        ServerConnector connector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, "http/1.1"),
                new HttpConnectionFactory(httpsConfig));
        connector.setPort(port);
        server.addConnector(connector);

        return this;
    }

    public Server build()
    {
        server.setHandler(context);
        return server;
    }
}
