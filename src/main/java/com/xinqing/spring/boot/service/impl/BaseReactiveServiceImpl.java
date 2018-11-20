package com.xinqing.spring.boot.service.impl;

import com.xinqing.spring.boot.dao.BaseReactiveMongoRepository;
import com.xinqing.spring.boot.entity.Entity;
import com.xinqing.spring.boot.service.BaseReactiveService;
import com.xinqing.spring.boot.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

/**
 * 基础Reactive服务实现
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public abstract class BaseReactiveServiceImpl<T extends Entity> implements BaseReactiveService<T> {

    @Autowired
    private BaseReactiveMongoRepository<T> reactiveMongoRepository;

    @Override
    public Mono<T> findById(String id) {
        return reactiveMongoRepository.findById(id);
    }

    @Override
    public Flux<T> findByIds(List<String> ids) {
        return reactiveMongoRepository.findAllById(ids);
    }

    @Override
    public Flux<T> findAll(Pageable pageable) {
        return reactiveMongoRepository.findAll(pageable);
    }

    @Override
    public Mono<T> add(T entity) {
        // 创建时间、更新时间
        Date now = new Date();
        if (entity.getCreateAt() == null) {
            entity.setCreateAt(now);
        }
        if (entity.getUpdateAt() == null) {
            entity.setUpdateAt(now);
        }
        return reactiveMongoRepository.insert(entity);
    }

    @Override
    public Mono<T> modify(T entity) {
        return reactiveMongoRepository.findById(entity.getId()).flatMap(persist -> doUpdate(persist, entity));
    }

    private Mono<T> doUpdate(T persist, T entity) {
        EntityUtils.copyEntityIgnoreNull(entity, persist);
        // 修改时间
        persist.setUpdateAt(new Date());
        return reactiveMongoRepository.save(persist);
    }

    @Override
    public Mono<Void> removeById(String id) {
        return reactiveMongoRepository.deleteById(id);
    }
}
