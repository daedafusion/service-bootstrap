package com.daedafusion.service;

import com.daedafusion.configuration.Configuration;
import com.daedafusion.crypto.CryptoFactory;
import com.daedafusion.crypto.keys.KeyMaterialException;
import com.daedafusion.sf.ServiceFramework;
import com.daedafusion.sf.ServiceFrameworkFactory;
import com.daedafusion.security.authentication.Authentication;
import com.daedafusion.security.authentication.Subject;
import com.daedafusion.security.authentication.Token;
import com.daedafusion.security.authentication.TokenExchange;
import com.daedafusion.security.common.Callback;
import com.daedafusion.security.common.CallbackHandler;
import com.daedafusion.security.exceptions.AccountLockedException;
import com.daedafusion.security.exceptions.AuthenticationFailedException;
import com.daedafusion.security.exceptions.PasswordQualityException;
import com.daedafusion.security.exceptions.PasswordResetRequiredException;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import java.security.KeyStoreException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

/**
 * Created by mphilpot on 8/27/14.
 */
public class ServiceIdentity
{
    private static final Logger log = Logger.getLogger(ServiceIdentity.class);

    private static ServiceIdentity ourInstance = new ServiceIdentity();

    public static ServiceIdentity getInstance()
    {
        return ourInstance;
    }

    private Token currentToken;

    private ServiceIdentity()
    {
        reAuthenticate();
    }

    public String getToken()
    {
        return currentToken.getTokenString();
    }

    public synchronized void checkAuth()
    {
        ServiceFramework framework = ServiceFrameworkFactory.getInstance().getFramework();
        TokenExchange exchange = framework.getService(TokenExchange.class);

        if(!exchange.isTokenValid(currentToken))
        {
            reAuthenticate();
        }
    }

    public synchronized boolean reAuthenticate()
    {
        try
        {
            ServiceFramework framework = ServiceFrameworkFactory.getInstance().getFramework();

            Authentication auth = framework.getService(Authentication.class);
            TokenExchange exchange = framework.getService(TokenExchange.class);

            Subject subject = auth.login(new CallbackHandler()
            {
                @Override
                public void handle(Callback... callbacks)
                {
                    for(Callback cb : callbacks)
                    {
                        if(cb.getName().equals(Callback.X509))
                        {
                            try
                            {
                                X509Certificate cert = (X509Certificate) CryptoFactory.getInstance().getKeyStore().getCertificate(
                                        Configuration.getInstance().getString("serviceName")
                                );

                                cb.setValue(Base64.encodeBase64String(cert.getEncoded()));
                            }
                            catch (KeyStoreException | KeyMaterialException | CertificateEncodingException e)
                            {
                                log.error("", e);
                            }
                        }
                    }
                }
            });

            currentToken = exchange.exchange(subject);

            return true;
        }
        catch(AuthenticationFailedException e)
        {
            log.error("Authentication Failure", e);
        }
        catch (PasswordQualityException | PasswordResetRequiredException | AccountLockedException e)
        {
            log.error("Unexpected Exception (should not happen)", e);
        }

        return false;
    }
}
