package it.communikein.municipalia.data.model;

import android.provider.CalendarContract;

public class User {

    public enum typeOfUser {
        Citizien,
        Municipality_Worker
    };

    private String username;
    private String email;
    private typeOfUser type;
    private boolean logged;
    private String Uid;

    private User() {
        setLogged(false);
    }

    private static class SingletonHolder {
        private static final User userInstance = new User();

    }

    public static User getInstance() {
        return SingletonHolder.userInstance;
    }


    public String getUsername() {
        return username;
    }

    public String getUid() {
        return Uid;
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

    public void setUid(String Uid) {
        this.email = Uid;
    }

    public void setType(typeOfUser type) {
        this.type = type;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

}
// [END blog_user_class]
