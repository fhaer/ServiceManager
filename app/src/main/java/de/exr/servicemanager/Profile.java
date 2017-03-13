package de.exr.servicemanager;

import android.content.pm.ServiceInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by felix on 06.07.15.
 */
public class Profile implements Serializable {

    protected String name;

    private List<Package> installedPackages;

    private List<Package> enabledPackages = new ArrayList<>(10);
    private List<Package> disabledPackages = new ArrayList<>(10);
    private List<String> enabledPackagesStr = new ArrayList<>(10);
    private List<String> disabledPackagesStr = new ArrayList<>(10);

    private List<Service> enabledServices = new ArrayList<>(10);
    private List<Service> disabledServices = new ArrayList<>(10);
    private List<String> enabledServicesStr = new ArrayList<>(10);
    private List<String> disabledServicesStr = new ArrayList<>(10);

    public Profile(String name) {
        this.name = name;
    }

    public void setInstalledPackages(List<Package> installedPackages) {
        this.installedPackages = installedPackages;
    }

    public void addEnabledPackage(Package p) {
        enabledPackages.add(p);
    }

    public void addDisabledPackage(Package p) {
        disabledPackages.add(p);
    }

    public void addEnabledPackage(String s) {
        enabledPackagesStr.add(s);
    }

    public void addDisabledPackage(String s) {
        disabledPackagesStr.add(s);
    }

    public void addEnabledService(Service s) {
        enabledServices.add(s);
    }

    public void addDisabledService(Service s) {
        disabledServices.add(s);
    }

    public void addEnabledService(String s) {
        enabledServicesStr.add(s);
    }

    public void addDisabledService(String s) {
        disabledServicesStr.add(s);
    }

    public List<Package> getEnabledPackages() {
        return buildPackageReturnList(enabledPackages, enabledPackagesStr);
    }

    public List<String> getEnabledPackagesStr() {
        return enabledPackagesStr;
    }

    public List<Package> getDisabledPackages() {
        return buildPackageReturnList(disabledPackages, disabledPackagesStr);
    }

    public List<String> getDisabledPackagesStr() {
        return disabledPackagesStr;
    }

    public List<Service> getEnabledServices() {
        return buildServiceReturnList(enabledServices, enabledServicesStr);
    }

    public List<String> getEnabledServicesStr() {
        return enabledServicesStr;
    }

    public List<Service> getDisabledServices() {
        return buildServiceReturnList(disabledServices, disabledServicesStr);
    }

    public List<String> getDisabledServicesStr() {
        return disabledServicesStr;
    }

    private List<Package> buildPackageReturnList(List<Package> listToReturn, List<String> patternList) {
        List<Package> packages = new ArrayList<>();
        packages.addAll(listToReturn);
        for (String pattern : patternList)
            packages.addAll(searchMatchingPackages(pattern));
        return packages;
    }

    private List<Service> buildServiceReturnList(List<Service> listToReturn, List<String> patternList) {
        List<Service> services = new ArrayList<>();
        services.addAll(listToReturn);
        for (String pattern : patternList)
            services.addAll(searchMatchingServices(pattern));
        return services;
    }

    private List<Package> searchMatchingPackages(String pattern) {
        List<Package> packages = new ArrayList<>();
        int searchType = getSearchType(pattern);
        String text = pattern.replace("*", "");
        for (Package p : installedPackages) {
            if (searchType == 0 && p.packageName.equals(text)) {
                packages.add(p);
            } else if (searchType == 1 && p.packageName.startsWith(text)) {
                packages.add(p);
            } else if (searchType == 2 && p.packageName.endsWith(text)) {
                packages.add(p);
            } else if (searchType == 3 && p.packageName.contains(text)) {
                packages.add(p);
            }
        }
        return packages;
    }

    private List<Service> searchMatchingServices(String pattern) {
        List<Service> services = new ArrayList<>();
        int searchType = getSearchType(pattern);
        String text = pattern.replace("*", "");
        for (Package p : installedPackages) {
            if (p.services != null) {
                for (ServiceInfo s : p.services) {
                    if (searchType == 0 && s.name.equals(text)) {
                        services.add(new Service(s.packageName, s.name));
                    } else if (searchType == 1 && s.name.startsWith(text)) {
                        services.add(new Service(s.packageName, s.name));
                    } else if (searchType == 2 && s.name.endsWith(text)) {
                        services.add(new Service(s.packageName, s.name));
                    } else if (searchType == 3 && s.name.contains(text)) {
                        services.add(new Service(s.packageName, s.name));
                    }
                }
            }
        }
        return services;
    }

    private int getSearchType(String pattern) {
        int searchType = 0; // exact match
        if (!pattern.startsWith("*") && pattern.endsWith("*")) {
            searchType = 1; // starts with
        } else if (pattern.startsWith("*") && !pattern.endsWith("*")) {
            searchType = 2; // ends with
        } else if (pattern.startsWith("*") && pattern.endsWith("*")) {
            searchType = 3; // contains
        }
        return searchType;
    }
}
