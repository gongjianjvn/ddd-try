package com.runbo.auth.share.commands;

import com.runbo.auth.share.model.ContactData;
import com.runbo.auth.share.model.Timezone;
import com.runbo.auth.share.model.UserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.Locale;

/**
 * Created by Administrator on 2018/1/1.
 */
@Getter
@AllArgsConstructor
public class UpdateUserCommand {

    @TargetAggregateIdentifier
    private UserId id;
    private ContactData contactData = new ContactData();
    private Timezone timezone = Timezone.AMERICA_LOS_ANGELES;
    private Locale locale = Locale.US;

}
