package com.example.ainurbayanova.cooltestapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {
    String auth;
    DatabaseReference databaseReference;
    private int[] tabIcons = {
            R.drawable.ic_beenhere_black_24dp,
            R.drawable.ic_storage_black_24dp,
            R.drawable.ic_group_black_24dp
    };
    FirebaseUser username;
    ViewPager viewPager;
    TabLayout tabLayout;
    private ArrayList<String> images;
    private ArrayList<String> user_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        images = new ArrayList<>();
        user_title = new ArrayList<>();
        setContentView(R.layout.profile);

        Toolbar toolbar = findViewById(R.id.toolbars);

        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setTitle(auth);

        viewPager = findViewById(R.id.viewProfilePager);

        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.profileTabLayout);

        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

    }
    private void setupViewPager(ViewPager viewPager) {
        Profile.SimplePageFragmentAdapter adapter = new SimplePageFragmentAdapter(getSupportFragmentManager());

        adapter.addFragment(new tutorialFragment(), "ONE");

        adapter.addFragment(new achievements(), "TWO");

        adapter.addFragment(new UsersFragment(), "Three");

        viewPager.setAdapter(adapter);
    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        username = FirebaseAuth.getInstance().getCurrentUser();
        user_title.clear();
        images.clear();
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Profile.this,LoginPage.class);
                startActivity(intent);
                startIt();
                break;
            case R.id.personAdd:
                Intent intent1 = new Intent(Profile.this,PersonAdd.class);

                startActivity(intent1);
        }
        return super.onOptionsItemSelected(item);
    }
    public void startIt(){
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            finish();
        }
    }
    public class SimplePageFragmentAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        public SimplePageFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String one) {
            mFragmentList.add(fragment);
        }
    }
}
