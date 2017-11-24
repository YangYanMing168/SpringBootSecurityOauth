package com.szu.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by kamyang on 2017/11/17.
 */
@Entity
public class Oauth2ScoeEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String scope;
    private Long clientId;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
