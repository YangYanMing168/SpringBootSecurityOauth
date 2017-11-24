package com.szu.service.impl;

import com.szu.dao.Oauth2ClientDao;
import com.szu.entity.Oauth2AuthorityEntity;
import com.szu.entity.Oauth2AuthorizedGrantTypeEntity;
import com.szu.entity.Oauth2ClientEntity;
import com.szu.entity.Oauth2ScoeEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kamyang on 2017/11/17.
 */
public class ClientRegisterService {
    @Autowired
    private Oauth2ClientDao clientDao;

    public void register() {
        Oauth2ClientEntity clientEntity = new Oauth2ClientEntity();
        clientEntity.setClient("my-trusted-client");
        clientEntity.setSecret("secret");

        Set<Oauth2AuthorityEntity> authorityEntities = new HashSet<>();
        Oauth2AuthorityEntity authorityEntity = new Oauth2AuthorityEntity();
        authorityEntity.setAuthority("ROLE_CLIENT");
        authorityEntities.add(authorityEntity);
        authorityEntity = new Oauth2AuthorityEntity();
        authorityEntity.setAuthority("ROLE_TRUSTED_CLIENT");
        authorityEntities.add(authorityEntity);
        clientEntity.setAuthorityEntities(authorityEntities);

        Set<Oauth2AuthorizedGrantTypeEntity> grantTypeEntities = new HashSet<>();
        Oauth2AuthorizedGrantTypeEntity grantTypeEntity = new Oauth2AuthorizedGrantTypeEntity();
        grantTypeEntity.setAuthorizedGrantType("client_credentials");
        grantTypeEntities.add(grantTypeEntity);
        grantTypeEntity = new Oauth2AuthorizedGrantTypeEntity();
        grantTypeEntity.setAuthorizedGrantType("password");
        grantTypeEntities.add(grantTypeEntity);
        grantTypeEntity = new Oauth2AuthorizedGrantTypeEntity();
        grantTypeEntity.setAuthorizedGrantType("authorization_code");
        grantTypeEntities.add(grantTypeEntity);
        grantTypeEntity = new Oauth2AuthorizedGrantTypeEntity();
        grantTypeEntity.setAuthorizedGrantType("refresh_token");
        grantTypeEntities.add(grantTypeEntity);
        grantTypeEntity = new Oauth2AuthorizedGrantTypeEntity();
        grantTypeEntity.setAuthorizedGrantType("implicit");
        grantTypeEntities.add(grantTypeEntity);
        clientEntity.setGrantTypeEntities(grantTypeEntities);

        Set<Oauth2ScoeEntity> scoeEntities = new HashSet<>();
        Oauth2ScoeEntity scoeEntity = new Oauth2ScoeEntity();
        scoeEntity.setScope("read");
        scoeEntities.add(scoeEntity);
        scoeEntity = new Oauth2ScoeEntity();
        scoeEntity.setScope("write");
        scoeEntities.add(scoeEntity);
        scoeEntity = new Oauth2ScoeEntity();
        scoeEntity.setScope("trust");
        scoeEntities.add(scoeEntity);
        clientEntity.setScoeEntities(scoeEntities);
        clientDao.save(clientEntity);
    }
}
