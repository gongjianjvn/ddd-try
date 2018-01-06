package com.runbo.auth.share.events;

import com.runbo.auth.share.model.UserId;

/**
 * Created by Administrator on 2018/1/1.
 */
public class PasswordChangedEvent {
    private UserId id;
    private String password;
}
