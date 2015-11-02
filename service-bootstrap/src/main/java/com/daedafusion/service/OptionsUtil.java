package com.daedafusion.service;

import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

/**
 * Created by mphilpot on 7/24/14.
 */
public class OptionsUtil
{
    private static final Logger log = Logger.getLogger(OptionsUtil.class);

    public static Options getStandardServerOptions()
    {
        Options options = new Options();

        options.addOption("h", "help", false, "Show help message");
        options.addOption("b", "bootstrap", true, "Alternate location of bootstrap.properties");
        options.addOption("H", "hostname", true, "Hostname to use for service discovery registration");
        options.addOption("s", "ssl", false, "Enable SSL");
        options.addOption("m", "mutual", false, "Enable mutual SSL");

        return options;
    }
}
