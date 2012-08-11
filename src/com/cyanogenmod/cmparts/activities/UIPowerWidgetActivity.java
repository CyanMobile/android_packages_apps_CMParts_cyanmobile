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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import java.io.IOException;

import com.cyanogenmod.cmparts.R;

public class UIPowerWidgetActivity extends PreferenceActivity
        implements OnPreferenceChangeListener {

    private static final String UI_EXP_WIDGET = "expanded_widget";

    private static final String PREF_SQUADZONE = "squadkeys";

    private static final String UI_EXP_WIDGET_HIDE_ONCHANGE = "expanded_hide_onchange";

    private static final String UI_EXP_WIDGET_HIDE_INDICATOR = "expanded_hide_indicator";

    private static final String UI_EXP_WIDGET_HIDE_SCROLLBAR = "expanded_hide_scrollbar";

    private static final String UI_EXP_WIDGET_HAPTIC_FEEDBACK = "expanded_haptic_feedback";

    private static final String UI_EXP_WIDGET_COLOR = "expanded_color_mask";

    private static final String UI_EXP_WIDGET_PICKER = "widget_picker";

    private static final String UI_EXP_WIDGET_ORDER = "widget_order";

    private static final String POWER_WIDGET_LOC = "pref_widget_loc";

    private static final String MUSIC_WIDGET_BUTTON = "pref_music_widget";

    private static final String POWER_WIDGET_GRID = "pref_widget_grid";

    private static final String POWER_WIDGET_TAB = "pref_widget_tab";

    private static final String POWER_WIDGET_GRID_ONE = "pref_widget_grid_one";

    private static final String POWER_WIDGET_GRID_TWO = "pref_widget_grid_two";

    private static final String POWER_WIDGET_GRID_THREE = "pref_widget_grid_three";

    private static final String POWER_WIDGET_GRID_FOUR = "pref_widget_grid_four";

    private CheckBoxPreference mPowerWidget;

    private CheckBoxPreference mMusicWidget;

    private CheckBoxPreference mPowerWidgetLoc;

    private CheckBoxPreference mPowerWidgetGrid;

    private CheckBoxPreference mPowerWidgetTab;

    private CheckBoxPreference mPowerWidgetGridOne;

    private CheckBoxPreference mPowerWidgetGridTwo;

    private CheckBoxPreference mPowerWidgetGridThree;

    private CheckBoxPreference mPowerWidgetGridFour;

    private CheckBoxPreference mPowerWidgetHideOnChange;

    private CheckBoxPreference mPowerWidgetIndicatorHide;

    private CheckBoxPreference mPowerWidgetHideScrollBar;

    private ListPreference mPowerWidgetHapticFeedback;

    private Preference mPowerWidgetColor;

    private PreferenceScreen mPowerPicker;

    private Preference mSquadzone;

    private PreferenceScreen mPowerOrder;

    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.title_expanded_widget);
        addPreferencesFromResource(R.xml.ui_power_widget_settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        mSquadzone = (Preference) prefSet.findPreference(PREF_SQUADZONE);
        mSquadzone.setSummary("CyanMobile");

        /* Expanded View Power Widget */
        mPowerWidget = (CheckBoxPreference) prefSet.findPreference(UI_EXP_WIDGET);
        mMusicWidget = (CheckBoxPreference) prefSet.findPreference(MUSIC_WIDGET_BUTTON);
        mPowerWidgetLoc = (CheckBoxPreference) prefSet.findPreference(POWER_WIDGET_LOC);
        mPowerWidgetGrid = (CheckBoxPreference) prefSet.findPreference(POWER_WIDGET_GRID);
        mPowerWidgetTab = (CheckBoxPreference) prefSet.findPreference(POWER_WIDGET_TAB);
        mPowerWidgetGridOne = (CheckBoxPreference) prefSet.findPreference(POWER_WIDGET_GRID_ONE);
        mPowerWidgetGridTwo = (CheckBoxPreference) prefSet.findPreference(POWER_WIDGET_GRID_TWO);
        mPowerWidgetGridThree = (CheckBoxPreference) prefSet.findPreference(POWER_WIDGET_GRID_THREE);
        mPowerWidgetGridFour = (CheckBoxPreference) prefSet.findPreference(POWER_WIDGET_GRID_FOUR);
        mPowerWidgetHideOnChange = (CheckBoxPreference) prefSet
                .findPreference(UI_EXP_WIDGET_HIDE_ONCHANGE);
        mPowerWidgetHideScrollBar = (CheckBoxPreference) prefSet
                .findPreference(UI_EXP_WIDGET_HIDE_SCROLLBAR);
        mPowerWidgetIndicatorHide = (CheckBoxPreference) prefSet
                .findPreference(UI_EXP_WIDGET_HIDE_INDICATOR);
        mPowerWidgetHapticFeedback = (ListPreference) prefSet
                .findPreference(UI_EXP_WIDGET_HAPTIC_FEEDBACK);
        mPowerWidgetHapticFeedback.setOnPreferenceChangeListener(this);

        mPowerWidgetColor = prefSet.findPreference(UI_EXP_WIDGET_COLOR);
        mPowerPicker = (PreferenceScreen) prefSet.findPreference(UI_EXP_WIDGET_PICKER);
        mPowerOrder = (PreferenceScreen) prefSet.findPreference(UI_EXP_WIDGET_ORDER);

        mPowerWidget.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_VIEW_WIDGET, 0) == 1));
        mMusicWidget.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.MUSIC_WIDGET_TOGGLE, 0) == 1));
        mPowerWidgetLoc.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_VIEW_WIDGET, 0) == 2));
        mPowerWidgetGrid.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_VIEW_WIDGET, 0) == 3));
        mPowerWidgetTab.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_VIEW_WIDGET, 0) == 4));
        mPowerWidgetGridOne.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_VIEW_WIDGET_GRID_ONE, 1) == 1));
        mPowerWidgetGridTwo.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_VIEW_WIDGET_GRID_TWO, 1) == 1));
        mPowerWidgetGridThree.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_VIEW_WIDGET_GRID_THREE, 1) == 1));
        mPowerWidgetGridFour.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_VIEW_WIDGET_GRID_FOUR, 1) == 1));

        if (Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_BOTTOM, 0) == 1) {
            mPowerWidgetGrid.setEnabled(false);
            mPowerWidgetLoc.setEnabled(false);
            mPowerWidgetTab.setEnabled(false);
            mPowerWidgetGridOne.setEnabled(false);
            mPowerWidgetGridTwo.setEnabled(false);
            mPowerWidgetGridThree.setEnabled(false);
            mPowerWidgetGridFour.setEnabled(false);
        } else if (Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_VIEW_WIDGET, 0) == 3) {
            mPowerWidgetGridOne.setEnabled(true);
            mPowerWidgetGridTwo.setEnabled(true);
            mPowerWidgetGridThree.setEnabled(true);
            mPowerWidgetGridFour.setEnabled(true);
        } else if (Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_VIEW_WIDGET, 0) == 4) {
            mPowerWidgetGrid.setEnabled(false);
            mPowerWidgetLoc.setEnabled(false);
            mPowerWidget.setEnabled(false);
            mPowerWidgetGridOne.setChecked(true);
            mPowerWidgetGridTwo.setChecked(true);
            mPowerWidgetGridThree.setChecked(true);
            mPowerWidgetGridFour.setChecked(true);
        } else {
            mPowerWidgetGridOne.setEnabled(false);
            mPowerWidgetGridTwo.setEnabled(false);
            mPowerWidgetGridThree.setEnabled(false);
            mPowerWidgetGridFour.setEnabled(false);
            mPowerWidgetGrid.setEnabled(true);
            mPowerWidgetTab.setEnabled(true);
            mPowerWidgetLoc.setEnabled(true);
        }

        mPowerWidgetHideOnChange.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_HIDE_ONCHANGE, 0) == 1));
        mPowerWidgetHideScrollBar.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_HIDE_SCROLLBAR, 1) == 1));
        mPowerWidgetIndicatorHide.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_HIDE_INDICATOR, 1) == 1));
        mPowerWidgetHapticFeedback.setValue(Integer.toString(Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_HAPTIC_FEEDBACK, 2)));

        // Set up the warning
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("CyanMobile Notice");
        alertDialog.setMessage(getResources().getString(R.string.powerwidget_notice_summary));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                getResources().getString(com.android.internal.R.string.ok),
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        
        alertDialog.show();

    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mPowerWidgetHapticFeedback) {
            int intValue = Integer.parseInt((String)newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_HAPTIC_FEEDBACK, intValue);
            return true;
        }

        return false;
    }



    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        /* Preference Screens */
        if (preference == mPowerPicker) {
            startActivity(mPowerPicker.getIntent());
        }
        if (preference == mPowerOrder) {
            startActivity(mPowerOrder.getIntent());
        }

        if (preference == mPowerWidget) {
            value = mPowerWidget.isChecked();
            if (value) {
            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET, 1);
                            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_ONE, 0);
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_TWO, 0);
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_THREE, 0);
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_FOUR, 0);
                mPowerWidgetGrid.setChecked(false);
                mPowerWidgetLoc.setChecked(false);
                mPowerWidgetTab.setChecked(false);
                mPowerWidgetGridOne.setEnabled(false);
                mPowerWidgetGridTwo.setEnabled(false);
                mPowerWidgetGridThree.setEnabled(false);
                mPowerWidgetGridFour.setEnabled(false);
            } else {
            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET, 0);
            }
        }

        if (preference == mMusicWidget) {
            value = mMusicWidget.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.MUSIC_WIDGET_TOGGLE,
                    value ? 1 : 0);
        }

        if (preference == mPowerWidgetGridOne) {
            value = mPowerWidgetGridOne.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_ONE,
                    value ? 1 : 0);
        }

        if (preference == mPowerWidgetGridTwo) {
            value = mPowerWidgetGridTwo.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_TWO,
                    value ? 1 : 0);
        }

        if (preference == mPowerWidgetGridThree) {
            value = mPowerWidgetGridThree.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_THREE,
                    value ? 1 : 0);
        }

        if (preference == mPowerWidgetGridFour) {
            value = mPowerWidgetGridFour.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_FOUR,
                    value ? 1 : 0);
        }

        if (preference == mPowerWidgetLoc) {
            boolean checked = ((CheckBoxPreference) preference).isChecked();
            
            if(checked) {
                Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET, 2);
                            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_ONE, 0);
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_TWO, 0);
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_THREE, 0);
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_FOUR, 0);
                mPowerWidgetGrid.setChecked(false);
                mPowerWidget.setChecked(false);
                mPowerWidgetGridOne.setEnabled(false);
                mPowerWidgetGridTwo.setEnabled(false);
                mPowerWidgetGridThree.setEnabled(false);
                mPowerWidgetGridFour.setEnabled(false);
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET, 1);
                mPowerWidget.setChecked(true);
            }
        }

        if (preference == mPowerWidgetGrid) {
            boolean checked = ((CheckBoxPreference) preference).isChecked();
            if(checked) {
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET, 3);
                                mPowerWidgetLoc.setChecked(false);
                                mPowerWidget.setChecked(false);
                                mPowerWidgetGridOne.setEnabled(true);
                                mPowerWidgetGridTwo.setEnabled(true);
                                mPowerWidgetGridThree.setEnabled(true);
                                mPowerWidgetGridFour.setEnabled(true);
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET, 1);
                            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_ONE, 0);
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_TWO, 0);
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_THREE, 0);
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_FOUR, 0);
                                mPowerWidgetGridOne.setChecked(false);
                                mPowerWidgetGridTwo.setChecked(false);
                                mPowerWidgetGridThree.setChecked(false);
                                mPowerWidgetGridFour.setChecked(false);
                mPowerWidget.setChecked(true);
                mPowerWidgetGridOne.setEnabled(false);
                mPowerWidgetGridTwo.setEnabled(false);
                mPowerWidgetGridThree.setEnabled(false);
                mPowerWidgetGridFour.setEnabled(false);
            }
        }

        if (preference == mPowerWidgetTab) {
            boolean checked = ((CheckBoxPreference) preference).isChecked();
            if(checked) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_CENTER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_LEFT, 1);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_DATE, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_HIDE_CARRIER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_COMPACT_CARRIER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LABEL_BOTTOM, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO_CENTER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO_LEFT, 0);
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET, 4);
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_ONE, 1);
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_TWO, 1);
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_THREE, 1);
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_FOUR, 1);
                                mPowerWidgetLoc.setChecked(false);
                                mPowerWidget.setChecked(false);
                                mPowerWidgetGrid.setChecked(false);
                                mPowerWidgetLoc.setEnabled(false);
                                mPowerWidget.setEnabled(false);
                mPowerWidgetGridOne.setEnabled(true);
                mPowerWidgetGridTwo.setEnabled(true);
                mPowerWidgetGridThree.setEnabled(true);
                mPowerWidgetGridFour.setEnabled(true);
                                mPowerWidgetGrid.setEnabled(false);
                                mPowerWidgetGridOne.setChecked(true);
                                mPowerWidgetGridTwo.setChecked(true);
                                mPowerWidgetGridThree.setChecked(true);
                                mPowerWidgetGridFour.setChecked(true);
            } else {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                       Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET, 1);
                            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_ONE, 0);
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_TWO, 0);
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_THREE, 0);
                             Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_GRID_FOUR, 0);
                         mPowerWidget.setChecked(true);
                         mPowerWidgetLoc.setEnabled(true);
                         mPowerWidget.setEnabled(true);
                         mPowerWidgetGrid.setEnabled(true);
                mPowerWidgetGridOne.setEnabled(false);
                mPowerWidgetGridTwo.setEnabled(false);
                mPowerWidgetGridThree.setEnabled(false);
                mPowerWidgetGridFour.setEnabled(false);
                                mPowerWidgetGridOne.setChecked(false);
                                mPowerWidgetGridTwo.setChecked(false);
                                mPowerWidgetGridThree.setChecked(false);
                                mPowerWidgetGridFour.setChecked(false);
            }
        }

        if (preference == mPowerWidgetHideOnChange) {
            value = mPowerWidgetHideOnChange.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_HIDE_ONCHANGE,
                    value ? 1 : 0);
        }

        if (preference == mPowerWidgetHideScrollBar) {
            value = mPowerWidgetHideScrollBar.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_HIDE_SCROLLBAR,
                    value ? 1 : 0);
        }

        if (preference == mPowerWidgetIndicatorHide) {
            value = mPowerWidgetIndicatorHide.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_HIDE_INDICATOR,
                    value ? 1 : 0);
        }

        if (preference == mPowerWidgetColor) {
            ColorPickerDialog cp = new ColorPickerDialog(this, mWidgetColorListener,
                    readWidgetColor());
            cp.show();
        }

        return true;
    }

    private int readWidgetColor() {
        try {
            return Settings.System.getInt(getContentResolver(),
                    Settings.System.EXPANDED_VIEW_WIDGET_COLOR);
        } catch (SettingNotFoundException e) {
            return -16777216;
        }
    }

    ColorPickerDialog.OnColorChangedListener mWidgetColorListener = new ColorPickerDialog.OnColorChangedListener() {
        public void colorChanged(int color) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.EXPANDED_VIEW_WIDGET_COLOR, color);
        }

        public void colorUpdate(int color) {
        }
    };

}
