package com.example.app.repository;

import com.example.app.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory user repository.
 * Uses: commons-lang3, log4j
 */
public class UserRepository {
    private static final Logger logger = LogManager.getLogger(UserRepository.class);
    private final List<User> users = new ArrayList<>();

    public User save(User user) {
        if (user != null && StringUtils.isNotBlank(user.getUsername())) {
            users.add(user);
            logger.info("Saved user: {}", user.getUsername());
        }
        return user;
    }

    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(u -> StringUtils.equals(u.getUsername(), username))
                .findFirst();
    }

    public List<User> findAll() {
        logger.debug("Fetching all users, count={}", users.size());
        return new ArrayList<>(users);
    }
}
