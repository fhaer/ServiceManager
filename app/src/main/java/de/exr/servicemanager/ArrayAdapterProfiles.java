package de.exr.servicemanager;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
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
public class ArrayAdapterProfiles extends ArrayAdapter<Profile> {

    private ActivityManager activityManager;
    private List<Package> installedPackages;

    private PackageManager pm = getContext().getPackageManager();

    private PagerAdapterMain pagerAdapter;

    public ArrayAdapterProfiles(Context context, List<Profile> profileList, List<Package> installedPkgs, PagerAdapterMain pagerAdapter) {
        super(context, R.layout.list_checkbox_entry, profileList);
        activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        installedPackages = installedPkgs;
        this.pagerAdapter = pagerAdapter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.list_checkbox_entry, parent, false);
        final CheckBox entry = (CheckBox) rowView.findViewById(R.id.entry);
        final TextView entryText = (TextView) rowView.findViewById(R.id.entryText);

        final Profile p = getItem(position);
        p.setInstalledPackages(installedPackages);

        updateListItem(p, entry, entryText);

        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.entry);
                String out = "";
                if (checkBox.isChecked()) {
                    for (Package pa : p.getEnabledPackages()) {
                        out = SystemCommand.exec("pm enable " + pa.packageName);
                    }
                    for (Package pa : p.getDisabledPackages()) {
                        out = SystemCommand.exec("pm disable " + pa.packageName);
                    }
                    for (Service se : p.getEnabledServices()) {
                        out = SystemCommand.exec("pm enable " + se.packageName + "/" + se.name);
                    }
                    for (Service se : p.getDisabledServices()) {
                        out = SystemCommand.exec("pm disable " + se.packageName + "/" + se.name);
                    }
                } else {
                    for (Package pa : p.getEnabledPackages()) {
                        out = SystemCommand.exec("pm enable " + pa.packageName);
                    }
                    for (Package pa : p.getDisabledPackages()) {
                        out = SystemCommand.exec("pm enable " + pa.packageName);
                    }
                    for (Service se : p.getEnabledServices()) {
                        out = SystemCommand.exec("pm enable " + se.packageName + "/" + se.name);
                    }
                    for (Service se : p.getDisabledServices()) {
                        out = SystemCommand.exec("pm enable " + se.packageName + "/" + se.name);
                    }
                }
                Toast.makeText(getContext(), out, Toast.LENGTH_SHORT).show();
                updateView();
            }
        });

        entryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityProfile.class);
                Bundle b = new Bundle();
                b.putSerializable(ActivityProfile.PACKAGE_NAME_MESSAGE, p);
                intent.putExtra(ActivityProfile.PACKAGE_NAME_MESSAGE, b);
                getContext().startActivity(intent);
            }
        });

        return rowView;
    }

    private void updateView() {
        pagerAdapter.updateFragments();
    }

    private void updateListItem(Profile p, CheckBox entry, TextView entryText) {
        long nPackagesToEnable = p.getEnabledPackages().size();
        long nPackagesToDisable = p.getDisabledPackages().size();

        long nServicesToEnable = p.getEnabledServices().size();
        long nServicesToDisable = p.getDisabledServices().size();

        long n = 0;

        n = getRunningPackages(p.getEnabledPackages());
        long nPackagesToEnableRunning = n >> 12;
        long nPackagesToEnableEnabled = n & 0xFFF;

        n = getRunningPackages(p.getDisabledPackages());
        long nPackagesToDisableRunning = n >> 12;
        long nPackagesToDisableEnabled = n & 0xFFF;

        n = getRunningServices(p.getEnabledServices());
        long nServicesToEnableRunning = n >> 12;
        long nServicesToEnableEnabled = n & 0xFFF;

        n = getRunningServices(p.getDisabledServices());
        long nServicesToDisableRunning = n >> 12;
        long nServicesToDisableEnabled = n & 0xFFF;

        String str = p.name + "\n";
        //str += "Packages:\n";
        //str += "e: " + nPackagesToEnableRunning + "/" + nPackagesToEnableEnabled + "/" + nPackagesToEnable + "\n";
        str += "p: [" + nPackagesToDisableRunning + "/" + nPackagesToDisableEnabled + "/" + nPackagesToDisable + "]\n";
        //str += "\n";
        //str += "Services: " + nServicesToEnable + "/" + nServicesToDisable + "\n";
        //str += "e: " + nServicesToEnableRunning + "/" + nServicesToEnableEnabled + "\n";
        str += "s: [" + nServicesToDisableRunning + "/" + nServicesToDisableEnabled + "/" + nServicesToDisable + "]";

        entryText.setText(str);
        if (nPackagesToDisableRunning > 0 || nServicesToDisableRunning > 0) {
            entryText.setTextColor(Color.BLUE);
        } else {
            entryText.setTextColor(Color.DKGRAY);
        }

        boolean isActive = true;
        isActive &= (nPackagesToEnable == nPackagesToEnableEnabled);
        isActive &= (nPackagesToDisableEnabled == 0);
        isActive &= (nServicesToEnable == nServicesToEnableEnabled);
        isActive &= (nServicesToDisableEnabled == 0);

        entry.setChecked(isActive);
    }

    private long getRunningPackages(List<Package> packages) {
        int nRunningPackages = 0;
        int nEnabledPackages = 0;
        for (Package p : packages) {
            if ((pm.COMPONENT_ENABLED_STATE_DISABLED != pm.getApplicationEnabledSetting(p.packageName))) {
                nEnabledPackages++;
            }
            for (ActivityManager.RunningAppProcessInfo rpi : activityManager.getRunningAppProcesses()) {
                if (p.process != null && p.process.equals(rpi.processName)) {
                    nRunningPackages++;
                    break;
                }
            }
        }
        long out = nRunningPackages;
        out <<= 12;
        out |= nEnabledPackages;
        return out;
    }

    private long getRunningServices(List<Service> services) {
        int nRunningServices = 0;
        int nEnabledServices = 0;
        for (Service si : services) {
            ComponentName cn = si.getComponentName();
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
        long out = nRunningServices;
        out <<= 12;
        out |= nEnabledServices;
        return out;
    }
}
