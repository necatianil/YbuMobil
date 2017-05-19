package com.example.asus.ybumobil;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 8.05.2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> FragmentList= new ArrayList<>();
    private final List<String> TitleList=new ArrayList<>();

    public void addFragment(Fragment fragment,String str){
        FragmentList.add(fragment);
        TitleList.add(str);
    }

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
       return TitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentList.get(position);
    }

    @Override
    public int getCount() {
        return FragmentList.size();
    }
}
