package com.runbo.auth.main;

import com.runbo.auth.security.AuthSecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Administrator on 2018/1/1.
 */


@SpringBootApplication
@Configuration
@Import({
        AuthSecurityConfiguration.class
})
@EnableJpaRepositories(basePackages = {
        "com.runbo"
})

@EntityScan({
        "com.runbo",
        "org.axonframework.eventhandling.saga.repository.jpa"
})
@ComponentScan("com.runbo")
public class UserServiceApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
