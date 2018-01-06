package com.runbo.auth.command.aggregate;

import com.runbo.auth.share.events.UserSignedUpEvent;
import com.runbo.auth.share.model.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Gongjj on 2017/12/31.
 */
@Getter
@Setter
@NoArgsConstructor
@Aggregate(repository = "userAggregateRepository")
public class UserAggregate {

    private static final Logger LOGGER = getLogger(UserAggregate.class);

    @AggregateIdentifier
    private UserId id;
    private String screenName;

    private ContactData contactData = new ContactData();

    private Password password;

    private Timezone timezone = Timezone.ASIA_SHANGHAI;
    private Locale locale = Locale.CHINA;

    private boolean confirmed;
    private boolean locked;
    private boolean deleted;

    private Set<ConfirmationToken> confirmationTokens = new LinkedHashSet<>();

    public UserAggregate(UserId id, String screenName, ContactData contactData, Password password, Timezone timezone, Locale locale) {
        apply(new UserSignedUpEvent(id, screenName, contactData, password, timezone, locale));
    }

    @EventHandler
    public void on(UserSignedUpEvent event) {
        this.id = event.getId();
        this.screenName = event.getScreenName();
        this.contactData = event.getContactData();
        this.password = event.getPassword();
        this.timezone = event.getTimezone();
        this.locale = event.getLocale();
        this.confirmed = false;
        this.locked = false;
        this.deleted = false;
    }
}
