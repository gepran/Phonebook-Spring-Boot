package com.phonebook.service;

import com.phonebook.model.Privilege;
import com.phonebook.model.Role;
import com.phonebook.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CustomSocialUserDetailsService implements SocialUserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
        Optional<User> optionalUser = userService.findUserByLogin(userId);
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("UserName " + userId
                    + " not found");
        }

        User user = optionalUser.get();

        return new SocialUser(user.getLogin(), user.getPassword(), getAuthorities(user));
    }

    public Collection<? extends GrantedAuthority> getAuthorities(User user) {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        Collection<Role> userRoles = user.getRoles();
        for (Role userRole: userRoles) {
            if (userRole != null) {
                authorities.add(new SimpleGrantedAuthority("ROLE_"+userRole.getName()));
            }
        }

        authorities.addAll(getPrivileges(user.getRoles()));

        return authorities;
    }

    private final Set<GrantedAuthority> getPrivileges(final Collection<Role> roles) {
        Collection<Privilege> privileges = roles.stream()
                .map(r->r.getPrivileges())
                .flatMap(r->r.stream())
                .collect(Collectors.toList());

        Set<GrantedAuthority> authorities = privileges.stream()
                .map(p->new SimpleGrantedAuthority(p.getName()))
                .collect(Collectors.toSet());

        return authorities;
    }
}
