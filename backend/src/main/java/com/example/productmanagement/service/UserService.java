package com.example.productmanagement.service;

import com.example.productmanagement.model.User;
import com.example.productmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 根據信箱查找用戶
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("用戶未找到"));
    }

    // 更新信箱
    public void updateEmail(String currentEmail, String newEmail) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("用戶未找到"));

        // 檢查新信箱是否已被使用
        if (userRepository.existsByEmail(newEmail)) {
            throw new RuntimeException("信箱已被使用");
        }

        // 更新信箱
        user.setEmail(newEmail);
        userRepository.save(user); // 使用 save() 方法更新用戶資料
    }

    // 更新密碼
    public void updatePassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("用戶未找到"));

        // 驗證當前密碼（明文比對）
        if (!currentPassword.equals(user.getPassword())) {
            throw new RuntimeException("當前密碼不正確");
        }

        // 更新密碼（明文存儲）
        user.setPassword(newPassword);
        userRepository.save(user); // 使用 save() 方法更新用戶資料
    }
}