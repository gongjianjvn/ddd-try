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

package com.runbo.commons.security;

import com.runbo.commons.util.IdentityGenerator;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.lang.System.currentTimeMillis;

/**
 * Created by lcsontos on 5/17/17.
 */
public class JwtTokenServiceImpl implements JwtTokenService {

  private static final String AUTHORITIES = "authorities";

  private final byte[] secretkey;

  public JwtTokenServiceImpl(String secretkey) {
    this.secretkey = Base64.getDecoder().decode(secretkey);
  }

  @Override
  public String createJwtToken(Authentication authentication, int minutes) {
    Claims claims = Jwts.claims()
        .setId(String.valueOf(IdentityGenerator.generate()))
        .setSubject(authentication.getName())
        .setExpiration(new Date(currentTimeMillis() + minutes * 60 * 1000))
        .setIssuedAt(new Date());

    String authorities = authentication.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .map(String::toUpperCase)
        .collect(Collectors.joining(","));

    claims.put(AUTHORITIES, authorities);

    return Jwts.builder()
        .setClaims(claims)
        .signWith(HS512, secretkey)
        .compact();
  }

  @Override
  public Authentication parseJwtToken(String jwtToken) throws AuthenticationException {
    try {
      Claims claims = Jwts.parser()
            .setSigningKey(secretkey)
            .parseClaimsJws(jwtToken)
            .getBody();

      return JwtAuthenticationToken.of(claims);
    } catch (ExpiredJwtException | SignatureException e) {
      throw new BadCredentialsException(e.getMessage(), e);
    } catch (UnsupportedJwtException | MalformedJwtException e) {
      throw new AuthenticationServiceException(e.getMessage(), e);
    } catch (IllegalArgumentException e) {
      throw new InternalAuthenticationServiceException(e.getMessage(), e);
    }
  }

}
