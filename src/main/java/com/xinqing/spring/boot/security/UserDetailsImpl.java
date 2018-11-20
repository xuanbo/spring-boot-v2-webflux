package com.xinqing.spring.boot.security;

import com.xinqing.spring.boot.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UserDetails实现
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class UserDetailsImpl implements UserDetails {

    private User user;
    private List<SimpleGrantedAuthority> authorities;

    public UserDetailsImpl(User user) {
        Assert.notNull(user, "user not be null");
        this.user = user;
        this.authorities = user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
