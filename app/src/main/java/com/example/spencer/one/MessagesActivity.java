package com.example.spencer.one;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.spencer.one.model.Messages;

public class MessagesActivity extends AppCompatActivity {

    private EditText etMessage;
    private TextView tvConvo;
    private Messages messageObject;
    private String friendID;
    private String friendUsername;
    private String concatIdOne;
    private String concatIdTwo;
    private String currentUserName;
    private String convo;
    private String objectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        etMessage = (EditText) findViewById(R.id.etMessage);
        tvConvo = (TextView) findViewById(R.id.tvConvo);

        friendID = getIntent().getExtras().get(FriendPageActivity.FRIEND_ID).toString();
        friendUsername = getIntent().getExtras().get(FriendPageActivity.FRIEND_USERNAME).toString();
        currentUserName = Backendless.UserService.CurrentUser().getProperty(getString(R.string.username)).toString();
        concatIdOne = friendID + Backendless.UserService.CurrentUser().getObjectId();
        concatIdTwo = Backendless.UserService.CurrentUser().getObjectId() + friendID;

        TextView tvHeader = (TextView) findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.conversation_with)+friendUsername);

        Button btnSend = (Button) findViewById(R.id.btnSendMessage);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        refresh();
    }


    private void refresh() {
        BackendlessDataQuery convoQuery = new BackendlessDataQuery();
        convoQuery.setWhereClause(getString(R.string.concat_ids) + concatIdOne + getString(R.string.concat_ids2) + concatIdTwo + "'");
        Backendless.Persistence.of(Messages.class).find(convoQuery, new AsyncCallback<BackendlessCollection<Messages>>() {
            @Override
            public void handleResponse(BackendlessCollection<Messages> response) {
                if (response.getTotalObjects() == 0) {
                    makeNewConvo();
                } else {
                    messageObject = response.getData().get(0);
                    convo = response.getData().get(0).getConvo();
                    tvConvo.setText(convo);
                    Toast.makeText(MessagesActivity.this, R.string.messages_loaded, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    private void makeNewConvo() {
        Messages newConvo = new Messages(concatIdOne, friendUsername, currentUserName);
        Backendless.Persistence.of(Messages.class).save(newConvo, new AsyncCallback<Messages>() {
            @Override
            public void handleResponse(Messages response) {
                messageObject = response;
                Toast.makeText(MessagesActivity.this, R.string.conversation_created, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(MessagesActivity.this, getString(R.string.error) + fault.getMessage(), Toast.LENGTH_SHORT).show();
               ;
            }
        });
    }


    private void send() {
        String newMessage = etMessage.getText().toString();
        messageObject.setConvo(convo+"<"+currentUserName+"> "+newMessage+"\n");
        Backendless.Persistence.save(messageObject, new AsyncCallback<Messages>() {
            @Override
            public void handleResponse(Messages response) {
                Toast.makeText(MessagesActivity.this, R.string.message_sent, Toast.LENGTH_SHORT).show();
                refresh();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(MessagesActivity.this, R.string.error+fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Toast.makeText(MessagesActivity.this, R.string.sent, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_messages,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnRefresh:
                refresh();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
