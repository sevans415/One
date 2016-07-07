package com.example.spencer.one;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.bumptech.glide.Glide;
import com.dd.CircularProgressButton;
import com.example.spencer.one.model.Friends;
import com.example.spencer.one.model.Users;

public class AddFriendActivity extends AppCompatActivity {

    public static final String FRIEND_NAME = "friendName";
    public static final String FRIEND_USER_NAME = "friendUserName";
    public static final String FRIEND_USER_ID = "friendUserId";
    public static final String CURRENT_USER_ID = "currentUserId";
    public static final String OBJECT_ID = "objectId";
    public static final String FBID = "fbid";

    private EditText etFriendInput;
    private Spinner querySpinner;
    private ImageView ivQrCode;
    private CircularProgressButton btnSaveFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        ivQrCode = (ImageView) findViewById(R.id.ivQrCode);

        String url = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=";
        url += Backendless.UserService.CurrentUser().getObjectId();
        Glide.with(AddFriendActivity.this).load(url).into(ivQrCode);

        etFriendInput = (EditText) findViewById(R.id.etFriendQueryInput);

        querySpinner = (Spinner) findViewById(R.id.querySpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.query_options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        querySpinner.setAdapter(adapter);
        String chosenQuery = querySpinner.getSelectedItem().toString();

        querySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etFriendInput.setHint(getString(R.string.type_friends)+parent.getItemAtPosition(position).toString()+getString(R.string.here));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnSaveFriend = (CircularProgressButton) findViewById(R.id.btnSaveFriend);
        btnSaveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedQuery = querySpinner.getSelectedItem().toString();
                String queryType;
                if (selectedQuery.matches("phone number"))
                    queryType = getString(R.string.phone_number_add);
                else queryType = selectedQuery;
                saveFriend(queryType);
            }
        });

    }

    private void saveFriend(String queryType) {
        btnSaveFriend.setIndeterminateProgressMode(true);
        btnSaveFriend.setProgress(50);
        final String friendsInfo = etFriendInput.getText().toString();

        String whereClause = queryType+" = '"+friendsInfo+"'";
        BackendlessDataQuery findFriendQuery = new BackendlessDataQuery();
        findFriendQuery.setWhereClause(whereClause);
        Backendless.Persistence.of(Users.class).find(findFriendQuery, new AsyncCallback<BackendlessCollection<Users>>() {
            @Override
            public void handleResponse(BackendlessCollection<Users> response) {
                if (response.getData().isEmpty()) {
                    Toast.makeText(AddFriendActivity.this, getString(R.string.find_friend_error)+
                            friendsInfo+getString(R.string.error_part_2), Toast.LENGTH_SHORT).show();
                }
                else if (response.getData().get(0).getObjectId().equals(Backendless.UserService.CurrentUser().getUserId())) {
                    Toast.makeText(AddFriendActivity.this, R.string.cant_add_self, Toast.LENGTH_SHORT).show();
                } else {
                    final Friends friendToAdd = new Friends();
                    friendToAdd.setCurrentUserId(Backendless.UserService.CurrentUser().getUserId());
                    friendToAdd.setFriendId(response.getData().get(0).getObjectId());
                    friendToAdd.setActualName(response.getData().get(0).getName());
                    friendToAdd.setUserName(response.getData().get(0).getUserName());
                    friendToAdd.setFbid(response.getData().get(0).getFbid());

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
                            result.putExtra(OBJECT_ID, response.getObjectId());
                            result.putExtra(FBID,friendToAdd.getFbid());
                            setResult(Activity.RESULT_OK, result);

                            successEndButtonAnimation();

                            Toast.makeText(AddFriendActivity.this, friendName + getString(R.string.friend_confirmation), Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            faultEndButtonAnimation();

                            Toast.makeText(AddFriendActivity.this, getString(R.string.saving_friend_error)+
                                    fault.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                faultEndButtonAnimation();
                Toast.makeText(AddFriendActivity.this, getString(R.string.find_error)+fault.getMessage(), Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }

    private void faultEndButtonAnimation() {
        btnSaveFriend.setProgress(-1);
        btnSaveFriend.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnSaveFriend.setProgress(0);
            }
        }, 500);

        btnSaveFriend.setEnabled(true);
    }

    private void successEndButtonAnimation() {
        btnSaveFriend.setProgress(100);
        btnSaveFriend.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnSaveFriend.setProgress(0);
            }
        }, 300);
        btnSaveFriend.setEnabled(true);
    }
}
