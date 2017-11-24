package com.szu.dao;

import com.szu.entity.Oauth2ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by kamyang on 2017/11/17.
 */
public interface Oauth2ClientDao extends JpaRepository<Oauth2ClientEntity, Long> {
    /**
     * 通过客户端名字进行查找客户端信息
     *
     * @param client 要查找的客户端
     * @return 返回查找到的客户端
     */
    Oauth2ClientEntity findAllByClient(String client);
}
