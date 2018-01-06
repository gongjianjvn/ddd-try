package com.runbo.auth.query.repository;

import com.runbo.auth.domain.model.user.exceptions.NoSuchUserException;
import com.runbo.auth.query.entry.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Administrator on 2018/1/1.
 */
@Repository
public interface UserEntryRepository extends JpaRepository<User, String> {

    /**
     * Deletes the given user, provided that it exists.
     *
     * @param userId {@link com.runbo.auth.domain.model.user.User}'s ID
     * @throws NoSuchUserException if the user doesn't
     *     exist
     */
    void delete(String userId);

    /**
     * Finds a user based on its ID.
     *
     * @param id ID
     * @return a {@link com.runbo.auth.domain.model.user.User}
     */
    Optional<User> findById(String id);

    /**
     * Finds a user by email address.
     *
     * @param email Email address
     * @return a {@link com.runbo.auth.domain.model.user.User}
     */
    @Query("select o from User o where o.contactData.email = :email")
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by screen name.
     * @param screenName Screen name
     * @return a {@link com.runbo.auth.domain.model.user.User}
     */
    Optional<User> findByScreenName(String screenName);

    /**
     * Stores the given user.
     * @param user a {@link com.runbo.auth.domain.model.user.User}
     * @return the stored {@link com.runbo.auth.domain.model.user.User}
     */
    User save(User user);

}
