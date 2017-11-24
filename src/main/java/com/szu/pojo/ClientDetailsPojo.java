package com.szu.pojo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by kamyang on 2017/11/17.
 */
public class ClientDetailsPojo implements ClientDetails {
    private String clientId;
    private Set<String> resourceIds;
    private String secret;
    private Set<String> scoes;
    private Set<String> grantTypes;
    private Set<String> redirectUris;
    private Collection<GrantedAuthority> authorities;
    private Integer accessTokenValiditySeconds = 60 * 60 * 24 * 7;//7天
    private Integer refreshTokenValiditySeconds = 60 * 60 * 24 * 30;//30天
    private Map<String, Object> additionalInformation;

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        return resourceIds;
    }

    @Override
    public boolean isSecretRequired() {
        if (secret == null)
            return false;
        else
            return true;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String getClientSecret() {
        return secret;
    }

    @Override
    public boolean isScoped() {
        if (scoes == null || scoes.size() == 0)
            return false;
        return true;
    }

    public void setScoes(Set<String> scoes) {
        this.scoes = scoes;
    }

    @Override
    public Set<String> getScope() {
        return scoes;
    }

    public void setGrantTypes(Set<String> grantTypes) {
        this.grantTypes = grantTypes;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return grantTypes;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return redirectUris;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    @Override
    public boolean isAutoApprove(String s) {
        return true;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }
}
