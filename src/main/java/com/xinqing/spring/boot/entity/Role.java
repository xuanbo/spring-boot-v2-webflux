package com.xinqing.spring.boot.entity;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 角色
 *
 * @author 奔波儿灞
 * @since 1.0
 */
@Document(collection = "Role")
public class Role extends Entity {

    private String name;

    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
