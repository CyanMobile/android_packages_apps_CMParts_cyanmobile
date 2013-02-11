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
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.CmSystem;
import android.provider.Settings;
import java.io.IOException;

import com.cyanogenmod.cmparts.R;

public class TabletTweaksActivity extends PreferenceActivity implements OnPreferenceChangeListener{
    private static final String PREF_STATUS_BAR_BOTTOM = "pref_status_bar_bottom";
    private static final String PREF_STATUS_BAR_SOFT = "pref_status_bar_soft";
    private static final String PREF_SQUADZONE = "squadkeys";
    private static final String PREF_STATUS_BAR_DEAD_ZONE = "pref_status_bar_dead_zone";
    private static final String PREF_SOFT_BUTTONS_LEFT = "pref_soft_buttons_left";
    private static final String PREF_DISABLE_LOCKSCREEN = "pref_disable_lockscreen";
    private static final String PREF_DISABLE_FULLSCREEN = "pref_disable_fullscreen";
    private static final String PREF_UNHIDE_BUTTON = "pref_unhide_button";
    private static final String PREF_EXTEND_PM = "pref_extend_pm";
    private static final String PREF_REVERSE_VOLUME_BEHAVIOR = "pref_reverse_volume_behavior";
    private static final String PREF_VOLUME_REMAP_BEHAVIOR = "pref_volume_remap_behavior";
    private static final String PREF_GENERAL_CATEGORY = "pref_general_category";
    private static final String PREF_INTERFACE_CATEGORY = "pref_interface_category";
    private static final String PREF_BUTTON_CATEGORY = "pref_button_category";
    private static final String PREF_EXTEND_PM_LIST = "pref_extend_pm_list";
    private static final String PREF_SOFT_BUTTON_LIST = "pref_soft_button_list";

    static Context mContext;

    private CheckBoxPreference mStatusBarBottom;
    private CheckBoxPreference mStatusBarSoft;
    private CheckBoxPreference mStatusBarDeadZone;
    private CheckBoxPreference mSoftButtonsLeft;
    private CheckBoxPreference mDisableLockscreen;
    private CheckBoxPreference mDisableFullscreen;
    private CheckBoxPreference mExtendPm;
    private Preference mSquadzone;
    private CheckBoxPreference mReverseVolumeBehavior;
    private CheckBoxPreference mVolumeRemapBehavior;
    private ListPreference mUnhideButton;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.tablet_tweaks_title_head);
        addPreferencesFromResource(R.xml.tablet_settings);

		mContext = this.getBaseContext();

        PreferenceScreen prefSet = getPreferenceScreen();

        mSquadzone = (Preference) prefSet.findPreference(PREF_SQUADZONE);
        mSquadzone.setSummary("CyanMobile");

        mStatusBarBottom = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_BOTTOM);
        mStatusBarSoft = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_SOFT);
        mStatusBarDeadZone = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_DEAD_ZONE);
        mSoftButtonsLeft = (CheckBoxPreference) prefSet.findPreference(PREF_SOFT_BUTTONS_LEFT);
        mDisableLockscreen = (CheckBoxPreference) prefSet.findPreference(PREF_DISABLE_LOCKSCREEN);
        mDisableFullscreen = (CheckBoxPreference) prefSet.findPreference(PREF_DISABLE_FULLSCREEN);
        mUnhideButton = (ListPreference) prefSet.findPreference(PREF_UNHIDE_BUTTON);
        mExtendPm = (CheckBoxPreference) prefSet.findPreference(PREF_EXTEND_PM);
        mReverseVolumeBehavior = (CheckBoxPreference) prefSet.findPreference(PREF_REVERSE_VOLUME_BEHAVIOR);
        mVolumeRemapBehavior = (CheckBoxPreference) prefSet.findPreference(PREF_VOLUME_REMAP_BEHAVIOR);

        int defValue;

        defValue=CmSystem.getDefaultBool(getBaseContext(), CmSystem.CM_DEFAULT_BOTTOM_STATUS_BAR)==true ? 1 : 0;
        mStatusBarBottom.setChecked((Settings.System.getInt(getContentResolver(),
               Settings.System.STATUS_BAR_BOTTOM, 0) == 1));
        mStatusBarSoft.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.USE_SOFT_BUTTONS, 0) == 1));
        defValue=CmSystem.getDefaultBool(getBaseContext(), CmSystem.CM_DEFAULT_USE_DEAD_ZONE)==true ? 1 : 0;
        mStatusBarDeadZone.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_DEAD_ZONE, defValue) == 1));
        defValue=CmSystem.getDefaultBool(getBaseContext(), CmSystem.CM_DEFAULT_SOFT_BUTTONS_LEFT)==true ? 1 : 0;
        mSoftButtonsLeft.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.SOFT_BUTTONS_LEFT, defValue) == 1));
        defValue=CmSystem.getDefaultBool(getBaseContext(), CmSystem.CM_DEFAULT_DISABLE_LOCKSCREEN)==true ? 1 : 0;
        mDisableLockscreen.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_DISABLED, defValue) == 1));
        defValue=CmSystem.getDefaultBool(getBaseContext(), CmSystem.CM_DEFAULT_DISABLE_FULLSCREEN)==true ? 1 : 0;
        mDisableFullscreen.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.FULLSCREEN_DISABLED, defValue) == 1));
        defValue=CmSystem.getDefaultBool(getBaseContext(), CmSystem.CM_DEFAULT_EXTEND_POWER_MENU)==true ? 1 : 0;
        mExtendPm.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.EXTEND_PM, 0) == 1));
        defValue=CmSystem.getDefaultBool(getBaseContext(), CmSystem.CM_DEFAULT_REVERSE_VOLUME_BEHAVIOR)==true ? 1 : 0;
        mReverseVolumeBehavior.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.REVERSE_VOLUME_BEHAVIOR, defValue) == 1));
        mVolumeRemapBehavior.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.VOLUME_REMAP_BEHAVIOR, 0) == 1));

        defValue=CmSystem.getDefaultInt(getBaseContext(), CmSystem.CM_DEFAULT_UNHIDE_BUTTON_INDEX);
        mUnhideButton.setOnPreferenceChangeListener(this);
        mUnhideButton.setValueIndex(Settings.System.getInt(getContentResolver(),
                Settings.System.UNHIDE_BUTTON, defValue));

        if (Settings.System.getInt(getContentResolver(),
                            Settings.System.EXPANDED_VIEW_WIDGET, 5) == 4) {
            new AlertDialog.Builder(this)
            .setTitle("Changing Status Bar Layout")
            .setMessage("System has detect you are using Tab layout.\nneed change to default before enable statusbar bottom option.\nRestart statusbar now?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET, 1);
                        restartStatusBar();
                    }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                         mStatusBarBottom.setEnabled(false);
                    }
             })
            .show();
        } else if (Settings.System.getInt(getContentResolver(),
                            Settings.System.EXPANDED_VIEW_WIDGET, 5) == 5) {
            new AlertDialog.Builder(this)
            .setTitle("Changing Status Bar Layout")
            .setMessage("System has detect you are using TileView layout.\nneed change to default before enable statusbar bottom option.\nRestart statusbar now?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET, 1);
                        restartStatusBar();
                    }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                         mStatusBarBottom.setEnabled(false);
                    }
             })
            .show();
        } else if (Settings.System.getInt(getContentResolver(),
                            Settings.System.EXPANDED_VIEW_WIDGET, 5) == 3) {
            new AlertDialog.Builder(this)
            .setTitle("Changing Status Bar Layout")
            .setMessage("System has detect you are using Grid layout.\nneed change to default before enable statusbar bottom option.\nSet to default now?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                       Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET, 1);
                    }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                         mStatusBarBottom.setEnabled(false);
                    }
             })
            .show();
        }

        updateDependencies();
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        /* Preference Screens */
        if (preference == mStatusBarBottom) {
            value = mStatusBarBottom.isChecked();
            if (value) {
                 if (Settings.System.getInt(getContentResolver(), Settings.System.STATUS_BAR_CARRIER, 6) == 4) {
                     Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CARRIER, 6);
                 }
                 Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET, 1);
                 updateDependencies();
                 if (Settings.System.getInt(getContentResolver(),
                             Settings.System.SHOW_NAVI_BUTTONS, 1) == 1) {
                     Settings.System.putInt(getContentResolver(), Settings.System.SHOW_NAVI_BUTTONS, 0);
                     Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BOTTOM, 1);
                     updateDependencies();
                     restartStatusBar();
                 } else {
                     Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BOTTOM, 1);
                     updateDependencies();
                 }
            } else {
                 Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BOTTOM, 0);
            }
            return true;
        } else if (preference == mStatusBarSoft) {
            value = mStatusBarSoft.isChecked();
            if (value)  {
               Settings.System.putInt(getContentResolver(), Settings.System.USE_SOFT_BUTTONS, 1);
               restartStatusBar();
            } else {
               Settings.System.putInt(getContentResolver(), Settings.System.USE_SOFT_BUTTONS, 0);
               restartStatusBar();
            }
            return true;
        } else if (preference == mStatusBarDeadZone) {
            value = mStatusBarDeadZone.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_DEAD_ZONE,
                    value ? 1 : 0);
            return true;
        } else if (preference == mSoftButtonsLeft) {
            value = mSoftButtonsLeft.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.SOFT_BUTTONS_LEFT,
                    value ? 1 : 0);
            return true;
        } else if (preference == mDisableLockscreen) {
            value = mDisableLockscreen.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.LOCKSCREEN_DISABLED,
                    value ? 1 : 0);
            return true;
        } else if (preference == mDisableFullscreen) {
            value = mDisableFullscreen.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.FULLSCREEN_DISABLED,
                    value ? 1 : 0);
            return true;
        } else if (preference == mExtendPm) {
            value = mExtendPm.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.EXTEND_PM,
                    value ? 1 : 0);
            return true;
        } else if (preference == mReverseVolumeBehavior) {
            value = mReverseVolumeBehavior.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.REVERSE_VOLUME_BEHAVIOR,
                    value ? 1 : 0);
            updateDependencies();
            return true;
        } else if (preference == mVolumeRemapBehavior) {
            value = mVolumeRemapBehavior.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.VOLUME_REMAP_BEHAVIOR,
                    value ? 1 : 0);
            updateDependencies();
            return true;
        }

        return false;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mUnhideButton) {
            int value = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.UNHIDE_BUTTON, value);
            return true;
        }
        return false;
    }

    private void restartStatusBar() {
        try {
            Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
        } catch (IOException e) {
           // we're screwed here fellas
        }
    }

    private void updateDependencies() {
        if(!mStatusBarBottom.isChecked()){
            mStatusBarDeadZone.setChecked(false);
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_DEAD_ZONE, 0);
        }

        if (mReverseVolumeBehavior.isChecked()) {
            mReverseVolumeBehavior.setSummary(R.string.tablet_tweaks_reverse_volume_behavior_summary_on);
        } else {
            mReverseVolumeBehavior.setSummary(R.string.tablet_tweaks_reverse_volume_behavior_summary_off);
        }

        if (mVolumeRemapBehavior.isChecked()) {
            mVolumeRemapBehavior.setSummary(R.string.tablet_tweaks_volume_remap_behavior_summary_on);
        } else {
            mVolumeRemapBehavior.setSummary(R.string.tablet_tweaks_volume_remap_behavior_summary_off);
        }

    }
}
