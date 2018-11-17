package com.example.ainurbayanova.cooltestapp;

import android.view.View;

public interface ItemOnMeClickListener {
    void onMeClick(View view, int position);
    void onClickForFinished(View view, int position);
    void onClickForMySelf(View view,int position);
}
