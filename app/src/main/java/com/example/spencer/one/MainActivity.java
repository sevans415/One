package com.example.spencer.one;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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


                if (friendIDList.isEmpty())
                    initFriendsListView();
                else {
                    final Friends newFriend = new Friends();
                    newFriend.setActualName(data.getExtras().get(AddFriendActivity.FRIEND_NAME).toString());
                    newFriend.setUserName(data.getExtras().get(AddFriendActivity.FRIEND_USER_NAME).toString());
                    newFriend.setFriendId(data.getExtras().get(AddFriendActivity.FRIEND_USER_ID).toString());
                    newFriend.setCurrentUserId(data.getExtras().get(AddFriendActivity.CURRENT_USER_ID).toString());
                    newFriend.setObjectId(data.getExtras().get(AddFriendActivity.OBJECT_ID).toString());
                    newFriend.setFbid(data.getExtras().getString(AddFriendActivity.FBID));

                    boolean notDuplicateFriend = deleteDuplicateFriends(newFriend);
                    if (notDuplicateFriend)
                        friendsAdapter.addItem(newFriend);
                }
            }
        }
    }

    private boolean deleteDuplicateFriends(final Friends newFriend) {
        boolean notDuplicateFriend = true;
        friendIDList = friendsAdapter.getFriendsList();
        for (final Friends friend : friendIDList) {
            Log.d("TAG", "friend: "+friend.getFriendId()+" newFriend: "+newFriend.getFriendId());
            if (friend.getFriendId().equals(newFriend.getFriendId())) {
                notDuplicateFriend = false;

                Backendless.Persistence.of(Friends.class).findById(newFriend.getObjectId(), new AsyncCallback<Friends>() {
                    @Override
                    public void handleResponse(Friends response) {
                        Backendless.Persistence.of(Friends.class).remove(response, new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                Toast.makeText(MainActivity.this, "Removed duplicate friend: "+ newFriend.getUserName(), Toast.LENGTH_SHORT).show();
                                Log.d("TAG","removed "+newFriend.getUserName()+"as friend of "+friend.getUserName());
                            }
                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(MainActivity.this, "Error contacting server", Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "error: "+fault.getMessage());
                            }
                        });
                    }
                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(MainActivity.this, "Error contacting server", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "error: "+fault.getMessage());
                    }
                });
            }
        }
        return notDuplicateFriend;
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

                if (friendIDList.isEmpty()) {
                    TextView tvNoFrands = (TextView) findViewById(R.id.tvNoFrands);
                    tvNoFrands.setVisibility(View.VISIBLE);
                } else {
                    RecyclerView friendsRecyclerView = (RecyclerView) findViewById(R.id.friends);
                    final GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
                    friendsRecyclerView.setLayoutManager(layoutManager);

                    friendsAdapter = new FriendsAdapter(friendIDList, MainActivity.this);
                    friendsRecyclerView.setAdapter(friendsAdapter);
                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(MainActivity.this, "Error retrieving your friends: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "getting friend Id error: " + fault.getMessage());
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionStart:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
