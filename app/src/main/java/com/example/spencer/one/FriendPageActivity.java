package com.example.spencer.one;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
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
            public void handleResponse(Users response) {
                tvFriendUsername.setText(response.getUserName());
                tvFriendEmail.setText("Email: " + response.getEmail());
                if(response.getSnapchat()!=null){
                    tvFriendSnapchat.setText("Snapchat: " + response.getSnapchat());
                }
                if(response.getPhone_Number()!=null){
                    tvFriendPhoneNumber.setText("Phone Number: " + response.getPhone_Number());
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
