package com.szu.service.impl;

import com.sun.media.jfxmedia.logging.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Created by kamyang on 2017/11/16.
 */
@Service
public class AuthenticationProviderCustom extends DaoAuthenticationProvider {
    public AuthenticationProviderCustom(UserDetailsService userDetailsService) {
        setUserDetailsService(userDetailsService);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        super.additionalAuthenticationChecks(userDetails, usernamePasswordAuthenticationToken);
        Logger.logMsg(Logger.DEBUG,
                "AuthenticationProviderCustom",
                "additionalAuthenticationChecks",
                "进行校验");
        System.out.println("进行校验");
    }

//    @Autowired
//    private UserDetailsService userDetailsService;

//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//        String password = (String) authentication.getCredentials();
//        UserDetails user = userDetailsService.loadUserByUsername(username);
//        if (user == null) {
//            throw new BadCredentialsException("Username not found.");
//        }
//        System.out.println("验证密码    " + password);
//        //加密过程在这里体现
//        if (!password.equals(user.getPassword())) {
//            throw new BadCredentialsException("Wrong password.");
//        }
//
//        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
//        return new UsernamePasswordAuthenticationToken(user, password, authorities);
//    }
//
//    @Override
//    public boolean supports(Class<?> aClass) {
//        return true;
//    }
}
