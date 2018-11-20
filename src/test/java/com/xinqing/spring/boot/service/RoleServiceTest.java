package com.xinqing.spring.boot.service;

import com.xinqing.spring.boot.entity.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

/**
 * @author 奔波儿灞
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.main.web-application-type=reactive")
public class RoleServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(RoleServiceTest.class);

    @Autowired
    private RoleService roleService;

    @Test
    public void add() {
        Role role = new Role();
        role.setName("ROLE_USER");
        role.setDesc("普通用户");
        roleService.add(role).block();
    }

    @Test
    public void find() {
        roleService.findByIds(Arrays.asList("5bee904f4057e97a57d9e013", "5bee908f4057e97a5d6b0fd1"))
                .doOnNext(role -> LOG.info("role: {}", role))
                .blockLast();
    }

}
