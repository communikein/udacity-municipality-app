package it.communikein.udacity_municipality.data.login;

import org.json.JSONException;
import org.json.JSONObject;

import it.communikein.udacity_municipality.data.model.User;

/**
 * Created by F63801B on 10/01/2018.
 */

public class JsonUtils {

    private static void writeNewUser(String userId, String name, String email,User.typeOfUser type) {
        User user = User.getInstance();
        user.setEmail(email);
        user.setType(type);
        user.setLogged(true);
        user.setUsername(name);
    }
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
            User user = User.getInstance();
            writeNewUser(userId,name,email,type);
        } catch (JSONException e) {
            e.printStackTrace();
            throw e;
        }

    }
}
