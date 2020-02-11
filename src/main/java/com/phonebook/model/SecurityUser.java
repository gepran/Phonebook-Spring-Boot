package com.phonebook.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityUser extends User implements UserDetails {
	private static final long serialVersionUID = 1L;

	public SecurityUser(User user) {
		if (user != null) {
			this.setId(user.getId());
			this.setLogin(user.getLogin());
			this.setPassword(user.getPassword());
			this.setRoles(user.getRoles());
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		Collection<Role> userRoles = this.getRoles();
		for (Role userRole: userRoles) {
			if (userRole != null) {
				authorities.add(new SimpleGrantedAuthority("ROLE_"+userRole.getName()));
			}
		}

		authorities.addAll(getPrivileges(this.getRoles()));

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


	@Override
	public String getPassword() {
		return super.getPassword();
	}

	@Override
	public String getUsername() {
		return super.getLogin();
	}

	@Override
	public Collection<Role> getRoles() {
		return super.getRoles();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}