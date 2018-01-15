package it.communikein.municipalia.data.login;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import org.json.JSONException;

import javax.inject.Inject;
import javax.inject.Singleton;

import it.communikein.municipalia.R;
import it.communikein.municipalia.data.model.User;

/**
 * Created by Francesco Reale on 10/01/2018.
 * francescoa.reale@gmail.com
 *
 */

/*! \class LoginFirebase
    \brief This class performs the  Login operation and retrieves User data thanks to Firebase.
*/

@Singleton
public class LoginFirebase {

    private static final String TAG = LoginFirebase.class.getSimpleName();

    /* Request code used to invoke sign in user interactions for Google+ */
    public static final int RC_GOOGLE_SIGNIN = 1;
    // firebase authentication
    private FirebaseAuth mAuth;

    /* The class used for Client*/
    private GoogleSignInClient mGoogleSignInClient;

    // the app where this class refers
    private Application app;

    @Inject
    public LoginFirebase(Application application) {
        this.app = application;

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(app.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(app, gso);
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * This method revoke the access to the user and set the UserLogged attribute to flase
     * @return
     */
    private void revokeAccess(Activity activity) {
        // Firebase sign out
        mAuth.signOut();
        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(activity,
                task -> User.getInstance().setLogged(false));
        User.getInstance().setLogged(false);
    }

    /**
     * The signin function to call where you want perform login
     *
     * @return
     */
    public void signIn(Activity activity) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_GOOGLE_SIGNIN);
    }

    /**
     * This ask the data of the User to the server
     *
     * @param mUser the firebase user
     * @param callback the callback where to set the result in case of success
     * @return
     */
    private void askUserToServer(Activity activity, FirebaseUser mUser, final ResultsCallback callback) {

        NetworkUtils.getServerTokenFromFirebase(mUser, token -> {
            if (token != null) {
                Log.d(TAG, "sendRequestToServer: " + token);
                String urlOfServer = NetworkUtils.retrieveUrlOfServer(activity);
                Log.d(TAG, "url of Server" + urlOfServer);
                NetworkUtils.getUserFromServerHttp(app, urlOfServer, token, result -> {
                    if (result != null) {
                        try {
                            JsonUtils.setUserFromJson(result);
                            // TODO: put this in the JsonUtil .
                            // TODO: you have to get the image url from the server not directly from Firebase
                            User.getInstance().setImg(mUser.getPhotoUrl().toString());
                            callback.onSuccess(result);
                            //   updateUI();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "sendRequestToServer: error on parsing Json");

                        }
                        Log.d(TAG, "sendRequestToServer: User" + result);
                    } else {
                        Log.e(TAG, "sendRequestToServer: error user not authorized");
                        Toast.makeText(app, "User not authorized",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(app, "ID TOKEN NOT AVAILABLE",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ...
    }

    /**
     * Call this method where you want perform the FirebaseLogin
     *
     * @param acct an istance of SigniInAccount
     * @param callback the callback where to set the result in case of success
     * @return
     */
    // [START auth_with_google]
    public void firebaseAuthWithGoogle(Activity activity, GoogleSignInAccount acct, final ResultsCallback callback) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        askUserToServer(activity, user, result -> callback.onSuccess("USER CORRECTLY SET"));

                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Snackbar.make(
                                activity.findViewById(R.id.tab_container),
                                "Authentication Failed - Maybe you are not connected.",
                                Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * get the Email from Username
     *
     * @param email the email to parse the UserName
     * @param
     * @return the username as string
     */
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    /**
     * signInAnonymously performs signInAnonymously
     * @return
     */
    private void signInAnonymously(Activity activity) {
        // [START signin_anonymously]
        mAuth.signInAnonymously()
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInAnonymously:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        // that to do here????? For now just a Toast is enough
                        Toast.makeText(app, "Authentication Anonymous is okay.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInAnonymously:failure", task.getException());
                        Toast.makeText(app, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }

                });
        // [END signin_anonymously]
    }

    /**
     * signout the current user
     * @return
     */
    public void signOut(Activity activity) {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(activity, task -> {
                    //  updateUI();

                });
        User.getInstance().setLogged(false);
    }

    // retry true if the user is already log with google otherwise return false and
    // @param true if the User is already logged with google false if not
    public boolean VerifyAlreadyLog(Activity activity) {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.isAnonymous()) {
                // Do something
                Toast.makeText(app, "User Authenticated as Anonymous",
                        Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }

        }
        signInAnonymously(activity);
        return false;

    }
}


