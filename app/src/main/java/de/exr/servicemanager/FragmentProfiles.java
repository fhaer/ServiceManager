package de.exr.servicemanager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by felix on 28.06.15.
 */
public class FragmentProfiles extends FragmentMain {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profiles, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.listView);
        init(inflater.getContext(), lv);
        listView.setAdapter(getArrayAdapter());
        return rootView;
    }

    protected ArrayAdapter getArrayAdapter() {
        loadPackageList();
        List<Profile> profileList = getProfileList();
        return new ArrayAdapterProfiles(context, profileList, pkgList, pagerAdapter);
    }

    public List<Profile> getProfileList() {
        List<Profile> profileList = new ArrayList<>();

        Profile p1 = new Profile("Google");
        p1.addDisabledPackage("com.android.vending");
        p1.addDisabledPackage("com.google.android.g*"); // gms, gsf
        p1.addDisabledPackage("com.google.android.partnersetup");
        p1.addDisabledPackage("com.google.android.one*"); //onetimeinit

        profileList.add(p1);
        return profileList;
    }

}
