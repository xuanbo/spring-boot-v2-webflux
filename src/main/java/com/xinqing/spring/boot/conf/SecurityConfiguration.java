package com.xinqing.spring.boot.conf;

import com.xinqing.spring.boot.security.ReactiveUserDetailsServiceImpl;
import com.xinqing.spring.boot.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Reactive Security配置
 *
 * @author 奔波儿灞
 * @since 1.0
 */
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .anyExchange().authenticated()
                .and()
                // 禁用csrf
                .csrf().disable()
                // login
                .formLogin();
        return http.build();
    }

    /**
     * 自定义ReactiveUserDetailsService
     *
     * @param userService UserService
     * @return ReactiveUserDetailsServiceImpl
     */
    @Bean
    public ReactiveUserDetailsService userDetailsService(UserService userService) {
        return new ReactiveUserDetailsServiceImpl(userService);
    }

    /**
     * 配置密码
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
