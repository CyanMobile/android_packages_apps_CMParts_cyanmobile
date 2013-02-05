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

public class PieBackButtonsActivity extends PreferenceActivity implements ShortcutPickHelper.OnPickListener, OnPreferenceChangeListener{
    private static final String PREF_PIE_BUTTONS_BACK = "pref_pie_buttons_back";
    private static final String PREF_PIE_BUTTONS_BACK1 = "pref_pie_buttons_back1";
    private static final String PREF_PIE_BUTTONS_BACK2 = "pref_pie_buttons_back2";
    private static final String PREF_PIE_BUTTONS_BACK3 = "pref_pie_buttons_back3";
    private static final String PREF_PIE_BUTTONS_BACK4 = "pref_pie_buttons_back4";
    private static final String PREF_PIE_BUTTONS_BACK_APP_TOGGLE = "pref_pie_buttons_back_app_toggle";
    private static final String PREF_PIE_BUTTONS_BACK_LEVEL_TOGGLE = "pref_pie_buttons_back_level_toggle";

    private ShortcutPickHelper mPicker;
    private Context mContext;
    private ListPreference mPieButtonsBack;
    private ListPreference mPieButtonsBack1;
    private ListPreference mPieButtonsBack2;
    private ListPreference mPieButtonsBack3;
    private ListPreference mPieButtonsBack4;
    private CheckBoxPreference mPieBackAppTogglePref;
    private CheckBoxPreference mBackLevelTogglePref;

    private int mKeyNumber = 1;

    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.pie_back_controls_title);
        addPreferencesFromResource(R.xml.pie_back_buttons);

        PreferenceScreen prefSet = getPreferenceScreen();

        int ButtonsBackPref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_BACK, 0);
        mPieButtonsBack = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_BACK);
        mPieButtonsBack.setValue(String.valueOf(ButtonsBackPref));
        mPieButtonsBack.setOnPreferenceChangeListener(this);

        int ButtonsBack1Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_BACK1, 10);
        mPieButtonsBack1 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_BACK1);
        mPieButtonsBack1.setValue(String.valueOf(ButtonsBack1Pref));
        mPieButtonsBack1.setOnPreferenceChangeListener(this);

        int ButtonsBack2Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_BACK2, 10);
        mPieButtonsBack2 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_BACK2);
        mPieButtonsBack2.setValue(String.valueOf(ButtonsBack2Pref));
        mPieButtonsBack2.setOnPreferenceChangeListener(this);

        int ButtonsBack3Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_BACK3, 10);
        mPieButtonsBack3 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_BACK3);
        mPieButtonsBack3.setValue(String.valueOf(ButtonsBack3Pref));
        mPieButtonsBack3.setOnPreferenceChangeListener(this);

        int ButtonsBack4Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_BACK4, 10);
        mPieButtonsBack4 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_BACK4);
        mPieButtonsBack4.setValue(String.valueOf(ButtonsBack4Pref));
        mPieButtonsBack4.setOnPreferenceChangeListener(this);

        mPieBackAppTogglePref = (CheckBoxPreference) prefSet
                .findPreference(PREF_PIE_BUTTONS_BACK_APP_TOGGLE);
        mPieBackAppTogglePref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_ENABLE_BUTTON_BACK_APP, 0) == 1);

        mBackLevelTogglePref = (CheckBoxPreference) prefSet
                .findPreference(PREF_PIE_BUTTONS_BACK_LEVEL_TOGGLE);
        mBackLevelTogglePref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_ENABLE_BUTTON_BACK_LEVEL, 0) == 1);

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
        alertDialog.setMessage("Enable Pie Back App Toggle to use this!");
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
        int ButtonsBack1Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_BACK1, 10);
        mPieButtonsBack1.setValue(String.valueOf(ButtonsBack1Pref));

        int ButtonsBack2Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_BACK2, 10);
        mPieButtonsBack2.setValue(String.valueOf(ButtonsBack2Pref));

        int ButtonsBack3Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_BACK3, 10);
        mPieButtonsBack3.setValue(String.valueOf(ButtonsBack3Pref));

        int ButtonsBack4Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_BACK4, 10);
        mPieButtonsBack4.setValue(String.valueOf(ButtonsBack4Pref));
   }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        if (preference == mPieBackAppTogglePref) {
            value = mPieBackAppTogglePref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_BACK_APP, value ? 1 : 0);
            return true;
        } else if (preference == mBackLevelTogglePref) {
            value = mBackLevelTogglePref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_BACK_LEVEL, value ? 1 : 0);
            return true;
        }
        return false;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        /* Preference Screens */
        if (preference == mPieButtonsBack) {
            int ButtonsBackPref = Integer.parseInt(String.valueOf(newValue));
            Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_BACK,
                          ButtonsBackPref);
            return true;
        } else if (preference == mPieButtonsBack1) {
            int ButtonsBack1Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsBack1Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_BACK_APP, 0) == 1) {
                    mKeyNumber = 1;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_BACK1,
                          ButtonsBack1Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_BACK1,
                          ButtonsBack1Pref);
            }
            return true;
        } else if (preference == mPieButtonsBack2) {
            int ButtonsBack2Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsBack2Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_BACK_APP, 0) == 1) {
                    mKeyNumber = 2;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_BACK2,
                          ButtonsBack2Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_BACK2,
                          ButtonsBack2Pref);
            }
            return true;
        } else if (preference == mPieButtonsBack3) {
            int ButtonsBack3Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsBack3Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_BACK_APP, 0) == 1) {
                    mKeyNumber = 3;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_BACK3,
                          ButtonsBack3Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_BACK3,
                          ButtonsBack3Pref);
            }
            return true;
        } else if (preference == mPieButtonsBack4) {
            int ButtonsBack4Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsBack4Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_BACK_APP, 0) == 1) {
                    mKeyNumber = 4;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_BACK4,
                          ButtonsBack4Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_BACK4,
                          ButtonsBack4Pref);
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
                key = Settings.System.PIE_CUSTOM_BUTTON_BACK_APP1;
                pref = mPieButtonsBack1;
                break;
            case 2:
                key = Settings.System.PIE_CUSTOM_BUTTON_BACK_APP2;
                pref = mPieButtonsBack2;
                break;
            case 3:
                key = Settings.System.PIE_CUSTOM_BUTTON_BACK_APP3;
                pref = mPieButtonsBack3;
                break;
            case 4:
                key = Settings.System.PIE_CUSTOM_BUTTON_BACK_APP4;
                pref = mPieButtonsBack4;
                break;
            default:
                return;
        }

        if (Settings.System.putString(getContentResolver(), key, uri)) {
            //pref.setSummary(friendlyName);
        }
    }
}
