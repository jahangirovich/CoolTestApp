package com.example.ainurbayanova.cooltestapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChallengeWithMyself extends AppCompatActivity {
    FrameLayout frameForRounChange;
    FrameLayout frameForCheckAnswer;
    TextView roundCount;
    TextView checkCount;
    LinearLayout allTests;
    TextView leftCount;
    TextView rightCount;
    TextView timeCount;
    TextView question;
    RadioGroup radioGroup;
    LinearLayout checkIt;
    Toolbar toolbar;
    LinearLayout onSave;
    LinearLayout onCancel;
    String title;
    DatabaseReference databaseReference;
    ArrayList<testQuestions> testInfo = new ArrayList<>();
    ArrayList<Integer> numbers = new ArrayList<>();
    ArrayList<String> questions = new ArrayList<>();
    ArrayList<String> right_answers = new ArrayList<>();
    ArrayList<String> wrong_answers = new ArrayList<>();
    ArrayList<String> coding = new ArrayList<>();
    Boolean buttonPressed = false;
    ArrayList<CollectAllForTes> collections = new ArrayList<>();
    CountDownTimer countDownTimer;
    String that_right = "";
    CircleImageView userImage;
    TextView endResult;
    TextView username;
    int x = 50;
    Boolean started = true;
    int counter = 0;
    int main_score = 0;
    String key = "";
    LinearLayout results;
    TextView showResult;
    RadioButton rdbtn;
    TextView code;
    boolean pressed = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challenge_with_myself);
        initializeBundle();
        initWidgets();
        uploadTest();
        initilizeToolbar();
        initializeAnimation(0);
    }

    private void initializeBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("title");
        }
    }

    private void uploadTest() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
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
                    testInfo.add(test);
                }
                randomIt();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void goodAnimate(int nymber){
        allTests.setVisibility(View.GONE);
        frameForCheckAnswer.setVisibility(View.VISIBLE);
        testQuestions test = testInfo.get(numbers.get(nymber));
        that_right = test.getRights();
        String question = test.getQuestions();
        String wrongAnswer = "";
        Animation animation1 = AnimationUtils.loadAnimation(ChallengeWithMyself.this,R.anim.slide_to_top);
        checkCount.startAnimation(animation1);
        CollectAllForTes collect = null;
        String code = test.getCode();
        right_answers.add(that_right);

        questions.add(question);
        coding.add(code);
        String value ="";
        if(radioGroup.getCheckedRadioButtonId() != -1){
            value =
                    ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId()))
                            .getText().toString();
        }
        if(radioGroup.getCheckedRadioButtonId() == -1){
            value = "";
        }

        if(value.equals(that_right)){
            checkCount.setText("Right");
            checkCount.setTextColor(getResources().getColor
                    (R.color.green2));
            main_score += 1;
            leftCount.setText(main_score+"");
            collect = new CollectAllForTes(question,that_right,"",false,code);
        }
        else if(value.equals("")){
            checkCount.setText("Wrong");
            checkCount.setTextColor(getResources().getColor(R.color.cards));
            collect = new CollectAllForTes(question,that_right,"Not Answered",false,code);
        }
        else{
            checkCount.setText("Wrong");
            checkCount.setTextColor(getResources().getColor(R.color.cards));
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
                Animation animation3 = AnimationUtils.loadAnimation(ChallengeWithMyself.this,R.anim.slide_to_botto);
                checkCount.startAnimation(animation3);
                animation3.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        counter++;
                        if(counter < 5){
                            initializeAnimation(counter);
                        }
                        if(counter == 5){
                            allTests.setVisibility(View.GONE);
                            frameForRounChange.setVisibility(View.GONE);
                            frameForCheckAnswer.setVisibility(View.GONE);
                            results.setVisibility(View.VISIBLE);
                            Glide.with(ChallengeWithMyself.this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(userImage);

                            username.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                            String key2 = databaseReference.child("Finished").push().getKey();
                            started = false;
                            showResult.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ChallengeWithMyself.this,DetailPageOfResults.class);
                                    intent.putExtra("enemy_image",FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
                                    intent.putExtra("enemy_name","Joha");
                                    intent.putExtra("enemy_score", "2");
                                    intent.putExtra("my_scoring",main_score + "");
                                    intent.putExtra("title",title);
                                    intent.putExtra("check","right");
                                    intent.putStringArrayListExtra("questions",questions);
                                    intent.putStringArrayListExtra("rightAnswers",right_answers);
                                    intent.putStringArrayListExtra("wrongAnswers",wrong_answers);
                                    intent.putStringArrayListExtra("code",coding);
                                    startActivity(intent);
                                }
                            });
                            onSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    HashMap<String,String> hashMap = new HashMap<>();
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                    DateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
                                    Calendar cal = Calendar.getInstance();
                                    String date = dateFormat.format(cal.getTime()) + " and " + dateFormat2.format(cal.getTime()) + " o'clock";
                                    hashMap.put("date",date);
                                    hashMap.put("main_score",main_score + "");
                                    String key = databaseReference.child("MyFinished").push().getKey();
                                    databaseReference.child("MyFinished").child(key).child(username.getText().toString()).child(title).setValue(collections);
                                    databaseReference.child("MyFinished").child(key).child(username.getText().toString()).child(title).child("date").setValue(hashMap);
                                    finish();
                                }
                            });
                            onCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                        }
                        checkCount.setVisibility(View.GONE);
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
    public void randomIt() {
        numbers.clear();
        for (int x = 0; x < 5; x++) {
            int y = (int) (Math.random() * testInfo.size());
            numbers.add(y);
        }
        for (int x = 0; x < numbers.size(); x++) {
            for (int y = x + 1; y < numbers.size(); y++) {
                if (numbers.get(x) == numbers.get(y)) {
                    randomIt();
                }
            }
        }
    }

    private void initializeAnimation(final int y) {
        Animation animation = AnimationUtils.loadAnimation(ChallengeWithMyself.this, R.anim.slide_to_top);
        roundCount.startAnimation(animation);
        frameForCheckAnswer.setVisibility(View.GONE);
        frameForRounChange.setVisibility(View.VISIBLE);
        allTests.setVisibility(View.GONE);
        int counting = y + 1;
        roundCount.setText("Round " + counting);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation = AnimationUtils.loadAnimation(ChallengeWithMyself.this, R.anim.slide_to_botto);
                roundCount.setAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        frameForRounChange.setVisibility(View.GONE);
                        allTests.setVisibility(View.VISIBLE);
                        countIt(y);
                        setTestContent(y);
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

    public void countIt(final int number){
        countDownTimer = new CountDownTimer(52000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                x--;
                timeCount.setText(x+ "");
                if(x == -1){
                    x = 50;
                    timeCount.setText("50");
                    goodAnimate(number);
                }
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
    }

    @SuppressLint("ResourceAsColor")
    public void setTestContent(int number){
        testQuestions test = testInfo.get(numbers.get(number));
        radioGroup.removeAllViews();
        code.setText(test.getCode());
        question.setText(test.getQuestions());
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

    private void initilizeToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(started){
                    new AlertDialog.Builder(ChallengeWithMyself.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage("Do you wish to leave?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ChallengeWithMyself.this.finish();
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
        getSupportActionBar().setTitle("Challenge");
    }

    private void initWidgets() {
        frameForRounChange = findViewById(R.id.frame);
        frameForCheckAnswer = findViewById(R.id.chechAnswers);
        roundCount = findViewById(R.id.round);
        checkCount = findViewById(R.id.correct);
        allTests = findViewById(R.id.testS);
        leftCount = findViewById(R.id.count1);
        timeCount = findViewById(R.id.time);
        question = findViewById(R.id.questionTitle);
        radioGroup = findViewById(R.id.radioGroups);
        results = findViewById(R.id.results);

        toolbar = findViewById(R.id.toolbar);
        checkIt = (LinearLayout) findViewById(R.id.checkIt);
        username = findViewById(R.id.username);
        endResult = findViewById(R.id.endRightAnswer);
        userImage = findViewById(R.id.userImage);
        showResult = findViewById(R.id.showResult);
        onSave = findViewById(R.id.onSave);
        onCancel = findViewById(R.id.onCancel);
        code = findViewById(R.id.code);
    }
}
