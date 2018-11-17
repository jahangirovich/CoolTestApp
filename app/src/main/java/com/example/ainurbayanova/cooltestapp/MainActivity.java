package com.example.ainurbayanova.cooltestapp;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import android.content.Context;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int[] tabIcons = {
            R.drawable.ic_school_black_24dp,
            R.drawable.ic_flash_on_black_24dp,
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_code_black_24dp,
            R.drawable.ic_mode_comment_black_24dp
    };
    ArrayList<String> users;
    TabLayout tabLayout;

    FirebaseUser auth;
    private boolean bool = true;
    String user;
    Toolbar toolbar;
    String username;
    FirebaseUser userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final ViewPager viewPager = findViewById(R.id.viewPager);

        setTheme(R.style.AppTheme2);
        setupViewPager(viewPager);

        FirebaseApp.initializeApp(this);

        tabLayout = findViewById(R.id.tabLayout);

        tabLayout.setupWithViewPager(viewPager);

        userAuth = FirebaseAuth.getInstance().getCurrentUser();

        if(userAuth != null){
            user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            if(user == null){
                Intent intent = new Intent(MainActivity.this,LoginPage.class);
                startActivity(intent);
                finish();
            }
        }



        SetupToolbar();

        setupTabIcons();

        if(isNetworkAvailable()){

        }
        else{
            Snackbar.make(tabLayout,"No internet Connection",Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void SetupToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupViewPager(ViewPager viewPager) {

        SimplePagerAdapter adapter = new SimplePagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FirstFragment(), "ONE");

        adapter.addFragment(new SecondFragment(), "TWO");

        adapter.addFragment(new ThirdFragment(), "THREE");

        adapter.addFragment(new FourthFragment(), "THREE");

        adapter.addFragment(new FifthFragment(), "THREE");

        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(0);
    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        auth = FirebaseAuth.getInstance().getCurrentUser();
        switch(item.getItemId())
        {
            case R.id.menu:
                if(auth == null){
                    Intent intent = new Intent(this,LoginPage.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(this,Profile.class);
                    startActivity(intent);
                }
                break;
            case R.id.addLibrary:
                Intent intent = new Intent(this,TestAdd.class);
                startActivity(intent);
        }
        return true;
    }


    class SimplePagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SimplePagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @SuppressLint("RestrictedApi")
        @Override
        public Fragment getItem(int position) {

            Fragment men = mFragmentList.get(position);

            return men;
        }

        @Override
        public int getCount() {
            return 5;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);

        }

        @Override
        public CharSequence getPageTitle(int position) {

            return  null;
        }
    }

}

