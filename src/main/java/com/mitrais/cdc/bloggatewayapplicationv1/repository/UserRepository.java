package com.mitrais.cdc.bloggatewayapplicationv1.repository;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Serializable> {
    User findByUsername(String username);
    Optional<User> findByEmail(String email);
}
