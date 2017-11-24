package com.szu.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by kamyang on 2017/11/17.
 */
@Entity
public class Oauth2AuthorityEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String authority;
    private Long clientId;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
