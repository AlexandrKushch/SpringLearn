package com.khpi.vragu.repos;

import com.khpi.vragu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findById(Long id);
}
