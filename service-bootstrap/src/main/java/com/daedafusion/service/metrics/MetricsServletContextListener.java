package com.daedafusion.service.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;
import org.apache.log4j.Logger;

/**
 * Created by mphilpot on 1/8/15.
 */
public class MetricsServletContextListener extends MetricsServlet.ContextListener
{
    private static final Logger log = Logger.getLogger(MetricsServletContextListener.class);

    @Override
    protected MetricRegistry getMetricRegistry()
    {
        return MetricsRegistrySingleton.getInstance().getRegistry();
    }
}
