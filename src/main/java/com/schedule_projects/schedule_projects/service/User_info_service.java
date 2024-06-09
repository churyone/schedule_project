package com.schedule_projects.schedule_projects.service;

import com.schedule_projects.schedule_projects.domain.User_info;
import com.schedule_projects.schedule_projects.repository.User_info_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.Optional;

@Service
public class User_info_service {
    @Autowired
    private User_info_repository user_info_repository;

    private static final Logger logger = Logger.getLogger(User_info_service.class.getName());  // Logger 변수 선언 추가

    public User_info getUserById(int userId) {
        Optional<User_info> user = user_info_repository.findByUserId(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }
    public boolean validateUser(String identifier, String password) {
        return user_info_repository.findByIdentifierAndPassword(identifier, password).isPresent();
    }

    public boolean registerUser(String identifier, String password, String userName) {
        logger.info("Registering user: " + identifier + ", userName: " + userName);
        Optional<User_info> existingUser = user_info_repository.findByIdentifier(identifier);
        if (existingUser.isPresent()) {
            logger.info("User already exists: " + identifier);
            return false;
        }
        User_info newUser = new User_info();
        newUser.setIdentifier(identifier);
        newUser.setPassword(password);
        newUser.setUserName(userName);

        user_info_repository.save(newUser);
        logger.info("User registered successfully: " + identifier);
        return true;
    }
    public User_info findByUserName(String userName) {
        return user_info_repository.findByUserName(userName);
    }

    public List<User_info> findByConnectionStatus(String connected) {
        return user_info_repository.findByConnectionStatus(connected);
    }
}