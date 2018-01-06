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

import com.runbo.auth.share.model.ContactData;
import com.runbo.auth.share.model.Timezone;
import lombok.Data;

import java.util.Locale;

/**
 * Created by lcsontos on 5/10/17.
 */
@Data
public class UserDto {

  private Long id;
  private String screenName;

  private ContactData contactData = new ContactData();

  private Timezone timezone = Timezone.AMERICA_LOS_ANGELES;
  private Locale locale = Locale.US;

  private String password;

}
