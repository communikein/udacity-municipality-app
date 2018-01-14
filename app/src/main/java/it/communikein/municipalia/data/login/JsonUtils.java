package it.communikein.municipalia.data.login;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import it.communikein.municipalia.data.model.User;

import static android.content.ContentValues.TAG;

/**
 * Created by Francesco Reale on 10/01/2018.
 * francescoa.reale@gmail.com
 *
 */

/*! \class JsonUtils
    \brief This class provides utilities for Json parse
*/
public class JsonUtils {

    // attributes of the User
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String UID = "uid";
    private static final String ROLE = "role";

    /**
     * Fill out the User object .
     * @param userId  the User Id.
     * @param name the name of the user retrived from the server.
     * @param email the email of the user retrived from the server
     * @param type the type of the user if Citizien or not
     * @return nothing becuase the User is implemented with a singleton class
     */
    private static void writeNewUser(String userId, String name, String email,User.typeOfUser type) {
        User user = User.getInstance();
        user.setEmail(email);
        user.setType(type);
        user.setLogged(true);
        user.setUsername(name);
        user.setUid(userId);
        Log.d(TAG, "setting User with" + user.getEmail() + " "+ user.getUid()+" " + user.getUsername() );
    }
    /**
     * make a JsonObject with User data specified
     * @param name the name of the user retrived from the server.
     * @param email the email of the user retrived from the server
     * @param roleOfUser the type of the user if Citizien or not
     * @param uid the user id uniwie for each user
     * @return JSONObject wready to be sent to the server
     */
    public static JSONObject userToJson (String name, String email, String uid, User.typeOfUser roleOfUser){

        JSONObject mainObject = new JSONObject();
        try {
            mainObject.put(NAME,name);
            mainObject.put(EMAIL,email);
            mainObject.put(UID,uid);
            if(roleOfUser==User.typeOfUser.Citizien)
                mainObject.put(ROLE,1);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
            return mainObject;
    }
    /**
     * Set the user attributes starting from a JsonObject string
     * @param json the Json String containing User data.
     * @return the User istance is taken from singleton
     */

    public static void setUserFromJson(String json) throws JSONException {

        JSONObject mainObject = new JSONObject(json);
        JSONObject uniObject = null;
        User.typeOfUser type;
        try {
            String email  = mainObject.getString("email");
            String userId = mainObject.getString("uid");
            String name = mainObject.getString("name");
            int role = Integer.parseInt(mainObject.getString("role"));
            if(role ==1)
                type = User.typeOfUser.Citizien;
            else
                type = User.typeOfUser.Municipality_Worker;

            writeNewUser(userId,name,email,type);
        } catch (JSONException e) {
            e.printStackTrace();
            throw e;
        }

    }
}
