package com.lospirineos.api.auth.service.impl;

import com.lospirineos.api.auth.dto.request.LoginRequest;
import com.lospirineos.api.auth.dto.response.LoginResponse;
import com.lospirineos.api.auth.service.AuthService;
import com.lospirineos.api.common.exception.BusinessException;
import com.lospirineos.api.security.jwt.JwtService;
import com.lospirineos.api.security.principal.AuthenticatedUser;
import com.lospirineos.api.user.entity.User;
import com.lospirineos.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByUsername(request.username())
                .orElseThrow(() ->
                        new BusinessException(
                                "Usuario o contraseña incorrectos"
                        )
                );

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BusinessException(
                    "El usuario se encuentra inactivo"
            );
        }

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        )) {
            throw new BusinessException(
                    "Usuario o contraseña incorrectos"
            );
        }

        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(user);

        String token =
                jwtService.generateToken(authenticatedUser);

        return new LoginResponse(
                token,
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().getCode()
        );
    }
}