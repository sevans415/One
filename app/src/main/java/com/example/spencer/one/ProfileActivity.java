package com.example.spencer.one;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.spencer.one.model.Users;

import butterknife.InjectView;

public class ProfileActivity extends AppCompatActivity {

    public static final String ERROR = "Error";
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

        setHints();

        phoneNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = etPhoneNumber.getText().toString();
                addProperty("phoneNumber", phoneNumber);
            }
        });
        snapchatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String snapchat = etSnapchat.getText().toString();
                addProperty("Snapchat", snapchat);
            }
        });
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                addProperty("email", email);
            }
        });

    }

    private void addProperty(final String property, String value){
        currentUser.setProperty(property, value);
        Backendless.UserService.update(currentUser, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Toast.makeText(ProfileActivity.this, property+" updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(ProfileActivity.this, ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

/*
    private void addNumber(){
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
    */

    public void setHints(){
        Backendless.Persistence.of(Users.class).findById(currentUser.getUserId(), new AsyncCallback<Users>() {
            @Override
            public void handleResponse(Users response) {
                Users cu = response;
                etEmail.setHint(cu.getEmail());
                if(cu.getSnapchat()!=null){
                    etSnapchat.setHint(cu.getSnapchat());
                }
                else{
                    etSnapchat.setHint("Enter Snapchat");
                }
                if(cu.getPhone_Number()!=null){
                    etPhoneNumber.setHint(cu.getPhone_Number());
                }else{
                    etPhoneNumber.setHint("Enter Phone Number");
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutBtn:
                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {
                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(ProfileActivity.this, "Error Logging out", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
