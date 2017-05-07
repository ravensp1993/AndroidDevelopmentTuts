package com.example.android.signinactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.concurrent.Executor;

/**
 * Created by RavenSP on 7/5/2017.
 */


public class LoginFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "LoginFragment";
    private GoogleSignInManager GSIM;
    private Button googleLoginButton;
    private ArrayList<String> logInData;
    //googlesignin variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    protected AppCompatActivity mActivity;

    private static final int GOOGLE_SIGN_IN = 9099;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false); // inflate new layout for fragment

        //initialise
        googleLoginButton = (Button) view.findViewById(R.id.google_login_button);
        googleLoginButton.setOnClickListener(this);
        logInData = new ArrayList<String>();
        //instantiate GoogleSignInManager for GSO and googleAPI
        initGoogleLogInService();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_login_button:
                signInGoogle();
                break;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.w(TAG, "On Start of this fragment was called ");
        //onstart check if google account has already been signed in before
        mAuth.addAuthStateListener(mAuthListener);
    }

    //INIT GOOGLE SERVICES
    private void initGoogleLogInService() {
        GSIM = GoogleSignInManager.get(mActivity);

        GSIM.setmGoogleApiClient(new GoogleApiClient.Builder(getActivity()).enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, GSIM.getGso()).build());

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.w(TAG, "OnAuthStateChange Detected ");
                if (firebaseAuth.getCurrentUser() != null) {
                    setLogInData(firebaseAuth.getCurrentUser());
                    FragmentManager fm = mActivity.getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.fragment_container, ProfileFragment.newInstance(logInData)).commit();
                }
            }
        };
    }
    //create intent to start Google API authentication, Result will be returned in onActivityResult.
    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(GSIM.getmGoogleApiClient());
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }
    //retrieve data from account
    public void setLogInData(FirebaseUser user){
        Log.i(TAG, "--------------------------------");
        Log.i(TAG, "Display Name: " + user.getDisplayName());
        Log.i(TAG, "Email: " + user.getEmail());
        Log.i(TAG, "PhotoURL: " + user.getPhotoUrl());

        logInData.add(user.getPhotoUrl().toString());
        logInData.add(user.getDisplayName());
        logInData.add(user.getEmail());


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(mActivity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    //END OF GOOGLE SERVICES
}
