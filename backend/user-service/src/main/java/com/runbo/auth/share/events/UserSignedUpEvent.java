package com.runbo.auth.share.events;

import com.runbo.auth.share.model.ContactData;
import com.runbo.auth.share.model.Password;
import com.runbo.auth.share.model.Timezone;
import com.runbo.auth.share.model.UserId;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

/**
 * Created by Administrator on 2018/1/1.
 */
@Getter
@AllArgsConstructor
public class UserSignedUpEvent {
    private UserId id;
    private String screenName;
    private ContactData contactData;
    private Password password;
    private Timezone timezone;
    private Locale locale;
}
