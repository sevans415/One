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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_page);

        tvFriendEmail = (TextView) findViewById(R.id.tvFriendEmail);
        tvFriendUsername = (TextView) findViewById(R.id.tvFriendUsername);

        Bundle friendEmailBundle = getIntent().getExtras();
        if (friendEmailBundle != null) {
            friendID = friendEmailBundle.getString(FriendViewHolder.FRIEND_ID);
        }

        Backendless.Persistence.of(Users.class).findById(friendID, new AsyncCallback<Users>() {
            @Override
            public void handleResponse(Users response) {
                tvFriendUsername.setText(response.getUserName());
                tvFriendEmail.setText(response.getEmail());

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(FriendPageActivity.this, "Server Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "server error: " + fault.getMessage());
            }
        });


    }
}
