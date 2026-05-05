package com.tatvasoft.course_management.repository;

import java.util.Optional;

import com.tatvasoft.course_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.tatvasoft.course_management.enums.Role;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByEmail(String email,Role role);
}
