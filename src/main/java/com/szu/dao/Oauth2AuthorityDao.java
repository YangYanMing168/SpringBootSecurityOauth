package com.szu.dao;

import com.szu.entity.Oauth2AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by kamyang on 2017/11/17.
 */
public interface Oauth2AuthorityDao extends JpaRepository<Oauth2AuthorityEntity,Long> {

}
