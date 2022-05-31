package com.itransition.itransitiontaskfour.repository;

import com.itransition.itransitiontaskfour.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
