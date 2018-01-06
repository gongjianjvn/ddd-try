package com.runbo.auth.share.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.axonframework.common.Assert;
import org.axonframework.common.IdentifierFactory;

import java.io.Serializable;

/**
 * Created by Gongjj on 2018/1/1.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "identifier")
@ToString(of = "identifier")
public class UserId implements Serializable {

    private static final long serialVersionUID = -4163440749566043686L;

    private final String identifier;

    public UserId() {
        this.identifier = IdentifierFactory.getInstance().generateIdentifier();
    }

    public UserId(String identifier) {
        Assert.notNull(identifier, ()->"Identifier may not be null");
        this.identifier = identifier;
    }

}
