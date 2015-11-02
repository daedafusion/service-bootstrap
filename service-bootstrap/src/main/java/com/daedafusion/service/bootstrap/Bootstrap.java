package com.daedafusion.service.bootstrap;

/**
 * Created by mphilpot on 6/30/14.
 */
public interface Bootstrap
{
    Bootstrap boot() throws BootstrapException;
    void teardown();
}
