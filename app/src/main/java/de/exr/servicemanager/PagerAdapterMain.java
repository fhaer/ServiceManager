package de.exr.servicemanager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class PagerAdapterMain extends FragmentPagerAdapter {

    public PagerAdapterMain(FragmentManager fm) {
        super(fm);
    }


    private FragmentMain fragment0 = null;
    private FragmentMain fragment1 = null;

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                fragment0 = new FragmentProfiles();
                fragment0.init(this);
                return fragment0;
            case 1:
                fragment1 = new FragmentPackages();
                fragment1.init(this);
                return fragment1;
        }
        throw new IllegalArgumentException("fragment for position not found: " + position);
    }

    protected void updateFragment(int position) {
        switch (position) {
            case 0:
                if (fragment0 != null)
                    fragment0.update();
                return;
            case 1:
                if (fragment1 != null)
                    fragment1.update();
                return;
        }
        throw new IllegalArgumentException("fragment for position not found: " + position);
    }

    protected void updateFragments() {
        if (fragment0 != null)
            fragment0.update();
        if (fragment1 != null)
            fragment1.update();
    }

    @Override
    public int getCount() {
        // total pages
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "Profiles".toUpperCase(l);
            case 1:
                return "Packages".toUpperCase(l);
        }
        return null;
    }
}
