package com.example.spencer.one;


import android.content.Intent;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    public boolean isRegistered = false;

    @InjectView(R.id.input_username)
    EditText usernameText;
    @InjectView(R.id.input_email)
    EditText emailText;
    @InjectView(R.id.input_password)
    EditText passwordText;
    @InjectView(R.id.btn_signup)
    Button signupButton;
    @InjectView(R.id.link_login)
    TextView loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    // yo

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String userName = usernameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        BackendlessUser newUser = new BackendlessUser();
        newUser.setEmail(email);
        newUser.setProperty("userName",userName);
        newUser.setPassword(password);
        newUser.setProperty("name", userName);


        Backendless.UserService.register(newUser, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                isRegistered = true;
                Backendless.UserService.setCurrentUser(response);
                //onSignupSuccess();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("TAG",fault.getMessage());
                Toast.makeText(SignupActivity.this, "Registration Failed: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        if (isRegistered)
                            onSignupSuccess();
                        else onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);



        //progressDialog.dismiss();
        Log.d("TAG", "DIsmiseed progress dialog");

    }


    public void onSignupSuccess() {
        Toast.makeText(SignupActivity.this,"You are registered!", Toast.LENGTH_SHORT).show();
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = usernameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            usernameText.setError("at least 3 characters");
            valid = false;
        } else {
            usernameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}
