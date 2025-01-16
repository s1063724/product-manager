package com.example.productmanagement.controller;

import com.example.productmanagement.model.User;
import com.example.productmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user-info")
public class UserInfoController {

    @Autowired
    private UserService userService;

    // 1. 獲取用戶訊息
    @GetMapping
    public ResponseEntity<?> getUserInfo() {
        // 取得當前登入者的信箱
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 根據信箱查找用戶
        User user = userService.findByEmail(email);

        // 返回用戶資料（只有名稱和信箱）
        return ResponseEntity.ok(Map.of(
                "username", user.getUsername(), // 如果 User 類中有 username 字段
                "email", user.getEmail()
        ));
    }

    // 2. 更新用戶名
    @PutMapping("/email")
    public ResponseEntity<?> updateEmail(@RequestBody Map<String, String> request) {
        String newEmail = request.get("email");

        // 取得當前登入者的信箱
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            // 更新信箱
            userService.updateEmail(currentEmail, newEmail);
            return ResponseEntity.ok("信箱更新成功");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 3. 更新密碼
    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> request) {
        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");

        // 取得當前登入者的用戶名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            // 更新密碼
            userService.updatePassword(username, currentPassword, newPassword);
            return ResponseEntity.ok("密碼更新成功");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}