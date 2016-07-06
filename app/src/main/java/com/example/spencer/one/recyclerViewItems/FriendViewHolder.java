package com.example.spencer.one.recyclerViewItems;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.BackendlessUser;
import com.bumptech.glide.Glide;
import com.example.spencer.one.AddFriendActivity;
import com.example.spencer.one.FriendPageActivity;
import com.example.spencer.one.MainActivity;
import com.example.spencer.one.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

/**
 * Created by spencerevans on 7/3/16.
 */
public class FriendViewHolder extends RecyclerView.ViewHolder {

    public static final String FRIEND_ID = "friendID";
    public TextView tvFriendUsername;
    public String friendID;
    public String fbid;
    public ImageView ivFbPhoto;


    public FriendViewHolder(final View itemView) {
        super(itemView);
        tvFriendUsername = (TextView) itemView.findViewById(R.id.tvFriendUsername);

        ivFbPhoto = (ImageView) itemView.findViewById(R.id.ivFbPhoto);


        CardView cardView = (CardView) itemView.findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = itemView.getContext();
                Intent getFriendsPageIntent = new Intent(context,FriendPageActivity.class);
                getFriendsPageIntent.putExtra(FRIEND_ID, friendID);
                context.startActivity(getFriendsPageIntent);
            }
        });


    }


    public void setFbid(String fbid) {
        this.fbid = fbid;
    }
}
