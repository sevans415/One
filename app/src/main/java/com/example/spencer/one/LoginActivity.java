package com.example.spencer.one;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserTokenStorageFactory;
import com.dd.CircularProgressButton;
import com.example.spencer.one.model.Users;
import com.facebook.CallbackManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private boolean isLoggedIn = false;

    // TESTING COMMIT - WAHOOO!!
    //Tested committing to the testingBranch - YIPPEE!!


    @InjectView(R.id.input_email) EditText emailText;
    @InjectView(R.id.input_password) EditText passwordText;
    @InjectView(R.id.btn_login) CircularProgressButton loginButton;
    @InjectView(R.id.link_signup) TextView signupLink;
    private Button fbLoginBtn;
    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        callbackManager = CallbackManager.Factory.create();
        fbLoginBtn= (Button) findViewById(R.id.fbBtn);
        String userToken = UserTokenStorageFactory.instance().getStorage().get();
        if( userToken != null && !userToken.equals( "" ) ) {
            Log.d("TAG", "userToken: "+userToken);

            String currentUserObjectId = Backendless.UserService.loggedInUser();
            Backendless.UserService.findById(currentUserObjectId, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser response) {
                    Backendless.UserService.setCurrentUser(response);
                    onLoginSuccess();
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                }
            });
        }

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        fbLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbLogin();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent signupIntent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(signupIntent, REQUEST_SIGNUP);
            }
        });
    }

    public void fbLogin(){
        Map<String, String> facebookFieldMappings = new HashMap<String, String>();
        facebookFieldMappings.put( "email", "userName" );
        facebookFieldMappings.put("name", "name");
        facebookFieldMappings.put("id", "fbid");

        List<String> permissions = new ArrayList<String>();
        permissions.add( "email" );
        //permissions.add("picture");
        Backendless.UserService.loginWithFacebookSdk( LoginActivity.this,facebookFieldMappings, permissions,
                callbackManager,
                new AsyncCallback<BackendlessUser>()
                {
                    @Override
                    public void handleResponse(final BackendlessUser loggedInUser )
                    {
                        //loggedInUser.setProperty("name", loggedInUser.getEmail());
                        Log.d("TAG",loggedInUser.getProperty("name").toString());
                        onLoginSuccess();
                    }

                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        // failed to log in
                        Log.d("TAG", "Error:" + fault.getMessage());
                    }
                }, true );
    }



    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        loginButton.setIndeterminateProgressMode(true);
        loginButton.setProgress(50);

        //loginButton.setEnabled(false);

        /*final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();*/

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        //onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
                */
        // moved this ^ from below the backendless login call

        // TODO: Implement your own authentication logic here.
        Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                onLoginSuccess();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                onLoginFailed();
            }
        }, true);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                Log.d("TAG", "Result received");
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish(); // finishes the process, eventually get rid of ^ line, leave
            }
        }
        callbackManager.onActivityResult( requestCode, resultCode, data );
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setProgress(100);
        loginButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                loginButton.setProgress(0);
            }
        }, 1000);
        loginButton.setEnabled(true);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        loginButton.setProgress(-1);
        loginButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                loginButton.setProgress(0);
            }
        }, 2000);

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

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