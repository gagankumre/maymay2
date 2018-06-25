package com.example.gagan.maymay_test1;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

/**
 * Created by GAGAN on 26-05-2018.
 */

public class memepostid{

    @Exclude
    public String memepostid;

    public <T extends memepostid> T withId(@NonNull final String id){
        this.memepostid=id;
        return (T) this;
    }
}
