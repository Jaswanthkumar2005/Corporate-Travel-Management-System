package com.travel.system.repositories;

import com.travel.system.models.User;
import com.travel.system.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(Role role);
}
