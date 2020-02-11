package com.phonebook.social;

import com.phonebook.model.Role;
import com.phonebook.model.SecurityUser;
import com.phonebook.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Arrays;

@Component
public class FacebookSignInAdapter implements SignInAdapter {
    @Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
        System.out.println(" ====== Sign In adapter");

        User user = new User();
        user.setLogin(connection.getDisplayName());
        //user.setRoles(Arrays.asList(new Role("FACEBOOK_USER"), new Role("USER")));

        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(new SecurityUser(user),null,
                        Arrays.asList(new SimpleGrantedAuthority("FACEBOOK_USER"))));
        return null;
    }

    private String generateLoginId(Connection<?> connection) {
        UserProfile profile = connection.fetchUserProfile();

        return StringUtils.isEmpty(profile.getEmail()) ? profile.getUsername() : profile.getEmail();
    }
}