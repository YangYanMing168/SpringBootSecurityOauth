package com.szu.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

//    private static final String RESOURCE_ID = "my_rest_api";
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) {
//        resources.resourceId(RESOURCE_ID).stateless(false);
//    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
//        http.
//                csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .and()
//                .anonymous().disable()
//                .authorizeRequests().anyRequest().access("hasRole('ADMIN')")
//		.requestMatchers().antMatchers("/user/**")
//		.and().authorizeRequests()
//		.antMatchers("/user/**").access("hasRole('ADMIN')")
//                .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

}