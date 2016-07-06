package com.example.spencer.one;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.spencer.one.recyclerViewItems.FriendViewHolder;
import com.example.spencer.one.model.Users;

public class FriendPageActivity extends AppCompatActivity {

    private TextView tvFriendEmail;
    private String friendEmail;
    private TextView tvFriendUsername;
    private String friendID;
    private TextView tvFriendSnapchat;
    private TextView tvFriendPhoneNumber;
    private Button btnPhoneNumber;
    private Button btnEmail;
    private Button fbBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_page);

        tvFriendEmail = (TextView) findViewById(R.id.tvFriendEmail);
        tvFriendUsername = (TextView) findViewById(R.id.tvFriendUsername);
        tvFriendSnapchat = (TextView) findViewById(R.id.tvFriendSnapchat);
        tvFriendPhoneNumber = (TextView) findViewById(R.id.tvFriendPhoneNumber);
        btnPhoneNumber = (Button) findViewById(R.id.callClick);
        btnEmail = (Button) findViewById(R.id.emailClick);
        fbBtn = (Button) findViewById(R.id.goToFb);

        Bundle friendIdBundle = getIntent().getExtras();
        if (friendIdBundle != null) {
            friendID = friendIdBundle.getString(FriendViewHolder.FRIEND_ID);
        }
       fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFb();
            }
        });

        Backendless.Persistence.of(Users.class).findById(friendID, new AsyncCallback<Users>() {
            @Override
            public void handleResponse(final Users response) {
                tvFriendUsername.setText(response.getUserName());
                tvFriendEmail.setText(response.getEmail());
                btnEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent emailintent = new Intent(android.content.Intent.ACTION_SEND);
                        emailintent.setType("plain/text");
                        emailintent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {response.getEmail()});
                        emailintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                        emailintent.putExtra(android.content.Intent.EXTRA_TEXT,"");
                        startActivity(Intent.createChooser(emailintent, "Send mail..."));
                    }
                });
                if (response.getSnapchat() != null) {
                    tvFriendSnapchat.setText(response.getSnapchat());
                } else {
                    tvFriendSnapchat.setText("No snapchat listed");
                }
                if (response.getPhone_Number() != null) {
                    tvFriendPhoneNumber.setText(response.getPhone_Number());
                    btnPhoneNumber.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + response.getPhone_Number()));
                            if (ActivityCompat.checkSelfPermission(FriendPageActivity.this, Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                Toast.makeText(FriendPageActivity.this, "Calling Permission Disabled",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            startActivity(callIntent);
                        }
                    });
                }else{
                    tvFriendPhoneNumber.setText("No phone number listed");
                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(FriendPageActivity.this, "Server Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "server error: " + fault.getMessage());
            }
        });


    }

    public void goToFb(){
        Backendless.Persistence.of(Users.class).findById(friendID, new AsyncCallback<Users>() {
            @Override
            public void handleResponse(Users response) {
                if(response.getFbid()!=null){
                    String url = "https://www.facebook.com/"+ response.getFbid();
                    Uri uriUrl = Uri.parse(url);
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                    startActivity(launchBrowser);
                }else {
                    Toast.makeText(FriendPageActivity.this, "User has no Facebook connected"
                            , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }
}
