package com.thenairn.reflectivesettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseArray;

import com.thenairn.reflectivesettings.annotation.SettingsField;
import com.thenairn.reflectivesettings.annotation.SettingsHeader;
import com.thenairn.reflectivesettings.classloader.ClassScanner;
import com.thenairn.reflectivesettings.entity.SettingsPreference;
import com.thenairn.reflectivesettings.entity.SettingsSection;
import com.thenairn.reflectivesettings.util.Mutators;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dalvik.system.DexFile;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;

/**
 * Created by Tom on 09/09/2015.
 */
public class SettingsInitializer {

    private static final Map<String, SettingsInitializer> cache = new HashMap<>();

    public static SettingsInitializer forPackage(String packages, Context context) {
        SettingsInitializer initializer = cache.get(packages);
        if (initializer == null)
            cache.put(packages, initializer = new SettingsInitializer(packages, context));
        return initializer;
    }

    private SharedPreferences sharedPreferences;
    private Context context;
    private String packages;
    private SparseArray<SettingsSection> sections;
    private List<SettingsSection> mainPreferences;
    private SettingsBuilder builder;
    private boolean isDone = false;

    private SettingsInitializer(final String packages, Context context) {
        this.sharedPreferences = context.getSharedPreferences(packages + "_preferences", Context.MODE_PRIVATE);
        this.builder = new SettingsBuilder(context);
        this.sections = new SparseArray<>();
        this.mainPreferences = new ArrayList<>();
        this.context = context;
        this.packages = packages;
        init();
        Log.d("SettingsCreator", "Starting reflections at package: " + packages);
    }

    private void init() {
        if (isDone) {
            Log.d("SettingsInitializer", "Settings Already Cached");
            return;
        }
        try {
            new ClassScanner(context) {

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
                    handleClass(clazz);
                }
            }.scan();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        isDone = true;
    }

    private int key = 0;

    private void handleClass(Class clazz) {
        SettingsSection section = createSection(clazz);
        handleFields(section, clazz);
        if (section.isTop()) {
            mainPreferences.add(section);
            return;
        }
        sections.put(key++, section);
    }


    private void handleFields(SettingsSection section, Class scan) {
        Set<Field> fields = getFields(scan, SettingsField.class);
        for (Field field : fields) {
            Log.e("SettingsInitializer", field.getName());
            initField(field);
            section.add(createPreference(field));
        }
    }

    private Set<Field> getFields(Class scan, Class<? extends Annotation> annotation) {
        Set<Field> fields = new LinkedHashSet();
        for (Field field : scan.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotation))
                fields.add(field);
        }
        return fields;
    }

    private SettingsPreference createPreference(Field field) {
        field.setAccessible(true);
        if (!Modifier.isStatic(field.getModifiers())) {
            throw new IllegalStateException("Field must be declared static: " + field.getName());
        }
        Object configurable = field.getAnnotation(SettingsField.class);
        try {
            String title = getString(configurable, "titleId", "title");
            String summary = getString(configurable, "summaryId", "summary");
            String category = getString(configurable, "categoryId", "category");
            String key = (String) configurable.getClass().getMethod("key").invoke(configurable);
            return new SettingsPreference(field, key, title, summary, category);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void initField(Field field) {
        field.setAccessible(true);
        if (!Modifier.isStatic(field.getModifiers())) {
            throw new IllegalStateException("Field must be declared static: " + field.getName());
        }
        Log.e("SettingsInitialzier", "Set field: " + field.getName());
        SettingsField configurable = field.getAnnotation(SettingsField.class);
        Mutators.getMutator(configurable.type(), field.getType()).initField(field, configurable.key(), sharedPreferences);
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
        String category = builder.parseOrDefault(header.categoryId(), header.category());
        int icon = builder.parseDrawableOrDefault(header.iconId(), -1);
        return new SettingsSection(title, summary, icon, header.headerTop(), category);
    }

    public SparseArray<SettingsSection> getSections() {
        return sections;
    }

    public List<SettingsSection> getMainPreferences() {
        return mainPreferences;
    }

    public SettingsSection getSection(int i) {
        return sections.get(i);
    }
}
