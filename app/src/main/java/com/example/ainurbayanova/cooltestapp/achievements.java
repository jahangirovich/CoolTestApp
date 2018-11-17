package com.example.ainurbayanova.cooltestapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class achievements extends Fragment {
    View view;
    RecyclerViewForMusic recyclerAdapterForMusic;
    ArrayList<String> producers = new ArrayList<>();
    ArrayList<String> nameOf = new ArrayList<>();
    ArrayList<String> music = new ArrayList<>();
    RecyclerView recyclerView;
    MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(container.getContext()).inflate(R.layout.achievements,container,false);

        producers.add("Rhianna");
        nameOf.add("Diamond");
        music.add("https://music.yandex.ru/album/5263065/track/37387240");

        producers.add("Malbek && Siuzanna");
        nameOf.add("Gipnozi");
        music.add("gs://cooltestapp-3841c.appspot.com/Cartoon – On & On (feat. Daniel Levi).mp3");

        recyclerView = view.findViewById(R.id.recyclerForMusic);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);

        recyclerAdapterForMusic = new RecyclerViewForMusic(getActivity(),producers,nameOf,music);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.getItemAnimator();

        recyclerView.setAdapter(recyclerAdapterForMusic);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapterForMusic.notifyDataSetChanged();
        recyclerAdapterForMusic.setOnClickMe(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(music.get(position));
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                }
                catch (IOException e){
                }
            }
        });
        return view;
    }

    public class RecyclerViewForMusic extends RecyclerView.Adapter<RecyclerViewForMusic.ViewHolder>{

        Context context;
        ArrayList<String> creator = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> music = new ArrayList<>();
        ItemClickListener itemClickListener;

        public RecyclerViewForMusic(Context context,ArrayList<String> creator,ArrayList<String> name,ArrayList<String> music){
            this.context = context;
            this.creator = creator;
            this.name = name;
            this.music = music;
        }

        @NonNull
        @Override
        public RecyclerViewForMusic.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_music,viewGroup,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        public void setOnClickMe(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerViewForMusic.ViewHolder viewHolder, int i) {
            viewHolder.creator.setText(creator.get(i));
            viewHolder.name.setText(name.get(i));

        }

        @Override
        public int getItemCount() {
            return creator.size();
        }

        @Override
        public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
            super.onViewAttachedToWindow(holder);

        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView name;
            TextView creator;
            ImageView imageView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.nameOfMusic);
                creator = itemView.findViewById(R.id.creatorOfMusic);
                imageView = itemView.findViewById(R.id.clickPlay);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                itemClickListener.onClick(v,getPosition());
            }
        }
    }
}
