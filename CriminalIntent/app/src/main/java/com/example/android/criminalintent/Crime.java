package com.example.android.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by RavenSP on 26/4/2017.
 */

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private String mSuspect;
    private boolean mSolved;

    public String getmSuspect() {
        return mSuspect;
    }

    public void setmSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
    }

    public Crime() {
        mId = UUID.randomUUID();
        mTitle = "";
        mDate = new Date();
    }

    public Crime(UUID id) {
        mId = id;
        mTitle = "";
        mDate = new Date();
    }

    public String getPhotoFileName(){
        return "IMG_" + getId().toString() + ".jpg";
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}