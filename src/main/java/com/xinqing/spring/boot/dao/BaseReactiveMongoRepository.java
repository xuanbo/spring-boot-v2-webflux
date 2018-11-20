package com.xinqing.spring.boot.dao;

import com.xinqing.spring.boot.entity.Entity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Repository基础接口
 *
 * @author 奔波儿灞
 * @since 1.0
 */
@NoRepositoryBean
public interface BaseReactiveMongoRepository<T extends Entity> extends ReactiveMongoRepository<T, String> {
}
