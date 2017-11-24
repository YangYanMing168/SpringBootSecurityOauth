package com.szu.service.impl;

import com.szu.dao.OauthAccessTokenDao;
import com.szu.dao.OauthRefreshTokenDao;
import com.szu.entity.OauthAccessTokenEntity;
import com.szu.entity.OauthRefreshTokenEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@EnableTransactionManagement
public class TokenStoreServiceImpl implements TokenStore {
    private static final Log LOG = LogFactory.getLog(TokenStoreServiceImpl.class);

    @Autowired
    private OauthAccessTokenDao accessTokenDao;
    @Autowired
    private OauthRefreshTokenDao refreshTokenDao;
    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    public void setAuthenticationKeyGenerator(AuthenticationKeyGenerator authenticationKeyGenerator) {
        this.authenticationKeyGenerator = authenticationKeyGenerator;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken oAuth2AccessToken) {
        return readAuthentication(oAuth2AccessToken.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        LOG.info("readAuthentication  读取信息");
        OAuth2Authentication authentication = null;

        try {
            OauthAccessTokenEntity accessTokenEntity = accessTokenDao.findById(this.extractTokenKey(token));
            if (accessTokenEntity != null) {
                byte[] data = accessTokenEntity.getAuthentication();
                if (data != null)
                    authentication = this.deserializeAuthentication(data);
            }
        } catch (EmptyResultDataAccessException var4) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for token " + token);
            }
        } catch (IllegalArgumentException var5) {
            LOG.warn("Failed to deserialize authentication for " + token, var5);
            this.removeAccessToken(token);
        }
        return authentication;
    }

    @Override
    @Transactional
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        LOG.info("storeAccessToken  ");
        System.out.println("accessToken scope:" + token.getScope().toString()
                + " refreshToken " + token.getRefreshToken().getValue()
                + " ExpiresIn    " + token.getExpiresIn()
                + "  Expiration  " + token.getExpiration().toString()
                + "  value   " + token.getValue()
                + "  TokenType   " + token.getTokenType()
                + "  AdditionalInformation  " + token.getAdditionalInformation().toString());
        System.out.println("\n"+authentication.toString());
        String refreshToken = null;
        if (token.getRefreshToken() != null) {
            refreshToken = token.getRefreshToken().getValue();
        }

        if (this.readAccessToken(token.getValue()) != null) {
            this.removeAccessToken(token.getValue());
        }
        OauthAccessTokenEntity accessTokenEntity = new OauthAccessTokenEntity();
        accessTokenEntity.setId(this.extractTokenKey(token.getValue()));
        byte[] data = this.serializeAccessToken(token);
        LOG.info("AccessTokenLength  " + data.length);
        accessTokenEntity.setToken(data);
        accessTokenEntity.setAuthenticationId(this.authenticationKeyGenerator.extractKey(authentication));
        accessTokenEntity.setUserName(authentication.isClientOnly() ? null : authentication.getName());
        accessTokenEntity.setClientId(authentication.getOAuth2Request().getClientId());
        data = this.serializeAuthentication(authentication);
        LOG.info("AuthenticationLength  " + data.length);
        accessTokenEntity.setAuthentication(data);
        accessTokenEntity.setRefreshToken(this.extractTokenKey(refreshToken));//进行加密
        accessTokenDao.save(accessTokenEntity);//this.extractTokenKey(refreshToken)
    }

    @Override
    public OAuth2AccessToken readAccessToken(String token) {
        LOG.info("readAccessToken  ");
        OAuth2AccessToken accessToken = null;

        try {
            OauthAccessTokenEntity accessTokenEntity = accessTokenDao.findById(this.extractTokenKey(token));
            if (accessTokenEntity != null) {
                byte[] data = accessTokenEntity.getToken();
                if (data != null)
                    accessToken = this.deserializeAccessToken(data);
            }
        } catch (EmptyResultDataAccessException var4) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for token " + token);
            }
        } catch (IllegalArgumentException var5) {
            LOG.warn("Failed to deserialize access token for " + token, var5);
            this.removeAccessToken(token);
        }
        return accessToken;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        this.removeAccessToken(token.getValue());
    }

    @Transactional
    public void removeAccessToken(String tokenValue) {
        accessTokenDao.delete(this.extractTokenKey(tokenValue));
    }

    @Override
    @Transactional
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        LOG.info("storeRefreshToken");
        OauthRefreshTokenEntity refreshTokenEntity = new OauthRefreshTokenEntity();
        refreshTokenEntity.setOauthRefreshToken(this.extractTokenKey(refreshToken.getValue()));
        refreshTokenEntity.setToken(this.serializeRefreshToken(refreshToken));
        refreshTokenEntity.setAuthentication(this.serializeAuthentication(authentication));
        refreshTokenDao.save(refreshTokenEntity);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String token) {
        LOG.info("OAuth2RefreshToken");
        OAuth2RefreshToken refreshToken = null;

        try {
            OauthRefreshTokenEntity refreshTokenEntity = refreshTokenDao.findOne(this.extractTokenKey(token));
            if (refreshTokenEntity != null) {
                byte[] data = refreshTokenEntity.getToken();
                if (data != null)
                    refreshToken = this.deserializeRefreshToken(data);
            }
        } catch (EmptyResultDataAccessException var4) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find refresh token for token " + token);
            }
        } catch (IllegalArgumentException var5) {
            LOG.warn("Failed to deserialize refresh token for token " + token, var5);
            this.removeRefreshToken(token);
        }

        return refreshToken;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        LOG.info("readAuthenticationForRefreshToken");
        return this.readAuthenticationForRefreshToken(oAuth2RefreshToken.getValue());
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(String tokenValue) {
        LOG.info("readAuthenticationForRefreshToken");
        OAuth2Authentication authentication = null;

        try {
            OauthRefreshTokenEntity refreshTokenEntity = refreshTokenDao.findOne(this.extractTokenKey(tokenValue));
            if (refreshTokenEntity != null) {
                byte[] data = refreshTokenEntity.getAuthentication();
                if (data != null)
                    authentication = this.deserializeAuthentication(data);
            }
        } catch (EmptyResultDataAccessException var4) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for token " + tokenValue);
            }
        } catch (IllegalArgumentException var5) {
            LOG.warn("Failed to deserialize access token for " + tokenValue, var5);
            this.removeRefreshToken(tokenValue);
        }

        return authentication;
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
        this.removeRefreshToken(refreshToken);
    }

    @Transactional
    public void removeRefreshToken(String token) {
        refreshTokenDao.delete(this.extractTokenKey(token));
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        this.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken.getValue());
    }

    @Transactional
    public void removeAccessTokenUsingRefreshToken(String refreshToken) {
        String id = this.extractTokenKey(refreshToken);
        accessTokenDao.deleteByRefreshToken(id);
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        LOG.info("getAccessToken    " + authentication.toString());
        OAuth2AccessToken accessToken = null;
        String key = this.authenticationKeyGenerator.extractKey(authentication);

        try {
            OauthAccessTokenEntity accessTokenEntity = accessTokenDao.findByAuthenticationId(key);
            if (accessTokenEntity != null) {
                byte[] data = accessTokenEntity.getToken();
                if (data != null)
                    accessToken = this.deserializeAccessToken(data);
            }
        } catch (EmptyResultDataAccessException var5) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Failed to find access token for authentication " + authentication);
            }
        } catch (IllegalArgumentException var6) {
            LOG.error("Could not extract access token for authentication " + authentication, var6);
        }

        if (accessToken != null && !key.equals(this.authenticationKeyGenerator.extractKey(this.readAuthentication(accessToken.getValue())))) {
            this.removeAccessToken(accessToken.getValue());
            this.storeAccessToken(accessToken, authentication);
        }
        LOG.info("accessToken 是否为 NULL  " + (accessToken == null));
        return accessToken;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        List<OauthAccessTokenEntity> accessTokenEntities = new ArrayList();

        try {
            accessTokenEntities = accessTokenDao.findAllByUserNameAndClientId(userName, clientId);
        } catch (EmptyResultDataAccessException var5) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for clientId " + clientId + " and userName " + userName);
            }
        }

        List<OAuth2AccessToken> oAuth2AccessTokens = this.removeNulls(accessTokenEntities);
        return oAuth2AccessTokens;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        List<OauthAccessTokenEntity> accessTokenEntities = new ArrayList();
        try {
            accessTokenEntities = accessTokenDao.findAllByClientId(clientId);
        } catch (EmptyResultDataAccessException var4) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for clientId " + clientId);
            }
        }

        List<OAuth2AccessToken> oAuth2AccessTokens = this.removeNulls(accessTokenEntities);
        return oAuth2AccessTokens;
    }

    private List<OAuth2AccessToken> removeNulls(List<OauthAccessTokenEntity> accessTokenEntities) {
        List<OAuth2AccessToken> tokens = new ArrayList();
        if (accessTokenEntities == null)
            return tokens;
        Iterator var3 = accessTokenEntities.iterator();
        for (OauthAccessTokenEntity accessTokenEntity : accessTokenEntities) {
            byte[] data = accessTokenEntity.getToken();
            OAuth2AccessToken token = null;
            if (data != null)
                token = deserializeAccessToken(data);
            if (token != null) {
                tokens.add(token);
            }
        }

        return tokens;
    }

    protected String extractTokenKey(String value) {
        if (value == null) {
            return null;
        } else {
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException var5) {
                throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
            }

            try {
                byte[] bytes = digest.digest(value.getBytes("UTF-8"));
                return String.format("%032x", new Object[]{new BigInteger(1, bytes)});
            } catch (UnsupportedEncodingException var4) {
                throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
            }
        }
    }

    protected byte[] serializeAccessToken(OAuth2AccessToken token) {
        return SerializationUtils.serialize(token);
    }

    protected byte[] serializeRefreshToken(OAuth2RefreshToken token) {
        return SerializationUtils.serialize(token);
    }

    protected byte[] serializeAuthentication(OAuth2Authentication authentication) {
        return SerializationUtils.serialize(authentication);
    }

    protected OAuth2AccessToken deserializeAccessToken(byte[] token) {
        return (OAuth2AccessToken) SerializationUtils.deserialize(token);
    }

    protected OAuth2RefreshToken deserializeRefreshToken(byte[] token) {
        return (OAuth2RefreshToken) SerializationUtils.deserialize(token);
    }

    protected OAuth2Authentication deserializeAuthentication(byte[] authentication) {
        return (OAuth2Authentication) SerializationUtils.deserialize(authentication);
    }
}