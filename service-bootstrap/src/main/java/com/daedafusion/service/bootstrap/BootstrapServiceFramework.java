package com.daedafusion.service.bootstrap;

import com.daedafusion.sf.ServiceFramework;
import com.daedafusion.sf.ServiceFrameworkException;
import com.daedafusion.sf.ServiceFrameworkFactory;
import org.apache.log4j.Logger;

/**
 * Created by mphilpot on 7/24/14.
 */
public class BootstrapServiceFramework implements Bootstrap
{
    private static final Logger log = Logger.getLogger(BootstrapServiceFramework.class);

    @Override
    public Bootstrap boot() throws BootstrapException
    {
        ServiceFramework framework = ServiceFrameworkFactory.getInstance().getFramework();
        try
        {
            framework.start();
        }
        catch (ServiceFrameworkException e)
        {
            log.error("", e);
            throw new BootstrapException(e);
        }

        return this;
    }

    @Override
    public void teardown()
    {
        ServiceFramework framework = ServiceFrameworkFactory.getInstance().getFramework();

        framework.stop();

        ServiceFrameworkFactory.getInstance().destroy();
    }
}
