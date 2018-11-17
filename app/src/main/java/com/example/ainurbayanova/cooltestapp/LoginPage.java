package com.example.ainurbayanova.cooltestapp;

import android.app.Activity;
import android.content.Intent;
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

public class LoginPage extends AppCompatActivity implements View.OnClickListener{
    private FirebaseUser firebaseAuth;
    Button button;
    TextView inputEmail;
    TextView textView2;
    FirebaseAuth auth;

    ProgressBar progressBar;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        TextView textView = findViewById(R.id.register);

        inputEmail = findViewById(R.id.inputEmail);

        textView2 = findViewById(R.id.inputPassword);

        textView.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseAuth != null){
            user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            if(user != null){
                Intent intent = new Intent(LoginPage.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else{

        }

        button = findViewById(R.id.btnLogin);

        progressBar = findViewById(R.id.progress);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                final String email = inputEmail.getText().toString().trim();
                String password = textView2.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginPage.this,
                        new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginPage.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else if(!task.isSuccessful()){
                            Toast.makeText(LoginPage.this,"Something was wrong",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,registrationPage.class);
        startActivity(intent);
    }
}
