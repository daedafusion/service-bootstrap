package com.daedafusion.service.bootstrap;

import org.apache.log4j.Logger;

/**
 * Created by mphilpot on 7/24/14.
 */
public class BootstrapException extends Exception
{
    private static final Logger log = Logger.getLogger(BootstrapException.class);

    public BootstrapException(Throwable cause)
    {
        super(cause);
    }
}
