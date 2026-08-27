package com.lospirineos.api.user.repository;

import com.lospirineos.api.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "role")
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @EntityGraph(attributePaths = "role")
    List<User> findAllByActiveTrue();

    @EntityGraph(attributePaths = "role")
    List<User> findAllByRoleCodeAndActiveTrue(String roleCode);
}