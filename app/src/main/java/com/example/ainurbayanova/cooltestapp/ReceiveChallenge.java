package com.example.ainurbayanova.cooltestapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ReceiveChallenge extends AppCompatActivity {
    Toolbar toolbar;
    CircleImageView main_user_image;
    int x = 20;
    TextView count1;
    TextView count2;
    RelativeLayout startBtn;
    String circle1;
    TextView countRounds;
    TextView countChecks;
    String circle2;
    Button btnStart;
    LinearLayout checkIt;
    String text1;
    String text2;
    LinearLayout counting;
    TextView questionTitle;
    String theme;
    CircleImageView second_user_image;
    TextView title;
    TextView count;
    RelativeLayout allShit;
    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton radioButton4;
    RelativeLayout switchRounds;
    RelativeLayout switchChecks;
    LinearLayout wholeTest;
    TextView textView1;
    ArrayList<Integer> enemyTest = new ArrayList<>();
    DatabaseReference databaseReference;
    ArrayList<testQuestions> allThings = new ArrayList<>();
    TextView textView2;
    TextView time;
    String circleImageView;
    CountDownTimer countDownTimer;
    int counter = 0;
    int score = 0;
    int secondScore = 0;
    String firstKey = "";
    String secondKey = "";
    TextView checkThis;
    TextView finish;
    boolean started = false;
    Boolean pressed;
    ArrayList<testQuestions> listOfTest = new ArrayList<>();
    ArrayList<Boolean> allChecks = new ArrayList<>();
    String main_key = "";
    String that_right = "";
    ArrayList<CollectionForReceive> collectioner = new ArrayList<>();
    ArrayList<CollectionForReceive> second_collectioner = new ArrayList<>();
    ArrayList<getSuperItemsInReceive> getSuperItemsInReceives = new ArrayList<>();
    RadioButton rdbtn;
    TextView code;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receivechallenge);

        finish = findViewById(R.id.finish);
        radioGroup = findViewById(R.id.radioGroups);
        radioButton1 = findViewById(R.id.challengeButton1);
        radioButton2 = findViewById(R.id.challengeButton2);
        radioButton3 = findViewById(R.id.challengeButton3);
        radioButton4 = findViewById(R.id.challengeButton4);
        code = findViewById(R.id.code);

        time = findViewById(R.id.time);

        radioButton1.setButtonTintList(ColorStateList.valueOf(R.color.colorPrimaryDark));

        radioButton2.setButtonTintList(ColorStateList.valueOf(R.color.colorPrimaryDark));

        radioButton3.setButtonTintList(ColorStateList.valueOf(R.color.colorPrimaryDark));

        radioButton4.setButtonTintList(ColorStateList.valueOf(R.color.colorPrimaryDark));

        checkIt = findViewById(R.id.checkIt);
        allShit = findViewById(R.id.allShit);
        switchChecks = findViewById(R.id.switchChecks);
        switchRounds = findViewById(R.id.switchRounds);
        wholeTest = findViewById(R.id.wholeTest);

        countRounds = findViewById(R.id.countRounds);
        countChecks = findViewById(R.id.checkThiss);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        count1 = findViewById(R.id.count1);
        count2 = findViewById(R.id.count2);

        title = findViewById(R.id.titleOfThemeInChallenge);
        counting = findViewById(R.id.counting);

        circleImageView = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();

        toolbar = findViewById(R.id.receiveToolbar);
        startBtn = findViewById(R.id.StartBtn);
        count = findViewById(R.id.main_count);

        btnStart = findViewById(R.id.btnStart);

        textView1 = findViewById(R.id.main_user_text);
        textView2 = findViewById(R.id.second_user_text);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        questionTitle = findViewById(R.id.questionTitle);

        main_user_image = findViewById(R.id.main_user_image);
        second_user_image = findViewById(R.id.second_user_image);
        if (bundle != null) {
            circle1 = bundle.getString("main_image");
            text1 = bundle.getString("main_user_text");
            circle2 = bundle.getString("second_image");
            text2 = bundle.getString("second_user_text");

            Glide.with(this).load(circle1).into(main_user_image);

            Glide.with(this).load(circle2).into(second_user_image);

            textView1.setText(text1);
            textView2.setText(text2);

            title.setText(bundle.getString("theme"));

            if (circle1 != null) {
                counting.setVisibility(View.GONE);
            }

            main_key = bundle.getString("key");
            if (bundle.getString("second_image2") != null) {
                startBtn.setVisibility(View.GONE);
                title.setText(bundle.getString("theme"));

                circle1 = bundle.getString("main_image2");
                text1 = bundle.getString("main_user_text2");
                circle2 = bundle.getString("second_image2");
                text2 = bundle.getString("second_user_text2");

                Glide.with(this).load(circleImageView).into(main_user_image);

                Glide.with(this).load(circle2).into(second_user_image);

                count.setText(bundle.getString("counts") + "");

                textView1.setText(text1);
                textView2.setText(text2);
            }
        }


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadTest();
                startAnimate(0);
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void uploadTest() {
        databaseReference.child("Test").child(title.getText().toString()).addValueEventListener(new ValueEventListener() {
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
                    listOfTest.add(test);

                }
                databaseReference.child("Receives").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        firstKey = "";
                        secondKey = "";
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (data.getKey().equals(main_key)) {
                                secondKey = data.getKey();
                                for (DataSnapshot datas : data.getChildren()) {
                                    for (DataSnapshot into : datas.getChildren()) {
                                        for (DataSnapshot into_key : into.getChildren()) {
                                            for (DataSnapshot dataSize : into_key.getChildren()) {
                                                if (!dataSize.getKey().equals("enemyKey")) {
                                                    enemyTest.add(Integer.parseInt(dataSize.getValue().toString()));
                                                } else if (dataSize.getKey().equals("enemyKey")) {
                                                    firstKey = dataSize.child("key").getValue().toString();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                databaseReference.child("Challenges").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (data.getKey().equals(firstKey)) {
                                for (DataSnapshot datas : data.getChildren()) {
                                    allChecks.clear();
                                    for (DataSnapshot into_data : datas.getChildren()) {
                                        collectioner.clear();
                                        for (DataSnapshot into : into_data.getChildren()) {
                                            for (DataSnapshot last : into.getChildren()) {
                                                if(!last.getKey().equals("main")){
                                                    if (last.child("wrongAnswers").getValue().toString().equals("")) {
                                                        allChecks.add(true);
                                                    } else if (!last.child("wrongAnswers").getValue().toString().equals("")) {
                                                        allChecks.add(false);
                                                    }
                                                    String correctAnswers = last.child("correctAnswers").getValue().toString();
                                                    String wrongAnswers = last.child("wrongAnswers").getValue().toString();
                                                    String questions = last.child("questions").getValue().toString();
                                                    String code = last.child("code").getValue().toString();
                                                    collectioner.add(new CollectionForReceive(correctAnswers, questions, wrongAnswers,code));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void startAnimate(final int y) {
        int counting = y + 1;
        allShit.setVisibility(View.GONE);
        switchRounds.setVisibility(View.VISIBLE);
        countRounds.setText("Round " + counting);
        switchChecks.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_to_top);
        countRounds.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation2 = AnimationUtils.loadAnimation(ReceiveChallenge.this, R.anim.slide_to_botto);
                countRounds.startAnimation(animation2);
                animation2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        switchRounds.setVisibility(View.GONE);
                        wholeTest.setVisibility(View.VISIBLE);
                        setTestContent(y);
                        countIt(y);
                        pressed = false;
                        checkIt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pressed = true;
                                goodAnimate(y);
                                x = 20;
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

    public void goodAnimate(int nymber) {
        wholeTest.setVisibility(View.GONE);
        switchChecks.setVisibility(View.VISIBLE);
        testQuestions test = listOfTest.get(enemyTest.get(nymber));
        that_right = test.getRights();
        Animation animation1 = AnimationUtils.loadAnimation(ReceiveChallenge.this, R.anim.slide_to_top);

        countChecks.startAnimation(animation1);

        if (allChecks.get(nymber)) {
            secondScore += 1;
        }
        String question = test.getQuestions();
        String code = test.getCode();
        count2.setText(secondScore + "");


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
            countChecks.setText("Right");
            countChecks.setTextColor(getResources().getColor(R.color.green2));
            score += 1;
            count1.setText(score+"");
            second_collectioner.add(new CollectionForReceive(that_right, question, "",code));
        }
        else if(value.equals("")){
            countChecks.setText("Wrong");
            countChecks.setTextColor(getResources().getColor(R.color.cards));
            second_collectioner.add(new CollectionForReceive(that_right, question, "Not Answered",code));
        }
        else{
            countChecks.setText("Wrong");
            countChecks.setTextColor(getResources().getColor(R.color.cards));
            second_collectioner.add(new CollectionForReceive(that_right, question, value,code));
        }

        radioGroup.clearCheck();
        countDownTimer.cancel();
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation3 = AnimationUtils.loadAnimation(ReceiveChallenge.this, R.anim.slide_to_botto);
                countChecks.startAnimation(animation3);
                animation3.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        counter++;
                        if (counter < 5) {

                            startAnimate(counter);
                        }
                        if (counter == 5) {
                            allShit.setVisibility(View.VISIBLE);
                            startBtn.setVisibility(View.GONE);
                            counting.setVisibility(View.VISIBLE);
                            Animation animation2 = AnimationUtils.loadAnimation(ReceiveChallenge.this, R.anim.slide_out_left);
                            Animation animation3 = AnimationUtils.loadAnimation(ReceiveChallenge.this, R.anim.slide_in_right);
                            main_user_image.startAnimation(animation2);
                            second_user_image.startAnimation(animation3);

                            count.setText(score + "");
                            finish.setText("Finished");
                            TextView textView = findViewById(R.id.second_count);

                            HashMap<String, String> scores = new HashMap<>();
                            scores.put("myScore", score + "");
                            scores.put("enemyScore", secondScore + "");
                            scores.put("enemyName", text2);
                            scores.put("enemyImage", circle2);
                            scores.put("title", title.getText().toString());

                            HashMap<String, String> scores2 = new HashMap<>();
                            scores2.put("myScore", secondScore + "");
                            scores2.put("enemyScore", score + "");
                            scores2.put("enemyName", text1);
                            scores2.put("enemyImage", circle1);
                            scores2.put("title", title.getText().toString());

                            textView.setText(secondScore + "");

                            String keys = databaseReference.child("Finished").push().getKey();
                            databaseReference.child("Finished").child(keys).child(text1).child(text2).child(title.getText().toString()).setValue(second_collectioner);
                            databaseReference.child("Finished").child(keys).child(text2).child(text1).child(title.getText().toString()).setValue(collectioner);

                            databaseReference.child("Finished").child(keys).child(text1).child(text2).child(title.getText().toString()).child("Detect").setValue(scores);
                            databaseReference.child("Finished").child(keys).child(text2).child(text1).child(title.getText().toString()).child("Detect").setValue(scores2);

                            databaseReference.child("Receives").child(secondKey).removeValue();
                            databaseReference.child("Challenges").child(firstKey).removeValue();

                            secondKey = "";
                            firstKey = "";

                        }
                        countChecks.setVisibility(View.GONE);
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
    public void setTestContent(int number) {
        testQuestions test = listOfTest.get(enemyTest.get(number));
        radioGroup.removeAllViews();
        questionTitle.setText(test.getQuestions());
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

    public void countIt(final int number) {
        countDownTimer = new CountDownTimer(22000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                x--;
                time.setText(x + "");
                if (x == 0) {
                    x = 20;
                    time.setText("20");
                    goodAnimate(number);
                }
            }
            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Do you wish to leave?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ReceiveChallenge.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }
}
