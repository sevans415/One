package com.example.spencer.one;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import butterknife.InjectView;

public class ProfileActivity extends AppCompatActivity {

    public static final String ERROR = "Error";
    public static final String SNAPCHAT_ADDED = "Snapchat Added!";
    public static final String NUMBER_ADDED = "Number Added!";
    public static final String EMAIL_UPDATED = "Email Updated";
    private BackendlessUser currentUser;
    private EditText etPhoneNumber;
    private Button phoneNumberBtn;
    private EditText etSnapchat;
    private Button snapchatBtn;
    private EditText etEmail;
    private Button emailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        phoneNumberBtn = (Button) findViewById(R.id.phoneNumberBtn);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        snapchatBtn = (Button) findViewById(R.id.snapchatBtn);
        etSnapchat = (EditText) findViewById(R.id.etSnapchat);
        emailBtn = (Button) findViewById(R.id.emailBtn);
        etEmail = (EditText) findViewById(R.id.etEmail);

        currentUser = Backendless.UserService.CurrentUser();

        phoneNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumber();
            }
        });
        snapchatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSnapchat();
            }
        });
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmail();
            }
        });

    }

    private void addNumber(){
        String phoneNumber = etPhoneNumber.getText().toString();
        currentUser.setProperty("Phone_Number", phoneNumber);
        Backendless.UserService.update(currentUser, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Toast.makeText(ProfileActivity.this, NUMBER_ADDED, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(ProfileActivity.this, ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addSnapchat(){
        String snapchat = etSnapchat.getText().toString();
        currentUser.setProperty("Snapchat", snapchat);
        Backendless.UserService.update(currentUser, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Toast.makeText(ProfileActivity.this, SNAPCHAT_ADDED, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(ProfileActivity.this, ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addEmail(){
        String email = etEmail.getText().toString();
        currentUser.setProperty("email", email);
        Backendless.UserService.update(currentUser, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Toast.makeText(ProfileActivity.this, EMAIL_UPDATED, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(ProfileActivity.this, ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
