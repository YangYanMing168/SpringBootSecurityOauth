package com.szu.service.impl;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.Assert;

public class InMemoryTokenStoreService implements TokenStore {
    private static final Log LOG = LogFactory.getLog(InMemoryTokenStoreService.class);

    private static final int DEFAULT_FLUSH_INTERVAL = 1000;
    private final ConcurrentHashMap<String, OAuth2AccessToken> accessTokenStore = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, OAuth2AccessToken> authenticationToAccessTokenStore = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, Collection<OAuth2AccessToken>> userNameToAccessTokenStore = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, Collection<OAuth2AccessToken>> clientIdToAccessTokenStore = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, OAuth2RefreshToken> refreshTokenStore = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, String> accessTokenToRefreshTokenStore = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, OAuth2Authentication> authenticationStore = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, OAuth2Authentication> refreshTokenAuthenticationStore = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, String> refreshTokenToAccessTokenStore = new ConcurrentHashMap();
    private final DelayQueue<InMemoryTokenStoreService.TokenExpiry> expiryQueue = new DelayQueue();
    private final ConcurrentHashMap<String, InMemoryTokenStoreService.TokenExpiry> expiryMap = new ConcurrentHashMap();
    private int flushInterval = 1000;
    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
    private AtomicInteger flushCounter = new AtomicInteger(0);

    public InMemoryTokenStoreService() {
    }

    public void setFlushInterval(int flushInterval) {
        this.flushInterval = flushInterval;
    }

    public int getFlushInterval() {
        return this.flushInterval;
    }

    public void clear() {
        this.accessTokenStore.clear();
        this.authenticationToAccessTokenStore.clear();
        this.clientIdToAccessTokenStore.clear();
        this.refreshTokenStore.clear();
        this.accessTokenToRefreshTokenStore.clear();
        this.authenticationStore.clear();
        this.refreshTokenAuthenticationStore.clear();
        this.refreshTokenToAccessTokenStore.clear();
        this.expiryQueue.clear();
    }

    public void setAuthenticationKeyGenerator(AuthenticationKeyGenerator authenticationKeyGenerator) {
        this.authenticationKeyGenerator = authenticationKeyGenerator;
    }

    public int getAccessTokenCount() {
        Assert.state(this.accessTokenStore.isEmpty() || this.accessTokenStore.size() >= this.accessTokenToRefreshTokenStore.size(), "Too many refresh tokens");
        Assert.state(this.accessTokenStore.size() == this.authenticationToAccessTokenStore.size(), "Inconsistent token store state");
        Assert.state(this.accessTokenStore.size() <= this.authenticationStore.size(), "Inconsistent authentication store state");
        return this.accessTokenStore.size();
    }

    public int getRefreshTokenCount() {
        Assert.state(this.refreshTokenStore.size() == this.refreshTokenToAccessTokenStore.size(), "Inconsistent refresh token store state");
        return this.accessTokenStore.size();
    }

    public int getExpiryTokenCount() {
        return this.expiryQueue.size();
    }

    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        LOG.info("getAccessToken");
        String key = this.authenticationKeyGenerator.extractKey(authentication);
        OAuth2AccessToken accessToken = (OAuth2AccessToken) this.authenticationToAccessTokenStore.get(key);
        if (accessToken != null && !key.equals(this.authenticationKeyGenerator.extractKey(this.readAuthentication(accessToken.getValue())))) {
            LOG.info("保存 accessToken");
            this.storeAccessToken(accessToken, authentication);
        }
//        if (accessToken != null)
        LOG.info("accessToken 是否为 NULL  " + (accessToken == null));

        return accessToken;
    }

    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        LOG.info("readAuthentication");
        return this.readAuthentication(token.getValue());
    }

    public OAuth2Authentication readAuthentication(String token) {
        LOG.info("readAuthentication");
        return (OAuth2Authentication) this.authenticationStore.get(token);
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        LOG.info("readAuthenticationForRefreshToken");
        return this.readAuthenticationForRefreshToken(token.getValue());
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(String token) {
        LOG.info("readAuthenticationForRefreshToken");
        return (OAuth2Authentication) this.refreshTokenAuthenticationStore.get(token);
    }

    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        LOG.info("storeAccessToken");
        if (this.flushCounter.incrementAndGet() >= this.flushInterval) {
            this.flush();
            this.flushCounter.set(0);
        }

        this.accessTokenStore.put(token.getValue(), token);
        this.authenticationStore.put(token.getValue(), authentication);
        this.authenticationToAccessTokenStore.put(this.authenticationKeyGenerator.extractKey(authentication), token);
        if (!authentication.isClientOnly()) {
            this.addToCollection(this.userNameToAccessTokenStore, this.getApprovalKey(authentication), token);
        }

        this.addToCollection(this.clientIdToAccessTokenStore, authentication.getOAuth2Request().getClientId(), token);
        if (token.getExpiration() != null) {
            InMemoryTokenStoreService.TokenExpiry expiry = new InMemoryTokenStoreService.TokenExpiry(token.getValue(), token.getExpiration());
            this.expiryQueue.remove(this.expiryMap.put(token.getValue(), expiry));
            this.expiryQueue.put(expiry);
        }

        if (token.getRefreshToken() != null && token.getRefreshToken().getValue() != null) {
            this.refreshTokenToAccessTokenStore.put(token.getRefreshToken().getValue(), token.getValue());
            this.accessTokenToRefreshTokenStore.put(token.getValue(), token.getRefreshToken().getValue());
        }

    }

    private String getApprovalKey(OAuth2Authentication authentication) {
        String userName = authentication.getUserAuthentication() == null ? "" : authentication.getUserAuthentication().getName();
        return this.getApprovalKey(authentication.getOAuth2Request().getClientId(), userName);
    }

    private String getApprovalKey(String clientId, String userName) {
        return clientId + (userName == null ? "" : ":" + userName);
    }

    private void addToCollection(ConcurrentHashMap<String, Collection<OAuth2AccessToken>> store, String key, OAuth2AccessToken token) {
        if (!store.containsKey(key)) {
            synchronized (store) {
                if (!store.containsKey(key)) {
                    store.put(key, new HashSet());
                }
            }
        }

        ((Collection) store.get(key)).add(token);
    }

    public void removeAccessToken(OAuth2AccessToken accessToken) {
        LOG.info("removeAccessToken");
        this.removeAccessToken(accessToken.getValue());
    }

    public OAuth2AccessToken readAccessToken(String tokenValue) {
        LOG.info("readAccessToken");
        return (OAuth2AccessToken) this.accessTokenStore.get(tokenValue);
    }

    public void removeAccessToken(String tokenValue) {
        OAuth2AccessToken removed = (OAuth2AccessToken) this.accessTokenStore.remove(tokenValue);
        this.accessTokenToRefreshTokenStore.remove(tokenValue);
        OAuth2Authentication authentication = (OAuth2Authentication) this.authenticationStore.remove(tokenValue);
        if (authentication != null) {
            this.authenticationToAccessTokenStore.remove(this.authenticationKeyGenerator.extractKey(authentication));
            String clientId = authentication.getOAuth2Request().getClientId();
            Collection<OAuth2AccessToken> tokens = (Collection) this.userNameToAccessTokenStore.get(this.getApprovalKey(clientId, authentication.getName()));
            if (tokens != null) {
                tokens.remove(removed);
            }

            tokens = (Collection) this.clientIdToAccessTokenStore.get(clientId);
            if (tokens != null) {
                tokens.remove(removed);
            }

            this.authenticationToAccessTokenStore.remove(this.authenticationKeyGenerator.extractKey(authentication));
        }

    }

    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        LOG.info("storeRefreshToken");
        this.refreshTokenStore.put(refreshToken.getValue(), refreshToken);
        this.refreshTokenAuthenticationStore.put(refreshToken.getValue(), authentication);
    }

    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        LOG.info("readRefreshToken");
        return (OAuth2RefreshToken) this.refreshTokenStore.get(tokenValue);
    }

    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
        LOG.info("removeRefreshToken");
        this.removeRefreshToken(refreshToken.getValue());
    }

    public void removeRefreshToken(String tokenValue) {
        this.refreshTokenStore.remove(tokenValue);
        this.refreshTokenAuthenticationStore.remove(tokenValue);
        this.refreshTokenToAccessTokenStore.remove(tokenValue);
    }

    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        LOG.info("removeAccessTokenUsingRefreshToken");
        this.removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }

    private void removeAccessTokenUsingRefreshToken(String refreshToken) {
        String accessToken = (String) this.refreshTokenToAccessTokenStore.remove(refreshToken);
        if (accessToken != null) {
            this.removeAccessToken(accessToken);
        }

    }

    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        LOG.info("findTokensByClientIdAndUserName");
        Collection<OAuth2AccessToken> result = (Collection) this.userNameToAccessTokenStore.get(this.getApprovalKey(clientId, userName));
        return (Collection) (result != null ? Collections.unmodifiableCollection(result) : Collections.emptySet());
    }

    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        LOG.info("findTokensByClientId");
        Collection<OAuth2AccessToken> result = (Collection) this.clientIdToAccessTokenStore.get(clientId);
        return (Collection) (result != null ? Collections.unmodifiableCollection(result) : Collections.emptySet());
    }

    private void flush() {
        for (InMemoryTokenStoreService.TokenExpiry expiry = (InMemoryTokenStoreService.TokenExpiry) this.expiryQueue.poll(); expiry != null; expiry = (InMemoryTokenStoreService.TokenExpiry) this.expiryQueue.poll()) {
            this.removeAccessToken(expiry.getValue());
        }

    }

    private static class TokenExpiry implements Delayed {
        private final long expiry;
        private final String value;

        public TokenExpiry(String value, Date date) {
            this.value = value;
            this.expiry = date.getTime();
        }

        public int compareTo(Delayed other) {
            if (this == other) {
                return 0;
            } else {
                long diff = this.getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
                return diff == 0L ? 0 : (diff < 0L ? -1 : 1);
            }
        }

        public long getDelay(TimeUnit unit) {
            return this.expiry - System.currentTimeMillis();
        }

        public String getValue() {
            return this.value;
        }
    }
}
