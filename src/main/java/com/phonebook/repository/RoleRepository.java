package com.phonebook.repository;

import com.phonebook.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Set;


@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findByNameIn(Set<String> roles);
    Set<Role> findByName(String role);
}
