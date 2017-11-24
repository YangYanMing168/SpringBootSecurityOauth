package com.szu.dao;

import com.szu.entity.OauthAccessTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by kamyang on 2017/11/18.
 */
public interface OauthAccessTokenDao extends JpaRepository<OauthAccessTokenEntity, String> {
    //    insertAccessTokenSql = "insert into oauth_access_token (token_id, token, authentication_id, user_name, client_id, authentication, refresh_token) values (?, ?, ?, ?, ?, ?, ?)";
//    selectAccessTokenSql = "select token_id, token from oauth_access_token where token_id = ?";
//    selectAccessTokenAuthenticationSql = "select token_id, authentication from oauth_access_token where token_id = ?";
//    selectAccessTokenFromAuthenticationSql = "select token_id, token from oauth_access_token where authentication_id = ?";
//    selectAccessTokensFromUserNameAndClientIdSql = "select token_id, token from oauth_access_token where user_name = ? and client_id = ?";
//    selectAccessTokensFromUserNameSql = "select token_id, token from oauth_access_token where user_name = ?";
//    selectAccessTokensFromClientIdSql = "select token_id, token from oauth_access_token where client_id = ?";
//    deleteAccessTokenSql = "delete from oauth_access_token where token_id = ?"
//    deleteAccessTokenFromRefreshTokenSql = "delete from oauth_access_token where refresh_token = ?";

    OauthAccessTokenEntity findById(String id);
    OauthAccessTokenEntity findByAuthenticationId(String id);

    List<OauthAccessTokenEntity> findAllByUserNameAndClientId(String username, String clientId);

    OauthAccessTokenEntity findAllByUserName(String username);

    List<OauthAccessTokenEntity> findAllByClientId(String clientId);

    OauthAccessTokenEntity findByRefreshToken(String refreshToken);
    void deleteByRefreshToken(String refreshToken);
}
