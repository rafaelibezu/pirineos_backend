package com.lospirineos.api.config;

import com.lospirineos.api.common.constant.RoleCode;
import com.lospirineos.api.user.entity.Role;
import com.lospirineos.api.user.entity.User;
import com.lospirineos.api.user.repository.RoleRepository;
import com.lospirineos.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "Admin123*";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {

        if (userRepository.existsByUsername(ADMIN_USERNAME)) {
            return;
        }

        Role adminRole = roleRepository
                .findByCode(RoleCode.ADMIN)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No existe el rol ADMIN en la tabla roles"
                        )
                );

        User admin = new User();

        admin.setUsername(ADMIN_USERNAME);

        admin.setPasswordHash(
                passwordEncoder.encode(ADMIN_PASSWORD)
        );

        admin.setFirstName("Administrador");
        admin.setLastName("Los Pirineos");

        admin.setRole(adminRole);
        admin.setActive(true);

        userRepository.save(admin);

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "ADMIN INICIAL CREADO CORRECTAMENTE"
        );

        System.out.println(
                "Usuario: " + ADMIN_USERNAME
        );

        System.out.println(
                "=========================================="
        );
    }
}