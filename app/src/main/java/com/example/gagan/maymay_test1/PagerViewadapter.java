package com.example.gagan.maymay_test1;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by GAGAN on 21-05-2018.
 */

class PagerViewadapter extends FragmentPagerAdapter{

    public PagerViewadapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                Homefragment homefragment = new Homefragment();
                return homefragment;
            case 1:
                Notificationfragment notificationfragment= new Notificationfragment();
                return notificationfragment;
            case 2:
                Createfragment createfragment= new Createfragment();
                return createfragment;
            case 3:
                Profiefragment profiefragment = new Profiefragment();
                return profiefragment;

            default: return null;
            }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
