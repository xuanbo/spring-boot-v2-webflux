package com.xinqing.spring.boot.entity;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 奔波儿灞
 * @since 1.0
 */
public abstract class Entity implements Serializable {

    public static final String ID_KEY = "_id";
    public static final String CREATE_KEY = "createAt";
    public static final String UPDATE_KEY = "updateAt";

    @Id
    private String id;
    private Date createAt;
    private Date updateAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

}
