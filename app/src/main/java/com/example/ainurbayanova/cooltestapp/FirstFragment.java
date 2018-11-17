package com.example.ainurbayanova.cooltestapp;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.support.v7.widget.SearchView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirstFragment extends Fragment implements ItemClickListener,ItemOnThisClickListener {
    public FirstFragment() {

    }

    View view;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private ArrayList<Blog> blogStore;
    private ArrayList<Blog> blogStoreForAccounting = new ArrayList<>();
    private ArrayList<String> keys;
    RecycleViewAdapter adapter;
    ProgressBar progressBar;
    SearchView searchView;
    LinearLayout linearLayout;
    TextView textView;
    SwipeRefreshLayout swipeRefreshLayout;
    BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView recyclerViewAccounting;
    RecyclerViewForAccouting recyclerViewForAccoutingAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.firstfragment, container, false);
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerViewAccounting = view.findViewById(R.id.accounting);

        progressBar = view.findViewById(R.id.progressBar);
        blogStore = new ArrayList<>();
        keys = new ArrayList<>();

        linearLayout = view.findViewById(R.id.bottom);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Blog").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                blogStore.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    keys.add(data.getKey());
                    blogStore.add(data.getValue(Blog.class));
                }
                progressBar.setVisibility(View.GONE);

                modifyAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        searchView = view.findViewById(R.id.searches);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                recyclerViewForAccoutingAdapter.getFilter().filter(newText);
                return false;
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorScheme(R.color.blue,

                R.color.cards,

                R.color.bronze,

                R.color.green2);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                blogStore.clear();
                databaseReference.child("Blog").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            keys.add(data.getKey());

                            blogStore.add(data.getValue(Blog.class));
                        }

                        progressBar.setVisibility(View.GONE);

                        modifyAdapter();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                blogStoreForAccounting.clear();
                databaseReference.child("Main").child("Accounting").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren()){
                            blogStoreForAccounting.add(data.getValue(Blog.class));
                        }
                        progressBar.setVisibility(View.GONE);
                        modifyAccountingAdapter();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        databaseReference.child("Main").child("Accounting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                blogStoreForAccounting.clear();
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    blogStoreForAccounting.add(data.getValue(Blog.class));
                }
                progressBar.setVisibility(View.GONE);
                modifyAccountingAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    public void modifyAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecycleViewAdapter(getActivity(), blogStore);

        recyclerView.setAdapter(adapter);

        adapter.setClickListener(this);

        adapter.notifyDataSetChanged();
    }

    public void modifyAccountingAdapter(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerViewAccounting.setLayoutManager(linearLayoutManager);

        recyclerViewForAccoutingAdapter = new RecyclerViewForAccouting(getActivity(), blogStoreForAccounting);

        recyclerViewAccounting.setAdapter(recyclerViewForAccoutingAdapter);

        recyclerViewForAccoutingAdapter.setClickListener(this);

        recyclerViewForAccoutingAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view, int position) {
        String men = blogStore.get(position).getTitle();
        String text = blogStore.get(position).getText();
        String notes = blogStore.get(position).getNotes();
        String image = blogStore.get(position).getImage();

        Intent i = new Intent(getActivity(), newActivity.class);

        i.putExtra("title", men);
        i.putExtra("text", text);
        i.putExtra("notes", notes);
        i.putExtra("image", image);


        textView = view.findViewById(R.id.firstText);

        Pair[] pairs = new Pair[2];

        pairs[0] = new Pair<View, String>(recyclerView, "layoutTransition");
        pairs[1] = new Pair<View, String>(textView, "titleTransition");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
        startActivity(i, options.toBundle());


    }

    @Override
    public void onThisItem(View v, int position) {
        Intent i = new Intent(getActivity(),newActivity.class);
        String men = blogStoreForAccounting.get(position).getTitle();
        String text = blogStoreForAccounting.get(position).getText();
        String notes = blogStoreForAccounting.get(position).getNotes();
        String image = blogStoreForAccounting.get(position).getImage();

        i.putExtra("title2", men);
        i.putExtra("text2", text);
        i.putExtra("notes2", notes);
        i.putExtra("image2", image);

//        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_to_top, R.anim.slide_to_botto);

    }

    public class RecyclerViewForAccouting extends RecyclerView.Adapter<RecyclerViewForAccouting.ViewHolder> implements Filterable {
        private static final String TAG = "RecycleViewAdapter";

        private ArrayList<Blog> blogStore;

        ItemOnThisClickListener itemClickListener;

        private ArrayList<Blog> exampleList;

        private Context mContext;

        public RecyclerViewForAccouting(Context context, ArrayList<Blog> blogStore) {
            mContext = context;
            this.blogStore = blogStore;
//            exampleList = blogStore;
            exampleList = blogStore;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_chain2, viewGroup, false);

            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewForAccouting.ViewHolder viewHolder, int i) {
            Blog curBlog = blogStore.get(i);

            viewHolder.text1.setText(curBlog.getTitle());
            if(i % 2 ==  0){
                viewHolder.relativeLayout.setBackgroundColor(getResources().getColor(R.color.greenGradient));
                viewHolder.text1.setTextColor(getResources().getColor(R.color.white));
            }
            else{
                viewHolder.relativeLayout.setBackgroundColor(getResources().getColor(R.color.fiolet));
                viewHolder.text1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }

        }

        @Override
        public int getItemCount() {
            return blogStore.size();
        }

        public void setClickListener(ItemOnThisClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView text1;
            RelativeLayout relativeLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                relativeLayout = itemView.findViewById(R.id.relate);

                text1 = itemView.findViewById(R.id.firstText);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                itemClickListener.onThisItem(v,getPosition());
            }
        }

        @Override
        public Filter getFilter() {
            return exampleFilter;
        }

        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Blog> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(exampleList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Blog blog : exampleList) {
                        if (blog.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(blog);
                        }
                    }
                }
                FilterResults results = new FilterResults();

                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                blogStore = (ArrayList) results.values;

                adapter.notifyDataSetChanged();

            }
        };
    }

    public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> implements Filterable {
        private static final String TAG = "RecycleViewAdapter";

        private ArrayList<Blog> blogStore;

        ItemClickListener itemClickListener;

        private ArrayList<Blog> exampleList;

        private Context mContext;

        public RecycleViewAdapter(Context context, ArrayList<Blog> blogStore) {
            mContext = context;
            this.blogStore = blogStore;
//            exampleList = blogStore;
            exampleList = blogStore;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_chain, viewGroup, false);

            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Blog curBlog = blogStore.get(i);

            Glide.with(mContext).load(curBlog.getImage()).into(viewHolder.imageView);

            viewHolder.text1.setText(curBlog.getTitle());
        }

        @Override
        public int getItemCount() {
            return blogStore.size();
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            CircleImageView imageView;
            public TextView text1;

            public ViewHolder(View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.firstImage);
                text1 = itemView.findViewById(R.id.firstText);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                itemClickListener.onClick(view, getPosition());

            }

        }

        @Override
        public Filter getFilter() {
            return exampleFilter;
        }

        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Blog> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(exampleList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Blog blog : exampleList) {
                        if (blog.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(blog);
                        }
                    }
                }
                FilterResults results = new FilterResults();

                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                blogStore = (ArrayList) results.values;

                adapter.notifyDataSetChanged();

            }
        };
    }



    @Override
    public void onStart() {
        super.onStart();

    }
}
