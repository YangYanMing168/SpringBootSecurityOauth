package com.szu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by kamyang on 2017/11/16.
 */
@RestController
public class HelloController {

    @RequestMapping(value = "/login1", method = RequestMethod.GET)
    public String login(String username, String password) {
        return username + " " + password;
    }

    @RequestMapping(value = {"/", "/welcome**"}, method = RequestMethod.GET)
    public String welcomePage() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return "welcome " + principal.getUsername()+"   "+principal.getPassword();
    }

    @RequestMapping(value = "/admin**", method = RequestMethod.GET)
    public String adminPage() {
        return "admin";

    }

    @RequestMapping(value = "/dba**", method = RequestMethod.GET)
    public String dbaPage() {
        return "adb";

    }
}
