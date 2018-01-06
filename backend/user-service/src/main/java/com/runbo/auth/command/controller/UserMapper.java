package com.runbo.auth.command.controller;

import com.runbo.auth.query.entry.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Created by lcsontos on 5/23/17.
 */
@Mapper
public interface UserMapper {

  User toUser(UserDto userDto);

  @Mappings({
      @Mapping(target = "contactData.email", ignore = true),
      @Mapping(target = "screenName", ignore = true)
  })
  User toUserWithoutEmailAndScreenName(UserDto userDto);

  @Mappings(
      @Mapping(target = "password", ignore = true)
  )
  UserDto toUserDto(User user);


}
