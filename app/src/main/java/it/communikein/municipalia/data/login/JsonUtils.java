package it.communikein.municipalia.data.login;

import org.json.JSONException;
import org.json.JSONObject;

import it.communikein.municipalia.data.model.User;

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
     * make a JsonObject with User data specified
     * @param name the name of the user retrieved from the server.
     * @param email the email of the user retrieved from the server
     * @param roleOfUser the type of the user if Citizen or not
     * @param uid the user id unique for each user
     * @return JSONObject ready to be sent to the server
     */
    public static JSONObject userToJson (String name, String email, String uid, User.typeOfUser roleOfUser){
        JSONObject mainObject = new JSONObject();

        try {
            mainObject.put(NAME, name);
            mainObject.put(EMAIL, email);
            mainObject.put(UID, uid);

            if(roleOfUser == User.typeOfUser.Citizen)
                mainObject.put(ROLE, 1);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return mainObject;
    }

    /**
     * Set the user attributes starting from a JsonObject string
     * @param json the Json String containing User data.
     * @return the User instance is taken from singleton
     */
    public static void setUserFromJson(User user, String json) throws JSONException {
        JSONObject mainObject = new JSONObject(json);

        try {
            User.typeOfUser type;
            String email  = mainObject.getString("email");
            String userId = mainObject.getString("uid");
            String name = mainObject.getString("name");
            int role = Integer.parseInt(mainObject.getString("role"));
            if(role == 1)
                type = User.typeOfUser.Citizen;
            else
                type = User.typeOfUser.Municipality_Worker;

            user.setEmail(email);
            user.setType(type);
            user.setLogged(true);
            user.setUsername(name);
            user.setUid(userId);
        } catch (JSONException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
