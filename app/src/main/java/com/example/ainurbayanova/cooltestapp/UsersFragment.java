package com.example.ainurbayanova.cooltestapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersFragment extends Fragment implements ItemClickListener {
    View view;
    FirebaseUser user;
    String auth;
    DatabaseReference databaseReference;
    ArrayList<AddFriends> friends = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar userProgress;
    UserRecyclerView adapter;
    int x = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.usersfragment, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            auth = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }

        userProgress = view.findViewById(R.id.userProgress);

        userProgress.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Friends").child(auth).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friends.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String username = data.child("username").getValue().toString();
                    String image = data.child("image").getValue().toString();
                    String key = data.child("key").getValue().toString();

                    AddFriends addFriends = new AddFriends(username, image, key);

                    friends.add(addFriends);

                }
                userProgress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new UserRecyclerView(getActivity(), friends);

        recyclerView = view.findViewById(R.id.userRecyclerView);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(horizontalLayoutManager);

        recyclerView.setAdapter(adapter);

        adapter.setItemClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view, final int position) {

        databaseReference.child("Friends").child(auth).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AddFriends currentFriend = friends.get(position);
                Log.i("info", "username: " + currentFriend.getUsername());
                Log.i("info", "key: " + currentFriend.getKey());
                dataSnapshot.getRef().child(currentFriend.getKey()).removeValue();
                friends.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}

class UserRecyclerView extends RecyclerView.Adapter<UserRecyclerView.ViewHolder> {
    ArrayList<AddFriends> friends = new ArrayList<>();
    Context mContext;
    DatabaseReference databaseReference;
    ItemClickListener itemClickListener;

    public UserRecyclerView(Context context, ArrayList<AddFriends> friend) {
        friends = friend;
        mContext = context;
    }

    @NonNull
    @Override
    public UserRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.userdetail, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerView.ViewHolder viewHolder, int i) {
        Glide.with(mContext)
                .load(friends.get(i).getImage())
                .into(viewHolder.imageView);

        viewHolder.textView.setText(friends.get(i).getUsername());

        final Button button = viewHolder.buttonOptions;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, button);

                popup.inflate(R.menu.user_menu);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });

                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        CircleImageView imageView;
        Button buttonOptions;
        Button removeIt;
        Button btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.friendText);
            imageView = itemView.findViewById(R.id.friendImage);
            btn = itemView.findViewById(R.id.addUser);
            btn.setVisibility(View.GONE);
            buttonOptions = (Button) itemView.findViewById(R.id.buttonOptions);

            removeIt = itemView.findViewById(R.id.removeUser);

            removeIt.setVisibility(View.VISIBLE);

            removeIt.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getPosition());
        }
    }
}
