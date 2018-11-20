package com.xinqing.spring.boot.entity;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author 奔波儿灞
 * @since 1.0
 */
@Document(collection = "Demo")
public class Demo extends Entity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
