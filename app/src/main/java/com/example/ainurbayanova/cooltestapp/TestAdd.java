package com.example.ainurbayanova.cooltestapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class TestAdd extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager second;
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_add);

        second = findViewById(R.id.secondViewPager);
        tabLayout = findViewById(R.id.secondTab);
        toolbar = findViewById(R.id.mainToolbar);

        setupViewPager(second);
        tabLayout.setupWithViewPager(second);

        setSupportActionBar(toolbar);

    }
    private void setupViewPager(ViewPager viewPager) {
        SimplePagerAdapters adapter = new SimplePagerAdapters(getSupportFragmentManager());

        adapter.addFragment(new BlankFragment(), "Buckets");

        adapter.addFragment(new BlankFragment(), "Room Flowers");

        adapter.addFragment(new BlankFragment(), "Flowers of garden");

        adapter.addFragment(new BlankFragment(), "About Us");

        adapter.addFragment(new BlankFragment(), "Settings");

        viewPager.setAdapter(adapter);
    }
    class SimplePagerAdapters extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SimplePagerAdapters(FragmentManager manager) {
            super(manager);
        }

        @SuppressLint("RestrictedApi")
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return 5;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
