package training.com.adapter;

import android.app.Fragment;
import android.support.v13.app.FragmentStatePagerAdapter;

import training.com.fragment.ContactFragment;
import training.com.fragment.RecentChatFragment;


/**
 * Created by hawk on 28/01/2016.
 */
public class HomePageAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public HomePageAdapter(android.app.FragmentManager fm, int mNumOfTabs) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new RecentChatFragment();
            case 1:
                return new ContactFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
