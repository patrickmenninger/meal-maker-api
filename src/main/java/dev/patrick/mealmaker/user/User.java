package dev.patrick.mealmaker.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.patrick.mealmaker.util.ArrayList;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;


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

    /** Number associated with admin role */
    public static int ADMIN_ROLE = 201;
    /** Number associated with user role */
    public static int USER_ROLE = 101;

    @Id
    @JsonIgnore
    private ObjectId id;
    /** The username of the user */
    private String username;
    /** The password of the user */
    @JsonIgnore
    private String password;
    /** The role of the user */
    private ArrayList<Integer> roles;
    /** The access token of the user */
    @Transient
    private String accessToken;
    /** The refresh token of the user */
    @JsonIgnore
    private String refreshToken;

    public User (String username, String password) {
        setUsername(username);
        setPassword(password);

        roles = new ArrayList<>();
        this.roles.add(USER_ROLE);
    }

    private void setUsername(String username) {
        if (username == null || username.equals("")) {
            throw new AuthenticationCredentialsNotFoundException("Username required");
        }

        this.username = username;
    }

    private void setPassword(String pw) {
        if (pw == null || pw.equals("")) {
            throw new AuthenticationCredentialsNotFoundException("Password required.");
        }

        this.password = pw;
    }

}
