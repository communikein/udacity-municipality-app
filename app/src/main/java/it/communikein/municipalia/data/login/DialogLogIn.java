package it.communikein.municipalia.data.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import it.communikein.municipalia.R;


/**
 * Created by Francesco Reale on 14/01/2018.
 * francescoa.reale@gmail.com
 */


// class representing the Dialog
public class DialogLogIn extends DialogFragment {
    private LoginFirebase login;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login = LoginFirebase.getInstance();
    }
    /**
     * Create the Dialog for the Login
     * @param savedInstanceState  bundle to get results from activity calling
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_signin, null))
                // Add action buttons
                .setPositiveButton("signIn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        login.signIn();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogLogIn.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
