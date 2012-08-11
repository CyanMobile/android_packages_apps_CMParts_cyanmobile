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

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.cyanogenmod.cmparts.R;
import com.cyanogenmod.cmparts.utils.ShortcutPickHelper;

public class ShortcutKeyActivity extends PreferenceActivity implements ShortcutPickHelper.OnPickListener {

    private static final String CUSTOM_SHORTCUT_APP_TOGGLE = "pref_custom_shortcut_app_toggle";

    private static final String CUSTOM_SHORTCUT1_APP_ACTIVITY = "pref_custom_shortcut1_app_activity";

    private static final String CUSTOM_SHORTCUT2_APP_ACTIVITY = "pref_custom_shortcut2_app_activity";

    private static final String CUSTOM_SHORTCUT3_APP_ACTIVITY = "pref_custom_shortcut3_app_activity";

    private static final String CUSTOM_SHORTCUT4_APP_ACTIVITY = "pref_custom_shortcut4_app_activity";

    private static final String CUSTOM_SHORTCUT5_APP_ACTIVITY = "pref_custom_shortcut5_app_activity";

    private static final String CUSTOM_SHORTCUT6_APP_ACTIVITY = "pref_custom_shortcut6_app_activity";

    private static final String CUSTOM_SHORTCUT7_APP_ACTIVITY = "pref_custom_shortcut7_app_activity";

    private static final String CUSTOM_SHORTCUT8_APP_ACTIVITY = "pref_custom_shortcut8_app_activity";

    private static final String CUSTOM_SHORTCUT9_APP_ACTIVITY = "pref_custom_shortcut9_app_activity";

    private static final String CUSTOM_SHORTCUT10_APP_ACTIVITY = "pref_custom_shortcut10_app_activity";

    private static final String CUSTOM_SHORTCUT11_APP_ACTIVITY = "pref_custom_shortcut11_app_activity";

    private static final String CUSTOM_SHORTCUT12_APP_ACTIVITY = "pref_custom_shortcut12_app_activity";

    private static final String CUSTOM_SHORTCUT13_APP_ACTIVITY = "pref_custom_shortcut13_app_activity";

    private static final String CUSTOM_SHORTCUT14_APP_ACTIVITY = "pref_custom_shortcut14_app_activity";

    private static final String CUSTOM_SHORTCUT15_APP_ACTIVITY = "pref_custom_shortcut15_app_activity";

    private static final String CUSTOM_SHORTCUT16_APP_ACTIVITY = "pref_custom_shortcut16_app_activity";

    private CheckBoxPreference mCustomShortcutTogglePref;

    private Preference mCustomShortcut1ActivityPref;

    private Preference mCustomShortcut2ActivityPref;

    private Preference mCustomShortcut3ActivityPref;

    private Preference mCustomShortcut4ActivityPref;

    private Preference mCustomShortcut5ActivityPref;

    private Preference mCustomShortcut6ActivityPref;

    private Preference mCustomShortcut7ActivityPref;

    private Preference mCustomShortcut8ActivityPref;

    private Preference mCustomShortcut9ActivityPref;

    private Preference mCustomShortcut10ActivityPref;

    private Preference mCustomShortcut11ActivityPref;

    private Preference mCustomShortcut12ActivityPref;

    private Preference mCustomShortcut13ActivityPref;

    private Preference mCustomShortcut14ActivityPref;

    private Preference mCustomShortcut15ActivityPref;

    private Preference mCustomShortcut16ActivityPref;

    private ShortcutPickHelper mPicker;

    private int mKeyNumber = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.shortcut_key_title);
        addPreferencesFromResource(R.xml.shortcut_key_settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        mCustomShortcutTogglePref = (CheckBoxPreference) prefSet
                .findPreference(CUSTOM_SHORTCUT_APP_TOGGLE);
        mCustomShortcutTogglePref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.USE_CUSTOM_SHORTCUT_TOGGLE, 0) == 1);
        mCustomShortcut1ActivityPref = (Preference) prefSet
                .findPreference(CUSTOM_SHORTCUT1_APP_ACTIVITY);
        mCustomShortcut2ActivityPref = (Preference) prefSet
                .findPreference(CUSTOM_SHORTCUT2_APP_ACTIVITY);
        mCustomShortcut3ActivityPref = (Preference) prefSet
                .findPreference(CUSTOM_SHORTCUT3_APP_ACTIVITY);
        mCustomShortcut4ActivityPref = (Preference) prefSet
                .findPreference(CUSTOM_SHORTCUT4_APP_ACTIVITY);
        mCustomShortcut5ActivityPref = (Preference) prefSet
                .findPreference(CUSTOM_SHORTCUT5_APP_ACTIVITY);
        mCustomShortcut6ActivityPref = (Preference) prefSet
                .findPreference(CUSTOM_SHORTCUT6_APP_ACTIVITY);
        mCustomShortcut7ActivityPref = (Preference) prefSet
                .findPreference(CUSTOM_SHORTCUT7_APP_ACTIVITY);
        mCustomShortcut8ActivityPref = (Preference) prefSet
                .findPreference(CUSTOM_SHORTCUT8_APP_ACTIVITY);
        mCustomShortcut9ActivityPref = (Preference) prefSet
                .findPreference(CUSTOM_SHORTCUT9_APP_ACTIVITY);
        mCustomShortcut10ActivityPref = (Preference) prefSet
                .findPreference(CUSTOM_SHORTCUT10_APP_ACTIVITY);
        mCustomShortcut11ActivityPref = (Preference) prefSet
                .findPreference(CUSTOM_SHORTCUT11_APP_ACTIVITY);
        mCustomShortcut12ActivityPref = (Preference) prefSet
                .findPreference(CUSTOM_SHORTCUT12_APP_ACTIVITY);
        mCustomShortcut13ActivityPref = (Preference) prefSet
                .findPreference(CUSTOM_SHORTCUT13_APP_ACTIVITY);
        mCustomShortcut14ActivityPref = (Preference) prefSet
                .findPreference(CUSTOM_SHORTCUT14_APP_ACTIVITY);
        mCustomShortcut15ActivityPref = (Preference) prefSet
                .findPreference(CUSTOM_SHORTCUT15_APP_ACTIVITY);
        mCustomShortcut16ActivityPref = (Preference) prefSet
                .findPreference(CUSTOM_SHORTCUT16_APP_ACTIVITY);

        mPicker = new ShortcutPickHelper(this, this);

    }

    @Override
    public void onResume() {
        super.onResume();
        setAppSummary(mCustomShortcut1ActivityPref, Settings.System.USE_CUSTOM_SHORTCUT1_ACTIVITY);
        setAppSummary(mCustomShortcut2ActivityPref, Settings.System.USE_CUSTOM_SHORTCUT2_ACTIVITY);
        setAppSummary(mCustomShortcut3ActivityPref, Settings.System.USE_CUSTOM_SHORTCUT3_ACTIVITY);
        setAppSummary(mCustomShortcut4ActivityPref, Settings.System.USE_CUSTOM_SHORTCUT4_ACTIVITY);
        setAppSummary(mCustomShortcut5ActivityPref, Settings.System.USE_CUSTOM_SHORTCUT5_ACTIVITY);
        setAppSummary(mCustomShortcut6ActivityPref, Settings.System.USE_CUSTOM_SHORTCUT6_ACTIVITY);
        setAppSummary(mCustomShortcut7ActivityPref, Settings.System.USE_CUSTOM_SHORTCUT7_ACTIVITY);
        setAppSummary(mCustomShortcut8ActivityPref, Settings.System.USE_CUSTOM_SHORTCUT8_ACTIVITY);
        setAppSummary(mCustomShortcut9ActivityPref, Settings.System.USE_CUSTOM_SHORTCUT9_ACTIVITY);
        setAppSummary(mCustomShortcut10ActivityPref, Settings.System.USE_CUSTOM_SHORTCUT10_ACTIVITY);
        setAppSummary(mCustomShortcut11ActivityPref, Settings.System.USE_CUSTOM_SHORTCUT11_ACTIVITY);
        setAppSummary(mCustomShortcut12ActivityPref, Settings.System.USE_CUSTOM_SHORTCUT12_ACTIVITY);
        setAppSummary(mCustomShortcut13ActivityPref, Settings.System.USE_CUSTOM_SHORTCUT13_ACTIVITY);
        setAppSummary(mCustomShortcut14ActivityPref, Settings.System.USE_CUSTOM_SHORTCUT14_ACTIVITY);
        setAppSummary(mCustomShortcut15ActivityPref, Settings.System.USE_CUSTOM_SHORTCUT15_ACTIVITY);
        setAppSummary(mCustomShortcut16ActivityPref, Settings.System.USE_CUSTOM_SHORTCUT16_ACTIVITY);
    }

    private void setAppSummary(Preference pref, String key) {
        String value = Settings.System.getString(getContentResolver(), key);
        pref.setSummary(mPicker.getFriendlyNameForUri(value));
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        if (preference == mCustomShortcutTogglePref) {
            value = mCustomShortcutTogglePref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.USE_CUSTOM_SHORTCUT_TOGGLE, value ? 1 : 0);
            return true;
        } else if (preference == mCustomShortcut1ActivityPref) {
            mKeyNumber = 1;
            mPicker.pickShortcut();
            return true;
        } else if (preference == mCustomShortcut2ActivityPref) {
            mKeyNumber = 2;
            mPicker.pickShortcut();
            return true;
        } else if (preference == mCustomShortcut3ActivityPref) {
            mKeyNumber = 3;
            mPicker.pickShortcut();
            return true;
        } else if (preference == mCustomShortcut4ActivityPref) {
            mKeyNumber = 4;
            mPicker.pickShortcut();
            return true;
        } else if (preference == mCustomShortcut5ActivityPref) {
            mKeyNumber = 5;
            mPicker.pickShortcut();
            return true;
        } else if (preference == mCustomShortcut6ActivityPref) {
            mKeyNumber = 6;
            mPicker.pickShortcut();
            return true;
        } else if (preference == mCustomShortcut7ActivityPref) {
            mKeyNumber = 7;
            mPicker.pickShortcut();
            return true;
        } else if (preference == mCustomShortcut8ActivityPref) {
            mKeyNumber = 8;
            mPicker.pickShortcut();
            return true;
        } else if (preference == mCustomShortcut9ActivityPref) {
            mKeyNumber = 9;
            mPicker.pickShortcut();
            return true;
        } else if (preference == mCustomShortcut10ActivityPref) {
            mKeyNumber = 10;
            mPicker.pickShortcut();
            return true;
        } else if (preference == mCustomShortcut11ActivityPref) {
            mKeyNumber = 11;
            mPicker.pickShortcut();
            return true;
        } else if (preference == mCustomShortcut12ActivityPref) {
            mKeyNumber = 12;
            mPicker.pickShortcut();
            return true;
        } else if (preference == mCustomShortcut13ActivityPref) {
            mKeyNumber = 13;
            mPicker.pickShortcut();
            return true;
        } else if (preference == mCustomShortcut14ActivityPref) {
            mKeyNumber = 14;
            mPicker.pickShortcut();
            return true;
        } else if (preference == mCustomShortcut15ActivityPref) {
            mKeyNumber = 15;
            mPicker.pickShortcut();
            return true;
        } else if (preference == mCustomShortcut16ActivityPref) {
            mKeyNumber = 16;
            mPicker.pickShortcut();
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
                key = Settings.System.USE_CUSTOM_SHORTCUT1_ACTIVITY;
                pref = mCustomShortcut1ActivityPref;
                break;
            case 2:
                key = Settings.System.USE_CUSTOM_SHORTCUT2_ACTIVITY;
                pref = mCustomShortcut2ActivityPref;
                break;
            case 3:
                key = Settings.System.USE_CUSTOM_SHORTCUT3_ACTIVITY;
                pref = mCustomShortcut3ActivityPref;
                break;
            case 4:
                key = Settings.System.USE_CUSTOM_SHORTCUT4_ACTIVITY;
                pref = mCustomShortcut4ActivityPref;
                break;
            case 5:
                key = Settings.System.USE_CUSTOM_SHORTCUT5_ACTIVITY;
                pref = mCustomShortcut5ActivityPref;
                break;
            case 6:
                key = Settings.System.USE_CUSTOM_SHORTCUT6_ACTIVITY;
                pref = mCustomShortcut6ActivityPref;
                break;
            case 7:
                key = Settings.System.USE_CUSTOM_SHORTCUT7_ACTIVITY;
                pref = mCustomShortcut7ActivityPref;
                break;
            case 8:
                key = Settings.System.USE_CUSTOM_SHORTCUT8_ACTIVITY;
                pref = mCustomShortcut8ActivityPref;
                break;
            case 9:
                key = Settings.System.USE_CUSTOM_SHORTCUT9_ACTIVITY;
                pref = mCustomShortcut9ActivityPref;
                break;
            case 10:
                key = Settings.System.USE_CUSTOM_SHORTCUT10_ACTIVITY;
                pref = mCustomShortcut10ActivityPref;
                break;
            case 11:
                key = Settings.System.USE_CUSTOM_SHORTCUT11_ACTIVITY;
                pref = mCustomShortcut11ActivityPref;
                break;
            case 12:
                key = Settings.System.USE_CUSTOM_SHORTCUT12_ACTIVITY;
                pref = mCustomShortcut12ActivityPref;
                break;
            case 13:
                key = Settings.System.USE_CUSTOM_SHORTCUT13_ACTIVITY;
                pref = mCustomShortcut13ActivityPref;
                break;
            case 14:
                key = Settings.System.USE_CUSTOM_SHORTCUT14_ACTIVITY;
                pref = mCustomShortcut14ActivityPref;
                break;
            case 15:
                key = Settings.System.USE_CUSTOM_SHORTCUT15_ACTIVITY;
                pref = mCustomShortcut15ActivityPref;
                break;
            case 16:
                key = Settings.System.USE_CUSTOM_SHORTCUT16_ACTIVITY;
                pref = mCustomShortcut16ActivityPref;
                break;
            default:
                return;
        }

        if (Settings.System.putString(getContentResolver(), key, uri)) {
            pref.setSummary(friendlyName);
        }
    }
}
