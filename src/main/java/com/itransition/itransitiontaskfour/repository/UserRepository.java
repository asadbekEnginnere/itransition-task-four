package com.itransition.itransitiontaskfour.repository;

import com.itransition.itransitiontaskfour.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);

}
