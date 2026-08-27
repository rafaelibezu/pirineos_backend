package com.lospirineos.api.user.service.impl;

import com.lospirineos.api.common.exception.BusinessException;
import com.lospirineos.api.common.exception.ResourceNotFoundException;
import com.lospirineos.api.user.dto.request.CreateUserRequest;
import com.lospirineos.api.user.dto.request.UpdateUserRequest;
import com.lospirineos.api.user.dto.response.UserResponse;
import com.lospirineos.api.user.entity.Role;
import com.lospirineos.api.user.entity.User;
import com.lospirineos.api.user.mapper.UserMapper;
import com.lospirineos.api.user.repository.RoleRepository;
import com.lospirineos.api.user.repository.UserRepository;
import com.lospirineos.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lospirineos.api.common.constant.RoleCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private static final String WAITER_ROLE = "WAITER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse create(
            CreateUserRequest request
    ) {

        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException(
                    "El nombre de usuario ya se encuentra registrado"
            );
        }

        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "El rol no existe"
                        )
                );

        User user = User.builder()
                .username(request.username().trim())
                .passwordHash(
                        passwordEncoder.encode(request.password())
                )
                .firstName(request.firstName().trim())
                .lastName(
                        request.lastName() != null
                                ? request.lastName().trim()
                                : null
                )
                .role(role)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {

        User user = findUser(id);

        return UserMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findActiveWaiters() {

        return userRepository
                .findAllByRoleCodeAndActiveTrue(RoleCode.WAITER)
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse update(
            Long id,
            UpdateUserRequest request
    ) {

        User user = findUser(id);

        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "El rol no existe"
                        )
                );

        user.setFirstName(
                request.firstName().trim()
        );

        user.setLastName(
                request.lastName() != null
                        ? request.lastName().trim()
                        : null
        );

        user.setRole(role);
        user.setActive(request.active());

        return UserMapper.toResponse(
                userRepository.save(user)
        );
    }

    private User findUser(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "El usuario no existe"
                        )
                );
    }
}