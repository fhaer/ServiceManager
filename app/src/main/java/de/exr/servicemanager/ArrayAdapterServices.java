package de.exr.servicemanager;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by felix on 28.06.15.
 */
public class ArrayAdapterServices extends ArrayAdapter<ServiceInfo> {

    private ActivityManager activityManager;
    private String packageName;

    public ArrayAdapterServices(Context context, List<ServiceInfo> list, String packageName) {
        super(context, R.layout.list_checkbox_entry, list);
        activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        this.packageName = packageName;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View listView = parent;
        final View rowView = inflater.inflate(R.layout.list_checkbox_entry, parent, false);
        final CheckBox entry = (CheckBox) rowView.findViewById(R.id.entry);

        final ServiceInfo si = getItem(position);

        updateListItem(si, entry);

        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.entry);
                String cmd;
                if (checkBox.isChecked()) {
                    cmd = "pm enable " + si.packageName + "/" + si.name;
                } else {
                    cmd = "pm disable " + si.packageName + "/" + si.name;
                }
                Toast.makeText(getContext(), cmd, Toast.LENGTH_LONG).show();
                String out = SystemCommand.exec(cmd);
                Toast.makeText(getContext(), out, Toast.LENGTH_LONG).show();
                updateView(listView);
            }
        });

        return rowView;

    }

    private void updateView(View listView) {
        listView.invalidate();
        notifyDataSetChanged();
    }

    private void updateListItem(ServiceInfo si, CheckBox entry) {
        PackageManager pm = getContext().getPackageManager();

        String entryText = si.name;
        entryText += "\n" + si.loadLabel(pm);
        if (!packageName.equals(si.processName)) {
            entryText += "\nProcess: " + si.processName;
        }
        entry.setText(entryText);

        ComponentName cn = new ComponentName(si.packageName, si.name);
        entry.setChecked((pm.COMPONENT_ENABLED_STATE_DISABLED != pm.getComponentEnabledSetting(cn)));

        entry.setTextColor(Color.DKGRAY);
        for (ActivityManager.RunningServiceInfo rsi : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (cn.equals(rsi.service)) {
                entry.setTextColor(Color.BLUE);
                break;
            }
        }
    }
}
