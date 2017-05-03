package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fragment.NewReleaseFragment;
import fragment.TopRated;
import fragment.UpComing;

/**
 * Created by shitul on 5/2/17.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    String[] tabsTitle = {"New Release", "Top Rates", "Up Coming"};

    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:

                return new NewReleaseFragment();
            case 1:

                return new TopRated();
            case 2:

                return new UpComing();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabsTitle[position];
    }
}
