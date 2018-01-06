package com.runbo.auth.share.commands;

import com.runbo.auth.share.model.ContactData;
import com.runbo.auth.share.model.Timezone;
import com.runbo.auth.share.model.UserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.Locale;

/**
 * Created by Administrator on 2018/1/1.
 */
@Getter
@Setter
@AllArgsConstructor
public class SignUpUserCommand {

    @TargetAggregateIdentifier
    private UserId id;
    private String screenName;
    private ContactData contactData = new ContactData();
    private String rawPassword;
    private Timezone timezone = Timezone.AMERICA_LOS_ANGELES;
    private Locale locale = Locale.US;

}
