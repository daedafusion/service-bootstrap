package com.daedafusion.service.bootstrap;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeThat;

/**
 * Created by mphilpot on 6/30/14.
 */
public class BootstrapConfigTest
{
    private static final Logger log = Logger.getLogger(BootstrapConfigTest.class);

    @BeforeClass
    public static void beforeClass()
    {
        assumeThat(System.getProperty("epiphyte"), is(nullValue()));
        assumeThat(System.getProperty("etc/test"), is(nullValue()));
    }

    @Before
    public void before()
    {

    }

    @After
    public void after()
    {
        System.clearProperty("epiphyte");
        System.clearProperty("etc/test");
    }

    @Test
    public void testDefault() throws BootstrapException
    {
        Bootstrap bc = new BootstrapConfig().boot();

        assertThat(System.getProperty("epiphyte"), is("avi"));
        assertThat(System.getProperty("etc/test"), is("true"));
    }

    @Test
    public void testNonExist() throws BootstrapException
    {
        BootstrapConfig bc = new BootstrapConfig();
        bc.setBootstrapFile("this file doesn't exist");
        bc.boot();

        assertThat(System.getProperty("epiphyte"), is(nullValue()));
        assertThat(System.getProperty("etc/test"), is(nullValue()));
    }


}
