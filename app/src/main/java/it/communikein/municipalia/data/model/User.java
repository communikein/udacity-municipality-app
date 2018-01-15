package it.communikein.municipalia.data.model;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class User {

    public enum typeOfUser {
        Citizen,
        Municipality_Worker
    }

    private String username;
    private String email;
    private typeOfUser type;
    private boolean logged;
    private String uid;
    private String imageUrl;

    @Inject
    public User() {
        setLogged(false);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public typeOfUser getType() {
        return type;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setType(typeOfUser type) {
        this.type = type;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

}