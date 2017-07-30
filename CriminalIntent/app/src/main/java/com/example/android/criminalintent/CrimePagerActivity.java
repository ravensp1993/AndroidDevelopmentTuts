package com.example.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

/**
 * Created by ravensp on 7/24/2017
 */

public class CrimePagerActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_CRIME_ID = "com.example.android.criminalintent.crime_id";
    private static final String TAG = "criminalintent";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private Button toFirst, toLast;

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);

        toFirst = (Button) findViewById(R.id.toFirst);
        toLast = (Button) findViewById(R.id.toLast);

        toFirst.setOnClickListener(this);
        toLast.setOnClickListener(this);


        mCrimes = CrimeLab.get(this).getCrimes();
        //To tweak the amount of neighboring page to be loaded in each direction, we can use setOffscreenPageLimit(int)

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "OPS+" + position);
                if (position == 0) {
                    toFirst.setVisibility(View.GONE);
                    toLast.setVisibility(View.VISIBLE);
                } else if (position == mCrimes.size() - 1) {
                    toFirst.setVisibility(View.VISIBLE);
                    toLast.setVisibility(View.GONE);
                } else {
                    toFirst.setVisibility(View.VISIBLE);
                    toLast.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            //gets fragment with information loaded with crime #position.
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        Log.d(TAG, "" + mViewPager.getCurrentItem());
        if (mViewPager.getCurrentItem() == 0) {
            toFirst.setVisibility(View.GONE);
        }
        if (mViewPager.getCurrentItem() == mCrimes.size() - 1) {
            toLast.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toFirst:
                mViewPager.setCurrentItem(0);
                toFirst.setVisibility(View.GONE);
                break;
            case R.id.toLast:
                mViewPager.setCurrentItem(mCrimes.size() - 1);
                toLast.setVisibility(View.GONE);
                break;

        }
    }

}
