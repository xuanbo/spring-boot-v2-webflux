package com.xinqing.spring.boot.dao;

import com.xinqing.spring.boot.entity.Demo;
import org.springframework.stereotype.Repository;

/**
 * @author 奔波儿灞
 * @since 1.0
 */
@Repository
public interface DemoRepository extends BaseReactiveMongoRepository<Demo> {
}
