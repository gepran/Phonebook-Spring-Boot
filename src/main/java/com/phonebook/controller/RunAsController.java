package com.phonebook.controller;

import com.phonebook.service.RunAsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/runas")
class RunAsController {

    @Autowired
    private RunAsService runAsService;

    @Secured({ "ROLE_USER", "RUN_AS_REPORTER" })
    @RequestMapping
    @ResponseBody
    public String tryRunAs() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().forEach(a -> System.out.println(a.getAuthority()));

        Authentication auth = runAsService.getCurrentUser();
        auth.getAuthorities().forEach(a -> System.out.println(a.getAuthority()));

        return "Current User Authorities inside this RunAS method only " + authentication.getAuthorities().toString();
    }

}