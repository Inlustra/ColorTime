package com.thenairn.reflectivesettings;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.thenairn.reflectivesettings.annotation.SettingsHeader;
import com.thenairn.reflectivesettings.classloader.ClassScanner;
import com.thenairn.reflectivesettings.entity.SettingsPreference;
import com.thenairn.reflectivesettings.entity.SettingsSection;
import com.thenairn.reflectivesettings.util.Mutators;
import com.thenairn.reflectivesettings.view.SettingsFragment;

import static org.reflections.ReflectionUtils.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
        for (Class annotation : Mutators.getTypes()) {
            handleFields(section, clazz, annotation);
        }
        fragments.put(key, SettingsFragment.fromSection(section));
    }

    private void handleFields(SettingsSection section, Class scan, Class<? extends Annotation> annotation) {
        Set<Field> fields = getAllFields(scan, withAnnotation(annotation));
        for (Field field : fields) {
            section.add(createPreference(annotation, field));
        }
    }

    private SettingsPreference createPreference(Class<? extends Annotation> annotation, Field field) {
        field.setAccessible(true);
        Object configurable = field.getAnnotation(annotation);
        try {
            String title = getString(configurable, "titleId", "title");
            String summary = getString(configurable, "summaryId", "summary");
            String category = getString(configurable, "categoryId", "category");
            String key = (String) configurable.getClass().getMethod("key").invoke(configurable);
            return new SettingsPreference(annotation, field, key, title, summary, category);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getString(Object object, String field1, String field2) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class clazz = object.getClass();
        String titleId = (String) clazz.getMethod(field1).invoke(object);
        String titleContent = (String) clazz.getMethod(field2).invoke(object);
        return builder.parseOrDefault(titleId, titleContent);
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
