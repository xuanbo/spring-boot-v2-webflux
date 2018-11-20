package com.xinqing.spring.boot.service;

import com.xinqing.spring.boot.entity.Entity;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 基础Reactive服务
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public interface BaseReactiveService<T extends Entity> {

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return Mono<T>
     */
    Mono<T> findById(String id);

    /**
     * 根据ids查询
     *
     * @param ids 主键
     * @return Flux<T>
     */
    Flux<T> findByIds(List<String> ids);

    /**
     * 分页查询
     *
     * @param pageable 分页信息
     * @return Flux<T>
     */
    Flux<T> findAll(Pageable pageable);

    /**
     * 新增
     *
     * @param entity 实体
     * @return Mono<T>
     */
    Mono<T> add(T entity);

    /**
     * 修改，字段不为null则修改
     *
     * @param entity 实体
     * @return Mono<T>
     */
    Mono<T> modify(T entity);

    /**
     * 删除
     *
     * @param id 主键
     * @return Mono<Void>
     */
    Mono<Void> removeById(String id);

}
