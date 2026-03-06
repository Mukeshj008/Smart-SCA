package com.example.app.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Bean validation service using Hibernate Validator.
 * Uses: hibernate-validator, log4j
 */
public class ValidationService {
    private static final Logger logger = LogManager.getLogger(ValidationService.class);
    private final Validator validator = Validation.byProvider(HibernateValidator.class)
            .configure()
            .buildValidatorFactory()
            .getValidator();

    public <T> Set<String> validate(T obj) {
        Set<ConstraintViolation<T>> violations = validator.validate(obj);
        if (!violations.isEmpty()) {
            logger.warn("Validation failed: {} violations", violations.size());
        }
        return violations.stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toSet());
    }

    public <T> boolean isValid(T obj) {
        return validator.validate(obj).isEmpty();
    }
}
