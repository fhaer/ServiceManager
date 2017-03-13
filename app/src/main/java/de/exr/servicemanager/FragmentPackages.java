package de.exr.servicemanager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by felix on 28.06.15.
 */
public class FragmentPackages extends FragmentMain {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_packages, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.listView);
        init(inflater.getContext(), lv);
        listView.setAdapter(getArrayAdapter());
        return rootView;
    }

    protected ArrayAdapter getArrayAdapter() {
        loadPackageList();
        return new ArrayAdapterPackages(context, pkgList, pagerAdapter);
    }
}
