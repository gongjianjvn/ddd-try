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

import com.runbo.auth.domain.model.user.exceptions.*;
import com.runbo.auth.query.entry.User;

import java.util.Optional;

/**
 * {@link UserService} groups functionaries which are needed to manage all the aspects of users.
 */
public interface UserService {

  /**
   * Changes the {@link User}'s email address, provided that {@code newEmail} is available.
   *
   * @param userId {@link User}'s ID
   * @param newEmail new email address
   * @return the modified {@link User}
   * @throws NoSuchUserException if the user doesn't exist
   * @throws EmailIsAlreadyTakenException if the given email is already taken
   * @throws InvalidEmailException if the given {@code email} isn't an email address.
   */
  User changeEmail(String userId, String newEmail)
      throws EmailIsAlreadyTakenException, InvalidEmailException, NoSuchUserException;

  /**
   * Changes the {@link User}'s password.
   *
   * @param userId {@link User}'s ID
   * @param rawPassword new (cleartext) password
   * @return the modified {@link User}
   * @throws NoSuchUserException if the user doesn't exist
   */
  User changePassword(String userId, String rawPassword) throws NoSuchUserException;

  /**
   * Changes the {@link User}'s screen name, provided that {@code newScreenName} is available.
   *
   * @param userId {@link User}'s ID
   * @param newScreenName new screen name
   * @return the modified {@link User}
   * @throws NoSuchUserException if the user doesn't exist
   * @throws ScreenNameIsAlreadyTakenException if the given email is already taken
   */
  User changeScreenName(String userId, String newScreenName)
      throws NoSuchUserException, ScreenNameIsAlreadyTakenException;

  /**
   * Confirms the {@link User}'s email address with the given token, provided that it's valid.
   *
   * @param userId {@link User}'s ID
   * @param token confirmation token
   * @return the modified {@link User}
   * @throws NoSuchUserException if the user doesn't exist
   * @throws InvalidConfirmationTokenException if the given confirmation token is invalid
   */
  User confirmEmail(String userId, String token)
      throws InvalidConfirmationTokenException, NoSuchUserException;

  /**
   * Confirms the {@link User}'s previously requested password reset.
   *
   * @param userId {@link User}'s ID
   * @param token confirmation token
   * @return the modified {@link User}
   * @throws NoSuchUserException if the user doesn't exist
   * @throws InvalidConfirmationTokenException if the given confirmation token is invalid
   */
  User confirmPasswordReset(String userId, String token)
      throws InvalidConfirmationTokenException, NoSuchUserException;

  /**
   * Deletes the given {@link User}.
   *
   * @param userId {@link User}'s ID
   * @throws NoSuchUserException if the user doesn't exist
   */
  void delete(String userId) throws NoSuchUserException;

  /**
   * Finds a {@link User} in the system by its ID.
   *
   * @param userId Email or screen name
   * @return the {@link User} if exists, null otherwise
   */
  Optional<User> findUser(String userId);

  /**
   * Finds a {@link User} in the system by its ID.
   *
   * @param emailOrScreenName Email or screen name
   * @return the {@link User} if exists, null otherwise
   */
  Optional<User> findUserByEmail(String emailOrScreenName);

  /**
   * Finds a {@link User} in the system by its ID.
   *
   * @param userId Email or screen name
   * @return the {@link User}'s ID if exists
   * @throws NoSuchUserException if the user doesn't exist
   */
  User getUser(String userId) throws NoSuchUserException;

  /**
   * Finds a {@link User} in the system by its ID.
   *
   * @param emailOrScreenName Email or screen name
   * @return the {@link User}'s ID if exists
   * @throws NoSuchUserException if the user doesn't exist
   */
  User getUserByEmail(String emailOrScreenName) throws NoSuchUserException;

  /**
   * Checks if the given {@code email} is taken.
   *
   * @param email email to check
   * @return true if it's taken, false otherwise
   */
  boolean isEmailTaken(String email);

  /**
   * Checks if the given {@code screenName} is taken.
   *
   * @param screenName screenName to check
   * @return true if it's taken, false otherwise
   */
  boolean isScreenNameTaken(String screenName);

  /**
   * Logs a user in with the given {@code emailOrScreenName} and {@code password}.
   *
   * @param emailOrScreenName Email or screen name
   * @param password password
   * @return the {@link User} if it exists and its password matches
   */
  User login(String emailOrScreenName, String password)
      throws NoSuchUserException, UnconfirmedUserException;

  /**
   * Returns the next available screen name based on the given email address.
   *
   * @param email Email
   * @return an available screen name.
   * @throws InvalidEmailException if the given {@code email} isn't an email address.
   */
  String nextScreenName(String email) throws InvalidEmailException;

  /**
   * Request email change for the given user.
   *
   * @param userId {@link User}'s ID
   * @param newEmail new email
   * @throws NoSuchUserException if the user doesn't exist
   */
  void requestEmailChange(String userId, String newEmail)
      throws InvalidEmailException, EmailIsAlreadyTakenException, NoSuchUserException;

  /**
   * Request password reset for the given {@link User}.
   *
   * @param userId {@link User}'s ID
   * @throws NoSuchUserException if the user doesn't exist
   */
  void requestPasswordReset(String userId) throws NoSuchUserException;

  /**
   * Signs a user up.
   *
   * @param user a {@link User}
   * @param rawPassword {@link User}'s cleartext password
   */
  void signup(User user, String rawPassword)
      throws InvalidEmailException, EmailIsAlreadyTakenException, ScreenNameIsAlreadyTakenException;

  /**
   * Stores the given {@link User}.
   *
   * @param user a {@link User} to store
   * @return the stored user
   */
  User store(User user)
      throws InvalidEmailException, EmailIsAlreadyTakenException, ScreenNameIsAlreadyTakenException;

}
