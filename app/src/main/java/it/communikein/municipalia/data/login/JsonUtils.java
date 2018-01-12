package it.communikein.udacity_municipality.data.login;

import org.json.JSONException;
import org.json.JSONObject;

import it.communikein.udacity_municipality.data.model.User;

/**
 * Created by F63801B on 10/01/2018.
 */

public class JsonUtils {

    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String UID = "uid";
    private static final String ROLE = "role";


    private static void writeNewUser(String userId, String name, String email,User.typeOfUser type,String Uid) {
        User user = User.getInstance();
        user.setEmail(email);
        user.setType(type);
        user.setLogged(true);
        user.setUsername(name);
        user.setUid(Uid);
    }

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
            writeNewUser(userId,name,email,type,userId);
        } catch (JSONException e) {
            e.printStackTrace();
            throw e;
        }

    }
}
