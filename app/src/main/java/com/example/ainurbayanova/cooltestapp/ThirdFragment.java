package com.example.ainurbayanova.cooltestapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThirdFragment extends Fragment {
    public ThirdFragment(){

    }
    View view;
    TextView textView;
    CircleImageView imageView;
    FirebaseUser auth;
    String user;
    String image;
    DatabaseReference databaseReference;
    CardView cardView;
    TextView editText;
    HashMap<String,String> hashMap = new HashMap<>();
    ArrayList<Posts> posts = new ArrayList<>();
    RecyclerPostView adapter ;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progresssBar;
    ImageView images;
    Spinner spinner;
    ArrayList<String> friends = new ArrayList<>();
    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    ImageView imageForUpload;
    CardView chooseImage;
    TextView showIt;
    FirebaseStorage storage;
    StorageReference storageReference;

    boolean determine = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.thirdfragment,container,false);

        View views = inflater.inflate(R.layout.post_fragment,container,false);

        imageForUpload = view.findViewById(R.id.imageForUpload);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        chooseImage = view.findViewById(R.id.addImage);
        showIt = view.findViewById(R.id.showHome);
        images = views.findViewById(R.id.trashImage);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        if(auth != null){
            user = auth.getDisplayName();
            image = auth.getPhotoUrl().toString();
        }
        textView = view.findViewById(R.id.userUseranme);

        imageView = view.findViewById(R.id.userCircle);

        spinner = view.findViewById(R.id.spinneR);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.choose,android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);

        images.setVisibility(View.GONE);

        databaseReference.child("Friends").child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friends.clear();
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    friends.add(data.child("username").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    determine = false;
                    upload();
                }
                else if(position == 1){
                    determine = true;
                    databaseReference.child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            posts.clear();
                            for (DataSnapshot data:dataSnapshot.getChildren()){
                                if(data.child("user").getValue().toString().equals(user)){
                                    Posts post = data.getValue(Posts.class);
                                    posts.add(post);
                                }
                            }
                            if(posts.size() == 0){
                                showIt.setVisibility(View.VISIBLE);
                            }
                            else{
                                showIt.setVisibility(View.GONE);
                            }
                            progresssBar.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    images.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        textView.setText(user);

        Glide.with(getActivity())
                .load(image)
                .into(imageView);

        swipeRefreshLayout = view.findViewById(R.id.swiper);

        swipeRefreshLayout.setColorScheme(R.color.bronze);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!determine){
                    upload();
                }
                else if(determine){

                    databaseReference.child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            posts.clear();
                            for (DataSnapshot data:dataSnapshot.getChildren()){
                                    if(data.child("user").getValue().toString().equals(user)){
                                        Posts post = data.getValue(Posts.class);
                                        posts.add(post);
                                    }
                            }
                            if(posts.size() == 0){
                                showIt.setVisibility(View.VISIBLE);
                            }
                            else{
                                showIt.setVisibility(View.GONE);
                            }
                            progresssBar.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        cardView = view.findViewById(R.id.cardView);

        editText = view.findViewById(R.id.edits);

        progresssBar = view.findViewById(R.id.postProgress);

        progresssBar.setVisibility(View.VISIBLE);

        databaseReference.child("Posts").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                posts.clear();

                for (DataSnapshot data:dataSnapshot.getChildren()){
                    for (String friend:friends){
                        if(data.child("user").getValue().toString().equals(friend)){
                            Posts post = data.getValue(Posts.class);
                            posts.add(post);
                        }
                    }
                }
                progresssBar.setVisibility(View.GONE);
                setPosts();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText.getText().toString().equals("")){
                    Snackbar.make(view, "Please fill it", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                else{
                    progresssBar.setVisibility(View.VISIBLE);
                    uploadImage();
                }
            }
        });

        Collections.reverse(posts);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        return view;
    }
    public void upload(){
        databaseReference.child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                posts.clear();
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    for (String friend:friends){
                        if(data.child("user").getValue().toString().equals(friend)){
                            Posts post = data.getValue(Posts.class);
                            posts.add(post);
                        }
                    }
                }
                progresssBar.setVisibility(View.GONE);
                if(posts.size() == 0){
                    showIt.setVisibility(View.VISIBLE);
                }
                else{
                    showIt.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void chooseImage() {
        imageForUpload.setVisibility(View.VISIBLE);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageForUpload.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");

            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+System.currentTimeMillis()+"."+ getFileExtension(filePath));

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                            hashMap.put("title",editText.getText().toString());

                            hashMap.put("user",user);

                            hashMap.put("image",image);

                            hashMap.put("post_image",taskSnapshot.getDownloadUrl().toString());

                            editText.setText("");

                            String key = databaseReference.child("Posts").push().getKey();

                            hashMap.put("key",key);

                            databaseReference.child("Posts").child(key).setValue(hashMap);

                            imageForUpload.setVisibility(View.GONE);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });

        }


    }
    private String getFileExtension(Uri uri){
        ContentResolver resolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    public void setPosts(){

        adapter = new RecyclerPostView(getActivity(),posts);

        recyclerView = view.findViewById(R.id.post_recyclerView);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(linearLayoutManager);
    }
    public class RecyclerPostView extends RecyclerView.Adapter<ThirdFragment.ViewHolder>{

        ArrayList<Posts> post = new ArrayList<>();
        Context mContext;

        public RecyclerPostView(Context context,ArrayList<Posts> posts){
            mContext = context;
            post = posts;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_fragment,viewGroup,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Glide.with(mContext).load(post.get(i).getImage()).into(viewHolder.imageView);
            viewHolder.title_text.setText(post.get(i).getTitle());
            viewHolder.user_text.setText(post.get(i).getUser());
            Glide.with(mContext).load(post.get(i).getPost_image()).into(viewHolder.relativeLayout);
        }

        @Override
        public int getItemCount() {
            return post.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imageView;
        TextView user_text;
        TextView title_text;
        String user;
        ImageView icon;
        ImageView relativeLayout;
        DatabaseReference databaseReference;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.post_image);
            user_text = itemView.findViewById(R.id.post_text);
            title_text = itemView.findViewById(R.id.post_title);
            icon = itemView.findViewById(R.id.trashImage);
            user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            relativeLayout = itemView.findViewById(R.id.my_post);
        }

    }
}
