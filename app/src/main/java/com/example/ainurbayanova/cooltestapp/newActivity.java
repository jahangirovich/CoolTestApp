package com.example.ainurbayanova.cooltestapp;

import android.annotation.SuppressLint;
import android.os.Handler;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class newActivity extends AppCompatActivity {
    DatabaseReference databaseReference;

    HashMap<String, String> map;
    String user;
    String all;
    String user2;
    String title2;
    HashMap<String,String> news = new HashMap<>();
    ArrayList<String> things = new ArrayList<>();
    ArrayList<tutorialClass> check2 = new ArrayList<>();
    ArrayList<String> strs = new ArrayList<>();
    ArrayList<String> answers = new ArrayList<>();
    ArrayList<Question> questionList = new ArrayList<>();
    String image = null;
    LinearLayout linearLayout;
    LinearLayout linearLayout2;
    TextView notes;
    RadioButton radioButton1;
    RadioButton radioButton2;
    ProgressBar textProgress;
    RadioButton radioButton3;
    RadioButton radioButton4;
    String q = null;
    String c = null;
    Button check;
    boolean first = false, second = false;
    Button quiz;

    CardView card1;
    CardView card2;
    Animation toTop;
    TextView what;
    String detail = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);

        check = findViewById(R.id.check);

        what = findViewById(R.id.what);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textProgress = findViewById(R.id.textProgress);
        textProgress.setVisibility(View.VISIBLE);


        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }


        final Bundle bundle = getIntent().getExtras();

        notes = findViewById(R.id.notes);

        if (bundle != null) {
            String me = bundle.getString("title");
            String img = bundle.getString("image");
            String note = bundle.getString("notes");
            String me2 = bundle.getString("title2");
            String note2 = bundle.getString("notes2");
            String text = bundle.getString("text2");
            String img2 = bundle.getString("image2");
            if (note == null && note2 != null) {
                notes.setText(note2 + "");
                detail += text;
                image = img2;
                getSupportActionBar().setTitle(me2);
                all = bundle.getString("title2");
            } else {
                notes.setText(note + "");
                detail += bundle.getString("text");
                image = img;
                getSupportActionBar().setTitle(me);
                all = bundle.getString("title");
            }
        }
        quiz = findViewById(R.id.quiz);
        String collectAll = "";
        int count = 0;
        String[] divide = detail.split(".");
        String huge = "";
        if(divide.length >= 4){
            for(int x = 0;x < 4;x++){
                huge += divide[x];
            }
            huge += "\n";
            for (int x = 4;x < divide.length;x++){
                huge += divide[x];
            }
        }
        what.setText(huge);
        things.add(image);
        things.add(all);

        news.put("tutorial",all);
        news.put("image",image);
        linearLayout = findViewById(R.id.linearLayout);
        linearLayout2 = findViewById(R.id.linearLayout2);

        databaseReference.child("Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uploadData(dataSnapshot);
                textProgress.setVisibility(View.GONE);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference.child("Themes").child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                strs.clear();
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    String tut = data.child("tutorial").getValue().toString();
                    strs.add(tut);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        databaseReference.child("Themes").child(user).push().setValue(news);

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTest();
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public void startTest(){

        ArrayList<String> anwers = new ArrayList<>();
        uploadToFirebase();
        for (int i = 0; i < questionList.size(); i++) {
            final int random = (int) (Math.random() * questionList.size());
            Question current = questionList.get(random);

            q = current.getQuestion();
            c = current.getCorrect();

            anwers = current.getAnswersList();

            linearLayout.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.VISIBLE);

            TextView textView = findViewById(R.id.question);

            textView.setText(q);

            radioButton1 = findViewById(R.id.button1);
            radioButton2 = findViewById(R.id.button2);
            radioButton3 = findViewById(R.id.button3);
            radioButton4 = findViewById(R.id.button4);

            radioButton1.setText(anwers.get(0));
            radioButton2.setText(anwers.get(1));
            radioButton3.setText(anwers.get(2));
            radioButton4.setText(anwers.get(3));

            radioButton1.setButtonTintList(ColorStateList.valueOf(R.color.colorPrimaryDark));
            radioButton2.setButtonTintList(ColorStateList.valueOf(R.color.colorPrimaryDark));
            radioButton3.setButtonTintList(ColorStateList.valueOf(R.color.colorPrimaryDark));
            radioButton4.setButtonTintList(ColorStateList.valueOf(R.color.colorPrimaryDark));

            answers.add(radioButton1.getText().toString());
            answers.add(radioButton2.getText().toString());
            answers.add(radioButton3.getText().toString());
            answers.add(radioButton4.getText().toString());

            toTop = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_to_top);

            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(radioButton1.getText().toString().equals(c) && radioButton1.isChecked()){
                        radioButton1.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                        radioButton1.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                        card1.setVisibility(View.VISIBLE);
                        card2.setVisibility(View.GONE);
                        card1.startAnimation(toTop);
                    }
                    else if(!radioButton1.getText().toString().equals(c) && radioButton1.isChecked()){
                        radioButton1.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.cards));
                        radioButton1.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.cards));
                        card1.setVisibility(View.GONE);
                        card2.setVisibility(View.VISIBLE);
                        card2.startAnimation(toTop);
                        for (int x = 0; x < answers.size();x++){
                            if(answers.get(x).equals(c)){
                                if(x == 1){
                                    radioButton2.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                    radioButton2.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                }
                                else if(x == 2){
                                    radioButton3.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                    radioButton3.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                }
                                else if(x == 3){
                                    radioButton4.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                    radioButton4.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                }
                                break;
                            }
                        }
                    }
                    if(radioButton2.getText().toString().equals(c) && radioButton2.isChecked()){
                        radioButton2.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                        radioButton2.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                        card1.setVisibility(View.VISIBLE);
                        card2.setVisibility(View.GONE);
                        card1.startAnimation(toTop);
                    }
                    else if(!radioButton2.getText().toString().equals(c) && radioButton2.isChecked()){
                        radioButton2.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.cards));
                        radioButton2.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.cards));
                        card1.setVisibility(View.GONE);
                        card2.setVisibility(View.VISIBLE);
                        card2.startAnimation(toTop);
                        for (int x = 0; x < answers.size();x++){
                            if(answers.get(x).equals(c)){
                                if(x == 0){
                                    radioButton1.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                    radioButton1.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                }
                                else if(x == 2){
                                    radioButton3.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                    radioButton3.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                }
                                else if(x == 3){
                                    radioButton4.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                    radioButton4.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                }
                                break;
                            }
                        }
                    }

                    if(radioButton3.getText().toString().equals(c) && radioButton3.isChecked()){
                        radioButton3.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                        radioButton3.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                        card1.setVisibility(View.VISIBLE);
                        card2.setVisibility(View.GONE);
                        card1.startAnimation(toTop);
                    }
                    else if(!radioButton3.getText().toString().equals(c) && radioButton3.isChecked()){
                        radioButton3.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.cards));
                        radioButton3.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.cards));
                        card1.setVisibility(View.GONE);
                        card2.setVisibility(View.VISIBLE);
                        card2.startAnimation(toTop);
                        for (int x = 0; x < answers.size();x++){
                            if(answers.get(x).equals(c)){
                                if(x == 0){
                                    radioButton1.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                    radioButton1.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                }
                                else if(x == 1){
                                    radioButton2.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                    radioButton2.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                }
                                else if(x == 3){
                                    radioButton4.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                    radioButton4.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                }
                                break;
                            }
                        }
                    }
                    if(radioButton4.getText().toString().equals(c) && radioButton4.isChecked()){
                        radioButton4.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                        radioButton4.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                        card1.setVisibility(View.VISIBLE);
                        card2.setVisibility(View.GONE);
                        card1.startAnimation(toTop);
                    }
                    else if(!radioButton4.getText().toString().equals(c) && radioButton4.isChecked()){
                        radioButton4.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.cards));
                        radioButton4.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.cards));
                        card1.setVisibility(View.GONE);
                        card2.setVisibility(View.VISIBLE);
                        card2.startAnimation(toTop);
                        for (int x = 0; x < answers.size();x++){
                            if(answers.get(x).equals(c)){
                                if(x == 0){
                                    radioButton1.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                    radioButton1.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                }
                                else if(x == 1){
                                    radioButton2.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                    radioButton2.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                }
                                else if(x == 2){
                                    radioButton3.setTextColor(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                    radioButton3.setButtonTintList(ContextCompat.getColorStateList(getBaseContext(), R.color.green2));
                                }
                                break;
                            }
                        }
                    }
                    new Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    startActivity(new Intent(newActivity.this, MainActivity.class));
                                    finish();
                                }
                    }, 3000);
                }

            }
            );
            break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.resize_word,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        float f = what.getTextSize();
        switch (item.getItemId()){
            case R.id.increase:
                f += 2.0;
                what.setTextSize(TypedValue.COMPLEX_UNIT_PX, f);
                break;
            case R.id.decrease:
                f -= 2.0;
                what.setTextSize(TypedValue.COMPLEX_UNIT_PX, f);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void uploadToFirebase(){
        boolean exist = false;
        for (String titles:strs){
            if(titles.equals(all)){
                exist = true;
                break;
            }
            else{
                exist = false;
            }
        }
        if (!exist){
            databaseReference.child("Themes").child(user).push().setValue(news);
        }
    }

    public void uploadData(DataSnapshot dataSnapshot){
        for (DataSnapshot data : dataSnapshot.getChildren()) {
            String question = data.getKey().toString();

            String correct = "";
            ArrayList<String> check = new ArrayList<>();

            for (DataSnapshot datas : data.getChildren()) {

                String key = datas.getKey().toString();

                String answers = datas.getValue().toString();

                check.add(key);

                if (answers.equals("t")) {
                    correct = key;
                }
            }
            Question question1 = new Question(question, check, correct);
            questionList.add(question1);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.slide_to_top, R.anim.slide_to_botto);
    }
}
