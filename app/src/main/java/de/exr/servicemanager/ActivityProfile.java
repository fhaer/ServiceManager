package de.exr.servicemanager;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ActivityProfile extends Activity {

    protected final static String PACKAGE_NAME_MESSAGE = "de.exr.servicemanager.PROFILE_NAME_MESSAGE";

    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profile = (Profile) getIntent().getBundleExtra(PACKAGE_NAME_MESSAGE).get(PACKAGE_NAME_MESSAGE);
        setContentView(R.layout.activity_package);
        populateInterface();
    }

    private void populateInterface() {

        setTitle(profile.name);

        ((TextView) findViewById(R.id.textView1)).setText(profile.name);
        ((TextView) findViewById(R.id.textView)).setText("Packages and Services");

        List<String> entries = new ArrayList<>();

        ListView listView = (ListView) findViewById(R.id.listView);
        for (Package p : profile.getEnabledPackages()) {
            entries.add("p:e " + p.packageName);
        }
        for (String p : profile.getEnabledPackagesStr()) {
            entries.add("p:e:s " + p);
        }
        for (Package p : profile.getDisabledPackages()) {
            entries.add("p:d " + p.packageName);
        }
        for (String p : profile.getDisabledPackagesStr()) {
            entries.add("p:d:s " + p);
        }
        for (Service s : profile.getEnabledServices()) {
            entries.add("s:e " + s.name);
        }
        for (String s : profile.getEnabledServicesStr()) {
            entries.add("s:e:s " + s);
        }
        for (Service s : profile.getDisabledServices()) {
            entries.add("s:d " + s.name);
        }
        for (String s : profile.getDisabledServicesStr()) {
            entries.add("s:d:s " + s);
        }

        Collections.sort(entries, new Comparator<String>() {
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, entries);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_activity_package, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
