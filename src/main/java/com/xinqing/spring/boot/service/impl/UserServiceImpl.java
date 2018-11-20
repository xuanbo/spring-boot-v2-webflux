package com.xinqing.spring.boot.service.impl;

import com.xinqing.spring.boot.constants.SecurityConstant;
import com.xinqing.spring.boot.dao.UserRepository;
import com.xinqing.spring.boot.entity.User;
import com.xinqing.spring.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * @author 奔波儿灞
 * @since 1.0
 */
@Service
public class UserServiceImpl extends BaseReactiveServiceImpl<User> implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Mono<User> add(User user) {
        String password = user.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        user.setPassword(encodePassword);
        return super.add(user);
    }

    @Override
    public Mono<User> modify(User user) {
        return Mono.just(user)
                // 加密
                .doOnNext(this::encodePassword)
                // 角色过滤
                .flatMap(ignore -> ReactiveSecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getAuthorities)
                .doOnNext(authorities -> filterRoles(authorities, user))
                // 修改
                .flatMap(ignore -> super.modify(user));
    }

    /**
     * 加密密码
     *
     * @param user User
     */
    private void encodePassword(User user) {
        String password = user.getPassword();
        if (!StringUtils.isEmpty(password)) {
            String encodePassword = passwordEncoder.encode(password);
            user.setPassword(encodePassword);
        }
    }

    /**
     * 管理员才能修改角色
     *
     * @param authorities Collection<? extends GrantedAuthority>
     * @param user        User
     */
    private void filterRoles(Collection<? extends GrantedAuthority> authorities, User user) {
        boolean isAdmin = authorities.stream().anyMatch(authority -> SecurityConstant.ROLE_ADMIN.equals(authority.getAuthority()));
        if (!isAdmin) {
            user.setRoles(null);
        }
    }
}
