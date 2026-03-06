package com.example.app.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

/**
 * Reflection utilities using Commons BeanUtils.
 * Uses: commons-beanutils, log4j
 */
public class ReflectionUtil {
    private static final Logger logger = LogManager.getLogger(ReflectionUtil.class);

    public static Object getProperty(Object bean, String name) {
        try {
            return PropertyUtils.getProperty(bean, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.warn("Failed to get property {} from {}", name, bean.getClass().getSimpleName());
            return null;
        }
    }

    public static void setProperty(Object bean, String name, Object value) {
        try {
            PropertyUtils.setProperty(bean, name, value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.warn("Failed to set property {} on {}", name, bean.getClass().getSimpleName());
        }
    }
}
