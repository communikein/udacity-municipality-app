package it.communikein.udacity_municipality.data.model;

public class User {
    public enum typeOfUser {

        Citizien,
        Municipality_Worker

    };

    public String username;
    public String email;
    public typeOfUser type;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email,typeOfUser type) {
        this.username = username;
        this.email = email;
        this.type = type;

    }

}
// [END blog_user_class]
