package com.xinqing.spring.boot.util;

import com.xinqing.spring.boot.entity.Entity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 奔波儿灞
 * @since 1.0
 */
public final class EntityUtils {

    /**
     * 实体中默认要忽略的字段
     */
    private static final String[] DEFAULT_ENTITY_IGNORE_PROPERTIES_ARRAY = new String[]{Entity.ID_KEY, Entity.CREATE_KEY, Entity.UPDATE_KEY};
    private static final List<String> DEFAULT_ENTITY_IGNORE_PROPERTIES_LIST = Arrays.asList(DEFAULT_ENTITY_IGNORE_PROPERTIES_ARRAY);

    private EntityUtils() {
    }

    public static <T extends Entity> void copyEntityIgnoreNull(T source, T target) {
        BeanUtils.copyProperties(source, target, getNullProperties(source));
    }

    public static <T extends Entity> void copyEntity(T source, T target) {
        BeanUtils.copyProperties(source, target, DEFAULT_ENTITY_IGNORE_PROPERTIES_ARRAY);
    }

    /**
     * 找出实体中为null的properties
     *
     * @param source 实体
     * @return 实体中为null的properties
     */
    private static <T extends Entity> String[] getNullProperties(T source) {
        BeanWrapper sourceBean = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = sourceBean.getPropertyDescriptors();
        Set<String> emptyProperties = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Object srcValue = sourceBean.getPropertyValue(propertyDescriptor.getName());
            if (srcValue == null) {
                emptyProperties.add(propertyDescriptor.getName());
            }
        }
        emptyProperties.addAll(DEFAULT_ENTITY_IGNORE_PROPERTIES_LIST);
        String[] result = new String[emptyProperties.size()];
        return emptyProperties.toArray(result);
    }

}
