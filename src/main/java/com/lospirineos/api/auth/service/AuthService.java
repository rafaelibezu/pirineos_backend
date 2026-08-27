package com.lospirineos.api.auth.service;

import com.lospirineos.api.auth.dto.request.LoginRequest;
import com.lospirineos.api.auth.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}