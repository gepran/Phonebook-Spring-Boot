package com.phonebook.controller;

import com.phonebook.model.User;
import com.phonebook.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/secured")
public class SecuredUserController {

        private final Logger LOGGER = LoggerFactory.getLogger(getClass());

        @Autowired
        private UserService userService;

        @RequestMapping("/users")
        @PreAuthorize("hasRole('ROLE_USER') and #oauth2.hasScope('write')")
        public List<User> getdList() {
            return userService.findAll();
        }
}
