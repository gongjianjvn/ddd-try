package com.runbo.auth.share.commands;

import com.runbo.auth.share.model.UserId;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

/**
 * Created by Administrator on 2018/1/1.
 */
public class ChangeEmailCommand {

    @TargetAggregateIdentifier
    private UserId id;
}
