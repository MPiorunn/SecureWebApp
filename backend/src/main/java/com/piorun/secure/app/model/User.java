package com.piorun.secure.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class User {

    @Id
    @JsonIgnore
    private String id;

    private String username;

    @JsonIgnore
    private String hash;

    private String email;

    @JsonIgnore
    private String saltId;



    public User() {
    }

    public User(String username, String hash, String email, String saltId) {
        this.username = username;
        this.hash = hash;
        this.email = email;
        this.saltId = saltId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSaltId() {
        return saltId;
    }

    public void setSaltId(String saltId) {
        this.saltId = saltId;
    }

}
