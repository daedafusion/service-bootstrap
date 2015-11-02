package com.daedafusion.service.bootstrap;

import com.daedafusion.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.Properties;

/**
 * Reset log4j appenders and initialize a standard logging structure
 *
 * /var/log/argos/serviceName.log
 *
 *
 */
public class BootstrapDefaultLogging implements Bootstrap
{
    private static final Logger log = Logger.getLogger(BootstrapDefaultLogging.class);

    @Override
    public Bootstrap boot() throws BootstrapException
    {
        Logger.getRootLogger().getLoggerRepository().resetConfiguration();

        Properties p = new Properties();
        try
        {
            p.load(Configuration.getInstance().getResource("log4j.properties"));

            p.put("log.dir", Configuration.getInstance().getString("log.dir", "/var/log/argos"));
            p.put("log.name", Configuration.getInstance().getString("log.name", "info.log"));
            p.put("serviceName", Configuration.getInstance().getString("serviceName", "unknown"));

            // TODO when you have multiple instances of a service, need to add a tag indicating which instance (hostname?)

            PropertyConfigurator.configure(p);
        }
        catch (IOException e)
        {
            log.error("", e);
            throw new BootstrapException(e);
        }

        return this;
    }

    @Override
    public void teardown()
    {

    }
}
