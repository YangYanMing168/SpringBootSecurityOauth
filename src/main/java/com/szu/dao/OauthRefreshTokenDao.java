package com.szu.dao;

import com.szu.entity.OauthRefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by kamyang on 2017/11/18.
 */
public interface OauthRefreshTokenDao extends JpaRepository<OauthRefreshTokenEntity, String> {
//    insertRefreshTokenSql = "insert into oauth_refresh_token (token_id, token, authentication) values (?, ?, ?)";
//    selectRefreshTokenSql = "select token_id, token from oauth_refresh_token where token_id = ?";
//    selectRefreshTokenAuthenticationSql = "select token_id, authentication from oauth_refresh_token where token_id = ?";
//    deleteRefreshTokenSql = "delete from oauth_refresh_token where token_id = ?";
//    deleteAccessTokenFromRefreshTokenSql = "delete from oauth_access_token where refresh_token = ?";
}
