package com.phonebook.service;

import com.phonebook.model.SecurityUser;
import com.phonebook.model.User;
import com.phonebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {
        Optional<User> optionalUser = userService.findUserByLogin(login);
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("UserName " + login
                    + " not found");
        }

        User user = optionalUser.get();

        return new SecurityUser(user);
    }
}
