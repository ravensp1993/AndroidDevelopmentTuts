package com.example.android.criminalintent;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.UUID;

/**
 * Created by RavenSP on 4/8/2017.
 */

public class ThumbnailViewer extends android.support.v4.app.DialogFragment{
    private static final String ARG_CRIME_ID = "crime_id";
    Crime mCrime;
    File mPhotoFile;
    private ImageView thumbnail;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID CrimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(CrimeId);
        //get FileProvider for this specific crime
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
    }

    public static ThumbnailViewer newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        ThumbnailViewer fragment = new ThumbnailViewer();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.thumbnail_viewer_fragment, container, false);

        thumbnail = (ImageView) v.findViewById(R.id.thumbnail_image);
        Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());

        thumbnail.setImageBitmap(bitmap);

        return v;
    }
}
