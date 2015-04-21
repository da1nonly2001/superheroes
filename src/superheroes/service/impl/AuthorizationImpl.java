package superheroes.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import superheroes.dao.KeyDao;
import superheroes.entity.Key;
import superheroes.service.Authorization;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by catop on 4/7/15.
 */
@Component
public class AuthorizationImpl implements Authorization, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LogManager.getLogger(AuthorizationImpl.class.getName());
    private static Key privateKey;
    private static Key publicKey;
    private Long timestamp;
    private static Boolean hasStarted = Boolean.FALSE;

    @Autowired
    KeyDao keyDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        LOGGER.trace("IN");
        if (!hasStarted) {
            hasStarted = Boolean.TRUE;
            this.init();
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    private void init() {
        LOGGER.trace("IN");
        privateKey = keyDao.getKey(Key.PRIVATE_KEY);
        publicKey = keyDao.getKey(Key.PUBLIC_KEY);
    }

    @Override
    public String getApiKey() {
        LOGGER.trace("IN");
        String result = null;
        if (publicKey == null) {
            LOGGER.warn("publicKey is null");
        } else {
            result = publicKey.getValue();
        }
        return result;
    }

    /**
     * The timestamp returned by this method must be the same
     * used when calculating the MD5 hash used in getMd5Hash
     * method.  Therefore, call this method before calling
     * getMd5Hash().
     *
     * @return An updated timestamp value
     */
    @Override
    public Long getTimestamp() {
        timestamp = new Date().getTime();
        return timestamp;
    }

    /**
     * This method requires a valid timestamp value, which
     * can only be set by calling the getTimestamp method
     * before using this method.  Failure to do so will
     * cause an exception to be thrown.
     *
     * @return An md5 hash that Marvel requires when making web service client calls
     */
    @Override
    public String getMd5Hash() {
        LOGGER.trace("IN");
        String hash = null;
        if (timestamp == null) {
            String errorMsg = "timestamp is null, call getTimestamp() to correct this illegal state";
            LOGGER.error(errorMsg);
            throw new IllegalStateException(errorMsg);
        } else if (privateKey == null || publicKey == null) {
            String errorMsg = "keys have not been set adequately, null value detected";
            LOGGER.error(errorMsg);
            throw new IllegalStateException(errorMsg);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(timestamp.toString()).append(privateKey.getValue()).append(publicKey.getValue());
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(sb.toString().getBytes("UTF-8"));
            BigInteger bigInteger = new BigInteger(1, messageDigest);
            hash = bigInteger.toString(16);
            while (hash.length() < 32) {
                hash = "0" + hash;
            }
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Unable to attain message digest", e);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Unable to digest with the provided encoding value", e);
        }
        if (hash == null) {
            LOGGER.warn("Returning hash=null");
        }
        timestamp = null;
        return hash;
    }
}