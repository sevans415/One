package com.example.spencer.one.recyclerViewItems;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.backendless.BackendlessUser;
import com.example.spencer.one.MainActivity;
import com.example.spencer.one.R;
import com.example.spencer.one.model.Friends;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spencerevans on 7/3/16.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendViewHolder> {

    private ArrayList<Friends> friendsList;

    public FriendsAdapter(ArrayList<Friends> friendsIDList) {
        friendsList = new ArrayList<>(friendsIDList.size());
        friendsList = friendsIDList;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.friend_list_row, parent, false);
        return new FriendViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        Friends friendObject = friendsList.get(position);
        String friendUsername;
        if (friendObject.getActualName() != null)
            friendUsername = friendObject.getActualName();
        else friendUsername = friendObject.getUserName();
        holder.tvFriendUsername.setText(friendUsername);
        holder.itemView.setTag(friendObject);
        holder.friendID = friendObject.getFriendId();
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }
}
