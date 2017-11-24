package com.szu.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by kamyang on 2017/11/17.
 */
@Entity
public class Oauth2AuthorizedGrantTypeEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String authorizedGrantType;
    private Long clientId;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorizedGrantType() {
        return authorizedGrantType;
    }

    public void setAuthorizedGrantType(String authorizedGrantType) {
        this.authorizedGrantType = authorizedGrantType;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
