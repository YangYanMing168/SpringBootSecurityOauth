package com.szu.service;

import com.szu.pojo.UserDetailsPojo;

/**
 * Created by kamyang on 2017/11/16.
 */
public interface RegisterService {
    boolean register(UserDetailsPojo userDetailsPojo);

    boolean saveUserDetail();
}
