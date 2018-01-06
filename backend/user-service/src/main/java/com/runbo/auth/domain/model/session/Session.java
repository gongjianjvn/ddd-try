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

package com.runbo.auth.domain.model.session;

import com.runbo.commons.domain.Entity;
import com.runbo.commons.util.DateTimeUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static com.runbo.commons.util.DateTimeUtil.nowUtc;
import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Data stored in {@link User}'s session.
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@javax.persistence.Entity
@Table(name = "COM_SESSION")
public class Session implements Entity<Long, Session> {

  public static final int DEFAULT_EXPIRATION_MINUTES = 30 * 24 * 60;

  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "token")
  private String token;

  @Column(name = "expires_at")
  private LocalDateTime expiresAt;
  @Column(name = "issue_at")
  private LocalDateTime issuedAt;
  @Column(name = "last_used_at")
  private LocalDateTime lastUsedAt;
  @Column(name = "removed_at")
  private LocalDateTime removedAt;

  @Column(name = "deleted")
  private boolean deleted;

  /**
   * Creates a new {@link Session} with the given ID for the given User ID.
   *
   * @param id Session ID
   * @param userId User ID
   */
  public Session(Long id, Long userId, String token) {
    this(id, userId, token, DEFAULT_EXPIRATION_MINUTES);
  }

  /**
   * Creates a new {@link Session} with the given ID for the given User ID.
   *
   * @param id Session ID
   * @param userId User ID
   * @param token Token's token
   * @param minutes minutes to expire from time of issue
   */
  public Session(Long id, Long userId, String token, int minutes) {
    this.id = id;
    this.userId = userId;
    this.token = token;
    if (minutes == 0) {
      minutes = DEFAULT_EXPIRATION_MINUTES;
    }
    expiresAt = DateTimeUtil.expireNowUtc(minutes, MINUTES);
    issuedAt = DateTimeUtil.nowUtc();
  }

  /**
   * Use for testing only.
   *
   * @param id Session ID
   * @param userId User ID
   * @param token Token's token
   * @param expiresAt expire at
   * @param issuedAt issued at
   */
  public Session(
      Long id, Long userId, String token, LocalDateTime expiresAt, LocalDateTime issuedAt) {

    this.id = id;
    this.userId = userId;
    this.token = token;
    this.expiresAt = expiresAt;
    this.issuedAt = issuedAt;
  }

  @Override
  public Long getId() {
    return id;
  }

  /**
   * Returns is this session if still valid.
   *
   * @return true if valid, false otherwise
   */
  public boolean isValid() {
    LocalDateTime now = DateTimeUtil.nowUtc();
    return isValid(now);
  }

  public boolean isValid(LocalDateTime now) {
    return expiresAt.isAfter(now) && !deleted;
  }

  @Override
  public boolean sameIdentityAs(Session other) {
    return false;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
    this.removedAt = deleted ? nowUtc() : null;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

}
