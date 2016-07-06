package com.example.spencer.one;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.spencer.one.recyclerViewItems.FriendViewHolder;
import com.example.spencer.one.model.Users;

public class FriendPageActivity extends AppCompatActivity {

    public static final String FRIEND_ID = "friendID";
    public static final String FRIEND_USERNAME = "friend username";
    private String friendEmail = "";
    private String friendName = "";
    private String friendPhoneNumber = "";
    private String friendID;
    private TextView tvFriendEmail;
    private TextView tvFriendUsername;
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

        Button btnAddContact = (Button) findViewById(R.id.btnAddContact);
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });

        Button btnMessages = (Button) findViewById(R.id.btnMessages);
        btnMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMessagesActivity();
            }
        });



        Bundle friendIdBundle = getIntent().getExtras();
        if (friendIdBundle != null) {
            friendID = friendIdBundle.getString(FriendViewHolder.FRIEND_ID);
        }
        getFriendInfo();
    }

    private void toMessagesActivity() {
        Intent messagesIntent = new Intent(FriendPageActivity.this, MessagesActivity.class);
        messagesIntent.putExtra(FRIEND_ID, friendID);
        messagesIntent.putExtra(FRIEND_USERNAME,friendName);
        startActivity(messagesIntent);
    }

    private void addContact() {
        Intent addContactIntent =  new Intent(ContactsContract.Intents.Insert.ACTION);
        addContactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        addContactIntent.putExtra(ContactsContract.Intents.Insert.EMAIL, friendEmail);
        addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, friendPhoneNumber);
        addContactIntent.putExtra(ContactsContract.Intents.Insert.NAME, friendName);
        startActivity(addContactIntent);
        Toast.makeText(FriendPageActivity.this, friendName+" added to your contacts!", Toast.LENGTH_SHORT).show();
    }


    private void getFriendInfo() {
        Backendless.Persistence.of(Users.class).findById(friendID, new AsyncCallback<Users>() {
            @Override
            public void handleResponse(Users response) {
                friendEmail = response.getEmail();
                friendName = response.getName();
                friendPhoneNumber = response.getPhone_Number();

                tvFriendUsername.setText(response.getUserName());
                tvFriendEmail.setText("Email: " + friendEmail);
                if(response.getSnapchat()!=null){
                    tvFriendSnapchat.setText("Snapchat: " + response.getSnapchat());
                }
                if(response.getPhone_Number()!=null){
                    tvFriendPhoneNumber.setText("Phone Number: " +friendPhoneNumber);
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
