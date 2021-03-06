package com.example.spencer.one;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.bumptech.glide.Glide;
import com.dd.CircularProgressButton;
import com.example.spencer.one.recyclerViewItems.FriendViewHolder;
import com.example.spencer.one.model.Users;

public class FriendPageActivity extends AppCompatActivity {

    public static final String FRIEND_ID = "friendID";
    public static final String FRIEND_USERNAME = "friend username";
    private String friendName = "";
    private String friendPhoneNumber = "";
    private TextView tvFriendEmail;
    private String friendEmail;
    private TextView tvFriendUsername;
    private String friendID;
    private TextView tvFriendSnapchat;
    private TextView tvFriendPhoneNumber;
    private Button btnPhoneNumber;
    private Button btnEmail;
    private CircularProgressButton fbBtn;
    private String fbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_page);

        tvFriendEmail = (TextView) findViewById(R.id.tvFriendEmail);
        tvFriendUsername = (TextView) findViewById(R.id.tvFriendUsername);
        tvFriendSnapchat = (TextView) findViewById(R.id.tvFriendSnapchat);
        tvFriendPhoneNumber = (TextView) findViewById(R.id.tvFriendPhoneNumber);
        btnPhoneNumber = (Button) findViewById(R.id.callClick);
        btnEmail = (Button) findViewById(R.id.emailClick);
        fbBtn = (CircularProgressButton) findViewById(R.id.goToFb);

        CircularProgressButton btnAddContact = (CircularProgressButton) findViewById(R.id.btnAddContact);
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });

        CircularProgressButton btnMessages = (CircularProgressButton) findViewById(R.id.btnMessages);
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
       fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFb();
            }
        });

        getFriendInfo();

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailintent = new Intent(android.content.Intent.ACTION_SEND);
                emailintent.setType(getString(R.string.plain_text));
                emailintent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {friendEmail});
                emailintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                emailintent.putExtra(android.content.Intent.EXTRA_TEXT,"");
                startActivity(Intent.createChooser(emailintent, getString(R.string.sending_mail)));
            }
        });
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
        Toast.makeText(FriendPageActivity.this, friendName+getString(R.string.added_contact), Toast.LENGTH_SHORT).show();
    }


    private void getFriendInfo() {
        Backendless.Persistence.of(Users.class).findById(friendID, new AsyncCallback<Users>() {
            @Override
            public void handleResponse(Users response) {
                friendEmail = response.getEmail();
                friendName = response.getName();
                friendPhoneNumber = response.getPhoneNumber();
                fbid = response.getFbid();

                tvFriendUsername.setText(response.getUserName());
                tvFriendEmail.setText(response.getEmail());
                if (response.getSnapchat() != null) {
                    tvFriendSnapchat.setText(response.getSnapchat());
                } else {
                    tvFriendSnapchat.setText(R.string.no_snapchat);
                }
                if (response.getPhoneNumber() != null) {
                    tvFriendPhoneNumber.setText(response.getPhoneNumber());
                    btnPhoneNumber.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + friendPhoneNumber));
                            if (ActivityCompat.checkSelfPermission(FriendPageActivity.this, Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(FriendPageActivity.this, R.string.calling_permission_no,
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            startActivity(callIntent);
                        }
                    });
                }else{
                    tvFriendPhoneNumber.setText(R.string.no_phone);
                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(FriendPageActivity.this, getString(R.string.server_error) + fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToFb(){
        Backendless.Persistence.of(Users.class).findById(friendID, new AsyncCallback<Users>() {
            @Override
            public void handleResponse(Users response) {
                if(response.getFbid()!=null){
                    String url = getString(R.string.facebook_glide)+ response.getFbid();
                    Uri uriUrl = Uri.parse(url);
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                    startActivity(launchBrowser);
                }else {
                    Toast.makeText(FriendPageActivity.this, R.string.no_facebook
                            , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }
}
