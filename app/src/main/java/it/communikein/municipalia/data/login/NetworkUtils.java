package it.communikein.municipalia.data.login;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import it.communikein.municipalia.BuildConfig;


/**
 * Created by Francesco Reale on 10/01/2018.
 * francescoa.reale@gmail.com
 *
 */
/*! \class NetworkUtils
    \brief This class provides utilities to interact with the server
*/


public class NetworkUtils {


    public static final String TAG = "NetworkUtils";
    private static final String USER_PROFILE_URI = "USER_PROFILE_URI";

    /**
     * This method retrive the url of server from FirebaseRemoteConfig
     * @param activity  the activity wehre to bind the liestner
     * @return String the url of server
     */

    public static String retriveUrlOfServer(Activity activity){
        long cacheExpiration = 3600;

        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        // we need to use fetch in order to use cache and retrive URI
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "retriveUrlOfServer: Fetch succed");
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Log.e(TAG, "retriveUrlOfServer: Fetch Failed");
                        }
                    }
                });

        return mFirebaseRemoteConfig.getString(USER_PROFILE_URI);
    }

    /**
     * This method get the IdTOken from Firebase. The IdToken will be useed to obtain Authorization from Server
     * @param mUser  the firebase User to get the IdToken,
     * @param callback  we use a callback to retrive data base on the listner
     * @return
     */
    public static void getServerTokenFromFirebase(FirebaseUser mUser,final ResultsCallback callback){

        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            callback.onSuccess(idToken);
                        }else{
                            callback.onSuccess(null);
                        }
                    }});

    }

    /**
     * This method send a jsonObject to the server.
     * the JsonObject contains information about User.
     * @param jsonBody  the Json Representing the User
     * @param url the url of the server
     * @param token the token to deliver to the Server. With no token you don't have the permission to update
     * @param callback deliver success or not object with callback
     * @return
     */

    public static void updateUserOnServer ( JSONObject jsonBody, String url,String token,
                                           final ResultsCallback callback){

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url,jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                      //  mTxtDisplay.setText("Response: " + response.toString());
                        Log.d(TAG, response.toString());
                        callback.onSuccess(response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
    }

    /**
     * This method retrive JsonObject with UserInfo from server
     * @param context the context
     * @param url the url of the server
     * @param token the token to deliver to the Server. With no token you don't have the permission to retrive info
     * @param callback deliver success or not object with callback
     * @return
     */

    public static void getUserFromServerHttp(Context context, String url,String token,final ResultsCallback callback) {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    Log.d(TAG, response);
                    callback.onSuccess(response);
                } else {
                    Log.d(TAG, "Data Null");
                    callback.onSuccess(null);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "" + error);
                callback.onSuccess(null);
            }
        }) {
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // put the token in the Authorization header
                params.put("Authorization", "Bearer "+token);
                // params.put("Bearer", );
                return params;
            }

            //Pass Your Parameters here
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("Bearer", idToken);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    }


