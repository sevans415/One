package com.example.spencer.one;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.spencer.one.model.Friends;
import com.example.spencer.one.model.Users;
import com.example.spencer.one.recyclerViewItems.FriendsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String USER_NAME = "userName";
    public static final int ADD_FRIEND_REQUEST_CODE = 1;
    private FriendsAdapter friendsAdapter;
    private ArrayList<Friends> friendIDList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addFriendOnButtonClick();

        initFriendsListView();


        //BackendlessUser currentUser = Backendless.UserService.CurrentUser();
        /*
        String friendInfo = "";

        for (BackendlessUser friend : friendList) {
            friendInfo += "Username :"+friend.getProperty(USER_NAME).toString()+"\n" +
                    "Email: "+friend.getEmail()+"\n\n";
        }

        if (friendInfo.equals(""))
            tvUsersFriends.setText("You have no friends");
        else
            tvUsersFriends.setText(friendInfo);
*/
        /*
        StringBuilder whereClause = new StringBuilder();
        whereClause.append( "Users[friends]" );
        whereClause.append( ".objectId='" ).append( currentUser.getObjectId() ).append( "'" );
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause.toString() );
        List<Users> result = Backendless.Persistence.of( Users.class ).find( dataQuery ).getCurrentPage();
        */

        // tvUsersFriends.setText(result.get(0).getUserName()+", "+result.get(0).getEmail());

        /*
        Object usersFriends = currentUser.getProperties().get("Friends");
        String friendsName = usersFriends.getClass().getName();
        tvUsersFriends.setText(friendsName);

        Backendess.Persistence.of(Users.class).find(new AsyncCallback<BakendlessCollection<Users>>(){
            @Override
            public void handleResponse( BakendlessCollection<Users> foundUsers )
            {
                Log.d("TAG", "Proper response");
                String user1 = foundUsers.getData().get(0).getEmail();
                tvUsersFriends.setText(user1);
                // all Contact instances have been found
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                Log.d("TAG", "error: "+fault.getMessage());
                // an error has occurred, the error code can be retrieved with fault.getCode()
          }
        });
       // */

    }

    private void addFriendOnButtonClick() {
        Button addFriendButton = (Button) findViewById(R.id.btnAddFriend);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, AddFriendActivity.class), ADD_FRIEND_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_FRIEND_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                initFriendsListView();
            }
        }
    }

    private void initFriendsListView() {
        BackendlessUser user = Backendless.UserService.CurrentUser();
        BackendlessDataQuery friendListQuery = new BackendlessDataQuery();
        friendListQuery.setWhereClause("currentUserId = '" + user.getObjectId() + "'");
        Backendless.Persistence.of(Friends.class).find(friendListQuery, new AsyncCallback<BackendlessCollection<Friends>>() {
            @Override
            public void handleResponse(BackendlessCollection<Friends> response) {
                friendIDList = new ArrayList<Friends>(response.getTotalObjects());
                friendIDList.addAll(response.getCurrentPage());

                if (friendIDList.isEmpty()) { // NEED TO UPDATE TO IF FRIENDIDLIST IS NULL
                    TextView tvNoFrands = (TextView) findViewById(R.id.tvNoFrands);
                    tvNoFrands.setVisibility(View.VISIBLE);
                } else {
                    RecyclerView friendsRecyclerView = (RecyclerView) findViewById(R.id.friends);
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    friendsRecyclerView.setLayoutManager(layoutManager);

                    friendsAdapter = new FriendsAdapter(friendIDList);
                    friendsRecyclerView.setAdapter(friendsAdapter);
                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("TAG", "getting friend Id error: " + fault.getMessage());
            }
        });
    }
}
