/*
 * Created by Sven Dawitz; Copyright (C) 2011 CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyanogenmod.cmparts.activities;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.CmSystem;
import android.provider.Settings;

import com.cyanogenmod.cmparts.R;

public class TabletNaviButtonsActivity extends PreferenceActivity implements OnPreferenceChangeListener{
    private static final String PREF_NAVI_BUTTONS_HOME = "pref_navi_buttons_home";
    private static final String PREF_NAVI_BUTTONS_MENU = "pref_navi_buttons_menu";
    private static final String PREF_NAVI_BUTTONS_BACK = "pref_navi_buttons_back";
    private static final String PREF_NAVI_BUTTONS_SEARCH = "pref_navi_buttons_search";
    private static final String PREF_NAVI_BUTTONS_QUICKER = "pref_navi_buttons_quicker";

    private Context mContext;
    private ListPreference mNaviButtonsHome;
    private ListPreference mNaviButtonsMenu;
    private ListPreference mNaviButtonsBack;
    private ListPreference mNaviButtonsSearch;
    private ListPreference mNaviButtonsQuicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.tablet_tweaks_navi_buttons_list_head);
        addPreferencesFromResource(R.xml.tablet_navi_buttons);

        PreferenceScreen prefSet = getPreferenceScreen();

        int ButtonsHomePref = Settings.System.getInt(getContentResolver(),
                Settings.System.NAVI_BUTTON_SHOW_HOME, 1);
        mNaviButtonsHome = (ListPreference) prefSet.findPreference(PREF_NAVI_BUTTONS_HOME);
        mNaviButtonsHome.setValue(String.valueOf(ButtonsHomePref));
        mNaviButtonsHome.setOnPreferenceChangeListener(this);

        int ButtonsMenuPref = Settings.System.getInt(getContentResolver(),
                Settings.System.NAVI_BUTTON_SHOW_MENU, 4);
        mNaviButtonsMenu = (ListPreference) prefSet.findPreference(PREF_NAVI_BUTTONS_MENU);
        mNaviButtonsMenu.setValue(String.valueOf(ButtonsMenuPref));
        mNaviButtonsMenu.setOnPreferenceChangeListener(this);

        int ButtonsBackPref = Settings.System.getInt(getContentResolver(),
                Settings.System.NAVI_BUTTON_SHOW_BACK, 2);
        mNaviButtonsBack = (ListPreference) prefSet.findPreference(PREF_NAVI_BUTTONS_BACK);
        mNaviButtonsBack.setValue(String.valueOf(ButtonsBackPref));
        mNaviButtonsBack.setOnPreferenceChangeListener(this);

        int ButtonsSearchPref = Settings.System.getInt(getContentResolver(),
                Settings.System.NAVI_BUTTON_SHOW_SEARCH, 3);
        mNaviButtonsSearch = (ListPreference) prefSet.findPreference(PREF_NAVI_BUTTONS_SEARCH);
        mNaviButtonsSearch.setValue(String.valueOf(ButtonsSearchPref));
        mNaviButtonsSearch.setOnPreferenceChangeListener(this);

        int ButtonsQuickerPref = Settings.System.getInt(getContentResolver(),
                Settings.System.NAVI_BUTTON_SHOW_QUICKER, 4);
        mNaviButtonsQuicker = (ListPreference) prefSet.findPreference(PREF_NAVI_BUTTONS_QUICKER);
        mNaviButtonsQuicker.setValue(String.valueOf(ButtonsQuickerPref));
        mNaviButtonsQuicker.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {

        /* Preference Screens */
        if (preference == mNaviButtonsHome) {
            int ButtonsHomePref = Integer.parseInt(String.valueOf(newValue));
            Settings.System.putInt(getContentResolver(), Settings.System.NAVI_BUTTON_SHOW_HOME,
                          ButtonsHomePref);
            return true;
        } else if (preference == mNaviButtonsMenu) {
            int ButtonsMenuPref = Integer.parseInt(String.valueOf(newValue));
            Settings.System.putInt(getContentResolver(), Settings.System.NAVI_BUTTON_SHOW_MENU,
                          ButtonsMenuPref);
            return true;
        } else if (preference == mNaviButtonsBack) {
            int ButtonsBackPref = Integer.parseInt(String.valueOf(newValue));
            Settings.System.putInt(getContentResolver(), Settings.System.NAVI_BUTTON_SHOW_BACK,
                          ButtonsBackPref);
            return true;
        } else if (preference == mNaviButtonsSearch) {
            int ButtonsSearchPref = Integer.parseInt(String.valueOf(newValue));
            Settings.System.putInt(getContentResolver(), Settings.System.NAVI_BUTTON_SHOW_SEARCH,
                          ButtonsSearchPref);
            return true;
        } else if (preference == mNaviButtonsQuicker) {
            int ButtonsQuickerPref = Integer.parseInt(String.valueOf(newValue));
            Settings.System.putInt(getContentResolver(), Settings.System.NAVI_BUTTON_SHOW_QUICKER,
                          ButtonsQuickerPref);
            return true;
        }
        return false;
    }
}
