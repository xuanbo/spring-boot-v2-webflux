package com.xinqing.spring.boot.controller;

import com.xinqing.spring.boot.entity.Entity;
import com.xinqing.spring.boot.service.BaseReactiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * controller基础父类
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public abstract class BaseReactiveController<T extends Entity> {

    @Autowired
    private BaseReactiveService<T> reactiveService;

    @GetMapping
    public Flux<T> page(@PageableDefault Pageable pageable) {
        return reactiveService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Mono<T> show(@PathVariable String id) {
        return reactiveService.findById(id);
    }

    @PostMapping
    public Mono<T> add(@RequestBody T entity) {
        return reactiveService.add(entity);
    }

    @PutMapping
    public Mono<T> modify(@RequestBody T entity) {
        return reactiveService.modify(entity);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> remove(@PathVariable String id) {
        return reactiveService.removeById(id);
    }

}
