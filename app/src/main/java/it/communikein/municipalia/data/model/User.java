package it.communikein.municipalia.data.model;

public class User {

    public enum typeOfUser {
        Citizen,
        Municipality_Worker
    }

    private String username;
    private String email;
    private typeOfUser type;
    private boolean logged;
    private String Uid;
    public String ImgLink;

    public String getImgLink() {
        return ImgLink;
    }

    public void setImg(String ImgLink) {
        this.ImgLink = ImgLink;
    }

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
        this.Uid = Uid;
    }

    public void setType(typeOfUser type) {
        this.type = type;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

}