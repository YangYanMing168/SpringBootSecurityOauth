package com.szu;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by kamyang on 2017/11/16.
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.szu.dao"})
@EntityScan(basePackages = {"com.szu.entity"})
public class JpaConfig {
}
