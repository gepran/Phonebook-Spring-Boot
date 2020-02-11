package com.phonebook.social;

import com.phonebook.model.Role;
import com.phonebook.model.User;
import com.phonebook.repository.RoleRepository;
import com.phonebook.repository.UserRepository;
import javafx.beans.binding.ObjectExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Component
public class FacebookConnectionSignup implements ConnectionSignUp {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public String execute(Connection<?> connection) {
        System.out.println("signup === ");
        UserProfile profile = connection.fetchUserProfile();

        final User user = new User();
        user.setLogin(connection.getDisplayName());
        user.setPassword(randomAlphabetic(8));

        Set<String> roleName = Stream.of("USER", "FACEBOOK_USER").collect(Collectors.toSet());
        Set<Role> userRoles = roleRepository.findByNameIn(roleName);

        user.setRoles(userRoles);

        userRepository.save(user);
        return user.getLogin();
    }

    private String generateLoginId(UserProfile profile) {
        return StringUtils.isEmpty(profile.getEmail()) ? profile.getUsername() : profile.getEmail();
    }
}