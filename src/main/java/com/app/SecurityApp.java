package com.app;

import com.szu.dao.Oauth2ClientDao;
import com.szu.dao.user.UserInfoDao;
import com.szu.entity.Oauth2AuthorityEntity;
import com.szu.entity.Oauth2AuthorizedGrantTypeEntity;
import com.szu.entity.Oauth2ClientEntity;
import com.szu.entity.Oauth2ScoeEntity;
import com.szu.entity.user.UserInfoEntity;
import com.szu.service.RegisterService;
import com.szu.service.impl.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

/**
 * Created by kamyang on 2017/11/16.
 */
@SpringBootApplication
//@ComponentScan(value = {"com.szu"})
public class SecurityApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SecurityApp.class, args);
//        List<String> strings = new ArrayList<>();
//        strings.add("123");
//        strings.add("123");
//        strings.add("123");
//        Map<String,Object> map = new HashMap<>();
//        map.put("123",new Integer(12));
//        map.put("abc",new Integer(12));
//        map.put("efg",new Double(3457));
//        System.out.println(strings.toString());
//        System.out.println(map.toString());
//        System.exit(0);
    }
}
