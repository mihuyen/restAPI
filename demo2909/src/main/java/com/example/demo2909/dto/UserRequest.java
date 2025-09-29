package com.example.demo2909.dto;

import com.example.demo2909.entity.Role;
import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
    private Role role;
}