package com.szu.service.impl;

import com.szu.dao.user.RoleDao;
import com.szu.dao.user.UserInfoDao;
import com.szu.entity.user.RoleEntity;
import com.szu.entity.user.UserInfoEntity;
import com.szu.pojo.UserDetailsPojo;
import com.szu.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    @Qualifier("userInfoDao")
    private UserInfoDao userInfoDao;
    @Autowired
    @Qualifier("roleDao")
    private RoleDao roleDao;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    @Transactional
    public boolean register(UserDetailsPojo userDetails) {
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setUsername(userDetails.getUsername());
        //进行加密处理
        String password = encoder.encode(userDetails.getPassword());
        userInfoEntity.setPassword(password);
        userInfoEntity = userInfoDao.save(userInfoEntity);
        List<RoleEntity> roleEntities = new ArrayList<>();
        for (String role : userDetails.getRoles()) {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setRole(role);
            roleEntity.setUserInfoEntity(userInfoEntity);
            roleEntities.add(roleEntity);
        }
        roleDao.save(roleEntities);
        return false;
    }

    @Override
    @Transactional
    public boolean saveUserDetail() {
        userInfoDao.updateUserInfoPassword("123456", 1l);
//        UserInfoEntity infoEntity = new UserInfoEntity();
//        infoEntity.setPassword("Kam123");
//        infoEntity.setUsername("KamaYang");
//        infoEntity.setId(1l);
//        userInfoDao.save(infoEntity);
        return true;
    }
}
