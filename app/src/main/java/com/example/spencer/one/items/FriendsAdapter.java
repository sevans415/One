package com.example.spencer.one.items;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.backendless.BackendlessUser;
import com.example.spencer.one.MainActivity;
import com.example.spencer.one.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spencerevans on 7/3/16.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendViewHolder> {

    List<BackendlessUser> friendsList = new ArrayList<>();

    public FriendsAdapter(List<BackendlessUser> friendsList) {
        this.friendsList = friendsList;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.friend_list_row, parent, false);
        return new FriendViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        BackendlessUser friend = friendsList.get(position);
        holder.tvFriendUsername.setText(friend.getProperty(MainActivity.USER_NAME).toString());
        holder.tvFriendEmail.setText(friend.getEmail());
        holder.itemView.setTag(friend);
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }
}
