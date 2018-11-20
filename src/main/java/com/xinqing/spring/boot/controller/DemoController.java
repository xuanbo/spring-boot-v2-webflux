package com.xinqing.spring.boot.controller;

import com.xinqing.spring.boot.constants.SecurityConstant;
import com.xinqing.spring.boot.entity.Demo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 奔波儿灞
 * @since 1.0
 */
@RestController
@RequestMapping("/api/demo")
@PreAuthorize(SecurityConstant.NEED_ROLE_USER)
public class DemoController extends BaseReactiveController<Demo> {
}
