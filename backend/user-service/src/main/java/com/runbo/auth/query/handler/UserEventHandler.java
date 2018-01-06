package com.runbo.auth.query.handler;

import com.runbo.auth.query.entry.User;
import com.runbo.auth.query.repository.UserEntryRepository;
import com.runbo.auth.share.events.UserSignedUpEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/1/1.
 */
@Component
public class UserEventHandler {

    @Autowired
    private UserEntryRepository userEntryRepository;

    @EventHandler
    public void on(UserSignedUpEvent event) {
        User user = new User();
        user.setId(event.getId().getIdentifier());
        user.setContactData(event.getContactData());
        user.setScreenName(event.getScreenName());
        user.setDeleted(false);
        user.setLocale(event.getLocale());
        user.setLocked(false);
        user.setPassword(event.getPassword());
        user.setConfirmed(false);
        userEntryRepository.save(user);
    }
}
