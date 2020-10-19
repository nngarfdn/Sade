package com.udindev.sade.pageradapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.udindev.sade.fragment.DashboardFragment;
import com.udindev.sade.fragment.FavoriteFragment;
import com.udindev.sade.fragment.ProfileFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }
    
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new DashboardFragment();
                break;

            case 1:
                fragment = new FavoriteFragment();
                break;

            case 2:
                fragment = new ProfileFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
