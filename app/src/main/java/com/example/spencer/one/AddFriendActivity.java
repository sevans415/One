package com.example.spencer.one;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.spencer.one.model.Friends;
import com.example.spencer.one.model.Users;

import java.util.ArrayList;
import java.util.Arrays;

public class AddFriendActivity extends AppCompatActivity {

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



        final Friends friendToAdd = new Friends();
        friendToAdd.setCurrentUserId(Backendless.UserService.CurrentUser().getUserId());
        friendToAdd.setFriendId("4AABBA2D-A844-E6D9-FFA4-769ED828F600");
        friendToAdd.setActualName("Feather Face");
        friendToAdd.setUserName("Foggy");
        Backendless.Persistence.of(Friends.class).save(friendToAdd, new AsyncCallback<Friends>() {
            @Override
            public void handleResponse(Friends response) {
                String friendName;
                if (friendToAdd.getActualName() != null)
                    friendName = friendToAdd.getActualName();
                else friendName = friendToAdd.getUserName();
                Toast.makeText(AddFriendActivity.this, friendName+" added as a friend", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }
}
