package com.xinqing.spring.boot.service;

import com.xinqing.spring.boot.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author 奔波儿灞
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.main.web-application-type=reactive")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void addUser() {
        User user = new User();
        user.setUsername("tony");
        user.setPassword("123456");
        user.setName("tony");
        user.setRoles(Collections.singletonList("ROLE_USER"));
        userService.add(user).block();
    }

    @Test
    public void addAdmin() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setName("admin");
        user.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
        userService.add(user).block();
    }

}
