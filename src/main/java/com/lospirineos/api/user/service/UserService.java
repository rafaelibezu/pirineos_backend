package com.lospirineos.api.user.service;

import com.lospirineos.api.user.dto.request.CreateUserRequest;
import com.lospirineos.api.user.dto.request.UpdateUserRequest;
import com.lospirineos.api.user.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse create(CreateUserRequest request);

    UserResponse findById(Long id);

    List<UserResponse> findAll();

    List<UserResponse> findActiveWaiters();

    UserResponse update(
            Long id,
            UpdateUserRequest request
    );
}