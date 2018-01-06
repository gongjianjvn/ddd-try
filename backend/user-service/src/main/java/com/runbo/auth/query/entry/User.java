package com.runbo.auth.query.entry;

import com.runbo.auth.domain.model.user.exceptions.InvalidConfirmationTokenException;
import com.runbo.auth.share.model.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Administrator on 2018/1/1.
 */

@Data
@NoArgsConstructor
@Entity
@Table(name = "COM_USER")
public class User {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "screen_name", length = 30)
    private String screenName;

    @Embedded
    private ContactData contactData = new ContactData();

    @Embedded
    private Password password;

    @Column(name = "timezone")
    @Enumerated(EnumType.STRING)
    private Timezone timezone = Timezone.ASIA_SHANGHAI;
    @Column(name = "locale")
    private Locale locale = Locale.CHINA;

    @Column(name = "confirmed")
    private boolean confirmed;
    @Column(name = "locked")
    private boolean locked;
    @Column(name = "deleted")
    private boolean deleted;

    @ElementCollection(targetClass=Long.class)
    // 映射保存集合属性的表
    @CollectionTable(
            name="COM_USER_AUTHORITY",
            joinColumns={
                    @JoinColumn(name="user_id", referencedColumnName = "id", nullable=false)
            }
    )
    @Column(name="authority")
    private Set<String> authorities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "owner")
    private Set<ConfirmationToken> confirmationTokens = new LinkedHashSet<>();

    /**
     * Creates a {@link User} instance.
     *
     * @param id ID
     * @param screenName screen name
     * @param email email
     */
    public User(String id, String screenName, String email) {
        this.id = id;
        this.screenName = screenName;
        contactData.setEmail(email);
    }

    /**
     * Add a confirmation token to the user and invalidates all other confirmation tokens of the same
     * type.
     *
     * @param type Token type
     * @return the newly added confirmation token
     */
    public ConfirmationToken addConfirmationToken(ConfirmationTokenType type) {
        return addConfirmationToken(type, 0);
    }


    /**
     * Add a confirmation token to the user and invalidates all other confirmation tokens of the same
     * type.
     *
     * @param type token type
     * @param minutes token's expiration period in minutes
     * @return the newly added confirmation token
     */
    public ConfirmationToken addConfirmationToken(ConfirmationTokenType type, int minutes) {
        if (minutes == 0) {
            minutes = ConfirmationToken.DEFAULT_EXPIRATION_MINUTES;
        }
        // TODO: invalide all other confirmation tokens.
        ConfirmationToken confirmationToken = new ConfirmationToken(this, type, minutes);
        confirmationTokens.add(confirmationToken);
        return confirmationToken;
    }

    /**
     * Gets the confirmation token instance for the given token value, provided that it exists.
     *
     * @param token token's value.
     * @return a confirmation token
     */
    public Optional<ConfirmationToken> getConfirmationToken(String token) {
        return confirmationTokens.stream()
                .filter(ct -> token.equals(ct.getValue()))
                .findFirst();
    }

    public String getEmail() {
        return contactData.getEmail();
    }

    public String getId() {
        return id;
    }

    public boolean sameIdentityAs(User other) {
        return equals(other);
    }

    public void setEmail(String email) {
        contactData.setEmail(email);
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Uses the given confirmation token if it exists and it's still valid.
     *
     * @param token token's value
     * @return the used token
     * @throws InvalidConfirmationTokenException if there was no such token or if it wasn't valid.
     */
    public ConfirmationToken useConfirmationToken(String token)
            throws InvalidConfirmationTokenException {

        Optional<ConfirmationToken> confirmationTokenHolder = getConfirmationToken(token);
        if (!confirmationTokenHolder.isPresent()) {
            throw new InvalidConfirmationTokenException();
        }

        ConfirmationToken confirmationToken = confirmationTokenHolder.get();
        if (!confirmationToken.isValid()) {
            throw new InvalidConfirmationTokenException();
        }

        return confirmationToken.use();
    }
}
