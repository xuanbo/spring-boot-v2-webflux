package com.xinqing.spring.boot.dao;

import com.xinqing.spring.boot.entity.Entity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;

/**
 * Repository基础接口
 *
 * @author 奔波儿灞
 * @since 1.0
 */
@NoRepositoryBean
public interface BaseReactiveMongoRepository<T extends Entity> extends ReactiveMongoRepository<T, String> {

    /**
     * 分页查询
     *
     * @param pageable 分页信息
     * @return Flux<T>
     */
    Flux<T> findAll(Pageable pageable);

}
