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

import com.cyanogenmod.cmparts.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;

public class CallActivity extends PreferenceActivity implements OnPreferenceChangeListener {

    private static final String PREF_SQUADZONE = "squadkeys";

    private static final String VIBRATE_IN_CALL = "vibrate-in-call";

    private static final String RINGER_LOOP = "ringer-loop";

    private static final String CALL_ME_LOUDER = "call-me-louder";

    private static final String FLIPPING_DOWN_MUTES_RINGER = "flipping-mutes-ringer";

    private static final String FLIPPING_DOWN_SNOOZES_ALARM = "flipping-snoozes-alarm";

    private static final String BACK_BUTTON_ENDS_CALL_PREF = "back-button-ends-call";

    private static final String MENU_BUTTON_ANSWERS_CALL_PREF = "pref_menu_button_answers_call";

    private static final String PICK_UP_TO_CALL_PREF = "pref_pick_up_to_call";

    private static final String INCALL_UI_FORCE_PREF = "pref_incall_ui_force";

    private static final String PREFIX = "persist.sys.";

    private static String getKey(String suffix) {
        return PREFIX + suffix;
    }

    private CheckBoxPreference mBackButtonEndsCall;
    private CheckBoxPreference mMenuButtonAnswersCall;
    private CheckBoxPreference mPickUpToCall;
    private Preference mSquadzone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.calls_settings_title_subhead);
        addPreferencesFromResource(R.xml.calls_settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        mSquadzone = (Preference) prefSet.findPreference(PREF_SQUADZONE);
        mSquadzone.setSummary("CyanMobile");

        CheckBoxPreference p = (CheckBoxPreference) prefSet.findPreference(VIBRATE_IN_CALL);
        p.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.VIBRATE_IN_CALL, 1) != 0);
	p.setOnPreferenceChangeListener(this);
	
        mMenuButtonAnswersCall = (CheckBoxPreference) prefSet.findPreference(MENU_BUTTON_ANSWERS_CALL_PREF);
        mMenuButtonAnswersCall.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.MENU_BUTTON_ANSWERS_CALL, 1) != 0);
        mMenuButtonAnswersCall.setOnPreferenceChangeListener(this);

        mPickUpToCall = (CheckBoxPreference) prefSet.findPreference(PICK_UP_TO_CALL_PREF);
        mPickUpToCall.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.PICK_UP_TO_CALL, 0) == 1);
        mPickUpToCall.setOnPreferenceChangeListener(this);

        p = (CheckBoxPreference) prefSet.findPreference(INCALL_UI_FORCE_PREF);
        p.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.PHONE_FORCE_INCOMING_CALL_UI, 0) != 0);
        p.setOnPreferenceChangeListener(this);

	p = (CheckBoxPreference) prefSet.findPreference(RINGER_LOOP);
        p.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.RINGER_LOOP, 1) != 0));
        p.setOnPreferenceChangeListener(this);

        p = (CheckBoxPreference) prefSet.findPreference(CALL_ME_LOUDER);
        p.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.CALL_ME_LOUDER, 0) != 0));
        p.setOnPreferenceChangeListener(this);

        p = (CheckBoxPreference) prefSet.findPreference(FLIPPING_DOWN_MUTES_RINGER);
        p.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.FLIPPING_DOWN_MUTES_RINGER, 0) != 0);
        p.setOnPreferenceChangeListener(this);

        p = (CheckBoxPreference) prefSet.findPreference(FLIPPING_DOWN_SNOOZES_ALARM);
        p.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.FLIPPING_DOWN_SNOOZES_ALARM, 0) != 0);
        p.setOnPreferenceChangeListener(this);

        mBackButtonEndsCall = (CheckBoxPreference) prefSet.findPreference(BACK_BUTTON_ENDS_CALL_PREF);
        mBackButtonEndsCall.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.BACK_BUTTON_ENDS_CALL, 1) != 0);
        mBackButtonEndsCall.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        if (key.equals(VIBRATE_IN_CALL)) {
            Settings.System.putInt(getContentResolver(), Settings.System.VIBRATE_IN_CALL,
                    getBoolean(newValue) ? 1 : 0);
        } else if (key.equals(CALL_ME_LOUDER)) {
            Settings.System.putInt(getContentResolver(), Settings.System.CALL_ME_LOUDER,
                    getBoolean(newValue) ? 1 : 0);
        } else if (key.equals(FLIPPING_DOWN_MUTES_RINGER)) {
            Settings.System.putInt(getContentResolver(), Settings.System.FLIPPING_DOWN_MUTES_RINGER,
                    getBoolean(newValue) ? 1 : 0);
        } else if (key.equals(FLIPPING_DOWN_SNOOZES_ALARM)) {
            Settings.System.putInt(getContentResolver(), Settings.System.FLIPPING_DOWN_SNOOZES_ALARM,
                    getBoolean(newValue) ? 1 : 0);
        } else if (key.equals(BACK_BUTTON_ENDS_CALL_PREF)) {
            Settings.System.putInt(getContentResolver(), Settings.System.BACK_BUTTON_ENDS_CALL,
                    getBoolean(newValue) ? 1 : 0);
        } else if (key.equals(MENU_BUTTON_ANSWERS_CALL_PREF)) {
            Settings.System.putInt(getContentResolver(), Settings.System.MENU_BUTTON_ANSWERS_CALL,
                    getBoolean(newValue) ? 1 : 0);
        } else if (key.equals(PICK_UP_TO_CALL_PREF)) {
            Settings.System.putInt(getContentResolver(), Settings.System.PICK_UP_TO_CALL,
                    getBoolean(newValue) ? 1 : 0);
        } else if (key.equals(INCALL_UI_FORCE_PREF)) {
            Settings.System.putInt(getContentResolver(), Settings.System.PHONE_FORCE_INCOMING_CALL_UI,
                    getBoolean(newValue) ? 1 : 0);
            Settings.System.putInt(getContentResolver(), Settings.System.MENU_BUTTON_ANSWERS_CALL,
                    getBoolean(newValue) ? 0 : 1);
            Settings.System.putInt(getContentResolver(), Settings.System.BACK_BUTTON_ENDS_CALL,
                    getBoolean(newValue) ? 0 : 1);
            mBackButtonEndsCall.setChecked(getBoolean(newValue) ? false : true);
            mMenuButtonAnswersCall.setChecked(getBoolean(newValue) ? false : true);
        } else if (key.equals(RINGER_LOOP)) {
            Settings.System.putInt(getContentResolver(), Settings.System.RINGER_LOOP,
                    getBoolean(newValue) ? 1 : 0);
        } else {
            SystemProperties.set(getKey(key), String.valueOf(getInt(newValue)));
            mHandler.sendMessage(mHandler.obtainMessage(0, key));
        }
        return true;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (msg.obj != null) {
                        ListPreference p = (ListPreference) findPreference(msg.obj.toString());
                        p.setSummary(p.getEntry());
                    }
                    break;
            }
        }
    };

    private boolean getBoolean(Object o) {
        return Boolean.valueOf(o.toString());
    }

    private int getInt(Object o) {
        return Integer.valueOf(o.toString());
    }
}
