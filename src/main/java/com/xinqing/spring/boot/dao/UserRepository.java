package com.xinqing.spring.boot.dao;

import com.xinqing.spring.boot.entity.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @author 奔波儿灞
 * @since 1.0
 */
@Repository
public interface UserRepository extends BaseReactiveMongoRepository<User> {

    Mono<User> findByUsername(String username);

}
