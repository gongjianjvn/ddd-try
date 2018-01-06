package com.runbo.auth.share.commands;

import com.runbo.auth.share.model.UserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

/**
 * Created by Administrator on 2018/1/1.
 */
@Getter
@AllArgsConstructor
public class RequestResetPasswordCommand {

    @TargetAggregateIdentifier
    private UserId id;
}
