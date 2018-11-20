package com.xinqing.spring.boot.controller;

import com.xinqing.spring.boot.constants.SecurityConstant;
import com.xinqing.spring.boot.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 奔波儿灞
 * @since 1.0
 */
@RestController
@RequestMapping("/api/user")
@PreAuthorize(SecurityConstant.NEED_ROLE_USER)
public class UserController extends BaseReactiveController<User> {

    @Override
    @GetMapping("/{id}")
    @PreAuthorize(SecurityConstant.NEED_ROLE_ADMIN)
    public Mono<User> show(@PathVariable String id) {
        return super.show(id).doOnNext(user -> user.setPassword("Unknown"));
    }

    @Override
    @PutMapping
    @PreAuthorize(SecurityConstant.NEED_ROLE_ADMIN + " or " + SecurityConstant.NEED_OWN)
    public Mono<User> modify(@P("u") @RequestBody User entity) {
        return super.modify(entity);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize(SecurityConstant.NEED_ROLE_ADMIN)
    public Mono<Void> remove(@PathVariable String id) {
        return super.remove(id);
    }
}
