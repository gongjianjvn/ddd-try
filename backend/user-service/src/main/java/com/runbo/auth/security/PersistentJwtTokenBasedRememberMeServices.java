package com.runbo.auth.security;

import com.runbo.commons.util.IdentityGenerator;
import com.runbo.commons.util.RandomUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.lang.System.currentTimeMillis;

/**
 * Created by lcsontos on 5/24/17.
 */
public class PersistentJwtTokenBasedRememberMeServices extends
    PersistentTokenBasedRememberMeServices {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(PersistentJwtTokenBasedRememberMeServices.class);

  public static final int DEFAULT_TOKEN_LENGTH = 16;

  public PersistentJwtTokenBasedRememberMeServices(
      String key, UserDetailsService userDetailsService,
      PersistentTokenRepository tokenRepository) {

    super(key, userDetailsService, tokenRepository);
  }

  @Override
  protected String[] decodeCookie(String cookieValue) throws InvalidCookieException {
    try {
      Claims claims = Jwts.parser()
          .setSigningKey(getKey())
          .parseClaimsJws(cookieValue)
          .getBody();

      return new String[] { claims.getId(), claims.getSubject() };
    } catch (JwtException e) {
      LOGGER.warn(e.getMessage());
      throw new InvalidCookieException(e.getMessage());
    }
  }

  @Override
  protected String encodeCookie(String[] cookieTokens) {
    Claims claims = Jwts.claims()
        .setId(cookieTokens[0])
        .setSubject(cookieTokens[1])
        .setExpiration(new Date(currentTimeMillis() + getTokenValiditySeconds() * 1000L))
        .setIssuedAt(new Date());

    return Jwts.builder()
        .setClaims(claims)
        .signWith(HS512, getKey())
        .compact();
  }

  @Override
  protected String generateSeriesData() {
    long seriesId = IdentityGenerator.generate();
    return String.valueOf(seriesId);
  }

  @Override
  protected String generateTokenData() {
    return RandomUtil.ints(DEFAULT_TOKEN_LENGTH)
        .mapToObj(i -> String.format("%04x", i))
        .collect(Collectors.joining());
  }

  @Override
  protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
    return Optional.ofNullable((Boolean)request.getAttribute(LoginFilter.REMEMBER_ME_ATTRIBUTE)).orElse(false);
  }

}
