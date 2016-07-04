package com.example.spencer.one;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.spencer.one.model.Users;

import java.util.ArrayList;
import java.util.Arrays;

public class AddFriendActivity extends AppCompatActivity {

    private Users friendObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        Button btnSaveFriend = (Button) findViewById(R.id.btnSaveFriend);
        btnSaveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFriend();
            }
        });



    }

    private void saveFriend() {
        Backendless.Persistence.of(Users.class).findById("4AABBA2D-A844-E6D9-FFA4-769ED828F600", new AsyncCallback<Users>() {
            @Override
            public void handleResponse(Users response) {
                friendObject = response;
                Log.d("TAG", "response username: "+response.getUserName());
                Log.d("TAG", "Friend object email: "+friendObject.getEmail());
                Log.d("TAG", "FRIEND TO ADD: "+friendObject.toString());

                BackendlessUser currentUser = Backendless.UserService.CurrentUser();
                BackendlessUser[] friends = (BackendlessUser[]) currentUser.getProperty("Friends");
                //Log.d("TAG", "original friend list: "+friends.toString());

                for (BackendlessUser friend : friends) {
                    Log.d("TAG", "original friend: "+friend.toString());
                }

                BackendlessUser[] newFriendList = new BackendlessUser[friends.length+1];
                System.arraycopy(friends,0,newFriendList,1,friends.length);
                newFriendList[0] = friendObject;

                for (BackendlessUser friend : newFriendList) {
                    Log.d("TAG", "new friend: "+friend.toString());
                }

                //Log.d("TAG", "new friend list: "+newFriendList.toString());
                //currentUser.setProperty("Friends", newFriendList);
                Backendless.UserService.update(currentUser).setProperty("Friends", newFriendList);
                //Backendless.UserService.update(currentUser, new AsyncCallback<BackendlessUser>() {
                   /* @Override
                    public void handleResponse(BackendlessUser response) {
                        Log.d("TAG","async came back: "+response.toString());
                        finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(AddFriendActivity.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "updating users friends error: "+fault.getMessage());
                    }
                }); */
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(AddFriendActivity.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "getting friend list error: "+fault.getMessage());
            }
        });
        /*
        //
        Log.d("TAG", "FRIEND TO ADD: "+friendObject.getEmail());
        BackendlessUser currentUser = Backendless.UserService.CurrentUser();
        BackendlessUser[] friends = (BackendlessUser[]) currentUser.getProperty("Friends");
        //Log.d("TAG", "original friend list: "+friends.toString());

        for (BackendlessUser friend : friends) {
            Log.d("TAG", "original friend: "+friend.toString());
        }

        BackendlessUser[] newFriendList = new BackendlessUser[friends.length+1];
        System.arraycopy(friends,0,newFriendList,1,friends.length);
        newFriendList[0] = friendObject;

        for (BackendlessUser friend : newFriendList) {
            //Log.d("TAG", "new friend: "+friend.toString());
        }

        //Log.d("TAG", "new friend list: "+newFriendList.toString());
        currentUser.setProperty("Friends", friends);
        Backendless.UserService.update(currentUser, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Log.d("TAG","async came back: "+response.toString());
                finish();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(AddFriendActivity.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "updating users friends error: "+fault.getMessage());
            }
        });
        */

    }

}
