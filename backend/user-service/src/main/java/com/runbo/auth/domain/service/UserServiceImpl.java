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

package com.runbo.auth.domain.service;

import com.runbo.auth.crypto.PasswordSecurity;
import com.runbo.auth.domain.model.user.exceptions.*;
import com.runbo.auth.query.entry.User;
import com.runbo.auth.query.repository.UserEntryRepository;
import com.runbo.auth.share.model.ConfirmationToken;
import com.runbo.auth.share.model.ConfirmationTokenType;
import com.runbo.auth.share.model.Password;
import com.runbo.commons.util.IdentityGenerator;
import com.runbo.commons.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

/**
 * Framework agnostic implementation of {@link UserService}.
 */
@Component
public class UserServiceImpl implements UserService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private static final int NEXT_SCREEN_NAME_MAX_TRIES = 20;

  @Autowired
  private PasswordSecurity passwordSecurity;
//  private final UserEventEmitter userEventEmitter;
  @Resource
  private UserEntryRepository userEntryRepository;

//  /**
//   * Creates an instance of {@link UserServiceImpl}, injecting its dependencies.
//   *
//   * @param passwordSecurity a concrete implementation of {@link PasswordSecurity}
//   * @param userEventEmitter a concrete implementation of {@link UserEventEmitter}
//   * @param userEntryRepository a concrete implementation of {@link UserRepository}
//   */
//  public UserServiceImpl(
//      PasswordSecurity passwordSecurity, UserEventEmitter userEventEmitter,
//      UserRepository userEntryRepository) {
//
//    Objects.requireNonNull(passwordSecurity);
//    Objects.requireNonNull(userEventEmitter);
//    Objects.requireNonNull(userEntryRepository);
//
//    this.passwordSecurity = passwordSecurity;
////    this.userEventEmitter = userEventEmitter;
//    this.userEntryRepository = userEntryRepository;
//  }

  @Override
  public User changeEmail(String userId, String newEmail)
      throws EmailIsAlreadyTakenException, InvalidEmailException, NoSuchUserException {

    Objects.requireNonNull(userId, "userId");
    Objects.requireNonNull(newEmail, "newEmail");

    User user = getUser(userId);
    checkEmail(user, newEmail);
    if (newEmail.equals(user.getEmail())) {
      return user;
    }

    user.setEmail(newEmail);
    user = store(user);

//    userEventEmitter.emit(new UserEvent(userId, UserEventType.EMAIL_CHANGED));

    return user;
  }

  @Override
  public User changePassword(String userId, String rawPassword) throws NoSuchUserException {
    Objects.requireNonNull(userId, "userId");
    Objects.requireNonNull(rawPassword, "rawPassword");

    User user = getUser(userId);
    Password newPassword = passwordSecurity.ecrypt(rawPassword);
    user.setPassword(newPassword);
    user = store(user);

//    userEventEmitter.emit(new UserEvent(userId, UserEventType.PASSWORD_CHANGED));

    return user;
  }

  @Override
  public User changeScreenName(String userId, String newScreenName)
      throws NoSuchUserException, ScreenNameIsAlreadyTakenException {

    Objects.requireNonNull(userId, "userId");
    Objects.requireNonNull(newScreenName, "newScreenName");

    User user = getUser(userId);
    checkScreenName(user, newScreenName);
    user.setScreenName(newScreenName);
    user = store(user);

//    userEventEmitter.emit(new UserEvent(userId, UserEventType.SCREEN_NAME_CHANGED));

    return user;
  }

  @Override
  public User confirmEmail(String userId, String token)
      throws InvalidConfirmationTokenException, NoSuchUserException {

    Objects.requireNonNull(userId, "userId");
    Objects.requireNonNull(token, "token");

    User user = getUser(userId);

    ConfirmationToken confirmationToken = user.useConfirmationToken(token);

    Optional<String> newEmail = confirmationToken.getPayload();
    if (!newEmail.isPresent()) {
      boolean confirmed = user.isConfirmed();
      user.setConfirmed(true);
      user = store(user);
      if (!confirmed) {
//        userEventEmitter.emit(new UserEvent(userId, UserEventType.EMAIL_CONFIRMED));
      }
    } else {
      try {
        user = changeEmail(userId, newEmail.get());
      } catch (EmailIsAlreadyTakenException | InvalidEmailException e) {
        LOGGER.warn(e.getMessage(), e);
      }
    }

    return user;
  }

  @Override
  public User confirmPasswordReset(String userId, String token)
      throws InvalidConfirmationTokenException, NoSuchUserException {

    Objects.requireNonNull(userId, "userId");
    Objects.requireNonNull(token, "token");

    User user = getUser(userId);
    user.useConfirmationToken(token);
    user = store(user);

//    userEventEmitter.emit(new UserEvent(userId, UserEventType.PASSWORD_RESET_CONFIRMED));

    return user;
  }

  @Override
  public void delete(String userId) throws NoSuchUserException {
    userEntryRepository.delete(userId);
//    userEventEmitter.emit(new UserEvent(userId, UserEventType.DELETED));
  }

  @Override
  public Optional<User> findUser(String userId) {
    Objects.requireNonNull(userId);
    return userEntryRepository.findById(userId);
  }

  @Override
  public Optional<User> findUserByEmail(String emailOrScreenName) {
    Objects.requireNonNull(emailOrScreenName);
    Optional<User> user = null;
    if (Validator.isEmail(emailOrScreenName)) {
      user = userEntryRepository.findByEmail(emailOrScreenName);
    } else {
      user = userEntryRepository.findByScreenName(emailOrScreenName);
    }
    return user;
  }

  @Override
  public User getUser(String userId) throws NoSuchUserException {
    Optional<User> user = findUser(userId);
    return user.orElseThrow(NoSuchUserException::new);
  }

  @Override
  public User getUserByEmail(String emailOrScreenName) throws NoSuchUserException {
    Optional<User> user = findUser(emailOrScreenName);
    return user.orElseThrow(NoSuchUserException::new);
  }

  @Override
  public boolean isEmailTaken(String email) {
    Objects.requireNonNull(email);
    Optional<User> user = userEntryRepository.findByEmail(email);
    return user.isPresent();
  }

  @Override
  public boolean isScreenNameTaken(String screenName) {
    Objects.requireNonNull(screenName);
    Optional<User> user = userEntryRepository.findByScreenName(screenName);
    return user.isPresent();
  }

  @Override
  public User login(String emailOrScreenName, String rawPassword)
      throws NoSuchUserException, UnconfirmedUserException {

    Objects.requireNonNull(emailOrScreenName, "emailOrScreenName");
    Objects.requireNonNull(rawPassword, "rawPassword");

    User user = getUser(emailOrScreenName);

    if (!user.isConfirmed()) {
      throw new UnconfirmedUserException();
    }

    if (passwordSecurity.check(user.getPassword(), rawPassword)) {
      // TODO: invalid all password reset tokens.
//      userEventEmitter.emit(new UserEvent(user.getId(), UserEventType.SIGNIN_SUCCEEDED));
      return user;
    }

//    userEventEmitter.emit(new UserEvent(user.getId(), UserEventType.SIGNIN_FAILED));
    throw new NoSuchUserException();
  }

  @Override
  public String nextScreenName(String email) throws InvalidEmailException {
    Objects.requireNonNull(email);
    if (!Validator.isEmail(email)) {
      throw new InvalidEmailException();
    }

    String screenName = email.split("@")[0];

    int index = 1;
    String possibleScreenName = screenName;
    while (isScreenNameTaken(possibleScreenName) && index < NEXT_SCREEN_NAME_MAX_TRIES) {
      possibleScreenName = screenName + (index++);
    }

    if (index < NEXT_SCREEN_NAME_MAX_TRIES) {
      return possibleScreenName;
    }

    if (!isScreenNameTaken(possibleScreenName)) {
      return possibleScreenName;
    } else {
      return screenName + IdentityGenerator.generate();
    }
  }

  @Override
  public void requestEmailChange(String userId, String newEmail)
      throws NoSuchUserException, EmailIsAlreadyTakenException, InvalidEmailException {

    Objects.requireNonNull(userId, "userId");
    Objects.requireNonNull(newEmail, "newEmail");

    User user = getUser(userId);
    checkEmail(user, newEmail);
    if (newEmail.equals(user.getEmail())) {
      return;
    }

    ConfirmationToken confirmationToken = user.addConfirmationToken(ConfirmationTokenType.PASSWORD_RESET);
    store(user);

//    userEventEmitter.emit(new UserEvent<>(userId, UserEventType.EMAIL_CHANGE_REQUESTED, confirmationToken));
  }

  @Override
  public void requestPasswordReset(String userId) throws NoSuchUserException {
    Objects.requireNonNull(userId, "userId");

    User user = getUser(userId);
    ConfirmationToken confirmationToken = user.addConfirmationToken(ConfirmationTokenType.PASSWORD_RESET);
    store(user);

//    userEventEmitter.emit(new UserEvent<>(userId, UserEventType.PASSWORD_RESET_REQUESTED, confirmationToken));
  }

  @Override
  public void signup(User user, String rawPassword)
      throws InvalidEmailException, EmailIsAlreadyTakenException,
      ScreenNameIsAlreadyTakenException {

    Objects.requireNonNull(user, "user");
    Objects.requireNonNull(rawPassword, "rawPassword");

    String email = user.getEmail();
    if (!Validator.isEmail(email)) {
      throw new InvalidEmailException();
    }

    if (isEmailTaken(email)) {
      throw new EmailIsAlreadyTakenException();
    }

    if (isScreenNameTaken(user.getScreenName())) {
      throw new ScreenNameIsAlreadyTakenException();
    }

    Password password = passwordSecurity.ecrypt(rawPassword);
    user.setPassword(password);
    user = store(user);

//    userEventEmitter.emit(new UserEvent(user.getId(), UserEventType.SIGNUP_REQUESTED));
  }

  @Override
  public User store(User user) {
    // FIXME: don't overwrite email, screenname and password here.
    return userEntryRepository.save(user);
  }

  private void checkEmail(User user, String newEmail)
      throws EmailIsAlreadyTakenException, InvalidEmailException {

    if (!Validator.isEmail(newEmail)) {
      throw new InvalidEmailException();
    }

    Optional<User> otherUser = findUser(newEmail);
    if (otherUser.isPresent() && !user.equals(otherUser.get())) {
      throw new EmailIsAlreadyTakenException();
    }
  }

  private void checkScreenName(User user, String newScreenName)
      throws ScreenNameIsAlreadyTakenException {

    Optional<User> otherUser = findUser(newScreenName);
    if (otherUser.isPresent() && !user.equals(otherUser.get())) {
      throw new ScreenNameIsAlreadyTakenException();
    }
  }

}
