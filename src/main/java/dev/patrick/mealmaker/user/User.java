package dev.patrick.mealmaker.user;

import dev.patrick.mealmaker.util.ArrayList;
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

    /** Number associated with admin role */
    public static int ADMIN_ROLE = 101;
    /** Number associated with the editor role */
    public static int EDITOR_ROLE = 201;
    /** Number associated with user role */
    public static int USER_ROLE = 301;

    @Id
    private ObjectId id;
    /** The username of the user */
    private String username;
    /** The password of the user */
    private String password;
    /** The role of the user */
    private ArrayList<Integer> roles;
    /** The access token of the user */
    @Transient
    private String accessToken;
    /** The refresh token of the user */
    private String refreshToken;

    public User (String username, String password) {
        this.username = username;
        this.password = password;

        roles = new ArrayList<>();
        this.roles.add(USER_ROLE);
    }

}
