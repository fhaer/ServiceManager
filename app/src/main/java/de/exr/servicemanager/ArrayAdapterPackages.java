package de.exr.servicemanager;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by felix on 28.06.15.
 */
public class ArrayAdapterPackages extends ArrayAdapter<Package> {

    private ActivityManager activityManager;

    private PackageManager pm = getContext().getPackageManager();

    private PagerAdapterMain pagerAdapter;

    public ArrayAdapterPackages(Context context, List<Package> pkgList, PagerAdapterMain pagerAdapter) {
        super(context, R.layout.list_checkbox_entry, pkgList);
        activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        this.pagerAdapter = pagerAdapter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.list_checkbox_entry, parent, false);
        final CheckBox entry = (CheckBox) rowView.findViewById(R.id.entry);
        final TextView entryText = (TextView) rowView.findViewById(R.id.entryText);

        final Package pi = getItem(position);
        updateListItem(pi, entry, entryText);

        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.entry);
                String cmd;
                if (checkBox.isChecked()) {
                    cmd = "pm enable " + pi.packageName;
                } else {
                    cmd = "pm disable " + pi.packageName;
                }
                Toast.makeText(getContext(), cmd, Toast.LENGTH_LONG).show();
                String out = SystemCommand.exec(cmd);
                Toast.makeText(getContext(), out, Toast.LENGTH_LONG).show();
                updateView();
            }
        });

        entryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityPackage.class);
                intent.putExtra(ActivityPackage.PACKAGE_NAME_MESSAGE, pi.packageName);
                getContext().startActivity(intent);
            }
        });

        return rowView;
    }

    private void updateView() {
        pagerAdapter.updateFragments();
    }

    private void updateListItem(Package pi, CheckBox entry, TextView entryText) {
        String str = pi.label + getServiceCount(pi) + "\n" + pi.packageName;
        entryText.setTextColor(Color.DKGRAY);
        for (ActivityManager.RunningAppProcessInfo rpi : activityManager.getRunningAppProcesses()) {
            if (pi.process.equals(rpi.processName)) {
                entryText.setTextColor(Color.BLUE);
                break;
            }
        }
        entryText.setText(str);
        entry.setChecked((pm.COMPONENT_ENABLED_STATE_DISABLED != pm.getApplicationEnabledSetting(pi.packageName)));
    }

    private String getServiceCount(Package pi) {
        String entryText = "";
        if (pi.services != null) {
            int nRunningServices = 0;
            int nEnabledServices = 0;
            for (ServiceInfo si : pi.services) {
                ComponentName cn = new ComponentName(si.packageName, si.name);
                if (pm.COMPONENT_ENABLED_STATE_DISABLED != pm.getComponentEnabledSetting(cn)) {
                    nEnabledServices++;
                }
                for (ActivityManager.RunningServiceInfo rsi : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                    if (cn.equals(rsi.service)) {
                        nRunningServices++;
                        break;
                    }
                }
            }
            entryText += " [" + nRunningServices + "/" + nEnabledServices + "/" + pi.services.length + "]";
        }
        return entryText;
    }
}
