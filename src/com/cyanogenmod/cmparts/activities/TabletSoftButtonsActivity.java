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
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.CmSystem;
import android.provider.Settings;

import com.cyanogenmod.cmparts.R;

public class TabletSoftButtonsActivity extends PreferenceActivity implements OnPreferenceChangeListener{
    private static final String PREF_SOFT_BUTTONS_HOME = "pref_soft_buttons_home";
    private static final String PREF_SOFT_BUTTONS_MENU = "pref_soft_buttons_menu";
    private static final String PREF_SOFT_BUTTONS_BACK = "pref_soft_buttons_back";
    private static final String PREF_SOFT_BUTTONS_SEARCH = "pref_soft_buttons_search";
    private static final String PREF_SOFT_BUTTONS_QUICK_NA = "pref_soft_buttons_quick_na";

    private ListPreference mSoftButtonsHome;
    private ListPreference mSoftButtonsMenu;
    private ListPreference mSoftButtonsBack;
    private ListPreference mSoftButtonsSearch;
    private CheckBoxPreference mSoftButtonsQuickNa;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.tablet_tweaks_soft_buttons_list_head);
        addPreferencesFromResource(R.xml.tablet_soft_buttons);

        PreferenceScreen prefSet = getPreferenceScreen();

        int ButtonsHomePref = Settings.System.getInt(getContentResolver(),
                Settings.System.SOFT_BUTTON_SHOW_HOME, 1);
        mSoftButtonsHome = (ListPreference) prefSet.findPreference(PREF_SOFT_BUTTONS_HOME);
        mSoftButtonsHome.setValue(String.valueOf(ButtonsHomePref));
        mSoftButtonsHome.setOnPreferenceChangeListener(this);

        int ButtonsMenuPref = Settings.System.getInt(getContentResolver(),
                Settings.System.SOFT_BUTTON_SHOW_MENU, 4);
        mSoftButtonsMenu = (ListPreference) prefSet.findPreference(PREF_SOFT_BUTTONS_MENU);
        mSoftButtonsMenu.setValue(String.valueOf(ButtonsMenuPref));
        mSoftButtonsMenu.setOnPreferenceChangeListener(this);

        int ButtonsBackPref = Settings.System.getInt(getContentResolver(),
                Settings.System.SOFT_BUTTON_SHOW_BACK, 2);
        mSoftButtonsBack = (ListPreference) prefSet.findPreference(PREF_SOFT_BUTTONS_BACK);
        mSoftButtonsBack.setValue(String.valueOf(ButtonsBackPref));
        mSoftButtonsBack.setOnPreferenceChangeListener(this);

        int ButtonsSearchPref = Settings.System.getInt(getContentResolver(),
                Settings.System.SOFT_BUTTON_SHOW_SEARCH, 3);
        mSoftButtonsSearch = (ListPreference) prefSet.findPreference(PREF_SOFT_BUTTONS_SEARCH);
        mSoftButtonsSearch.setValue(String.valueOf(ButtonsSearchPref));
        mSoftButtonsSearch.setOnPreferenceChangeListener(this);

        mSoftButtonsQuickNa = (CheckBoxPreference) prefSet.findPreference(PREF_SOFT_BUTTONS_QUICK_NA);

        int defValue;

        defValue=CmSystem.getDefaultBool(getBaseContext(), CmSystem.CM_DEFAULT_SHOW_SOFT_QUICK_NA)==true ? 1 : 0;
        mSoftButtonsQuickNa.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.SOFT_BUTTON_SHOW_QUICK_NA, defValue) == 1));
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        /* Preference Screens */
        if (preference == mSoftButtonsQuickNa) {
            value = mSoftButtonsQuickNa.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.SOFT_BUTTON_SHOW_QUICK_NA,
                    value ? 1 : 0);
            return true;
        }

        return false;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {

        /* Preference Screens */
        if (preference == mSoftButtonsHome) {
            int ButtonsHomePref = Integer.parseInt(String.valueOf(newValue));
            Settings.System.putInt(getContentResolver(), Settings.System.SOFT_BUTTON_SHOW_HOME,
                          ButtonsHomePref);
            return true;
        } else if (preference == mSoftButtonsMenu) {
            int ButtonsMenuPref = Integer.parseInt(String.valueOf(newValue));
            Settings.System.putInt(getContentResolver(), Settings.System.SOFT_BUTTON_SHOW_MENU,
                          ButtonsMenuPref);
            return true;
        } else if (preference == mSoftButtonsBack) {
            int ButtonsBackPref = Integer.parseInt(String.valueOf(newValue));
            Settings.System.putInt(getContentResolver(), Settings.System.SOFT_BUTTON_SHOW_BACK,
                          ButtonsBackPref);
            return true;
        } else if (preference == mSoftButtonsSearch) {
            int ButtonsSearchPref = Integer.parseInt(String.valueOf(newValue));
            Settings.System.putInt(getContentResolver(), Settings.System.SOFT_BUTTON_SHOW_SEARCH,
                          ButtonsSearchPref);
            return true;
        }
        return false;
    }
}
