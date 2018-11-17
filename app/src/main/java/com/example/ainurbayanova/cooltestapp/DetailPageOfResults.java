package com.example.ainurbayanova.cooltestapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailPageOfResults extends AppCompatActivity {
    CarouselView carouselView;
    CircleImageView main_image;
    CircleImageView second_image;
    TextView main_user;
    TextView second_user;
    TextView main_count;
    TextView second_count;
    TextView title;
    Toolbar toolbar;
    TextView rightAnswer;
    TextView wrongAnswer;
    TextView question;
    TextView setCheck;
    TextView checkOut;
    ImageView checkRight;
    ImageView checkFalse;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page_of_results);
        toolbar = findViewById(R.id.toolbarForDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Result");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initWidgets();
        final Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(main_image);
            Glide.with(this).load(bundle.getString("enemy_image")).into(second_image);
            main_user.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            second_user.setText(bundle.getString("enemy_name"));
            main_count.setText(bundle.getString("my_score"));
            second_count.setText(bundle.getString("enemy_score"));
            title.setText(bundle.getString("title"));
            setCheck.setText(bundle.getString("check"));
            if(bundle.getString("check").equals("Winned")){
                setCheck.setBackgroundResource(R.color.green3);
            }
            else if(bundle.getString("check").equals("Losed")){
                setCheck.setBackgroundResource(R.color.cards);
            }
            else{
                setCheck.setBackgroundResource(R.color.colorPrimaryDark);
            }
            if(bundle.getString("my_scoring") != null){
                linearLayout = findViewById(R.id.rightSide);
                linearLayout.setVisibility(View.GONE);
                TextView textView = findViewById(R.id.detailOf);
                textView.setVisibility(View.VISIBLE);
                main_count.setText(bundle.getString("my_scoring") + "");
                setCheck.setVisibility(View.GONE);
                second_user.setVisibility(View.GONE);
                second_count.setVisibility(View.GONE);
            }
            carouselView.setPageCount(5);
            carouselView.setViewListener(new ViewListener() {
                @Override
                public View setViewForPosition(int position) {
                    String question  = bundle.getStringArrayList("questions").get(position);
                    String right  = bundle.getStringArrayList("rightAnswers").get(position);
                    String wrong  = bundle.getStringArrayList("wrongAnswers").get(position);
                    String coding  = bundle.getStringArrayList("code").get(position);
                    View customView = getLayoutInflater().inflate(R.layout.custom_detail, null);
                    TextView questions = customView.findViewById(R.id.Question);
                    TextView rights = customView.findViewById(R.id.rightAnswer);
                    TextView wrongs = customView.findViewById(R.id.wrongAnswer);
                    TextView code = customView.findViewById(R.id.code);
                    checkRight =customView.findViewById(R.id.checkRight);
                    checkFalse =customView.findViewById(R.id.checkFalse);
                    checkOut = customView.findViewById(R.id.checkOut);

                    if (wrong.equals("n")){
                        checkFalse.setVisibility(View.GONE);
                        checkOut.setText("Right");
                        checkOut.setTextColor(getResources().getColor(R.color.green3));
                        wrongs.setText(right);
                    }
                    else if(wrong.equals("nNot Answered")){
                        checkRight.setVisibility(View.GONE);
                        checkOut.setText("Wrong");
                        checkOut.setTextColor(getResources().getColor(R.color.cards));
                        wrongs.setText("Not answered");
                    }
                    else{
                        checkRight.setVisibility(View.GONE);
                        checkOut.setText("Wrong");
                        checkOut.setTextColor(getResources().getColor(R.color.cards));
                        String sub = wrong.substring(1,wrong.length());
                        wrongs.setText(sub);
                    }
                    questions.setText(question);
                    rights.setText(right);
                    code.setText(coding);
                    return customView;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initWidgets() {
        carouselView = findViewById(R.id.carouselView);
        main_image = findViewById(R.id.mainImage);
        second_image = findViewById(R.id.secondImage);
        main_user = findViewById(R.id.mainUsername);
        second_user = findViewById(R.id.secondUsername);
        main_count = findViewById(R.id.mainCount);
        second_count = findViewById(R.id.secondCount);
        title = findViewById(R.id.setTitle);
        question = findViewById(R.id.Question);
        rightAnswer = findViewById(R.id.rightAnswer);
        wrongAnswer = findViewById(R.id.wrongAnswer);
        setCheck = findViewById(R.id.setCheck);
    }
}
