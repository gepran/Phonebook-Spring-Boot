package com.phonebook.service;

import com.phonebook.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RunAsService {
    @Autowired
    ContactService contactService;

    @Secured({ "ROLE_RUN_AS_REPORTER" })
    public Authentication getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        //Contact contact = new Contact("First Name1", "Heyyyyy1", "58584585");
        //contactService.create(contact);

        return authentication;
    }
}