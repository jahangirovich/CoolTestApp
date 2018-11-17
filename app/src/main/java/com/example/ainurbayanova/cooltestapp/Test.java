package com.example.ainurbayanova.cooltestapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Test extends AppCompatActivity {
    ArrayList<Question> questionArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            ArrayList<String> all = bundle.getStringArrayList("answers");
            String question = bundle.getString("question");
            String correct = bundle.getString("correct");




        }
    }
}
