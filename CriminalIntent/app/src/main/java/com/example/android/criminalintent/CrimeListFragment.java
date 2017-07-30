package com.example.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by RavenSP on 26/4/2017.
 */

//Layout must contain a <android.support.v7.wudget.RecyclerView /> view.
//ViewHolder holds on to Views
//RecyclerView Talks to the Adapter to retrieve ViewHolders which holds the views.
//CrimeAdapter interacts with the model data

/*  1) Recycler View asks how many objects are in the list by calling the Adapter's getItemCount
    2) RecyclerView Adapter call's the adapter OnCreateViewHolder method to create a ViewHolder.
    3) Finally, RecyclerView calls onBindViewHolder. The RecyclerView will pass in a ViewHolder into this method along with its position,
    Adapter will then look up the model data for that position and call bind() to fill up the View's Widgets with data.
    4) Once enough ViewHolder's are created, Only OnbindViewHolder will be created repeatedly to fill in new data for each view.
*/

//Futher Implementations notifyDataSetChanged(); - real time
public class CrimeListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private int posId;

    //when activity is reloaded from the backstack, we want to reupdateUI for any changes
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Log.d("CRIMEFRAG","" + crime.getId());
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                //return true to indicate that the menuitem call has been handled and that no further processing is necessary
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.crime_list_fragment, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //use savedinstancestate in oncreateview for fragments
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.crime_list_fragment, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        //RecyclerView requires a LayoutManager to work, The LayoutManager positions every item and defines how scrolling works.
        //LinearLayoutManger, gridLayoutManager.
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();
        return view;
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyItemChanged(posId);
        }
        //when deleting things from recyclerView add this 2 lines
        mCrimeRecyclerView.getRecycledViewPool().clear();
        mAdapter.notifyDataSetChanged();

        updateSubtitle();
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;
        private ImageView mCrimeSolved;

        //constructor to inflate View in viewholder, wiring of widgets in view
        public CrimeHolder(LayoutInflater inflater,
                           ViewGroup parent) {
            super(inflater.inflate(R.layout.crime_list_item, parent, false));
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mCrimeSolved = (ImageView) itemView.findViewById(R.id.crime_solved);

            itemView.setOnClickListener(this);
        }

        //setting model data into ViewHolder's view, Called by Adapter.
        public void bind(Crime crime, int viewType) {
            switch (viewType) {
                case 0:
                    mCrime = crime;
                    mTitleTextView.setText(mCrime.getTitle());
                    mDateTextView.setText(mCrime.getDate().toString());
                    mCrimeSolved.setVisibility(mCrime.isSolved() ? View.VISIBLE : View.GONE);
                    break;
                case 1:
                    mCrime = crime;
                    mTitleTextView.setText("TEST ROW");
                    mDateTextView.setText(mCrime.getDate().toString());
                    mCrimeSolved.setVisibility(mCrime.isSolved() ? View.VISIBLE : View.GONE);
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            posId = this.getAdapterPosition();
            startActivity(CrimePagerActivity.newIntent(getActivity(), mCrime.getId()));
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        //we can add logic to return different type of viewholders
        //override getitemviewtype that returns a logic
        //use case statement to return different holders for each viewtype
        @Override
        public int getItemViewType(int position) {
            return position % 1;
            //return position % 2;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime, holder.getItemViewType());
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();

        String subtitle = getString(R.string.subtitle_format, crimeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }
}
