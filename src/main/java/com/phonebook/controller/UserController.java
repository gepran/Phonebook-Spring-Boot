package com.phonebook.controller;

import com.phonebook.dto.PasswordReset;
import com.phonebook.model.Role;
import com.phonebook.model.SecurityUser;
import com.phonebook.model.User;
import com.phonebook.model.exception.UserNotFoundException;
import com.phonebook.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public class UserController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @RequestMapping("/listOfUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getdList() {
        return userService.findAll();
    }

    @RequestMapping(value="/addUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addUser(@RequestBody(required = true)User user) {
        userService.create(user);

        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @RequestMapping(value="/editUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Void> editUser(@RequestBody(required = true) User user) {
        userService.update(user);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value="/deleteUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@RequestBody(required = true)User user) {
        userService.delete(user.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/getRole")
    public ResponseEntity<List<String>> getRole() {
        List<String> auths = null;

        auths = userService.getAuthorities();

        return new ResponseEntity<>(auths, HttpStatus.OK);
    }

    @RequestMapping(value="/getProfileUser")
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<User> getProfileUser() {
        User user = null;

        user = userService.getProfileUser();

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(value="/forgotPassword")
    public ResponseEntity<?> setResetToken(final HttpServletRequest request, @RequestBody(required = true)String email) {
        String resetToken = null;

        Optional<User> optional = userService.findUserByEmail(email);
        if(!optional.isPresent()){
            throw new UserNotFoundException("User not find by email");
        }
        User user = optional.get();

        user.setResetToken(UUID.randomUUID().toString());
        userService.update(user);


        //mailSender.send(constructResetTokenEmail(getAppUrl(request), user));

        PasswordReset pr = new PasswordReset();
        pr.setResetToken(user.getResetToken());
        return new ResponseEntity<>(pr, HttpStatus.OK);
    }

    @RequestMapping(value="/resetPassword")
    public ResponseEntity<Void> applyToken(@RequestBody(required = true)PasswordReset passwordReset) {
        Optional<User> optional = userService.findUserByResetToken(passwordReset.getResetToken());

        if(!optional.isPresent()){
            throw new UserNotFoundException("Incorrect Token");
        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value="/setNewPassword")
    public ResponseEntity<Void> setNewPassword(@RequestBody(required = true)PasswordReset passwordReset) {
        Optional<User> optional = userService.findUserByResetToken(passwordReset.getResetToken());

        if(!optional.isPresent()){
            throw new UserNotFoundException("Incorrect Token");
        }

        User user = optional.get();
        user.setResetToken(null);
        user.setPassword(passwordEncoder.encode(passwordReset.getNewPassword()));

        userService.update(user);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final User user) {
        final String url = contextPath + "/#resetpassword/" + user.getResetToken();
        final String message = "Reset Password";
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
