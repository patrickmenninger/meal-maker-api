package dev.patrick.mealmaker.user;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The User class contains the username and password of a user
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Document(collection = "users")
public class User {

    @Id
    private ObjectId id;
    /** The username of the user */
    private String username;
    /** The password of the user */
    private String password;
    /** The access token of the user */
    @Transient
    private String accessToken;
    /** The refresh token of the user */
    private String refreshToken;

    public User (String username, String password) {
        this.username = username;
        this.password = password;
    }

}
