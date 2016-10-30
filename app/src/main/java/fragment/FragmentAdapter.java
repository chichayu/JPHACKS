package fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import fragment.FragmentAll;
import fragment.FragmentStatus;
import fragment.FragmentToday;

/**
 * Created by xixi on 10/29/16.
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            default:
                return new FragmentToday();
            case 0:
                return new FragmentAll();
            case 1:
                return new FragmentStatus();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}
