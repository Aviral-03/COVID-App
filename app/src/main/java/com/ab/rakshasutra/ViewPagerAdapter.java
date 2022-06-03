package com.ab.rakshasutra;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> Fragment = new ArrayList<>();
    private final List<String> Titles = new ArrayList<>();

    public ViewPagerAdapter (FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment.get(position);
    }

    @Override
    public int getCount() {
        return Titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles.get(position);
    }

    public void AddFragment (Fragment fragment,String title) {

        Fragment.add(fragment);
        Titles.add(title);
    }

}
