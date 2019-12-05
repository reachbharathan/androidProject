package com.example.udacitybasics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabWidget;

import com.google.android.material.tabs.TabLayout;

public class Miwok extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miwok);

        ViewPager viewPager = findViewById(R.id.pager);

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            private String tabTitles[] = new String[] { "COLORS", "NUMBERS", "FAMILY" , "PHRASES" };

            @Override
            public Fragment getItem(int position) {

                switch (position)
                {
                    case 1:
                        return new MiwokNumber();

                    case 3:
                        return new MiwokPhrase();

                    case 2:
                        return new MiwokFamily();

                    case 0:
                        return new MiwokColor();

                    default:
                        return new MiwokNumber();

                }
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitles[position];
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

}
