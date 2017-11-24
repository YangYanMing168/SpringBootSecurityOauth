package com.szu.dao;

import com.szu.entity.Oauth2AuthorizedGrantTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by kamyang on 2017/11/17.
 */
public interface Oauth2AuthorizedGrantTypeDao extends JpaRepository<Oauth2AuthorizedGrantTypeEntity,Long> {
}
