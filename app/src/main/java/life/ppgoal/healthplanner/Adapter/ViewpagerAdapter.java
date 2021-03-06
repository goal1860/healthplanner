package life.ppgoal.healthplanner.Adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import life.ppgoal.healthplanner.Fragment.DetailFragment;

public class ViewpagerAdapter extends FragmentPagerAdapter {

    private int Favourite_NumOfTabs;
    private int position;
    public Activity activity;
    public String type;

    public ViewpagerAdapter(FragmentManager fm, int favourite_NumOfTabs, Activity activity, int position, String type) {
        super(fm);
        Favourite_NumOfTabs = favourite_NumOfTabs;
        this.position = position;
        this.type = type;
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("type", type);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }


    @Override
    public int getCount() {
        return Favourite_NumOfTabs;
    }

}
