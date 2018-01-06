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

import com.runbo.commons.domain.ValueObject;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

/**
 * Represents a contact.
 */
@Data
@Embeddable
public class ContactData implements ValueObject<ContactData> {

  @Column(name = "email", length=100)
  private String email;

  @Column(name = "first_name", length = 30)
  private String firstName;
  @Column(name = "last_name", length = 30)
  private String lastName;

  @ElementCollection
  @CollectionTable(name = "COM_ADDRESS")
  private Set<AddressData> addresses;

  @Column(name = "gender")
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column(name = "birthday")
  private LocalDate birthday;

  @Override
  public boolean sameValueAs(ContactData other) {
    return equals(other);
  }

}
