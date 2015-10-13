package com.thenairn.reflectivesettings.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.thenairn.reflectivesettings.SettingsCreator;
import com.thenairn.reflectivesettings.entity.SettingsSection;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by thomas on 15/09/15.
 */
public abstract class ReflectiveSettingsActivity extends PreferenceActivity {

    private static final String HEADER_KEY = "SETTINGS_FRAGMENT";
    private SettingsCreator creator;

    private SettingsCreator getCreator() {
        if (creator == null) {
            creator = new SettingsCreator(getPackage(), getApplicationContext());
        }
        return creator;
    }

    private ViewGroup mPrefsContainer;
    private int prefsRes;
    private int headersRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Class clasz = null;
        try {
            clasz = Class.forName("com.android.internal.R$id");
            Field field = clasz.getDeclaredField("prefs");
            field.setAccessible(true);
            prefsRes = (int) field.get(null);
            field = clasz.getDeclaredField("headers");
            field.setAccessible(true);
            headersRes = (int) field.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected abstract String getPackage();

    private List<Header> headers;

    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        for (int i = 0; i < getCreator().getFragments().size(); i++) {
            SettingsSection section = getCreator().getFragments().get(i).getSection();
            Header header = new Header();
            header.title = section.getTitle();
            header.summary = section.getSummary();
            header.iconRes = section.getIcon();
            header.extras = new Bundle();
            header.extras.putInt(HEADER_KEY, i);
            target.add(header);
        }
        headers = target;
    }

    @Override
    public void onHeaderClick(@NonNull Header header, int position) {
        SettingsFragment fragment = getCreator().getFragment(header.extras.getInt(HEADER_KEY));
        switchToHeader(header, fragment);
    }

    @Override
    public void onBackPressed() {
        if (mPrefsContainer.getVisibility() == View.VISIBLE) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setClass(this, getClass());
            startActivity(intent);
        }
        super.onBackPressed();
    }

    public void switchToHeader(Header header, Fragment fragment) {
        setSelectedHeader(header);
        switchToHeaderInner(fragment);
    }

    void setSelectedHeader(Header header) {
        int index = headers.indexOf(header);
        if (index >= 0) {
            getListView().setItemChecked(index, true);
        } else {
            getListView().clearChoices();
        }
        showBreadCrumbs(header);
    }

    void showBreadCrumbs(Header header) {
        if (header != null) {
            CharSequence title = header.getBreadCrumbTitle(getResources());
            if (title == null) title = header.getTitle(getResources());
            if (title == null) title = getTitle();
            showBreadCrumbs(title, header.getBreadCrumbShortTitle(getResources()));
        } else {
            showBreadCrumbs(getTitle(), null);
        }
    }

    private static final String BACK_STACK_PREFS = ":android:prefs";

    private void switchToHeaderInner(Fragment fragment) {
        getFragmentManager().popBackStack(BACK_STACK_PREFS,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(headersRes, fragment);
        transaction.commitAllowingStateLoss();
    }

}