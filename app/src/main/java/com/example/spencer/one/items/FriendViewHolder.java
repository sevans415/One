package com.example.spencer.one.items;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.spencer.one.R;

/**
 * Created by spencerevans on 7/3/16.
 */
public class FriendViewHolder extends RecyclerView.ViewHolder {

    public TextView tvFriendUsername;
    public TextView tvFriendEmail;

    public FriendViewHolder(View itemView) {
        super(itemView);
        tvFriendUsername = (TextView) itemView.findViewById(R.id.tvFriendUsername);
        tvFriendEmail = (TextView) itemView.findViewById(R.id.tvFriendEmail);

        CardView cardView = (CardView) itemView.findViewById(R.id.card_view);


    }
}
