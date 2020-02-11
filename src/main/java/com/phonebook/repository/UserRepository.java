package com.phonebook.repository;

import com.phonebook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;


@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByResetToken(String resetToken);
}
