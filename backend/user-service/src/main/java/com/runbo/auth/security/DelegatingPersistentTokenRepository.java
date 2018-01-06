package com.runbo.auth.security;

import com.runbo.auth.domain.model.session.Session;
import com.runbo.auth.domain.model.session.exceptions.NoSuchSessionException;
import com.runbo.auth.domain.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static com.runbo.commons.util.DateTimeUtil.toDate;
import static com.runbo.commons.util.DateTimeUtil.toLocalDateTime;

/**
 * Created by lcsontos on 5/24/17.
 */
public class DelegatingPersistentTokenRepository implements PersistentTokenRepository {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(DelegatingPersistentTokenRepository.class);

  private final SessionService sessionService;

  public DelegatingPersistentTokenRepository(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  @Override
  public void createNewToken(PersistentRememberMeToken token) {
    Long sessionId = Long.valueOf(token.getSeries());
    Long userId = Long.valueOf(token.getUsername());
    sessionService.createSession(sessionId, userId, token.getTokenValue());
  }

  @Override
  public void updateToken(String series, String tokenValue, Date lastUsed) {
    Long sessionId = Long.valueOf(series);
    try {
      sessionService.useSession(sessionId, tokenValue, toLocalDateTime(lastUsed));
    } catch (NoSuchSessionException e) {
      LOGGER.warn("Session {} doesn't exists.", sessionId);
    }
  }

  @Override
  public PersistentRememberMeToken getTokenForSeries(String seriesId) {
    Long sessionId = Long.valueOf(seriesId);
    return sessionService
        .findSession(sessionId)
        .map(this::toPersistentRememberMeToken)
        .orElse(null);
  }

  @Override
  public void removeUserTokens(String username) {
    Long userId = Long.valueOf(username);
    sessionService.logoutUser(userId);
  }

  private PersistentRememberMeToken toPersistentRememberMeToken(Session session) {
    String username = String.valueOf(session.getUserId());
    String series = String.valueOf(session.getId());
    LocalDateTime lastUsedAt =
        Optional.ofNullable(session.getLastUsedAt()).orElseGet(session::getIssuedAt);
    return new PersistentRememberMeToken(
        username, series, session.getToken(), toDate(lastUsedAt));
  }

}
