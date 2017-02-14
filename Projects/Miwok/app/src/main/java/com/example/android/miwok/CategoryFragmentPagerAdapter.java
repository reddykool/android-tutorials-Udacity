package com.example.android.miwok;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Reddyz on 19-01-2017.
 */

public class CategoryFragmentPagerAdapter extends FragmentPagerAdapter {

    private String []mCategories;
    private static int mNumberOfFragments = 4;

    public CategoryFragmentPagerAdapter(FragmentManager fm, Context applicationContext) {
        super(fm);
        mCategories = applicationContext.getResources().getStringArray(R.array.categories);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NumbersFragment();
            case 1:
                return new FamilyFragment();
            case 2:
                return new ColorsFragment();
            case 3:
                return new PhrasesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mCategories.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCategories[position];
    }
}
