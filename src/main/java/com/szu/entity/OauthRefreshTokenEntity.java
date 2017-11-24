package com.szu.entity;

import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import javax.persistence.*;

/**
 * Created by kamyang on 2017/11/17.
 */
@Entity
public class OauthRefreshTokenEntity {
    @Id
    private String oauthRefreshToken;
    @Column(length = 1024)
    private byte[] token; // OAuth2RefreshToken token;
    @Column(length = 1024 * 3)
    private byte[] authentication;// Authentication authentication;

    public String getOauthRefreshToken() {
        return oauthRefreshToken;
    }

    public void setOauthRefreshToken(String oauthRefreshToken) {
        this.oauthRefreshToken = oauthRefreshToken;
    }

    public byte[] getToken() {
        return token;
    }

    public void setToken(byte[] token) {
        this.token = token;
    }

    public byte[] getAuthentication() {
        return authentication;
    }

    public void setAuthentication(byte[] authentication) {
        this.authentication = authentication;
    }
}
