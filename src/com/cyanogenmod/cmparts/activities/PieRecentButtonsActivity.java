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

public class PieRecentButtonsActivity extends PreferenceActivity implements ShortcutPickHelper.OnPickListener, OnPreferenceChangeListener{
    private static final String PREF_PIE_BUTTONS_RECENT = "pref_pie_buttons_recent";
    private static final String PREF_PIE_BUTTONS_RECENT1 = "pref_pie_buttons_recent1";
    private static final String PREF_PIE_BUTTONS_RECENT2 = "pref_pie_buttons_recent2";
    private static final String PREF_PIE_BUTTONS_RECENT3 = "pref_pie_buttons_recent3";
    private static final String PREF_PIE_BUTTONS_RECENT4 = "pref_pie_buttons_recent4";
    private static final String PREF_PIE_BUTTONS_RECENT_APP_TOGGLE = "pref_pie_buttons_recent_app_toggle";
    private static final String PREF_PIE_BUTTONS_RECENT_LEVEL_TOGGLE = "pref_pie_buttons_recent_level_toggle";

    private ShortcutPickHelper mPicker;
    private Context mContext;
    private ListPreference mPieButtonsRecent;
    private ListPreference mPieButtonsRecent1;
    private ListPreference mPieButtonsRecent2;
    private ListPreference mPieButtonsRecent3;
    private ListPreference mPieButtonsRecent4;
    private CheckBoxPreference mPieRecentAppTogglePref;
    private CheckBoxPreference mRecentLevelTogglePref;

    private int mKeyNumber = 1;

    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.pie_recent_controls_title);
        addPreferencesFromResource(R.xml.pie_recent_buttons);

        PreferenceScreen prefSet = getPreferenceScreen();

        int ButtonsRecentPref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_RECENT, 2);
        mPieButtonsRecent = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_RECENT);
        mPieButtonsRecent.setValue(String.valueOf(ButtonsRecentPref));
        mPieButtonsRecent.setOnPreferenceChangeListener(this);

        int ButtonsRecent1Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_RECENT1, 10);
        mPieButtonsRecent1 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_RECENT1);
        mPieButtonsRecent1.setValue(String.valueOf(ButtonsRecent1Pref));
        mPieButtonsRecent1.setOnPreferenceChangeListener(this);

        int ButtonsRecent2Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_RECENT2, 10);
        mPieButtonsRecent2 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_RECENT2);
        mPieButtonsRecent2.setValue(String.valueOf(ButtonsRecent2Pref));
        mPieButtonsRecent2.setOnPreferenceChangeListener(this);

        int ButtonsRecent3Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_RECENT3, 10);
        mPieButtonsRecent3 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_RECENT3);
        mPieButtonsRecent3.setValue(String.valueOf(ButtonsRecent3Pref));
        mPieButtonsRecent3.setOnPreferenceChangeListener(this);

        int ButtonsRecent4Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_RECENT4, 10);
        mPieButtonsRecent4 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_RECENT4);
        mPieButtonsRecent4.setValue(String.valueOf(ButtonsRecent4Pref));
        mPieButtonsRecent4.setOnPreferenceChangeListener(this);

        mPieRecentAppTogglePref = (CheckBoxPreference) prefSet
                .findPreference(PREF_PIE_BUTTONS_RECENT_APP_TOGGLE);
        mPieRecentAppTogglePref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_ENABLE_BUTTON_RECENT_APP, 0) == 1);

        mRecentLevelTogglePref = (CheckBoxPreference) prefSet
                .findPreference(PREF_PIE_BUTTONS_RECENT_LEVEL_TOGGLE);
        mRecentLevelTogglePref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_ENABLE_BUTTON_RECENT_LEVEL, 0) == 1);

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
        alertDialog.setMessage("Enable Pie Recent App Toggle to use this!");
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
        int ButtonsRecent1Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_RECENT1, 10);
        mPieButtonsRecent1.setValue(String.valueOf(ButtonsRecent1Pref));

        int ButtonsRecent2Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_RECENT2, 10);
        mPieButtonsRecent2.setValue(String.valueOf(ButtonsRecent2Pref));

        int ButtonsRecent3Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_RECENT3, 10);
        mPieButtonsRecent3.setValue(String.valueOf(ButtonsRecent3Pref));

        int ButtonsRecent4Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_RECENT4, 10);
        mPieButtonsRecent4.setValue(String.valueOf(ButtonsRecent4Pref));
   }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        if (preference == mPieRecentAppTogglePref) {
            value = mPieRecentAppTogglePref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_RECENT_APP, value ? 1 : 0);
            return true;
        } else if (preference == mRecentLevelTogglePref) {
            value = mRecentLevelTogglePref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_RECENT_LEVEL, value ? 1 : 0);
            return true;
        }
        return false;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        /* Preference Screens */
        if (preference == mPieButtonsRecent) {
            int ButtonsRecentPref = Integer.parseInt(String.valueOf(newValue));
            Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_RECENT,
                          ButtonsRecentPref);
            return true;
        } else if (preference == mPieButtonsRecent1) {
            int ButtonsRecent1Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsRecent1Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_RECENT_APP, 0) == 1) {
                    mKeyNumber = 1;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_RECENT1,
                          ButtonsRecent1Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_RECENT1,
                          ButtonsRecent1Pref);
            }
            return true;
        } else if (preference == mPieButtonsRecent2) {
            int ButtonsRecent2Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsRecent2Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_RECENT_APP, 0) == 1) {
                    mKeyNumber = 2;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_RECENT2,
                          ButtonsRecent2Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_RECENT2,
                          ButtonsRecent2Pref);
            }
            return true;
        } else if (preference == mPieButtonsRecent3) {
            int ButtonsRecent3Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsRecent3Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_RECENT_APP, 0) == 1) {
                    mKeyNumber = 3;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_RECENT3,
                          ButtonsRecent3Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_RECENT3,
                          ButtonsRecent3Pref);
            }
            return true;
        } else if (preference == mPieButtonsRecent4) {
            int ButtonsRecent4Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsRecent4Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_RECENT_APP, 0) == 1) {
                    mKeyNumber = 4;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_RECENT4,
                          ButtonsRecent4Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_RECENT4,
                          ButtonsRecent4Pref);
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
                key = Settings.System.PIE_CUSTOM_BUTTON_RECENT_APP1;
                pref = mPieButtonsRecent1;
                break;
            case 2:
                key = Settings.System.PIE_CUSTOM_BUTTON_RECENT_APP2;
                pref = mPieButtonsRecent2;
                break;
            case 3:
                key = Settings.System.PIE_CUSTOM_BUTTON_RECENT_APP3;
                pref = mPieButtonsRecent3;
                break;
            case 4:
                key = Settings.System.PIE_CUSTOM_BUTTON_RECENT_APP4;
                pref = mPieButtonsRecent4;
                break;
            default:
                return;
        }

        if (Settings.System.putString(getContentResolver(), key, uri)) {
            //pref.setSummary(friendlyName);
        }
    }
}
