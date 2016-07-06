package com.example.spencer.one;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_page);

        tvFriendEmail = (TextView) findViewById(R.id.tvFriendEmail);
        tvFriendUsername = (TextView) findViewById(R.id.tvFriendUsername);
        tvFriendSnapchat = (TextView) findViewById(R.id.tvFriendSnapchat);
        tvFriendPhoneNumber = (TextView) findViewById(R.id.tvFriendPhoneNumber);

        Bundle friendIdBundle = getIntent().getExtras();
        if (friendIdBundle != null) {
            friendID = friendIdBundle.getString(FriendViewHolder.FRIEND_ID);
        }

        Backendless.Persistence.of(Users.class).findById(friendID, new AsyncCallback<Users>() {
            @Override
            public void handleResponse(final Users response) {
                tvFriendUsername.setText(response.getUserName());
                tvFriendEmail.setText(response.getEmail());
                tvFriendEmail.setOnClickListener(new View.OnClickListener() {
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
                    tvFriendPhoneNumber.setOnClickListener(new View.OnClickListener() {
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
}
