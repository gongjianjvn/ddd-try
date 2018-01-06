package com.runbo.auth.security;

import com.runbo.auth.query.entry.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by lcsontos on 5/24/17.
 */
public class DelegatingUser implements UserDetails {

  private final User user;

  public DelegatingUser(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return String.valueOf(user.getId());
  }

  @Override
  public boolean isAccountNonExpired() {
    return !user.isDeleted();
  }

  @Override
  public boolean isAccountNonLocked() {
    return !user.isLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return user.isConfirmed();
  }

}
