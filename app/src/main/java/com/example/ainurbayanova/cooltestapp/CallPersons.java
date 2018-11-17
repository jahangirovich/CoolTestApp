package com.example.ainurbayanova.cooltestapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallPersons extends AppCompatActivity implements ItemClickListener{
    DatabaseReference databaseReference;
    ArrayList<String> keys = new ArrayList<>();
    String titles;
    FirebaseUser auth;
    String authed;
    RecyclerView recyclerView;
    RecycleViewForUser recycleViewForUser;
    ProgressBar callProgress;
    SwipeRefreshLayout swipeRefreshLayout;
    CardView card;
    ArrayList<for_user> user_with_image = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callpersons);

        callProgress = findViewById(R.id.progressCall);
        callProgress.setVisibility(View.VISIBLE);
        card = findViewById(R.id.cardToChallengeWithMyself);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Toolbar toolbar = findViewById(R.id.tool);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        if(auth != null){
            authed = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Bundle bundle = getIntent().getExtras();
        if(bundle !=null){
            String title = bundle.getString("title");
            titles = bundle.getString("title");
            getSupportActionBar().setTitle(title);
        }
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bundle != null){
                    Intent intent = new Intent(CallPersons.this,ChallengeWithMyself.class);
                    intent.putExtra("title",bundle.getString("title"));
                    startActivity(intent);
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        uploadThemes();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshCall);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                keys.clear();
                uploadThemes();
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setColorScheme(R.color.blue,

                        R.color.cards,

                        R.color.bronze,

                        R.color.green2);
            }
        });
    }

    private void uploadThemes() {
        databaseReference.child("Themes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user = "";
                for (DataSnapshot datas:dataSnapshot.getChildren()){
                    user = datas.getKey();
                    for (DataSnapshot into:dataSnapshot.child(user).getChildren()){
                        String hei = into.child("tutorial").getValue().toString();
                        if(titles.equals(hei)){
                            keys.add(datas.getKey());
                        }
                    }
                }
                for(int x = 0;x < keys.size();x++){
                    if(keys.get(x).equals(authed)){
                        keys.remove(x);
                    }
                }

                uploadItems();

                setChanged();

                recycleViewForUser.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setChanged(){

        recyclerView = findViewById(R.id.RecyclerView);

        recycleViewForUser = new RecycleViewForUser(user_with_image,CallPersons.this);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(horizontalLayoutManager);

        recyclerView.setAdapter(recycleViewForUser);

        recyclerView.setHasFixedSize(true);

        recycleViewForUser.setClickListener(this);
    }

    public void uploadItems(){
        calledThis(new CallBack() {
            @Override
            public void callIt(ArrayList<for_user> user_image) {
                callProgress.setVisibility(View.GONE);
            }
        });
    }

    private void calledThis(final CallBack callBack){
        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_with_image.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    for (String key:keys){
                        if(data.child("Username").getValue().toString().equals(key)){
                            String username = data.child("Username").getValue().toString();
                            String image = data.child("Image").getValue().toString();
                            for_user users = new for_user(image,username);
                            user_with_image.add(users);
                        }
                    }
                }
                callBack.callIt(user_with_image);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private interface CallBack{
        void callIt(ArrayList<for_user> user_image);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view, int position) {
        String main_user = authed;

        String main_image = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();

        String user = user_with_image.get(position).getUsername();

        String image = user_with_image.get(position).getImage();

        Intent intent = new Intent(CallPersons.this,Challenge.class);

        intent.putExtra("main_user",main_user);
        intent.putExtra("main_image",main_image);
        intent.putExtra("user",user);
        intent.putExtra("image",image);
        intent.putExtra("title",titles);

        startActivity(intent);
    }

    public class RecycleViewForUser extends RecyclerView.Adapter<RecycleViewForUser.ViewHolder>{
        ArrayList<for_user> user_with = new ArrayList<>();
        Context mContext;
        ItemClickListener itemClickListener;

        public RecycleViewForUser(ArrayList<for_user> u,Context context){
            user_with = u;
            mContext = context;
        }

        @NonNull
        @Override
        public RecycleViewForUser.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users,viewGroup,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull RecycleViewForUser.ViewHolder viewHolder, int i) {
            Glide.with(mContext)
                    .load(user_with.get(i).getImage())
                    .into(viewHolder.imageView);

            viewHolder.textView.setText(user_with.get(i).getUsername());
        }

        @Override
        public int getItemCount() {
            return user_with.size();
        }

        public void setClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            CircleImageView imageView;
            TextView textView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.usernameForUser);
                imageView = itemView.findViewById(R.id.imageForUser);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                itemClickListener.onClick(v,getPosition());
            }
        }
    }
}
