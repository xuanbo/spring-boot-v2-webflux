package com.xinqing.spring.boot.service;

import com.xinqing.spring.boot.entity.User;
import reactor.core.publisher.Mono;

/**
 * @author 奔波儿灞
 * @since 1.0
 */
public interface UserService extends BaseReactiveService<User> {

    Mono<User> findByUsername(String username);

}
