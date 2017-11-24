package com.szu.service.impl;

import com.szu.dao.user.RoleDao;
import com.szu.dao.user.UserInfoDao;
import com.szu.entity.user.RoleEntity;
import com.szu.entity.user.UserInfoEntity;
import com.szu.pojo.UserDetailsPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamyang on 2017/11/16.
 */
@Service
@EnableTransactionManagement
public class LoginServiceImpl implements UserDetailsService {
    @Autowired
    @Qualifier("userInfoDao")
    private UserInfoDao userInfoDao;
    @Autowired
    @Qualifier("roleDao")
    private RoleDao roleDao;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        System.out.println("开始查询");
        UserInfoEntity userInfo = userInfoDao.findByUsername(name);
        List<RoleEntity> roleEntities = roleDao.findAllByUserInfoEntity(userInfo);
        List<String> roles = new ArrayList<>(roleEntities.size());
        for (RoleEntity role : roleEntities) {
            roles.add(role.getRole());
        }
        //在这里用 Id 进行替换用户名
        UserDetailsPojo userDetails = new UserDetailsPojo(userInfo.getId().toString(),
                userInfo.getPassword(), roles);

        return userDetails;
    }
}
