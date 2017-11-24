package com.szu.service.impl;

import com.szu.dao.Oauth2ClientDao;
import com.szu.entity.Oauth2AuthorityEntity;
import com.szu.entity.Oauth2AuthorizedGrantTypeEntity;
import com.szu.entity.Oauth2ClientEntity;
import com.szu.entity.Oauth2ScoeEntity;
import com.szu.pojo.ClientDetailsPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by kamyang on 2017/11/17.
 */
@Service
@EnableTransactionManagement
public class ClientDetailsServiceImpl implements ClientDetailsService {
    @Autowired
    private Oauth2ClientDao clientDao;

    @Override
    @Transactional
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        ClientDetailsPojo clientDetails = new ClientDetailsPojo();
        Oauth2ClientEntity clientEntity = clientDao.findAllByClient(s);
        clientDetails.setClientId(clientEntity.getClient());
        clientDetails.setSecret(clientEntity.getSecret());
        Set<Oauth2AuthorityEntity> authorityEntities = clientEntity.getAuthorityEntities();
        Set<Oauth2AuthorizedGrantTypeEntity> grantTypeEntities = clientEntity.getGrantTypeEntities();
        Set<Oauth2ScoeEntity> scoeEntities = clientEntity.getScoeEntities();
        Set<String> set = new HashSet<>();
        for (Oauth2AuthorizedGrantTypeEntity grantTypeEntity : grantTypeEntities) {
            set.add(grantTypeEntity.getAuthorizedGrantType());
        }
        clientDetails.setGrantTypes(set);
        set = new HashSet<>();
        for (Oauth2ScoeEntity scoeEntity : scoeEntities) {
            set.add(scoeEntity.getScope());
        }
        clientDetails.setScoes(set);
        List<GrantedAuthority> authorities = new ArrayList<>(scoeEntities.size());
        for (Oauth2AuthorityEntity authorityEntity : authorityEntities) {
            authorities.add(new SimpleGrantedAuthority(authorityEntity.getAuthority()));
        }
        clientDetails.setAuthorities(authorities);
        return clientDetails;
    }
}
