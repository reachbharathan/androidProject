package com.example.counter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


public class ViewPager extends Fragment {

    String counterName;
    int countValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  rootView =  inflater.inflate(R.layout.fragment_view_pager, container, false);

        final Boolean isUpdate = getCounterData();
        final Bundle bundle = new Bundle();


        androidx.viewpager.widget.ViewPager viewPager = rootView.findViewById(R.id.pager);

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {

            private String tabTitles[] = new String[] { "COUNTER", "TAGS" };

            @Override
            public Fragment getItem(int position) {

                switch (position)
                {
                    case 1:

                        TagListing tagListing = new TagListing();

                        if (isUpdate) {
                            bundle.putString("CounterName", counterName);
                            bundle.putInt("CounterValue", countValue);
                            tagListing.setArguments(bundle);
                        }

                        return tagListing;

                    case 0:

                        MainCounter mainCounter = new MainCounter();

                        if (isUpdate) {

                            bundle.putString("CounterName", counterName);
                            bundle.putInt("CounterValue", countValue);
                            mainCounter.setArguments(bundle);

                        }

                        return mainCounter;

                    default:
                        return new MainCounter();

                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitles[position];
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);

        TabLayout tabLayout = rootView.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }

    private Boolean getCounterData() {


        Bundle extras = this.getArguments();

        if (extras != null) {

            counterName = extras.getString("CounterName", "");
            countValue = extras.getInt("CounterValue", 0);
            return true;

        } else {

            return false;

        }


    }

}
