package com.xinqing.spring.boot.constants;

/**
 * @author 奔波儿灞
 * @since 1.0
 */
public final class SecurityConstant {

    /**
     * 管理员角色
     */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * 普通用户
     */
    public static final String ROLE_USER = "ROLE_USER";

    /**
     * 需要管理员角色
     */
    public static final String NEED_ROLE_ADMIN = "hasRole('ADMIN')";

    /**
     * 需要普通用户角色
     */
    public static final String NEED_ROLE_USER = "hasRole('USER')";

    /**
     * 资产校验
     */
    public static final String NEED_OWN = "#u.username == authentication.name";

}
