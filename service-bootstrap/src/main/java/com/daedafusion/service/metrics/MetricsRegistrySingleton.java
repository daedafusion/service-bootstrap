package com.daedafusion.service.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import org.apache.log4j.Logger;

/**
 * Created by mphilpot on 1/8/15.
 */
public class MetricsRegistrySingleton
{
    private static final Logger log = Logger.getLogger(MetricsRegistrySingleton.class);

    private static MetricsRegistrySingleton ourInstance = new MetricsRegistrySingleton();

    public static MetricsRegistrySingleton getInstance()
    {
        return ourInstance;
    }

    private MetricRegistry registry;
    private HealthCheckRegistry healthCheckRegistry;

    private MetricsRegistrySingleton()
    {
        registry = new MetricRegistry();

        registry.register(MetricRegistry.name(MemoryUsageGaugeSet.class, null), new MemoryUsageGaugeSet());
        registry.register(MetricRegistry.name(GarbageCollectorMetricSet.class, null), new GarbageCollectorMetricSet());

        healthCheckRegistry = new HealthCheckRegistry();
    }

    public MetricRegistry getRegistry()
    {
        return registry;
    }

    public HealthCheckRegistry getHealthCheckRegistry()
    {
        return healthCheckRegistry;
    }
}
