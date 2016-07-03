package com.example.spencer.one;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.io.BackendlessUserFactory;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.spencer.one.items.FriendsAdapter;
import com.example.spencer.one.model.Friends;
import com.example.spencer.one.model.Users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String USER_NAME = "userName";
    private FriendsAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final TextView tvUsersFriends = (TextView) findViewById(R.id.tvData);

        //BackendlessUser currentUser = Backendless.UserService.CurrentUser();

        BackendlessUser user = Backendless.UserService.CurrentUser();
        BackendlessUser[] friends = (BackendlessUser[]) user.getProperty("Friends");

        ArrayList<BackendlessUser> friendList = new ArrayList<BackendlessUser>();

        friendList.addAll(Arrays.asList(friends));

        RecyclerView friendsRecyclerView = (RecyclerView) findViewById(R.id.friends);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        friendsRecyclerView.setLayoutManager(layoutManager);

        friendsAdapter = new FriendsAdapter(friendList);
        friendsRecyclerView.setAdapter(friendsAdapter);



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
}
