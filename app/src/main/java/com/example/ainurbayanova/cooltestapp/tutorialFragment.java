package com.example.ainurbayanova.cooltestapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class tutorialFragment extends Fragment {
    View view;
    DatabaseReference databaseReference;
    String user = null;
    FirebaseUser users = null;
    ArrayList<String> texts = new ArrayList<>();
    ArrayList<String> imgs = new ArrayList<>();
    RecycleTutorialsAdapter tutorialsAdapter;
    ProgressBar tutorialProgress;

    Button btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tutorialslayout,container,false);
        databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("Themes");

        users = FirebaseAuth.getInstance().getCurrentUser();

        if(users != null){
            user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }

        tutorialProgress = view.findViewById(R.id.tutorialProgress);

        tutorialProgress.setVisibility(View.VISIBLE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imgs.clear();
                texts.clear();
                DataSnapshot data = null;
                data = dataSnapshot.child(user);
                if(data.exists()) {
                    for (DataSnapshot datas : dataSnapshot.child(user).getChildren()) {
                        String values = datas.child("tutorial").getValue().toString();
                        String images = datas.child("image").getValue().toString();
                        texts.add(values);
                        imgs.add(images);
                    }
                }
                tutorialsAdapter.notifyDataSetChanged();
                tutorialProgress.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        RecyclerView recyclerView = view.findViewById(R.id.bicycle);

        tutorialsAdapter = new RecycleTutorialsAdapter(imgs,texts,getActivity());

        recyclerView.setAdapter(tutorialsAdapter);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(horizontalLayoutManager);


        return view;
    }
    public class RecycleTutorialsAdapter extends RecyclerView.Adapter<RecycleTutorialsAdapter.HolderView>{
        ArrayList<String> images = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        Context mContext;

        public RecycleTutorialsAdapter(ArrayList<String> i,ArrayList<String> t,Context context){
            images = i;
            titles = t;
            mContext = context;
        }
        @NonNull
        @Override
        public HolderView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.themes,viewGroup,false);
            HolderView viewHolder = new HolderView(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull HolderView holderView, int i) {
            Glide
                    .with(mContext)
                    .load(images.get(i))
                    .into(holderView.imageView);

            holderView.title.setText(titles.get(i));
        }

        @Override
        public int getItemCount() {
            return images.size();
        }
        public class HolderView extends RecyclerView.ViewHolder {

            CircleImageView imageView;
            TextView title;
            public HolderView(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.themeImage);
                title = itemView.findViewById(R.id.themeText);
            }
        }
    }

}
