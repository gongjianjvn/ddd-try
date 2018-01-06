package com.runbo.auth.command.handler;

import com.runbo.auth.command.aggregate.UserAggregate;
import com.runbo.auth.crypto.PasswordSecurity;
import com.runbo.auth.share.commands.SignUpUserCommand;
import com.runbo.auth.share.model.Password;
import com.runbo.auth.share.model.UserId;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Repository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Administrator on 2018/1/1.
 */

@Component
public class UserHandler {
    private static final Logger LOGGER = getLogger(UserHandler.class);

    @Autowired
    private Repository<UserAggregate> userAggregateRepository;
    @Autowired
    private PasswordSecurity passwordSecurity;


    @CommandHandler
    public UserId handle(SignUpUserCommand command) throws Exception {
        UserId identifier = command.getId();
        Password password = passwordSecurity.ecrypt(command.getRawPassword());
        userAggregateRepository.newInstance(() -> new UserAggregate(identifier, command.getScreenName(), command.getContactData(), password, command.getTimezone(), command.getLocale()));
        return identifier;
    }
}
