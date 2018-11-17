package com.example.ainurbayanova.cooltestapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonAdd extends AppCompatActivity implements ItemClickListener{
    Toolbar toolbar;
    DatabaseReference databaseReference;
    private ArrayList<Friends> friends;
    String auth;
    HashMap<String,String> friend = new HashMap<>();
    String user_image;
    private FriendRecycleView adapters;
    ProgressBar goodProgress;
    private ArrayList<AddFriends> copy = new ArrayList<>();
    FirebaseUser user;
    Button addUser;
    Boolean empty = false;
    int x = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personadd);

        friends = new ArrayList<>();

        goodProgress = findViewById(R.id.goodProgress);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        toolbar = findViewById(R.id.personToolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        goodProgress.setVisibility(View.VISIBLE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            auth = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            user_image = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
        }

        databaseReference.child("Friends").child(auth).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                copy.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    AddFriends addFriends = data.getValue(AddFriends.class);
                    copy.add(addFriends);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friends.clear();
                for (DataSnapshot data:dataSnapshot.getChildren()){

                    Friends blog = data.getValue(Friends.class);
                    friends.add(blog);

                }
                goodProgress.setVisibility(View.GONE);
                for (int x = 0;x < friends.size();x++){
                    if(friends.get(x).getUsername().equals(auth)){
                        friends.remove(x);
                    }
                }
                for (int x = 0;x < friends.size();x++){
                    for (int y = 0;y < copy.size();y++){
                        if(friends.get(x).getUsername().equals(copy.get(y).getUsername())){
                            friends.remove(x);
                        }
                    }
                }

                if(friends.size() == 0){
                    empty = true;
                }

                TextView textView = findViewById(R.id.emptyText);

                if(empty){
                    textView.setVisibility(View.VISIBLE);
                }
                adapters.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapters = new FriendRecycleView(PersonAdd.this,friends);

        RecyclerView recyclerView = findViewById(R.id.recyclerViews);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(PersonAdd.this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(horizontalLayoutManager);

        recyclerView.setAdapter(adapters);

        adapters.setClickListener(this);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public void onClick(View view, int position) {
        friend.put("username",friends.get(position).getUsername());
        friend.put("image",friends.get(position).getImage());
        String key = databaseReference.child("Friends").child(auth).push().getKey();

        friend.put("key",key);

        databaseReference.child("Friends").child(auth).child(key).setValue(friend);
    }
}
class FriendRecycleView extends RecyclerView.Adapter<FriendRecycleView.ViewHolder>{
    ArrayList<Friends> friends = new ArrayList<>();

    Context mContext;

    ItemClickListener itemClickListener;

    public FriendRecycleView(Context context,ArrayList<Friends> friend){
        mContext = context;
        friends = friend;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.userdetail,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Glide.with(mContext)
                .load(friends.get(i).getImage())
                .into(viewHolder.imageView);

        viewHolder.textView.setText(friends.get(i).getUsername());
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView;
        CircleImageView imageView;
        Button buttons;
        Animation animation;
        Button card;
        Button buttonOption;
        Button removeIt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.friendText);
            imageView = itemView.findViewById(R.id.friendImage);
            buttons = itemView.findViewById(R.id.addUser);
            buttonOption = itemView.findViewById(R.id.buttonOptions);
            buttonOption.setVisibility(View.GONE);
            buttons.setOnClickListener(this);
            removeIt = itemView.findViewById(R.id.removeUser);
            removeIt.setVisibility(View.GONE);
        }
        public void onClick(View view){
            itemClickListener.onClick(view,getPosition());
            switch (view.getId()){
                case R.id.addUser:
                    buttons.setVisibility(View.GONE);

                    break;
            }
        }
    }
}
