package it.communikein.udacity_municipality.data.login;

import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AndroidException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;


import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import it.communikein.udacity_municipality.BuildConfig;
import it.communikein.udacity_municipality.R;
import it.communikein.udacity_municipality.data.model.User;

public class LoginFirebase extends AppCompatActivity {

    private static final String TAG = LoginFirebase.class.getSimpleName();
    private static final String USER_PROFILE_URI = "USER_PROFILE_URI";
    /* *************************************
     *              GOOGLE                 *
     ***************************************/
    /* Request code used to invoke sign in user interactions for Google+ */
    public static final int RC_GOOGLE_SIGNIN = 1;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents us from starting further intents. */
    private boolean mGoogleIntentInProgress;

    /* Track whether the sign-in button has been clicked so that we know to resolve all issues preventing sign-in
     * without waiting. */
    private boolean mGoogleLoginClicked;

    /* Store the connection result from onConnectionFailed callbacks so that we can resolve them when the user clicks
     * sign-in. */
    private ConnectionResult mGoogleConnectionResult;

    // firebase authnetication
    private FirebaseAuth mAuth;

    /* The login button for Google */
    private SignInButton mGoogleLoginButton;

    /*The class used for Client*/
    GoogleSignInClient mGoogleSignInClient;

    /*The image view for dispalying personal pic */
    private TextView mStatusTextView;
    private TextView mDetailTextView;

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mSignUpButton;
    private User userLogged;
    String idToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_firebase);
        userLogged = User.getInstance();
        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(buttonListener);
       findViewById(R.id.sign_out_button).setOnClickListener(buttonListener);
       findViewById(R.id.disconnect_button).setOnClickListener(buttonListener);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();



    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.sign_in_button) {
                signIn();
            } else if (i == R.id.sign_out_button) {
                signOut();
            } else if (i == R.id.disconnect_button) {
                revokeAccess();
            }
        }
    };


    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI();
                    }
                });
        userLogged.setLogged(false);
        updateUI();
    }



    private void updateUI() {

        if (userLogged.isLogged()) {
            mStatusTextView.setText(getString(R.string.google_status_fmt, userLogged.getEmail()));
            if(userLogged.getType() == User.typeOfUser.Citizien)
                mDetailTextView.setText("Role : Cittadino");

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGNIN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGNIN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(LoginFirebase.this, "Google sign in failed ... retry",
                        Toast.LENGTH_SHORT).show();
                finish();
                // ...
            }
        }
    }




    private void sendRequestToServer(FirebaseUser mUser){
         final String idToken = null;
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                           String idToken = task.getResult().getToken();
                            Log.d(TAG, "sendRequestToServer: "+idToken);
                            String urlOfServer = retriveUrlOfServer();
                            NetworkUtilsVolley.sendToken(getBaseContext(),urlOfServer,idToken,new VolleyCallback(){
                                @Override
                                public void onSuccess(String result){
                                    if(result!=null) {
                                        try {
                                            JsonUtils.setUserFromJson(result);
                                            updateUI();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.e(TAG, "sendRequestToServer: error on parsing Json");

                                        }
                                        Log.d(TAG, "sendRequestToServer: User" + result);
                                    }else{
                                        Log.e(TAG, "sendRequestToServer: error user not authorized");
                                        Toast.makeText(LoginFirebase.this, "User not authorized",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            // ...
                        } else {
                            Toast.makeText(LoginFirebase.this, "failed to send request to server.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


    public String retriveUrlOfServer(){
        long cacheExpiration = 3600;

        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
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

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            sendRequestToServer(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed - Maybe you are not connected.", Snackbar.LENGTH_SHORT).show();
                            updateUI();
                        }
                    }
                });
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }



    private void signInAnonymously() {

        // [START signin_anonymously]
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // that to do here????? For now just a Toast is enough
                            Toast.makeText(LoginFirebase.this, "Authentication Anonimous is okay.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(LoginFirebase.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }

                    }
                });
        // [END signin_anonymously]
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI();
                    }
                });
        userLogged.setLogged(false);
        updateUI();
    }

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            if (currentUser.isAnonymous()) {
                // Do something
                Toast.makeText(LoginFirebase.this, "Authentication Anonimous is okay.",
                        Toast.LENGTH_SHORT).show();
            }else{
                updateUI();
            }
            //
        }else{
            signInAnonymously();
        }
            updateUI();
    }
}
