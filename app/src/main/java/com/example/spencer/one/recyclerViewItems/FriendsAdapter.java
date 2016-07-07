package com.example.spencer.one.recyclerViewItems;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.backendless.BackendlessUser;
import com.bumptech.glide.Glide;
import com.example.spencer.one.MainActivity;
import com.example.spencer.one.R;
import com.example.spencer.one.model.Friends;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by spencerevans on 7/3/16.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendViewHolder> {

    private ArrayList<Friends> friendsList;
    private Context context;

    public FriendsAdapter(ArrayList<Friends> friendsIDList, Context context) {
        this.context = context;
        friendsList = new ArrayList<>(friendsIDList.size());
        friendsList = friendsIDList;
    }

    public ArrayList<Friends> getFriendsList() {
        return friendsList;
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
        holder.fbid = friendObject.getFbid();
        Log.d("TAG", friendUsername+"adapter: "+friendObject.getFbid());
        if (friendObject.getFbid() == null) {
            Random randomImage = new Random();
            int n = randomImage.nextInt(9);
            switch (n) {
                case 0: holder.ivFbPhoto.setImageResource(R.drawable.default0);
                    break;
                case 1: holder.ivFbPhoto.setImageResource(R.drawable.default1);
                    break;
                case 2: holder.ivFbPhoto.setImageResource(R.drawable.default2);
                    break;
                case 3: holder.ivFbPhoto.setImageResource(R.drawable.default3);
                    break;
                case 4: holder.ivFbPhoto.setImageResource(R.drawable.default4);
                    break;
                case 5: holder.ivFbPhoto.setImageResource(R.drawable.default5);
                    break;
                case 6: holder.ivFbPhoto.setImageResource(R.drawable.default6);
                    break;
                case 7: holder.ivFbPhoto.setImageResource(R.drawable.default7);
                    break;
                case 8: holder.ivFbPhoto.setImageResource(R.drawable.default8);
                    break;
            }

        } else {
            String url = "http://graph.facebook.com/"+friendObject.getFbid()+"/picture?type=large";
            Glide.with(context).load(url).into(holder.ivFbPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public void addItem(Friends newFriend) {
        boolean notDuplicateFriend = true;
        for (Friends friend : friendsList) {
            Log.d("TAG", "friend: "+friend.getFriendId()+" newFriend: "+newFriend.getFriendId());
            if (friend.getFriendId().equals(newFriend.getFriendId())) {

                notDuplicateFriend = false;
            }
        }
        if (notDuplicateFriend) {
            friendsList.add(newFriend);
            notifyDataSetChanged();
        }
    }
}


