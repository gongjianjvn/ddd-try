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

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Represents an address.
 */
@Data
@Embeddable
public class AddressData implements ValueObject<AddressData> {

  @Column(name = "country")
  private String country;
  @Column(name = "state")
  private String state;
  @Column(name = "city")
  private String city;

  @Column(name = "address_line1")
  private String addressLine1;
  @Column(name = "address_line2")
  private String addressLine2;

  @Column(name = "zip_code")
  private String zipCode;

  @Column(name = "address_type")
  private AddressType addressType;

  @Override
  public boolean sameValueAs(AddressData other) {
    return equals(other);
  }

}
