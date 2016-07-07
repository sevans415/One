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
        facebookFieldMappings.put( "name", "userName" );
        facebookFieldMappings.put("id", "fbid");

        List<String> permissions = new ArrayList<String>();
        permissions.add( getString(R.string.email_string) );
        //permissions.add("picture");
        Backendless.UserService.loginWithFacebookSdk( LoginActivity.this,facebookFieldMappings, permissions,
                callbackManager,
                new AsyncCallback<BackendlessUser>()
                {
                    @Override
                    public void handleResponse(final BackendlessUser loggedInUser )
                    {
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


        if (!validate()) {
            onLoginFailed();
            return;
        }
        loginButton.setIndeterminateProgressMode(true);
        loginButton.setProgress(50);

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

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

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
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
        Toast.makeText(getBaseContext(), R.string.failed_login, Toast.LENGTH_LONG).show();
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
            emailText.setError(getString(R.string.bad_email));
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError(getString(R.string.validate_pword));
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}