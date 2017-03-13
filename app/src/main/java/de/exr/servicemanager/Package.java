package de.exr.servicemanager;

import android.content.pm.ApplicationInfo;
import android.content.pm.ServiceInfo;

import java.io.Serializable;

/**
 * Created by felix on 05.07.15.
 */
public class Package implements Serializable {

    protected String packageName;

    protected String label;

    protected String process;

    protected transient ServiceInfo[] services;

    public Package() {
    }

    public Package(String packageName, String label, String process) {
        this.packageName = packageName;
        this.label = label;
        this.process = process;
    }

}
