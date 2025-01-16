package com.example.productmanagement.service;

import com.example.productmanagement.model.User;
import com.example.productmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        logger.info("嘗試註冊用戶: {}", user.getEmail());

        // 檢查電子郵件是否已存在
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            logger.warn("帳號重複註冊: {}", user.getEmail());
            throw new UserAlreadyExistsException("帳號重複註冊");
        }

        // 直接保存用戶，不對密碼進行加密
        User savedUser = userRepository.save(user);
        logger.info("用戶註冊成功: {}", savedUser.getEmail());
        return savedUser;
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("帳號尚未註冊"));

        // 直接比對明文密碼
        if (password.equals(user.getPassword())) {
            String loginTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            logger.info("帳號登入 - 信箱: {}, 於: {} 登入", email, loginTime);
            return user;
        } else {
            logger.warn("登入失敗:密碼錯誤 - {}", email);
            throw new InvalidPasswordException("密碼錯誤");
        }
    }

    public class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException(String message) {
            super(message);
        }
    }

    public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }
}