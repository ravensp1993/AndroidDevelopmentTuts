package com.example.android.signinactivity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by RavenSP on 7/5/2017.
 */

public class GoogleSignInManager {
    private static GoogleSignInManager sGoogleSignInManager;
    private Context context;
    private static GoogleSignInOptions gso;
    private static GoogleApiClient mGoogleApiClient;

    public static GoogleSignInManager get(Context context){
        if (sGoogleSignInManager == null){
            sGoogleSignInManager = new GoogleSignInManager(context);
        }
        return sGoogleSignInManager;
    }

    private GoogleSignInManager(Context context){
        this.context = context;
        initGoogleSignInManager();
    }

    private void initGoogleSignInManager(){
        if(getmGoogleApiClient() == null) {
            setGso(new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build());
        }
    }

    public GoogleSignInOptions getGso() {
        return gso;
    }

    public static void setGso(GoogleSignInOptions gso) {
        GoogleSignInManager.gso = gso;
    }

    public static void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        GoogleSignInManager.mGoogleApiClient = mGoogleApiClient;
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }
}
