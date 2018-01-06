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

package com.runbo.auth.command.controller;

import com.runbo.auth.domain.service.UserService;
import com.runbo.auth.share.commands.SignUpUserCommand;
import com.runbo.auth.share.model.Timezone;
import com.runbo.auth.share.model.UserId;
import com.runbo.commons.domain.exceptions.ApplicationException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

/**
 * Created by lcsontos on 5/9/17.
 */
@RestController
@RequestMapping("/users")
public class UserController {

  private final UserMapper userMapper = new UserMapperImpl();
  @Autowired
  private UserService userService;
  @Autowired
  private CommandGateway commandGateway;

//  @GetMapping("/{userId}")
//  public UserDto getUser(@PathVariable long userId) throws ApplicationException {
//    User user = userService.getUser(userId);
//    return userMapper.toUserDto(user);
//  }
//
//  @GetMapping("/")
//  public List<User> getUsers() {
//    // TODO: add a service method for listing users
//    return null;
//  }

  @PostMapping
  public void createUser(@RequestBody @Validated UserDto userDto) throws ApplicationException {
//    User user = userMapper.toUser(userDto);
//    userService.signup(user, userDto.getPassword());
    UserId userId = new UserId();
    SignUpUserCommand command = new SignUpUserCommand(userId, userDto.getScreenName(), userDto.getContactData(),
            userDto.getPassword(), Timezone.ASIA_SHANGHAI, Locale.SIMPLIFIED_CHINESE);
    this.commandGateway.sendAndWait(command);
  }

//  @DeleteMapping("/{userId}")
//  public void deleteUser(@PathVariable long userId) throws ApplicationException {
//    userService.delete(userId);
//  }
//
//  @PutMapping
//  public UserDto updateUser(@RequestBody UserDto userDto) throws ApplicationException {
//    User user = userMapper.toUserWithoutEmailAndScreenName(userDto);
//    user = userService.store(user);
//    return userMapper.toUserDto(user);
//  }
//
//  @PutMapping("{userId}/confirm_email/{token}")
//  public void confirmEmail(@PathVariable Long userId, @PathVariable String token)
//      throws ApplicationException {
//
//    userService.confirmEmail(userId, token);
//  }
//
//  @PutMapping("{userId}/confirm_password_reset/{token}")
//  public void confirmPasswordReset(@PathVariable Long userId, @PathVariable String token)
//      throws ApplicationException {
//
//    userService.confirmPasswordReset(userId, token);
//  }

}
