package com.daedafusion.service.bootstrap;

import com.daedafusion.crypto.Crypto;
import com.daedafusion.crypto.CryptoFactory;
import org.apache.log4j.Logger;

/**
 * Created by mphilpot on 6/30/14.
 */
public class BootstrapCrypto implements Bootstrap
{
    private static final Logger log = Logger.getLogger(BootstrapCrypto.class);

    @Override
    public Bootstrap boot() throws BootstrapException
    {
        // Cause crypto to initialize
        Crypto.getProperty(Crypto.KEY_MATERIAL_PROVIDER);

        CryptoFactory.getInstance();

        return this;
    }

    @Override
    public void teardown()
    {

    }
}
