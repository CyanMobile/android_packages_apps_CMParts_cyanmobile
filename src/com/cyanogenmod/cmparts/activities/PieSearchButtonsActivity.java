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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Dialog;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import java.util.ArrayList;

import com.cyanogenmod.cmparts.R;
import com.cyanogenmod.cmparts.utils.ShortcutPickHelper;

public class PieSearchButtonsActivity extends PreferenceActivity implements ShortcutPickHelper.OnPickListener, OnPreferenceChangeListener{
    private static final String PREF_PIE_BUTTONS_SEARCH = "pref_pie_buttons_search";
    private static final String PREF_PIE_BUTTONS_SEARCH1 = "pref_pie_buttons_search1";
    private static final String PREF_PIE_BUTTONS_SEARCH2 = "pref_pie_buttons_search2";
    private static final String PREF_PIE_BUTTONS_SEARCH3 = "pref_pie_buttons_search3";
    private static final String PREF_PIE_BUTTONS_SEARCH4 = "pref_pie_buttons_search4";
    private static final String PREF_PIE_BUTTONS_SEARCH_APP_TOGGLE = "pref_pie_buttons_search_app_toggle";
    private static final String PREF_PIE_BUTTONS_SEARCH_LEVEL_TOGGLE = "pref_pie_buttons_search_level_toggle";

    private ShortcutPickHelper mPicker;
    private Context mContext;
    private ListPreference mPieButtonsSearch;
    private ListPreference mPieButtonsSearch1;
    private ListPreference mPieButtonsSearch2;
    private ListPreference mPieButtonsSearch3;
    private ListPreference mPieButtonsSearch4;
    private CheckBoxPreference mPieSearchAppTogglePref;
    private CheckBoxPreference mSearchLevelTogglePref;

    private int mKeyNumber = 1;

    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.pie_search_controls_title);
        addPreferencesFromResource(R.xml.pie_search_buttons);

        PreferenceScreen prefSet = getPreferenceScreen();

        int ButtonsSearchPref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_SEARCH, 4);
        mPieButtonsSearch = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_SEARCH);
        mPieButtonsSearch.setValue(String.valueOf(ButtonsSearchPref));
        mPieButtonsSearch.setOnPreferenceChangeListener(this);

        int ButtonsSearch1Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_SEARCH1, 10);
        mPieButtonsSearch1 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_SEARCH1);
        mPieButtonsSearch1.setValue(String.valueOf(ButtonsSearch1Pref));
        mPieButtonsSearch1.setOnPreferenceChangeListener(this);

        int ButtonsSearch2Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_SEARCH2, 10);
        mPieButtonsSearch2 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_SEARCH2);
        mPieButtonsSearch2.setValue(String.valueOf(ButtonsSearch2Pref));
        mPieButtonsSearch2.setOnPreferenceChangeListener(this);

        int ButtonsSearch3Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_SEARCH3, 10);
        mPieButtonsSearch3 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_SEARCH3);
        mPieButtonsSearch3.setValue(String.valueOf(ButtonsSearch3Pref));
        mPieButtonsSearch3.setOnPreferenceChangeListener(this);

        int ButtonsSearch4Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_SEARCH4, 10);
        mPieButtonsSearch4 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_SEARCH4);
        mPieButtonsSearch4.setValue(String.valueOf(ButtonsSearch4Pref));
        mPieButtonsSearch4.setOnPreferenceChangeListener(this);

        mPieSearchAppTogglePref = (CheckBoxPreference) prefSet
                .findPreference(PREF_PIE_BUTTONS_SEARCH_APP_TOGGLE);
        mPieSearchAppTogglePref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_ENABLE_BUTTON_SEARCH_APP, 0) == 1);

        mSearchLevelTogglePref = (CheckBoxPreference) prefSet
                .findPreference(PREF_PIE_BUTTONS_SEARCH_LEVEL_TOGGLE);
        mSearchLevelTogglePref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_ENABLE_BUTTON_SEARCH_LEVEL, 0) == 1);

        mPicker = new ShortcutPickHelper(this, this);
    }

    private void setAppSummary(Preference pref, String key) {
        String value = Settings.System.getString(getContentResolver(), key);
        pref.setSummary(mPicker.getFriendlyNameForUri(value));
    }

    private void mDialogShow() {
        // Set up the warning
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("CyanMobile Warning!");
        alertDialog.setMessage("Enable Pie Search App Toggle to use this!");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                getResources().getString(com.android.internal.R.string.ok),
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                updateValues();
                return;
            }
        });
        
        alertDialog.show();
   }

   private void updateValues() {
        int ButtonsSearch1Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_SEARCH1, 10);
        mPieButtonsSearch1.setValue(String.valueOf(ButtonsSearch1Pref));

        int ButtonsSearch2Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_SEARCH2, 10);
        mPieButtonsSearch2.setValue(String.valueOf(ButtonsSearch2Pref));

        int ButtonsSearch3Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_SEARCH3, 10);
        mPieButtonsSearch3.setValue(String.valueOf(ButtonsSearch3Pref));

        int ButtonsSearch4Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_SEARCH4, 10);
        mPieButtonsSearch4.setValue(String.valueOf(ButtonsSearch4Pref));
   }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        if (preference == mPieSearchAppTogglePref) {
            value = mPieSearchAppTogglePref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_SEARCH_APP, value ? 1 : 0);
            return true;
        } else if (preference == mSearchLevelTogglePref) {
            value = mSearchLevelTogglePref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_SEARCH_LEVEL, value ? 1 : 0);
            return true;
        }
        return false;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        /* Preference Screens */
        if (preference == mPieButtonsSearch) {
            int ButtonsSearchPref = Integer.parseInt(String.valueOf(newValue));
            Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_SEARCH,
                          ButtonsSearchPref);
            return true;
        } else if (preference == mPieButtonsSearch1) {
            int ButtonsSearch1Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsSearch1Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_SEARCH_APP, 0) == 1) {
                    mKeyNumber = 1;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_SEARCH1,
                          ButtonsSearch1Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_SEARCH1,
                          ButtonsSearch1Pref);
            }
            return true;
        } else if (preference == mPieButtonsSearch2) {
            int ButtonsSearch2Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsSearch2Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_SEARCH_APP, 0) == 1) {
                    mKeyNumber = 2;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_SEARCH2,
                          ButtonsSearch2Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_SEARCH2,
                          ButtonsSearch2Pref);
            }
            return true;
        } else if (preference == mPieButtonsSearch3) {
            int ButtonsSearch3Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsSearch3Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_SEARCH_APP, 0) == 1) {
                    mKeyNumber = 3;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_SEARCH3,
                          ButtonsSearch3Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_SEARCH3,
                          ButtonsSearch3Pref);
            }
            return true;
        } else if (preference == mPieButtonsSearch4) {
            int ButtonsSearch4Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsSearch4Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_SEARCH_APP, 0) == 1) {
                    mKeyNumber = 4;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_SEARCH4,
                          ButtonsSearch4Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_SEARCH4,
                          ButtonsSearch4Pref);
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPicker.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void shortcutPicked(String uri, String friendlyName, boolean isApplication) {
        String key;
        Preference pref;

        switch (mKeyNumber) {
            case 1:
                key = Settings.System.PIE_CUSTOM_BUTTON_SEARCH_APP1;
                pref = mPieButtonsSearch1;
                break;
            case 2:
                key = Settings.System.PIE_CUSTOM_BUTTON_SEARCH_APP2;
                pref = mPieButtonsSearch2;
                break;
            case 3:
                key = Settings.System.PIE_CUSTOM_BUTTON_SEARCH_APP3;
                pref = mPieButtonsSearch3;
                break;
            case 4:
                key = Settings.System.PIE_CUSTOM_BUTTON_SEARCH_APP4;
                pref = mPieButtonsSearch4;
                break;
            default:
                return;
        }

        if (Settings.System.putString(getContentResolver(), key, uri)) {
            //pref.setSummary(friendlyName);
        }
    }
}
