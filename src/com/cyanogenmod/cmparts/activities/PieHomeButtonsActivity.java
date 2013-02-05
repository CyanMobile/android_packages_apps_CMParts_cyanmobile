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

public class PieHomeButtonsActivity extends PreferenceActivity implements ShortcutPickHelper.OnPickListener, OnPreferenceChangeListener{
    private static final String PREF_PIE_BUTTONS_HOME = "pref_pie_buttons_home";
    private static final String PREF_PIE_BUTTONS_HOME1 = "pref_pie_buttons_home1";
    private static final String PREF_PIE_BUTTONS_HOME2 = "pref_pie_buttons_home2";
    private static final String PREF_PIE_BUTTONS_HOME3 = "pref_pie_buttons_home3";
    private static final String PREF_PIE_BUTTONS_HOME4 = "pref_pie_buttons_home4";
    private static final String PREF_PIE_BUTTONS_HOME_APP_TOGGLE = "pref_pie_buttons_home_app_toggle";
    private static final String PREF_PIE_BUTTONS_HOME_LEVEL_TOGGLE = "pref_pie_buttons_home_level_toggle";

    private ShortcutPickHelper mPicker;
    private Context mContext;
    private ListPreference mPieButtonsHome;
    private ListPreference mPieButtonsHome1;
    private ListPreference mPieButtonsHome2;
    private ListPreference mPieButtonsHome3;
    private ListPreference mPieButtonsHome4;
    private CheckBoxPreference mPieHomeAppTogglePref;
    private CheckBoxPreference mHomeLevelTogglePref;

    private int mKeyNumber = 1;

    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.pie_home_controls_title);
        addPreferencesFromResource(R.xml.pie_home_buttons);

        PreferenceScreen prefSet = getPreferenceScreen();

        int ButtonsHomePref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_HOME, 1);
        mPieButtonsHome = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_HOME);
        mPieButtonsHome.setValue(String.valueOf(ButtonsHomePref));
        mPieButtonsHome.setOnPreferenceChangeListener(this);

        int ButtonsHome1Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_HOME1, 10);
        mPieButtonsHome1 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_HOME1);
        mPieButtonsHome1.setValue(String.valueOf(ButtonsHome1Pref));
        mPieButtonsHome1.setOnPreferenceChangeListener(this);

        int ButtonsHome2Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_HOME2, 10);
        mPieButtonsHome2 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_HOME2);
        mPieButtonsHome2.setValue(String.valueOf(ButtonsHome2Pref));
        mPieButtonsHome2.setOnPreferenceChangeListener(this);

        int ButtonsHome3Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_HOME3, 10);
        mPieButtonsHome3 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_HOME3);
        mPieButtonsHome3.setValue(String.valueOf(ButtonsHome3Pref));
        mPieButtonsHome3.setOnPreferenceChangeListener(this);

        int ButtonsHome4Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_HOME4, 10);
        mPieButtonsHome4 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_HOME4);
        mPieButtonsHome4.setValue(String.valueOf(ButtonsHome4Pref));
        mPieButtonsHome4.setOnPreferenceChangeListener(this);

        mPieHomeAppTogglePref = (CheckBoxPreference) prefSet
                .findPreference(PREF_PIE_BUTTONS_HOME_APP_TOGGLE);
        mPieHomeAppTogglePref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_ENABLE_BUTTON_HOME_APP, 0) == 1);

        mHomeLevelTogglePref = (CheckBoxPreference) prefSet
                .findPreference(PREF_PIE_BUTTONS_HOME_LEVEL_TOGGLE);
        mHomeLevelTogglePref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_ENABLE_BUTTON_HOME_LEVEL, 0) == 1);

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
        alertDialog.setMessage("Enable Pie Home App Toggle to use this!");
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
        int ButtonsHome1Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_HOME1, 10);
        mPieButtonsHome1.setValue(String.valueOf(ButtonsHome1Pref));

        int ButtonsHome2Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_HOME2, 10);
        mPieButtonsHome2.setValue(String.valueOf(ButtonsHome2Pref));

        int ButtonsHome3Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_HOME3, 10);
        mPieButtonsHome3.setValue(String.valueOf(ButtonsHome3Pref));

        int ButtonsHome4Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_HOME4, 10);
        mPieButtonsHome4.setValue(String.valueOf(ButtonsHome4Pref));
   }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        if (preference == mPieHomeAppTogglePref) {
            value = mPieHomeAppTogglePref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_HOME_APP, value ? 1 : 0);
            return true;
        } else if (preference == mHomeLevelTogglePref) {
            value = mHomeLevelTogglePref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_HOME_LEVEL, value ? 1 : 0);
            return true;
        }
        return false;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        /* Preference Screens */
        if (preference == mPieButtonsHome) {
            int ButtonsHomePref = Integer.parseInt(String.valueOf(newValue));
            Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_HOME,
                          ButtonsHomePref);
            return true;
        } else if (preference == mPieButtonsHome1) {
            int ButtonsHome1Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsHome1Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_HOME_APP, 0) == 1) {
                    mKeyNumber = 1;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_HOME1,
                          ButtonsHome1Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_HOME1,
                          ButtonsHome1Pref);
            }
            return true;
        } else if (preference == mPieButtonsHome2) {
            int ButtonsHome2Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsHome2Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_HOME_APP, 0) == 1) {
                    mKeyNumber = 2;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_HOME2,
                          ButtonsHome2Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_HOME2,
                          ButtonsHome2Pref);
            }
            return true;
        } else if (preference == mPieButtonsHome3) {
            int ButtonsHome3Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsHome3Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_HOME_APP, 0) == 1) {
                    mKeyNumber = 3;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_HOME3,
                          ButtonsHome3Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_HOME3,
                          ButtonsHome3Pref);
            }
            return true;
        } else if (preference == mPieButtonsHome4) {
            int ButtonsHome4Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsHome4Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_HOME_APP, 0) == 1) {
                    mKeyNumber = 4;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_HOME4,
                          ButtonsHome4Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_HOME4,
                          ButtonsHome4Pref);
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
                key = Settings.System.PIE_CUSTOM_BUTTON_HOME_APP1;
                pref = mPieButtonsHome1;
                break;
            case 2:
                key = Settings.System.PIE_CUSTOM_BUTTON_HOME_APP2;
                pref = mPieButtonsHome2;
                break;
            case 3:
                key = Settings.System.PIE_CUSTOM_BUTTON_HOME_APP3;
                pref = mPieButtonsHome3;
                break;
            case 4:
                key = Settings.System.PIE_CUSTOM_BUTTON_HOME_APP4;
                pref = mPieButtonsHome4;
                break;
            default:
                return;
        }

        if (Settings.System.putString(getContentResolver(), key, uri)) {
            //pref.setSummary(friendlyName);
        }
    }
}
