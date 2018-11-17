package com.example.ainurbayanova.cooltestapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class registrationPage extends AppCompatActivity implements View.OnClickListener {
    TextView inputEmail;
    TextView username;
    TextView inputPassword;
    FirebaseAuth auth;
    String picture;
    ProgressBar progressBar;
    Map<String, String> map;
    DatabaseReference databaseReference;
    ArrayList<String> users = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        picture = "https://firebasestorage.googleapis.com/v0/b/cooltestapp-3841c.appspot.com/o/phon.jpg?alt=media&token=44563ade-350f-4c84-b0ee-da2b456ccffd";
        setContentView(R.layout.registrationpage);
        map = new HashMap<>();

        auth = FirebaseAuth.getInstance();
        TextView textView = findViewById(R.id.login);
        textView.setOnClickListener(this);
        Button btnSignUp = findViewById(R.id.btnSignUp);

        inputEmail = findViewById(R.id.email);
        username = findViewById(R.id.username);

        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressing);

        progressBar.setVisibility(View.GONE);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    String user1 = data.child("Username").getValue().toString();

                    users.add(user1);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                final String userName = username.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(getApplicationContext(), "Enter username !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userName.length() < 5) {
                    Toast.makeText(getApplicationContext(), "Your username is too short!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(userName)){
                    for (String user:users){
                        if(user.equals(userName)){
                            Toast.makeText(registrationPage.this,"Sorry but username already exist",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            break;
                        }
                        else{
                            register(email,password);
                            break;
                        }
                    }
                }
            }
        });
    }
    public void register(String email,String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(registrationPage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(registrationPage.this, "Something was wrong . Maybe you have the same email.",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            map.put("Email",inputEmail.getText().toString());
                            map.put("Username",username.getText().toString());
                            map.put("Password",inputPassword.getText().toString());
                            map.put("Image",picture);
                            databaseReference.push().setValue(map);
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username.getText().toString())
                                    .setPhotoUri(Uri.parse(picture))
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("add name", "User profile updated.");
                                            }
                                        }
                                    });
                            Intent intent = new Intent(registrationPage.this,LoginPage.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
