package com.vijay.RobustScalableApp.util;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public static <S, T> T toDto(S source, Class<T> targetClass) {
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            System.err.println("Error during mapping to DTO: " + e.getMessage());
            throw new RuntimeException("Error during mapping", e);
        }
    }

    public static <S, T> T toEntity(S source, Class<T> targetClass) {
        return toEntity(source, targetClass, "id");
    }

    public static <S, T> T toEntity(S source, Class<T> targetClass, String... ignoreProperties) {
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target, ignoreProperties);
            return target;
        } catch (Exception e) {
            System.err.println("Error during mapping to Entity: " + e.getMessage());
            throw new RuntimeException("Error during mapping", e);
        }
    }
}
