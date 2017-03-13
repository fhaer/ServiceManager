package de.exr.servicemanager;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ActivityPackage extends Activity {

    protected final static String PACKAGE_NAME_MESSAGE = "de.exr.servicemanager.PACKAGE_NAME_MESSAGE";

    private PackageInfo packageInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String packageName = getIntent().getStringExtra(PACKAGE_NAME_MESSAGE);
        setContentView(R.layout.activity_package);
        try {
            packageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_SERVICES);
            populateInterface();
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private void populateInterface() {

        setTitle(packageInfo.packageName);

        ((TextView) findViewById(R.id.textView1)).setText(packageInfo.packageName);
        ((TextView) findViewById(R.id.textView2)).setText(packageInfo.versionName);

        ListView listView = (ListView) findViewById(R.id.listView);
        if (packageInfo.services != null) {
            List<ServiceInfo> sList = Arrays.asList(packageInfo.services);
            Collections.sort(sList, new Comparator<ServiceInfo>() {
                public int compare(ServiceInfo lhs, ServiceInfo rhs) {
                    return lhs.name.compareTo(rhs.name);
                }
            });
            listView.setAdapter(new ArrayAdapterServices(this, sList, packageInfo.packageName));
        }

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
