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

package com.runbo.auth.share.model;

import com.runbo.auth.query.entry.User;
import com.runbo.commons.domain.AuditData;
import com.runbo.commons.domain.Entity;
import com.runbo.commons.util.DateTimeUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.runbo.commons.util.DateTimeUtil.expireNowUtc;
import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Confirmation token functions as a one-time password for being able to perform operations like
 * email change and password reset.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "value")
@NoArgsConstructor
@javax.persistence.Entity
@Table(name = "COM_USER_CONFIRMATION_TOKEN")
public class ConfirmationToken implements Entity<String, ConfirmationToken > {

  public static final int DEFAULT_EXPIRATION_MINUTES = 10;

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "value")
  private String value;

  @ManyToOne()
  @JoinColumn(name = "user_id")
  private User owner;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private ConfirmationTokenType type;

  @Column(name = "valid")
  private boolean valid = true;

  @Column(name = "expires_at")
  private LocalDateTime expiresAt;

  @Column(name = "used_at")
  private LocalDateTime usedAt;

  @Column(name = "payload")
  private String payload;

//  private AuditData<User> auditData;

  /**
   * Creates a new confirmation token with the given {@link User} and expiration period.
   *
   * @param owner a {@link User}
   * @param type confirmation token's type
   */
  public ConfirmationToken(User owner, ConfirmationTokenType type) {
    // FIXME: Use a bit more sophisticated random token value generaton later
    this(owner, type, DEFAULT_EXPIRATION_MINUTES);
  }

  /**
   * Creates a new confirmation token with the given {@link User} and expiration period.
   *
   * @param owner a {@link User}
   * @param type confirmation token's type
   * @param minutes expiration in minutes
   */
  public ConfirmationToken(User owner, ConfirmationTokenType type, int minutes) {
    // FIXME: Use a bit more sophisticated random token value generaton later
    this(owner, UUID.randomUUID().toString(), type, minutes);
  }

  /**
   * Creates a new confirmation token with the given {@link User} and expiration period.
   *
   * @param owner a {@link User}
   * @param value tokens's value
   * @param type confirmation token's type
   */
  public ConfirmationToken(User owner, String value, ConfirmationTokenType type) {
    this(owner, value, type, DEFAULT_EXPIRATION_MINUTES, null);
  }

  /**
   * Creates a new confirmation token with the given {@link User} and expiration period.
   *
   * @param owner a {@link User}
   * @param value tokens's value
   * @param type confirmation token's type
   * @param minutes expiration in minutes
   */
  public ConfirmationToken(User owner, String value, ConfirmationTokenType type, int minutes) {
    this(owner, value, type, minutes, null);
  }

  /**
   * Creates a new confirmation token with the given {@link User} and expiration period.
   *
   * @param owner a {@link User}
   * @param value tokens's value
   * @param type confirmation token's type
   * @param minutes expiration in minutes
   */
  public ConfirmationToken(
      User owner, String value, ConfirmationTokenType type, int minutes, String payload) {

    this.value = value;
    this.owner = owner;
    this.type = type;
    this.payload = payload;

    expiresAt = expireNowUtc(minutes, MINUTES);
  }

  @Override
  public String getId() {
    return id;
  }

  /**
   * Gets the token's payload if any.
   *
   * @return token's payload.
   */
  public Optional<String> getPayload() {
    return Optional.ofNullable(payload);
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public boolean sameIdentityAs(ConfirmationToken other) {
    return equals(other);
  }

  /**
   * Use the confirmation token.
   *
   * @return this confirmation token.
   */
  public ConfirmationToken use() {
    valid = false;
    usedAt = DateTimeUtil.nowUtc();
    return this;
  }

}
