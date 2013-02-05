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

public class PieMenuButtonsActivity extends PreferenceActivity implements ShortcutPickHelper.OnPickListener, OnPreferenceChangeListener{
    private static final String PREF_PIE_BUTTONS_MENU = "pref_pie_buttons_menu";
    private static final String PREF_PIE_BUTTONS_MENU1 = "pref_pie_buttons_menu1";
    private static final String PREF_PIE_BUTTONS_MENU2 = "pref_pie_buttons_menu2";
    private static final String PREF_PIE_BUTTONS_MENU3 = "pref_pie_buttons_menu3";
    private static final String PREF_PIE_BUTTONS_MENU4 = "pref_pie_buttons_menu4";
    private static final String PREF_PIE_BUTTONS_MENU_APP_TOGGLE = "pref_pie_buttons_menu_app_toggle";
    private static final String PREF_PIE_BUTTONS_MENU_LEVEL_TOGGLE = "pref_pie_buttons_menu_level_toggle";

    private ShortcutPickHelper mPicker;
    private Context mContext;
    private ListPreference mPieButtonsMenu;
    private ListPreference mPieButtonsMenu1;
    private ListPreference mPieButtonsMenu2;
    private ListPreference mPieButtonsMenu3;
    private ListPreference mPieButtonsMenu4;
    private CheckBoxPreference mPieMenuAppTogglePref;
    private CheckBoxPreference mMenuLevelTogglePref;

    private int mKeyNumber = 1;

    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.pie_menu_controls_title);
        addPreferencesFromResource(R.xml.pie_menu_buttons);

        PreferenceScreen prefSet = getPreferenceScreen();

        int ButtonsMenuPref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_MENU, 3);
        mPieButtonsMenu = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_MENU);
        mPieButtonsMenu.setValue(String.valueOf(ButtonsMenuPref));
        mPieButtonsMenu.setOnPreferenceChangeListener(this);

        int ButtonsMenu1Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_MENU1, 10);
        mPieButtonsMenu1 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_MENU1);
        mPieButtonsMenu1.setValue(String.valueOf(ButtonsMenu1Pref));
        mPieButtonsMenu1.setOnPreferenceChangeListener(this);

        int ButtonsMenu2Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_MENU2, 10);
        mPieButtonsMenu2 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_MENU2);
        mPieButtonsMenu2.setValue(String.valueOf(ButtonsMenu2Pref));
        mPieButtonsMenu2.setOnPreferenceChangeListener(this);

        int ButtonsMenu3Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_MENU3, 10);
        mPieButtonsMenu3 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_MENU3);
        mPieButtonsMenu3.setValue(String.valueOf(ButtonsMenu3Pref));
        mPieButtonsMenu3.setOnPreferenceChangeListener(this);

        int ButtonsMenu4Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_MENU4, 10);
        mPieButtonsMenu4 = (ListPreference) prefSet.findPreference(PREF_PIE_BUTTONS_MENU4);
        mPieButtonsMenu4.setValue(String.valueOf(ButtonsMenu4Pref));
        mPieButtonsMenu4.setOnPreferenceChangeListener(this);

        mPieMenuAppTogglePref = (CheckBoxPreference) prefSet
                .findPreference(PREF_PIE_BUTTONS_MENU_APP_TOGGLE);
        mPieMenuAppTogglePref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_ENABLE_BUTTON_MENU_APP, 0) == 1);

        mMenuLevelTogglePref = (CheckBoxPreference) prefSet
                .findPreference(PREF_PIE_BUTTONS_MENU_LEVEL_TOGGLE);
        mMenuLevelTogglePref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_ENABLE_BUTTON_MENU_LEVEL, 0) == 1);

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
        alertDialog.setMessage("Enable Pie Menu App Toggle to use this!");
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
        int ButtonsMenu1Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_MENU1, 10);
        mPieButtonsMenu1.setValue(String.valueOf(ButtonsMenu1Pref));

        int ButtonsMenu2Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_MENU2, 10);
        mPieButtonsMenu2.setValue(String.valueOf(ButtonsMenu2Pref));

        int ButtonsMenu3Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_MENU3, 10);
        mPieButtonsMenu3.setValue(String.valueOf(ButtonsMenu3Pref));

        int ButtonsMenu4Pref = Settings.System.getInt(getContentResolver(),
                Settings.System.PIE_BUTTON_MENU4, 10);
        mPieButtonsMenu4.setValue(String.valueOf(ButtonsMenu4Pref));
   }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        if (preference == mPieMenuAppTogglePref) {
            value = mPieMenuAppTogglePref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_MENU_APP, value ? 1 : 0);
            return true;
        } else if (preference == mMenuLevelTogglePref) {
            value = mMenuLevelTogglePref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_MENU_LEVEL, value ? 1 : 0);
            return true;
        }
        return false;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        /* Preference Screens */
        if (preference == mPieButtonsMenu) {
            int ButtonsMenuPref = Integer.parseInt(String.valueOf(newValue));
            Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_MENU,
                          ButtonsMenuPref);
            return true;
        } else if (preference == mPieButtonsMenu1) {
            int ButtonsMenu1Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsMenu1Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_MENU_APP, 0) == 1) {
                    mKeyNumber = 1;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_MENU1,
                          ButtonsMenu1Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_MENU1,
                          ButtonsMenu1Pref);
            }
            return true;
        } else if (preference == mPieButtonsMenu2) {
            int ButtonsMenu2Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsMenu2Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_MENU_APP, 0) == 1) {
                    mKeyNumber = 2;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_MENU2,
                          ButtonsMenu2Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_MENU2,
                          ButtonsMenu2Pref);
            }
            return true;
        } else if (preference == mPieButtonsMenu3) {
            int ButtonsMenu3Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsMenu3Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_MENU_APP, 0) == 1) {
                    mKeyNumber = 3;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_MENU3,
                          ButtonsMenu3Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_MENU3,
                          ButtonsMenu3Pref);
            }
            return true;
        } else if (preference == mPieButtonsMenu4) {
            int ButtonsMenu4Pref = Integer.parseInt(String.valueOf(newValue));
            if (ButtonsMenu4Pref == 11) {
                if (Settings.System.getInt(getContentResolver(),
                    Settings.System.PIE_ENABLE_BUTTON_MENU_APP, 0) == 1) {
                    mKeyNumber = 4;
                    mPicker.pickShortcut();
                    Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_MENU4,
                          ButtonsMenu4Pref);
                } else {
                    mDialogShow();
                }
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.PIE_BUTTON_MENU4,
                          ButtonsMenu4Pref);
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
                key = Settings.System.PIE_CUSTOM_BUTTON_MENU_APP1;
                pref = mPieButtonsMenu1;
                break;
            case 2:
                key = Settings.System.PIE_CUSTOM_BUTTON_MENU_APP2;
                pref = mPieButtonsMenu2;
                break;
            case 3:
                key = Settings.System.PIE_CUSTOM_BUTTON_MENU_APP3;
                pref = mPieButtonsMenu3;
                break;
            case 4:
                key = Settings.System.PIE_CUSTOM_BUTTON_MENU_APP4;
                pref = mPieButtonsMenu4;
                break;
            default:
                return;
        }

        if (Settings.System.putString(getContentResolver(), key, uri)) {
            //pref.setSummary(friendlyName);
        }
    }
}
