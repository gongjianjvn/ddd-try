/**
 * Copyright (c) 2017-present Laszlo Csontos All rights reserved.
 *
 * This file is part of springuni-particles.
 *
 * springuni-particles is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * springuni-particles is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with springuni-particles.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.runbo.auth.security;

import com.runbo.auth.domain.model.user.exceptions.NoSuchUserException;
import com.runbo.auth.domain.model.user.exceptions.UnconfirmedUserException;
import com.runbo.auth.domain.service.UserService;
import com.runbo.auth.query.entry.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Authentication provider using {@link UserService} as a back-end to perform login.
 */
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

  private final UserService userService;

  public UsernamePasswordAuthenticationProvider(UserService userService) {
    this.userService = userService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = (String) authentication.getCredentials();

    try {
      User user = userService.login(username, password);

      Collection<GrantedAuthority> authorities = user.getAuthorities()
          .stream()
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toSet());

      return new UsernamePasswordAuthenticationToken(user.getId(), null, authorities);
    } catch (NoSuchUserException e) {
      throw new UsernameNotFoundException(e.getMessage(), e);
    } catch (UnconfirmedUserException e) {
      throw new DisabledException(e.getMessage(), e);
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }

}
