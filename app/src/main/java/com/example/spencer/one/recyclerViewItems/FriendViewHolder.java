package com.example.spencer.one.recyclerViewItems;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.backendless.BackendlessUser;
import com.example.spencer.one.FriendPageActivity;
import com.example.spencer.one.R;

/**
 * Created by spencerevans on 7/3/16.
 */
public class FriendViewHolder extends RecyclerView.ViewHolder {

    public static final String FRIEND_ID = "friendID";
    public TextView tvFriendUsername;
    public String friendID;

    public FriendViewHolder(final View itemView) {
        super(itemView);
        tvFriendUsername = (TextView) itemView.findViewById(R.id.tvFriendUsername);

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
}
