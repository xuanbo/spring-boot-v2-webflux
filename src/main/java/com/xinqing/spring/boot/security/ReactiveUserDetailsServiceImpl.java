package com.xinqing.spring.boot.security;

import com.xinqing.spring.boot.service.UserService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

/**
 * ReactiveUserDetailsService实现
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserService userService;

    public ReactiveUserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userService.findByUsername(username).map(UserDetailsImpl::new);
    }

}
