package com.szu.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by kamyang on 2017/11/17.
 */
@Entity
public class Oauth2ClientEntity {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    private String client;
    private String secret;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createTime;
    @OneToMany(targetEntity = Oauth2AuthorityEntity.class, fetch = FetchType.LAZY
            , cascade = CascadeType.ALL)
    @JoinColumn(name = "clientId", referencedColumnName = "id")
    Set<Oauth2AuthorityEntity> authorityEntities;
    @OneToMany(targetEntity = Oauth2ScoeEntity.class, fetch = FetchType.LAZY
            , cascade = CascadeType.ALL)
    @JoinColumn(name = "clientId", referencedColumnName = "id")
    Set<Oauth2ScoeEntity> scoeEntities;
    @OneToMany(targetEntity = Oauth2AuthorizedGrantTypeEntity.class, fetch = FetchType.LAZY
            , cascade = CascadeType.ALL)
    @JoinColumn(name = "clientId", referencedColumnName = "id")
    Set<Oauth2AuthorizedGrantTypeEntity> grantTypeEntities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Set<Oauth2AuthorityEntity> getAuthorityEntities() {
        return authorityEntities;
    }

    public void setAuthorityEntities(Set<Oauth2AuthorityEntity> authorityEntities) {
        this.authorityEntities = authorityEntities;
    }

    public Set<Oauth2ScoeEntity> getScoeEntities() {
        return scoeEntities;
    }

    public void setScoeEntities(Set<Oauth2ScoeEntity> scoeEntities) {
        this.scoeEntities = scoeEntities;
    }

    public Set<Oauth2AuthorizedGrantTypeEntity> getGrantTypeEntities() {
        return grantTypeEntities;
    }

    public void setGrantTypeEntities(Set<Oauth2AuthorizedGrantTypeEntity> grantTypeEntities) {
        this.grantTypeEntities = grantTypeEntities;
    }
}
