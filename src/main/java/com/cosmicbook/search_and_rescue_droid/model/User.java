package com.cosmicbook.search_and_rescue_droid.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "users")
public class User {

    // getters and setters from lombok
    @Getter
    @Id
    private String id;
    @Setter
    @Getter
    private String username;
    @Setter
    @Getter
    private String email;
    @Getter
    @Setter
    private String password;
    @Setter
    @Getter
    private Set<Role> role = new HashSet<>();
    private boolean emailVerified = false;
    private LocalDateTime emailVerifiedAt;

    public User(){}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
