package com.phonebook.service;

import com.phonebook.model.Role;
import com.phonebook.model.SecurityUser;
import com.phonebook.model.User;
import com.phonebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    public User create(User user){
        Role role = roleService.findRole(1L);
        user.getRoles().add(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public Optional<User> findUserByLogin(String login){
        return userRepository.findByLogin(login);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findOne(id);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.delete(id);
    }

    public Optional findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Optional findUserByResetToken(String resetToken) {
        return userRepository.findUserByResetToken(resetToken);
    }

    public List<String> getAuthorities() {
        List<String> auths = null;
        SecurityContext context = SecurityContextHolder.getContext();
        if ((context != null
                && context.getAuthentication() != null) &&
                !"anonymousUser".equals(context.getAuthentication().getPrincipal())) {
            if(context.getAuthentication().getPrincipal() instanceof SecurityUser) {
                SecurityUser securityUser = (SecurityUser) context.getAuthentication().getPrincipal();
                auths = CollectionUtils.isEmpty(securityUser.getRoles())? null : securityUser.getAuthorities()
                                                                                .stream()
                                                                                .map(r-> r.getAuthority())
                                                                                .collect(Collectors.toList());
            } else if(context.getAuthentication().getPrincipal() instanceof SocialUser) {
                SocialUser socialUser = (SocialUser) context.getAuthentication().getPrincipal();
                auths = CollectionUtils.isEmpty(socialUser.getAuthorities())? null : socialUser.getAuthorities()
                                                                                    .stream()
                                                                                    .map(s->s.getAuthority())
                                                                                    .collect(Collectors.toList());
            }

        }
        return auths;
    }

    public User getProfileUser() {
        User user = null;
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null && context.getAuthentication() != null) {
            if(context.getAuthentication().getPrincipal() instanceof SecurityUser) {
                SecurityUser securityUser = (SecurityUser) context.getAuthentication().getPrincipal();
                user = findUserById(securityUser.getId());
            } else if(context.getAuthentication().getPrincipal() instanceof SocialUser) {
                SocialUser socialUser = (SocialUser) context.getAuthentication().getPrincipal();
                user = findUserByLogin(socialUser.getUsername()).orElse(null);
            }

        }

        return user;
    }
}