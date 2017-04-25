package com.example.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by RavenSP on 26/4/2017.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
