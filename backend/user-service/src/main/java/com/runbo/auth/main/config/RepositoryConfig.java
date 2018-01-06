package com.runbo.auth.main.config;

import com.runbo.auth.command.aggregate.UserAggregate;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2018/1/2.
 */
@Configuration
public class RepositoryConfig {

    @Bean()
    public Repository<UserAggregate> userAggregateRepository(EventStore eventStore){
        return new EventSourcingRepository<UserAggregate>(UserAggregate.class, eventStore);
    }

}


