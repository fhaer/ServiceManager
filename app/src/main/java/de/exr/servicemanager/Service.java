package de.exr.servicemanager;

import android.content.ComponentName;

import java.io.Serializable;

/**
 * Created by felix on 06.07.15.
 */
public class Service implements Serializable {

    protected String name;

    protected String packageName;

    public Service(String packageName, String name) {
        this.packageName = packageName;
        this.name = name;
    }

    public ComponentName getComponentName() {
        return new ComponentName(packageName, name);
    }
}
