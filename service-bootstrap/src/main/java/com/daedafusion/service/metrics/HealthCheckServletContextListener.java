package com.daedafusion.service.metrics;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;
import org.apache.log4j.Logger;

/**
 * Created by mphilpot on 1/8/15.
 */
public class HealthCheckServletContextListener extends HealthCheckServlet.ContextListener
{
    private static final Logger log = Logger.getLogger(HealthCheckServletContextListener.class);

    @Override
    protected HealthCheckRegistry getHealthCheckRegistry()
    {
        return MetricsRegistrySingleton.getInstance().getHealthCheckRegistry();
    }
}
