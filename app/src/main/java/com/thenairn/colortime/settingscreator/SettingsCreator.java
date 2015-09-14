package com.thenairn.colortime.settingscreator;

import org.reflections.Reflections;

import java.util.Set;

/**
 * Created by Tom on 09/09/2015.
 */
public class SettingsCreator {

    private String packages;
    private Reflections reflections;

    public SettingsCreator(String packages) {
        this.packages = packages;
        this.reflections = new Reflections(packages);
        init();
    }

    private void init() {
    }
}
