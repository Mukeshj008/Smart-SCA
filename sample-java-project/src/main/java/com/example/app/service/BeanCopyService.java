package com.example.app.service;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

/**
 * Bean property copy utility using Commons BeanUtils.
 * Uses: commons-beanutils, log4j
 */
public class BeanCopyService {
    private static final Logger logger = LogManager.getLogger(BeanCopyService.class);

    public void copyProperties(Object dest, Object orig) {
        try {
            BeanUtils.copyProperties(dest, orig);
            logger.debug("Copied properties from {} to {}", orig.getClass().getSimpleName(), dest.getClass().getSimpleName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Failed to copy bean properties", e);
        }
    }

    public Object cloneBean(Object bean) {
        try {
            return BeanUtils.cloneBean(bean);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("Failed to clone bean", e);
            return null;
        }
    }
}
