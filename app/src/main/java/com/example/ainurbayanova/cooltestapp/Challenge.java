package com.example.ainurbayanova.cooltestapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Challenge extends AppCompatActivity {
    TextView main_users;
    CircleImageView main_images;
    TextView users;

    Animation animation;
    Animation animation2;
    TextView titles;
    CircleImageView second_images;
    LinearLayout checkIt;
    Button buttons;
    TextView round;
    RelativeLayout all;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    ArrayList<CollectAllForTes> collectAllForTes = new ArrayList<>();
    TextView count1;
    TextView count2;

    private Handler handler = new Handler();
    FrameLayout frameLayout;
    DatabaseReference databaseReference;
    LinearLayout finished;
    String title;
    LinearLayout testing;
    ArrayList<Integer> numbers = new ArrayList<>();

    ArrayList<testQuestions> QuestionsForQuiz = new ArrayList<>();
    TextView questionTitle;
    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton radioButton4;
    FrameLayout checkAnswers;
    TextView correct;

    String increaseString = "    \n";
    RelativeLayout cover;
    int delay = 1400;

    ArrayList<String> correctAnswers = new ArrayList<>();

    String wrongAnswers = "";

    String correctAnswer = "";

    String questions = "";

    CircleImageView main_image2;
    CircleImageView image2;

    CountDownTimer countDownTimer;
    TextView main_user2;

    TextView time;

    TextView user2;

    TextView score;

    String true_question;

    ArrayList<CollectAllForTes> collections = new ArrayList<>();

    int x = 50;
    int counter = 0;
    int main_score = 0;
    String key;
    TextView code;

    HashMap<String,Boolean> hashMap = new HashMap<>();

    boolean buttonPressed = false;
    String that_right = null;
    boolean started = false;
    int lengthOfTest = 0;
    RadioButton rdbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.challenge);

        initWidgets();
        Toolbar toolbar = findViewById(R.id.challenge_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(started){
                    new AlertDialog.Builder(Challenge.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage("Do you wish to leave?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Challenge.this.finish();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
                else{
                    finish();
                }

            }
        });

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String main_user = bundle.getString("main_user");
            String main_image = bundle.getString("main_image");
            String image = bundle.getString("image");
            String user = bundle.getString("user");
            title = bundle.getString("title");

            Glide.with(this).load(main_image).into(main_images);
            main_users.setText(main_user);

            Glide.with(this).load(image).into(second_images);
            users.setText(user);

            Glide.with(this).load(main_image).into(main_image2);
            main_user2.setText(main_user);

            Glide.with(this).load(image).into(image2);
            user2.setText(user);

            titles.setText(title);
        }

        buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                started = true;
                startAnimate(0);
                uploadTest();
            }
        });

    }
    public void initWidgets(){
        frameLayout = findViewById(R.id.frame);

        round = findViewById(R.id.round);

        testing = findViewById(R.id.testS);

        testing.setVisibility(View.GONE);

        all = findViewById(R.id.all);

        numbers.clear();

        buttons = findViewById(R.id.buttons);
        main_users = findViewById(R.id.main_user);
        main_images = findViewById(R.id.main_image);
        users = findViewById(R.id.user);
        second_images = findViewById(R.id.second_image);
        titles = findViewById(R.id.titleOfTheme);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);

        main_image2 = findViewById(R.id.main_image2);
        main_images.startAnimation(animation);

        animation2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);

        main_image2.startAnimation(animation);

        second_images.startAnimation(animation2);
        score = findViewById(R.id.scoreUser);

        cover = findViewById(R.id.cover);

        cover.setVisibility(View.GONE);

        time = findViewById(R.id.time);

        image2 = findViewById(R.id.second_image2);

        main_user2 = findViewById(R.id.main_user2);

        user2 = findViewById(R.id.user2);

        radioGroup = findViewById(R.id.radioGroups);

        checkAnswers = findViewById(R.id.chechAnswers);

        count1 = findViewById(R.id.count1);

        count2 = findViewById(R.id.count2);

        checkAnswers.setVisibility(View.GONE);

        correct = findViewById(R.id.correct);

        questionTitle = findViewById(R.id.questionTitle);

        checkIt = findViewById(R.id.checkIt);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        radioButton1 = findViewById(R.id.challengeButton1);

        radioButton2 = findViewById(R.id.challengeButton2);

        radioButton3 = findViewById(R.id.challengeButton3);

        radioButton4 = findViewById(R.id.challengeButton4);

        code = findViewById(R.id.code);
    }
    public void uploadTest(){
        databaseReference.child("Test").child(title).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String question = "";
                ArrayList<String> answers = new ArrayList<>();
                String right = "";
                int testSize = 0;
                testQuestions test = null;
                String coding = "";
                for (DataSnapshot all:dataSnapshot.getChildren()){
                    for (DataSnapshot dating:all.getChildren()){
                        if(dating.getKey().equals("answers")){
                            testSize = (int)dating.getChildrenCount();
                            for (DataSnapshot last:dating.getChildren()){
                                answers.add(last.getKey());
                                Boolean check = Boolean.parseBoolean(last.getValue().toString());
                                if (check) {
                                    right = last.getKey() + "";
                                }
                            }
                        }
                        if(dating.getKey().equals("question")){
                            question = dating.getValue().toString();
                        }
                        if(dating.getKey().equals("code")){
                            coding = dating.getValue().toString();
                        }
                        test = new testQuestions(question,right,answers,testSize,coding);
                    }
                    QuestionsForQuiz.add(test);
                }
                randomIt();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void startAnimate(final int y){
        int counting = y + 1;
        all.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        round.setText("Round " + counting);
        checkAnswers.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.slide_to_top);
        round.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation2 = AnimationUtils.loadAnimation(Challenge.this,R.anim.slide_to_botto);
                round.startAnimation(animation2);
                animation2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        frameLayout.setVisibility(View.GONE);
                        testing.setVisibility(View.VISIBLE);
                        setTestContent(y);
                        countIt(y);
                        buttonPressed = false;
                        checkIt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                buttonPressed = true;
                                goodAnimate(y);
                                x = 50;
                            }
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public void goodAnimate(int nymber){
        testing.setVisibility(View.GONE);
        checkAnswers.setVisibility(View.VISIBLE);
        testQuestions test = QuestionsForQuiz.get(numbers.get(nymber));
        that_right = test.getRights();
        int counting = 0;
        String question = test.getQuestions();
        String code = test.getCode();
        Animation animation1 = AnimationUtils.loadAnimation(Challenge.this,R.anim.slide_to_top);
        correct.startAnimation(animation1);
        CollectAllForTes collect = null;
        String value = "";

        if(radioGroup.getCheckedRadioButtonId() != -1){
            value =
                    ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId()))
                            .getText().toString();
        }
        if(radioGroup.getCheckedRadioButtonId() == -1){
            value = "";
        }

        if(value.equals(that_right)){
            correct.setText("Right");
            correct.setTextColor(getResources().getColor(R.color.green2));
            main_score += 1;
            count1.setText(main_score+"");
            collect = new CollectAllForTes(question,that_right,"",false,code);
        }
        else if(value.equals("")){
            correct.setText("Wrong");
            correct.setTextColor(getResources().getColor(R.color.cards));
            collect = new CollectAllForTes(question,that_right,"Not Answered",false,code);
        }
        else{
            correct.setText("Wrong");
            correct.setTextColor(getResources().getColor(R.color.cards));
            collect = new CollectAllForTes(question,that_right,value,false,code);
        }
        collections.add(collect);
        radioGroup.clearCheck();
        countDownTimer.cancel();
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation3 = AnimationUtils.loadAnimation(Challenge.this,R.anim.slide_to_botto);
                correct.startAnimation(animation3);
                animation3.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        counter++;
                        if(counter < 5){
                            startAnimate(counter);
                        }
                        if(counter == 5){
                            all.setVisibility(View.GONE);
                            cover.setVisibility(View.VISIBLE);
                            Animation animation2 = AnimationUtils.loadAnimation(Challenge.this,R.anim.slide_out_left);
                            Animation animation3 = AnimationUtils.loadAnimation(Challenge.this,R.anim.slide_in_right);
                            main_image2.startAnimation(animation2);
                            image2.startAnimation(animation3);
                            score.setText(main_score + "");
                            key = databaseReference.child("Challenges").push().getKey();
                            String key2 = databaseReference.child("Receives").push().getKey();
                            HashMap<String,String> keys = new HashMap<>();
                            keys.put("key",key);
                            keys.put("asked",false + "");
                            databaseReference.child("Challenges").child(key).child(main_users.getText().toString()).child(user2.getText().toString()).child(title).setValue(collections);
                            databaseReference.child("Receives").child(key2).child(user2.getText().toString()).child(main_users.getText().toString()).child(title).setValue(numbers);
                            databaseReference.child("Receives").child(key2).child(user2.getText().toString()).child(main_users.getText().toString()).child(title).child("enemyKey").setValue(keys);
                        }
                        correct.setVisibility(View.GONE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public void setTestContent(int number){
        testQuestions test = QuestionsForQuiz.get(numbers.get(number));
        radioGroup.removeAllViews();
        String getThis = test.getQuestions();

        String into = "";
        int all = 0;
        int nothing = 0;
        questionTitle.setText(getThis);
        code.setText(test.getCode());
        for (int x = 0;x < test.getTestSize();x++){
            rdbtn = new RadioButton(this);
            rdbtn.setText(test.getAnswers().get(x));
            rdbtn.setButtonTintList(ColorStateList.valueOf(R.color.colorPrimaryDark));
            radioGroup.addView(rdbtn);
            rdbtn.setId(x);
            rdbtn.setPadding(60,30,60,30);
            rdbtn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            rdbtn.setTextSize(16);
            RadioGroup.LayoutParams buttonparam = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT,1.0f);
            rdbtn.setLayoutParams(buttonparam);
            rdbtn.setBackgroundResource(R.drawable.shadow);
        }

    }
    public void countIt(final int number){
        countDownTimer = new CountDownTimer(52000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                x--;
                time.setText(x+ "");
                if(x == -1){
                    x = 50;
                    time.setText("50");
                    goodAnimate(number);
                }
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();

    }
    public void randomIt(){
        numbers.clear();
        for(int x = 0;x < 5;x++){
            int y = (int)(Math.random() * QuestionsForQuiz.size());
            numbers.add(y);
        }
        for (int x = 0;x< numbers.size();x++){
            for (int y = x+1;y < numbers.size();y++){
                if(numbers.get(x) == numbers.get(y)){
                    randomIt();
                }
            }
        }
    }
}
