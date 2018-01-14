package it.communikein.municipalia.data.login;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import org.json.JSONException;
import it.communikein.municipalia.R;
import it.communikein.municipalia.data.model.User;

/**
 * Created by Francesco Reale on 10/01/2018.
 * francescoa.reale@gmail.com
 *
 */

/*! \class LoginFirebase
    \brief This class performs the  Login operation and retrives User data thanks to Firebase.
*/

public class LoginFirebase {

    private static final String TAG = LoginFirebase.class.getSimpleName();

    /* Request code used to invoke sign in user interactions for Google+ */
    public static final int RC_GOOGLE_SIGNIN = 1;
    // firebase authnetication
    private FirebaseAuth mAuth;

    /*The class used for Client*/
    GoogleSignInClient mGoogleSignInClient;

    // the activity where this class refers
    Activity activity;

    private static class SingletonHolder {
        private static final LoginFirebase LoginInstance = new LoginFirebase();
    }

    /**
     * this is the method for the implementation of the singleton.
     * @return
     */
    public static LoginFirebase getInstance() {
        return LoginFirebase.SingletonHolder.LoginInstance;
    }
    /**
     * This set the Activity where to wait for result of the Login
     * @param activity  the activity where you have to wait the result of the activity
     * @return
     */
    public void setActivityLogin(Activity activity) {
        this.activity = activity;
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        mAuth = FirebaseAuth.getInstance();
    }

    private LoginFirebase() {
        this.activity = null;


    }
    /**
     * This method revoke the access to the user and set the UserLogged attribute to flase
     * @return
     */

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();
        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(activity,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        User.getInstance().setLogged(false);
                    }
                });
        User.getInstance().setLogged(false);
    }
    /**
     * The signin function to call where you want perform login
     *
     * @return
     */

    public void signIn() {
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
    private void askUserToServer(FirebaseUser mUser, final ResultsCallback callback) {

        NetworkUtils.getServerTokenFromFirebase(mUser, new ResultsCallback() {
            @Override
            public void onSuccess(String token) {
                if (token != null) {
                    Log.d(TAG, "sendRequestToServer: " + token);
                    String urlOfServer = NetworkUtils.retriveUrlOfServer(activity);
                    Log.d(TAG, "url of Server" + urlOfServer);
                    NetworkUtils.getUserFromServerHttp(activity.getBaseContext(), urlOfServer, token, new ResultsCallback() {
                        @Override
                        public void onSuccess(String result) {
                            if (result != null) {
                                try {
                                    JsonUtils.setUserFromJson(result);
                                    // TODO: put this in the JsonUtil .
                                    // TODO you have to get the image url from the server not directly from Firebase
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
                                Toast.makeText(activity, "User not authorized",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(activity, "ID TOKEN NOT AVAILABLE",
                            Toast.LENGTH_SHORT).show();
                }
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
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct, final ResultsCallback callback) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            askUserToServer(user, new ResultsCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    callback.onSuccess("USER CORRECTLY SET");
                                }
                            });

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(activity.findViewById(R.id.main_layout), "Authentication Failed - Maybe you are not connected.", Snackbar.LENGTH_SHORT).show();

                        }
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
     * signInAnonymously performs signInAnonumously
     * @return
     */

    private void signInAnonymously() {

        // [START signin_anonymously]
        mAuth.signInAnonymously()
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // that to do here????? For now just a Toast is enough
                            Toast.makeText(activity, "Authentication Anonimous is okay.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
        // [END signin_anonymously]
    }

    /**
     * Sstart the Dialog for the Login
     * @param fgm the fragment Manager of the Activity calling this method
     * @return
     */
    public static void startLoginDialog(FragmentManager fgm) {

        Log.i("Listner", "Google sign in pushed");
        DialogLogIn dialog = new DialogLogIn();
        dialog.show(fgm, "LoginDialog");
    }
    /**
     * signout the current user
     * @return
     */
    public void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(activity,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //  updateUI();

                    }
                });
        User.getInstance().setLogged(false);
    }

    // retry true if the user is already log with google otherways return false and
    // @param true if the User is already logged with google false if not
    public boolean VerifyAlreadyLog() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.isAnonymous()) {
                // Do something
                Toast.makeText(activity, "User Authenticated as Anonimous",
                        Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }

        }
        signInAnonymously();
        return false;

    }
}


