package com.runbo.auth.share.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Administrator on 2018/1/1.
 */
@Getter
@AllArgsConstructor
public class AuthenticateUserCommand {

    private String username;
    private String password;
}
