package de.exr.servicemanager;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public abstract class FragmentMain extends Fragment {

    protected Context context;
    protected ListView listView;

    protected List<Package> pkgList;

    protected PagerAdapterMain pagerAdapter;

    public FragmentMain() {
    }

    protected void init(PagerAdapterMain pagerAdapter) {
        this.pagerAdapter = pagerAdapter;
    }

    protected void init(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    protected void loadPackageList() {
        if (pkgList != null)
            return;
        pkgList = new ArrayList<>(200);
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> infoList = pm.getInstalledPackages(PackageManager.GET_SERVICES);
        for (PackageInfo pi : infoList) {
            ApplicationInfo ai = pi.applicationInfo;
            Package p =  new Package();
            p.label = pm.getApplicationLabel(ai).toString();
            p.packageName = ai.packageName;
            p.process = ai.processName;
            p.services = pi.services;
            pkgList.add(p);
        }
        Collections.sort(pkgList, new Comparator<Package>() {
            public int compare(Package lhs, Package rhs) {
                return lhs.label.compareTo(rhs.label);
            }
        });
    }

    protected void update() {
        listView.invalidate();
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

}
