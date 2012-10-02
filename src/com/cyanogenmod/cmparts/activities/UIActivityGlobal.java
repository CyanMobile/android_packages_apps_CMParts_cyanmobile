/*
 * Copyright (C) 2011 The CyanogenMod Project
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

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

import com.cyanogenmod.cmparts.R;

public class UIActivityGlobal extends PreferenceActivity {

    /* Preference Screens */
    private static final String PREF_SQUADZONE = "squadkeys";

    private static final String GENERAL_CATEGORY = "general_category";

    private static final String PREF_PROFILE = "profile_dialog_prompt";

    private static final String PREF_SILENT = "silent_dialog_prompt";

    private static final String PREF_EXTEND = "extend_dialog_prompt";

    private static final String PREF_AIRPLANE = "airplane_dialog_prompt";

    private static final String PREF_POWER_SAVER = "powersaver_dialog_prompt";

    private static final String PREF_SCREENSHOT = "screenshot_dialog_prompt";

    private static final String PREF_SUSPEND = "suspend_dialog_prompt";

    private static final String PREF_HIBERNATE = "hibernate_dialog_prompt";

    private static final String PREF_NAVBAR = "navbar_dialog_prompt";

    private static final String POWER_PROMPT_PREF = "power_dialog_prompt";

    private CheckBoxPreference mShowProfile;

    private CheckBoxPreference mShowSilent;

    private CheckBoxPreference mShowExtend;

    private CheckBoxPreference mShowAirplane;

    private CheckBoxPreference mShowScreenshot;

    private CheckBoxPreference mShowPowersaver;

    private CheckBoxPreference mShowSuspend;

    private CheckBoxPreference mShowHibernate;

    private CheckBoxPreference mShowNavbar;

    private Preference mSquadzone;

    private CheckBoxPreference mPowerPromptPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.interface_powermenu_settings_title_head);
        addPreferencesFromResource(R.xml.ui_globalsettings);

        PreferenceScreen prefSet = getPreferenceScreen();

        mSquadzone = (Preference) prefSet.findPreference(PREF_SQUADZONE);
        mSquadzone.setSummary("CyanMobile");

        mPowerPromptPref = (CheckBoxPreference) prefSet.findPreference(POWER_PROMPT_PREF);
        mPowerPromptPref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_DIALOG_PROMPT, 1) == 1);

        mShowProfile = (CheckBoxPreference) findPreference(PREF_PROFILE);
        mShowProfile.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_DIALOG_SHOW_PROFILE, 0) == 1);

        mShowSilent = (CheckBoxPreference) findPreference(PREF_SILENT);
        mShowSilent.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_DIALOG_SHOW_SILENT, 1) == 1);

        mShowExtend = (CheckBoxPreference) findPreference(PREF_EXTEND);
        mShowExtend.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_DIALOG_SHOW_EXTEND, 1) == 1);

        mShowAirplane = (CheckBoxPreference) findPreference(PREF_AIRPLANE);
        mShowAirplane.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_DIALOG_SHOW_AIRPLANE, 1) == 1);

        mShowScreenshot = (CheckBoxPreference) findPreference(PREF_SCREENSHOT);
        mShowScreenshot.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_DIALOG_SHOW_SCREENSHOT, 1) == 1);

        mShowSuspend = (CheckBoxPreference) findPreference(PREF_SUSPEND);
        mShowSuspend.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_DIALOG_SHOW_SUSPEND, 1) == 1);

        mShowHibernate = (CheckBoxPreference) findPreference(PREF_HIBERNATE);
        mShowHibernate.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_DIALOG_SHOW_HIBERNATE, 1) == 1);

        mShowNavbar = (CheckBoxPreference) findPreference(PREF_NAVBAR);
        mShowNavbar.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_DIALOG_SHOW_NAVI, 1) == 1);

        mShowPowersaver = (CheckBoxPreference) findPreference(PREF_POWER_SAVER);
        mShowPowersaver.setChecked(Settings.Secure.getInt(getContentResolver(),
                Settings.Secure.POWER_DIALOG_SHOW_POWER_SAVER, 0) == 1);

    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        /* Preference Screens */
        if (preference == mPowerPromptPref) {
            value = mPowerPromptPref.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.POWER_DIALOG_PROMPT,
                    value ? 1 : 0);
            return true;
        } else if (preference == mShowProfile) {
            value = mShowProfile.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.POWER_DIALOG_SHOW_PROFILE,
                    value ? 1 : 0);
            return true;
        } else if (preference == mShowSilent) {
            value = mShowSilent.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.POWER_DIALOG_SHOW_SILENT,
                    value ? 1 : 0);
            return true;
        } else if (preference == mShowExtend) {
            value = mShowExtend.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.POWER_DIALOG_SHOW_EXTEND,
                    value ? 1 : 0);
            return true;
        } else if (preference == mShowAirplane) {
            value = mShowAirplane.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.POWER_DIALOG_SHOW_AIRPLANE,
                    value ? 1 : 0);
            return true;
        } else if (preference == mShowSuspend) {
            value = mShowSuspend.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.POWER_DIALOG_SHOW_SUSPEND,
                    value ? 1 : 0);
            return true;
        } else if (preference == mShowHibernate) {
            value = mShowHibernate.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.POWER_DIALOG_SHOW_HIBERNATE,
                    value ? 1 : 0);
            return true;
        } else if (preference == mShowNavbar) {
            value = mShowNavbar.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.POWER_DIALOG_SHOW_NAVI,
                    value ? 1 : 0);
            return true;
        } else if (preference == mShowScreenshot) {
            value = mShowScreenshot.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.POWER_DIALOG_SHOW_SCREENSHOT,
                    value ? 1 : 0);
            return true;
        } else if (preference == mShowPowersaver) {
            value = mShowPowersaver.isChecked();
            Settings.Secure.putInt(getContentResolver(), Settings.Secure.POWER_DIALOG_SHOW_POWER_SAVER,
                    value ? 1 : 0);
            return true;
        }
        return false;
    }
}
