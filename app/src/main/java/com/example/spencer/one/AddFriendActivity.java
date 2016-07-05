package com.example.spencer.one;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.spencer.one.model.Friends;
import com.example.spencer.one.model.Users;

import java.util.ArrayList;
import java.util.Arrays;

public class AddFriendActivity extends AppCompatActivity {

    public static final String FRIEND_NAME = "friendName";
    public static final String FRIEND_USER_NAME = "friendUserName";
    public static final String FRIEND_USER_ID = "friendUserId";
    public static final String CURRENT_USER_ID = "currentUserId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        Spinner querySpinner = (Spinner) findViewById(R.id.querySpinner);

        Button btnSaveFriend = (Button) findViewById(R.id.btnSaveFriend);
        btnSaveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFriend();
            }
        });

    }


    private void saveFriend() {
        EditText etFriendsUserName = (EditText) findViewById(R.id.etFriendsUsername);
        final String friendsUserName = etFriendsUserName.getText().toString();

        String whereClause = "userName = '"+friendsUserName+"'";
        BackendlessDataQuery findFriendQuery = new BackendlessDataQuery();
        findFriendQuery.setWhereClause(whereClause);
        Backendless.Persistence.of(Users.class).find(findFriendQuery, new AsyncCallback<BackendlessCollection<Users>>() {
            @Override
            public void handleResponse(BackendlessCollection<Users> response) {
                if (response.getData().isEmpty()) {
                    Toast.makeText(AddFriendActivity.this, "We could not find the user '"+
                            friendsUserName+"' in our database", Toast.LENGTH_SHORT).show();
                }
                else if (response.getData().get(0).getObjectId().equals(Backendless.UserService.CurrentUser().getUserId())) {
                    Toast.makeText(AddFriendActivity.this, "You can't add yourself as a friend, ya dumby", Toast.LENGTH_SHORT).show();
                } else {
                    final Friends friendToAdd = new Friends();
                    friendToAdd.setCurrentUserId(Backendless.UserService.CurrentUser().getUserId());
                    friendToAdd.setFriendId(response.getData().get(0).getObjectId());
                    friendToAdd.setActualName(response.getData().get(0).getName());
                    friendToAdd.setUserName(response.getData().get(0).getUserName());
                    Backendless.Persistence.of(Friends.class).save(friendToAdd, new AsyncCallback<Friends>() {
                        @Override
                        public void handleResponse(Friends response) {
                            String friendName;
                            if (friendToAdd.getActualName() != null)
                                friendName = friendToAdd.getActualName();
                            else friendName = friendToAdd.getUserName();
                            Intent result = new Intent();
                            result.putExtra(FRIEND_NAME,friendToAdd.getActualName());
                            result.putExtra(FRIEND_USER_NAME, friendToAdd.getUserName());
                            result.putExtra(FRIEND_USER_ID, friendToAdd.getFriendId());
                            result.putExtra(CURRENT_USER_ID, friendToAdd.getCurrentUserId());
                            setResult(Activity.RESULT_OK, result);
                            Toast.makeText(AddFriendActivity.this, friendName + " added as a friend", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(AddFriendActivity.this, "Error saving friend: "+
                                    fault.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "SAVing friend error: " + fault.getMessage());
                            finish();
                        }
                    });
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(AddFriendActivity.this, "Error finding user: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG","FINDing friend error: "+fault.getMessage());
                finish();

            }
        });
    }
}
