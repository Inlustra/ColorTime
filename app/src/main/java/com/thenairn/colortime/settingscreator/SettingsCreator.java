package com.thenairn.colortime.settingscreator;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.thenairn.colortime.settingscreator.annotation.SettingsConfigurable;
import com.thenairn.colortime.settingscreator.annotation.SettingsHeader;
import com.thenairn.colortime.settingscreator.classloader.ClassScanner;
import com.thenairn.colortime.settingscreator.entity.SettingsPreference;
import com.thenairn.colortime.settingscreator.entity.SettingsSection;
import com.thenairn.colortime.settingscreator.view.SettingsFragment;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import static org.reflections.ReflectionUtils.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Set;

/**
 * Created by Tom on 09/09/2015.
 */
public class SettingsCreator {

    private Context context;
    private String packages;
    private SparseArray<SettingsFragment> fragments;
    private SettingsBuilder builder;

    public SettingsCreator(final String packages, Context context) {
        this.builder = new SettingsBuilder(context);
        this.fragments = new SparseArray<>();
        this.context = context;
        this.packages = packages;
        Log.e("SettingsCreator", "Starting reflections at package: " + packages);
        init();
    }

    private void init() {
        try {
            new ClassScanner(context) {
                private int key = 0;

                @Override
                protected boolean isTargetClassName(String className) {
                    return className.startsWith(packages) && !className.contains("$");
                }

                @Override
                protected boolean isTargetClass(Class clazz) {
                    return clazz.isAnnotationPresent(SettingsHeader.class);
                }

                @Override
                protected void onScanResult(Class clazz) {
                    handleClass(key++, clazz);

                }
            }.scan();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void handleClass(int key, Class clazz) {
        SettingsSection section = createSection(clazz);
        Set<Field> fields = getAllFields(clazz, withAnnotation(SettingsConfigurable.class));
        Log.e("SettingsCreator", "Listed " + fields.size() + " fields for " + clazz.getSimpleName());
        for (Field field : fields) {
            section.add(createPreference(field));
        }
        fragments.put(key, SettingsFragment.fromSection(section));
    }


    private SettingsPreference createPreference(Field field) {
        SettingsConfigurable configurable = field.getAnnotation(SettingsConfigurable.class);
        String title = builder.parseOrDefault(configurable.titleId(), configurable.title());
        String summary = builder.parseOrDefault(configurable.summaryId(), configurable.summary());
        String category = builder.parseOrDefault(configurable.categoryId(), configurable.category());
        return new SettingsPreference(field, configurable.key(), title, summary, category);
    }

    private SettingsSection createSection(Class clazz) {
        SettingsHeader header = (SettingsHeader) clazz.getAnnotation(SettingsHeader.class);
        String title = builder.parseOrDefault(header.titleId(), header.title());
        String summary = builder.parseOrDefault(header.summaryId(), header.summary());
        int icon = builder.parseDrawableOrDefault(header.iconId(), -1);
        return new SettingsSection(title, summary, icon);
    }

    public SparseArray<SettingsFragment> getFragments() {
        return fragments;
    }

    public SettingsFragment getFragment(int i) {
        return fragments.get(i);
    }
}
