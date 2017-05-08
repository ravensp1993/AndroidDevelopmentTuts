package com.example.android.signinactivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by RavenSP on 7/5/2017.
 */

public class ProfileFragment extends Fragment implements View.OnClickListener {


    private static final String ARG_CONTENT_ID = "log_in_data";
    private static final String TAG = "ProfileFragment";
    //UI Object Declaration
    private TextView displayNameTV;
    private TextView nameTV;
    private TextView emailTV;
    private ImageView profilePictureIV;

    private ArrayList<String> mLogInData;
    private Button logout;

    private static SignInManager SIM;

    //creation of bundle, put arguments in it and set it when creating fragment for return.
    public static ProfileFragment newInstance(ArrayList<String> details) {
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_CONTENT_ID, details);

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieve data from bundle
        mLogInData = getArguments().getStringArrayList(ARG_CONTENT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        String displayName = mLogInData.get(1);
        String email = mLogInData.get(2);

        logout = (Button) view.findViewById(R.id.log_out_button);
        logout.setOnClickListener(this);

        profilePictureIV = (ImageView) view.findViewById(R.id.user_profile_photo);
        Glide.with(getActivity()).load(mLogInData.get(0)).into(profilePictureIV);

        displayNameTV = (TextView) view.findViewById(R.id.user_profile_name);
        displayNameTV.setText(displayName);

        nameTV = (TextView) view.findViewById(R.id.user_name);
        nameTV.setText(displayName);

        emailTV = (TextView) view.findViewById(R.id.user_email);
        emailTV.setText(email);

        SIM = SignInManager.get(getActivity());
        return view;
    }

    @Override
    public void onClick(View v) {
        signOut();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
    }

    private void signOut() {
        Log.w(TAG,"" + "User Sign out!");
        //if facebook login is permitted
        for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if(user.getProviderId().equals("facebook.com")){
                LoginManager.getInstance().logOut();
            }
        }

        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(SIM.getmGoogleApiClient()).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
        revokeAccess();
        SIM.getmGoogleApiClient().stopAutoManage(getActivity());
        SIM.getmGoogleApiClient().disconnect();
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(SIM.getmGoogleApiClient()).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
    }

    // end of google methods


}
