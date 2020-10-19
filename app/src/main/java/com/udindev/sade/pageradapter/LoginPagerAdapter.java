package com.udindev.sade.pageradapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.udindev.sade.R;
import com.udindev.sade.fragment.LoginFragment;
import com.udindev.sade.fragment.RegisterFragment;

public class LoginPagerAdapter extends FragmentPagerAdapter {
    private final Context context;

    public LoginPagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }
    
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new LoginFragment();
                break;

            case 1:
                fragment = new RegisterFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.tab_title_login,
            R.string.tab_title_register
    };

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(TAB_TITLES[position]);
    }
}
