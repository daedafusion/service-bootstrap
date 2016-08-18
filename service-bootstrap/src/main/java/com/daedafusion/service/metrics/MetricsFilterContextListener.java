package com.daedafusion.service.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlet.InstrumentedFilterContextListener;
import org.apache.log4j.Logger;

/**
 * Created by mphilpot on 8/18/16.
 */
public class MetricsFilterContextListener extends InstrumentedFilterContextListener
{
    private static final Logger log = Logger.getLogger(MetricsFilterContextListener.class);

    @Override
    protected MetricRegistry getMetricRegistry()
    {
        return MetricsRegistrySingleton.getInstance().getRegistry();
    }
}
