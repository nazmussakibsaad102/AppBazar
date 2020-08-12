package com.saad102.jobbazar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.saad102.jobbazar.PictureTab;
import com.saad102.jobbazar.ProfileTab;
import com.saad102.jobbazar.UsersTab;

class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ProfileTab();
            case 1:
                return new UsersTab();
            case 2:
                return new PictureTab();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return ("My Company");
            case 1:
                return ("Companies");
            case 2:
                return ("Post Job");
            default:
                return null;
        }
    }
}