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
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.Window;
import android.widget.Toast;
import java.util.ArrayList;
import android.provider.MediaStore;

import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;
import android.widget.EditText;
import android.net.Uri;
import android.util.Log;
import java.util.logging.Level;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

import com.cyanogenmod.cmparts.R;
import com.cyanogenmod.cmparts.activities.ColorPickerDialog.OnColorChangedListener;

public class UIStatusBarActivity extends PreferenceActivity implements OnPreferenceChangeListener {

    private static final String PREF_STATUS_BAR_AM_PM = "pref_status_bar_am_pm";

    private static final String PREF_SQUADZONE = "squadkeys";

    private static final String PREF_STATUS_BAR_BATTERY = "pref_status_bar_battery";

    private static final String PREF_STATUS_BAR_BATTERY_STYLE = "pref_status_bar_battery_style";

    private static final String PREF_STATUS_BAR_BATTERY_COLOR = "pref_status_bar_battery_color";

    private static final String PREF_STATUS_BAR_CLOCK = "pref_status_bar_clock";

    private static final String PREF_STATUS_BAR_CM_WIFI_TEXT = "pref_status_bar_cm_wifi_text";

    private static final String PREF_STATUS_BAR_DATE = "pref_status_bar_date";

    private static final String PREF_STATUS_BAR_NOTIF = "pref_status_bar_notif";

    private static final String PREF_STATUS_BAR_ICON = "pref_status_bar_icon";

    private static final String PREF_STATUS_BAR_ALARM = "pref_status_bar_alarm";

    private static final String PREF_STATUS_BAR_CENTERCLOCK = "pref_status_bar_centerclock";

    private static final String PREF_STATUS_BAR_LEFTCLOCK = "pref_status_bar_leftclock";

    private static final String PREF_STATUS_BAR_CLOCKCOLOR = "pref_status_bar_clockcolor";

    private static final String PREF_STATUS_BAR_CARRIERCOLOR = "pref_status_bar_carriercolor";

    private static final String PREF_STATUS_BAR_CARRIER_LABEL =
            "pref_status_bar_carrier_label";

    private static final String PREF_STATUS_BAR_CARRIER_LABEL_CUSTOM =
            "pref_status_bar_carrier_label_custom";

    private static final String PREF_STATUS_BAR_COMPACT_CARRIER = "pref_status_bar_compact_carrier";

    private static final String PREF_STATUS_BAR_BOTTOM_CARRIER = "pref_status_bar_bottom_carrier";

    private static final String PREF_CARRIER_LOGO = "pref_carrier_logo";

    private static final String PREF_CARRIER_LOGO_IMAGE = "pref_carrier_logo_image";

    private static final String PREF_CARRIER_LOGO_CENTER = "pref_carrier_logo_center";

    private static final String PREF_CARRIER_LOGO_LEFT = "pref_carrier_logo_left";

    private static final String PREF_STATUS_BAR_HIDE_CARRIER = "pref_status_bar_hide_carrier";

    private static final String PREF_STATUS_BAR_REVERSE = "pref_status_bar_reverse";

    private static final String PREF_STATUS_BAR_STATUSBAR_CARRIER = "pref_status_bar_statusbar_carrier";

    private static final String PREF_STATUS_BAR_STATUSBAR_CARRIER_CENTER = "pref_status_bar_statusbar_carrier_center";

    private static final String PREF_STATUS_BAR_STATUSBAR_CARRIER_LEFT = "pref_status_bar_statusbar_carrier_left";

    private static final String PREF_STATUS_BAR_BRIGHTNESS_CONTROL =
            "pref_status_bar_brightness_control";

    private static final String PREF_STATUS_BAR_CM_SIGNAL = "pref_status_bar_cm_signal";

    private static final String PREF_STATUS_BAR_WIFI = "pref_status_bar_wifi";

    private static final String PREF_STATUS_BAR_BLUETOOTH = "pref_status_bar_bluetooth";

    private static final String PREF_STATUS_BAR_3G = "pref_status_bar_3g";

    private static final String PREF_STATUS_BAR_GPS = "pref_status_bar_gps";

    private static final String PREF_STATUS_BAR_SYNC = "pref_status_bar_sync";

    private static final String PREF_STATUS_BAR_HEADSET = "pref_status_bar_headset";

    private static final String PREF_STATUS_BAR_HIDDEN = "pref_status_bar_hidden";

    private static final String PREF_STATUS_BAR_FOURG = "pref_status_bar_fourg";

    private static final String MOVE_BACKGROUND_INTENT = "com.cyanogenmod.cmbackgroundchooser.COPY_BACKGROUND";

    private static final String TRANSPARENT_STATUS_BAR_PREF = "pref_transparent_status_bar";

    private static final String PREF_STATUS_BAR_COLOR = "pref_status_bar_color";

    private static final String PREF_NOTIFICATION_BACKGROUND_COLOR = "pref_notification_background_color";

    private static final String PREF_TRANSPARENT_NOTIFICATION_BACKGROUND = "pref_transparent_notification_background";

    private static final String PREF_CLOCKFONTSIZE = "status_bar_clockfontsize";

    private static final String PREF_CARRIERFONTSIZE = "status_bar_carrierfontsize";

    private static final String PREF_ICONFONTSIZE = "status_bar_iconfontsize";

    private static final String PREF_ICONSIZE = "status_bar_iconsize";

    private static final String PREF_STATSIZE = "status_bar_statsize";

    private static final String COLOR_DATE = "color_date";

    private static final String COLOR_NOTIFICATION_TICKER_TEXT = "color_ticker_text";

    private static final String COLOR_NOTIFICATION_NONE = "color_notification_none";

    private static final String COLOR_NOTIFICATION_LATEST = "color_notification_latest";

    private static final String COLOR_NOTIFICATION_ONGOING = "color_notification_ongoing";

    private static final String COLOR_NOTIFICATION_CLEAR_BUTTON = "color_clear_button";

    private static final String COLOR_NOTIFICATION_ITEM_TITLE = "color_notification_item_title";

    private static final String COLOR_NOTIFICATION_ITEM_TEXT = "color_notification_item_text";

    private static final String COLOR_NOTIFICATION_ITEM_TIME = "color_notification_item_time";

    private static final String RESTARTSTATUSBAR_PREF = "restartStatusBar";

    private static final String SETTINGSHORCUT_PREF = "settingStatusBar";

    //private static final String PREF_RECENT_APPS_STATUS_BAR = "pref_recent_apps_status_bar";

    static Context mContext;

    private static final int REQUEST_CODE_PICK_FILE = 999;

    private static final int REQUEST_CODE_LOGO_FILE = 1000;

    private static final int REQUEST_CODE_BACK_FILE = 1001;

    private ListPreference mStatusBarAmPm;

    private ListPreference mStatusBarBattery;

    private ListPreference mStatusBarBatteryStyle;

    private ListPreference mStatusBarBatteryColor;

    private CheckBoxPreference mStatusBarWifi;

    private CheckBoxPreference mStatusBarBluetooth;

    private CheckBoxPreference mStatusBar3g;

    private CheckBoxPreference mStatusBarGPS;

    private CheckBoxPreference mStatusBarSync;

    private ListPreference mStatusBarCmSignal;

    private ListPreference mStatusBarCarrierLabel;

    private CheckBoxPreference mStatusBarClock;

    private CheckBoxPreference mStatusBarHideCarrier;

    private CheckBoxPreference mStatusBarReverse;

    private CheckBoxPreference mStatusBarCarrier;

    private CheckBoxPreference mStatusBarCarrierBottom;

    private CheckBoxPreference mStatusBarCarrierCenter;

    private CheckBoxPreference mStatusBarCarrierLeft;

    private CheckBoxPreference mStatusBarDate;

    private CheckBoxPreference mStatusBarNotif;

    private CheckBoxPreference mStatusBarIcon;

    private CheckBoxPreference mStatusBarAlarm;

    private CheckBoxPreference mStatusBarCenterClock;

    private CheckBoxPreference mStatusBarLeftClock;

    private Preference mStatusBarClockColor;

    private Preference mStatusBarCarrierColor;

    private Preference mStatusBarColor;

    private Preference mSquadzone;

    private Preference mNotificationBackgroundColor;

    private CheckBoxPreference mStatusBarCompactCarrier;

    private CheckBoxPreference mStatusBarCarrierLogo;

    private CheckBoxPreference mStatusBarCarrierLogoImage;

    private CheckBoxPreference mStatusBarCarrierLogoCenter;

    private CheckBoxPreference mStatusBarCarrierLogoLeft;

    private CheckBoxPreference mStatusBarBrightnessControl;

    private CheckBoxPreference mStatusBarHeadset;

    private CheckBoxPreference mStatusBarHidden;

    private CheckBoxPreference mStatusBarCmWifiPref;

    private CheckBoxPreference mStatusBarFourG;

    private ListPreference mStatusBarSetting;

    private EditTextPreference mStatusBarCarrierLabelCustom;

    private ListPreference mTransparentStatusBarPref;

    private ListPreference mTransparentNotificationBackgroundPref;

    private ListPreference mClockFontsize;

    private ListPreference mCarrierFontsize;

    private ListPreference mIconFontsize;

    private ListPreference mIconsize;

    private ListPreference mStatsize;

    private Preference mDateColorPref;

    private Preference mNotifTickerColorPref;

    private Preference mNoNotifColorPref;

    private Preference mLatestNotifColorPref;

    private Preference mOngoingNotifColorPref;

    private Preference mClearLabelColorPref;

    private Preference mNotifItemTitlePref;

    private Preference mNotifItemTextPref;

    private Preference mNotifItemTimePref;

    private Preference mRestartStatusBar;

    //private CheckBoxPreference mRecentAppsStatusBar;

    private AlertDialog alertDialog;

    private File logoBackgroundImage;

    private File backBackgroundImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.ui_status_bar_title);
        addPreferencesFromResource(R.xml.ui_status_bar);

		mContext = this.getBaseContext();

        PreferenceScreen prefSet = getPreferenceScreen();

        mSquadzone = (Preference) prefSet.findPreference(PREF_SQUADZONE);
        mSquadzone.setSummary("CyanMobile");

        mStatusBarClock = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_CLOCK);
        mStatusBarCmWifiPref = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_CM_WIFI_TEXT);
        mStatusBarDate = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_DATE);
        mStatusBarNotif = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_NOTIF);
        mStatusBarIcon = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_ICON);
        mStatusBarAlarm = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_ALARM);
        mStatusBarWifi = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_WIFI);
        mStatusBarBluetooth = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_BLUETOOTH);
        mStatusBar3g = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_3G);
        mStatusBarGPS = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_GPS);
        mStatusBarSync = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_SYNC);
        mStatusBarHideCarrier = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_HIDE_CARRIER);
        mStatusBarReverse = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_REVERSE);
        mStatusBarCarrier = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_STATUSBAR_CARRIER);
        mStatusBarCarrierCenter = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_STATUSBAR_CARRIER_CENTER);
        mStatusBarCarrierLeft = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_STATUSBAR_CARRIER_LEFT);

        mRestartStatusBar = (Preference) prefSet.findPreference(RESTARTSTATUSBAR_PREF);
        mStatusBarSetting = (ListPreference) prefSet.findPreference(SETTINGSHORCUT_PREF);

        mDateColorPref = (Preference) prefSet.findPreference(COLOR_DATE);
        mDateColorPref.setOnPreferenceChangeListener(this);
        mNotifTickerColorPref = (Preference) prefSet.findPreference(COLOR_NOTIFICATION_TICKER_TEXT);
        mNotifTickerColorPref.setOnPreferenceChangeListener(this);
        mNoNotifColorPref = (Preference) prefSet.findPreference(COLOR_NOTIFICATION_NONE);
        mNoNotifColorPref.setOnPreferenceChangeListener(this);
        mLatestNotifColorPref = (Preference) prefSet.findPreference(COLOR_NOTIFICATION_LATEST);
        mLatestNotifColorPref.setOnPreferenceChangeListener(this);
        mOngoingNotifColorPref = (Preference) prefSet.findPreference(COLOR_NOTIFICATION_ONGOING);
        mOngoingNotifColorPref.setOnPreferenceChangeListener(this);
        mClearLabelColorPref = (Preference) prefSet.findPreference(COLOR_NOTIFICATION_CLEAR_BUTTON);
        mClearLabelColorPref.setOnPreferenceChangeListener(this);
        mNotifItemTitlePref = (Preference) prefSet.findPreference(COLOR_NOTIFICATION_ITEM_TITLE);
        mNotifItemTitlePref.setOnPreferenceChangeListener(this);
        mNotifItemTextPref = (Preference) prefSet.findPreference(COLOR_NOTIFICATION_ITEM_TEXT);
        mNotifItemTextPref.setOnPreferenceChangeListener(this);
        mNotifItemTimePref = (Preference) prefSet.findPreference(COLOR_NOTIFICATION_ITEM_TIME);
        mNotifItemTimePref.setOnPreferenceChangeListener(this);

	mStatusBarBatteryColor = (ListPreference) prefSet.
                findPreference(PREF_STATUS_BAR_BATTERY_COLOR);
	mStatusBarCenterClock = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_CENTERCLOCK);
	mStatusBarLeftClock = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_LEFTCLOCK);
        mStatusBarClockColor = (Preference) prefSet.findPreference(PREF_STATUS_BAR_CLOCKCOLOR);
        mStatusBarClockColor.setOnPreferenceChangeListener(this);
        mStatusBarCarrierColor = (Preference) prefSet.findPreference(PREF_STATUS_BAR_CARRIERCOLOR);
        mStatusBarCarrierColor.setOnPreferenceChangeListener(this);
        mStatusBarColor = (Preference) prefSet.findPreference(PREF_STATUS_BAR_COLOR);
        mStatusBarColor.setOnPreferenceChangeListener(this);
        mNotificationBackgroundColor = (Preference) prefSet.findPreference(PREF_NOTIFICATION_BACKGROUND_COLOR);
        mNotificationBackgroundColor.setOnPreferenceChangeListener(this);
        mStatusBarCompactCarrier = (CheckBoxPreference) prefSet
                .findPreference(PREF_STATUS_BAR_COMPACT_CARRIER);
        mStatusBarCarrierBottom = (CheckBoxPreference) prefSet
                .findPreference(PREF_STATUS_BAR_BOTTOM_CARRIER);
        mStatusBarCarrierLogo = (CheckBoxPreference) prefSet
                .findPreference(PREF_CARRIER_LOGO);
        mStatusBarCarrierLogoImage = (CheckBoxPreference) prefSet
                .findPreference(PREF_CARRIER_LOGO_IMAGE);
        mStatusBarCarrierLogoCenter = (CheckBoxPreference) prefSet
                .findPreference(PREF_CARRIER_LOGO_CENTER);
        mStatusBarCarrierLogoLeft = (CheckBoxPreference) prefSet
                .findPreference(PREF_CARRIER_LOGO_LEFT);
        mStatusBarBrightnessControl = (CheckBoxPreference) prefSet
                .findPreference(PREF_STATUS_BAR_BRIGHTNESS_CONTROL);
        mStatusBarHeadset = (CheckBoxPreference) prefSet
                .findPreference(PREF_STATUS_BAR_HEADSET);
        mStatusBarHidden = (CheckBoxPreference) prefSet
                .findPreference(PREF_STATUS_BAR_HIDDEN);
        mStatusBarFourG = (CheckBoxPreference) prefSet
                .findPreference(PREF_STATUS_BAR_FOURG);
        // clock font size
        mClockFontsize = (ListPreference) prefSet.findPreference(PREF_CLOCKFONTSIZE);
        mClockFontsize.setOnPreferenceChangeListener(this);
        mClockFontsize.setValue(Integer.toString(Settings.System.getInt(getContentResolver(),
                Settings.System.STATUSBAR_CLOCK_FONT_SIZE, 10)));
        // carrier font size
        mCarrierFontsize = (ListPreference) prefSet.findPreference(PREF_CARRIERFONTSIZE);
        mCarrierFontsize.setOnPreferenceChangeListener(this);
        mCarrierFontsize.setValue(Integer.toString(Settings.System.getInt(getContentResolver(),
                Settings.System.STATUSBAR_CARRIER_FONT_SIZE, 10)));
        // icon font size
        mIconFontsize = (ListPreference) prefSet.findPreference(PREF_ICONFONTSIZE);
        mIconFontsize.setOnPreferenceChangeListener(this);
        mIconFontsize.setValue(Integer.toString(Settings.System.getInt(getContentResolver(),
                Settings.System.STATUSBAR_ICON_FONT_SIZE, 10)));
        // icon size
        mIconsize = (ListPreference) prefSet.findPreference(PREF_ICONSIZE);
        mIconsize.setOnPreferenceChangeListener(this);
        mIconsize.setValue(Integer.toString(Settings.System.getInt(getContentResolver(),
                Settings.System.STATUSBAR_ICONS_SIZE, 25)));

        mStatsize = (ListPreference) prefSet.findPreference(PREF_STATSIZE);
        mStatsize.setOnPreferenceChangeListener(this);
        mStatsize.setValue(Integer.toString(Settings.System.getInt(getContentResolver(),
                Settings.System.STATUSBAR_STATS_SIZE, 25)));

        // wifi
        mStatusBarCmWifiPref.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_CM_WIFI_TEXT, 0) == 1));
        // right clock
        mStatusBarClock.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_CLOCK, 1) == 1));
        // center clock
	mStatusBarCenterClock.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_CENTERCLOCK, 0) == 1));
        // left clock
	mStatusBarLeftClock.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_LEFTCLOCK, 0) == 1));
        // date
        mStatusBarDate.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_DATE, 1) == 1));
        // hide all icon
        mStatusBarIcon.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_ALARM, 1) != 1) && (Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_NOTIF, 1) != 1) && (Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_WIFI, 1) != 1) && (Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_BLUETOOTH, 1) != 1) && (Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_3G, 1) != 1) && (Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_GPS, 1) != 1) && (Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_SYNC, 1) != 1) && (Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_CM_SIGNAL_TEXT, 0) == 4));
        // notif
        mStatusBarNotif.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_NOTIF, 1) == 1));
        // alarm
        mStatusBarAlarm.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_ALARM, 1) == 1));
        // wifi
        mStatusBarWifi.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_WIFI, 1) == 1));
        // bluetooth
        mStatusBarBluetooth.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_BLUETOOTH, 1) == 1));
        // 3g
        mStatusBar3g.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_3G, 1) == 1));
        // gps
        mStatusBarGPS.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_GPS, 1) == 1));
        // sync
        mStatusBarSync.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_SYNC, 1) == 1));
        // hide carrier
        mStatusBarHideCarrier.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_HIDE_CARRIER, 0) == 1));
        // reverse
        mStatusBarReverse.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_REVERSE, 0) == 1) );
        // right carrier
        mStatusBarCarrier.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_STATUSBAR_CARRIER, 0) == 1));
        // center carrier
        mStatusBarCarrierCenter.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_STATUSBAR_CARRIER_CENTER, 0) == 1));
        // left carrier
        mStatusBarCarrierLeft.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_STATUSBAR_CARRIER_LEFT, 0) == 1));
        // compact carrier
        mStatusBarCompactCarrier.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_COMPACT_CARRIER, 0) == 1));
        // enable compact carrier
        mStatusBarCompactCarrier.setEnabled((Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_VIEW_WIDGET, 1) != 4) || (Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_BOTTOM, 0) != 1));
        // bottom carrier
        mStatusBarCarrierBottom.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.CARRIER_LABEL_BOTTOM, 0) == 1));
        // enable bottom carrier
        mStatusBarCarrierBottom.setEnabled((Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_VIEW_WIDGET, 1) != 4) || (Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_BOTTOM, 0) != 1));
        // left logo
        mStatusBarCarrierLogo.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.CARRIER_LOGO, 0) == 1));
        // center logo
        mStatusBarCarrierLogoCenter.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.CARRIER_LOGO_CENTER, 0) == 1));
        // right logo
        mStatusBarCarrierLogoLeft.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.CARRIER_LOGO_LEFT, 0) == 1));
        // change logo
        mStatusBarCarrierLogoImage.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.CARRIER_LOGO_STATUS_BAR, 0) == 1));
        mStatusBarCarrierLogoImage.setEnabled((Settings.System.getInt(getContentResolver(),
                Settings.System.CARRIER_LOGO, 0) == 1) || (Settings.System.getInt(getContentResolver(),
                Settings.System.CARRIER_LOGO_CENTER, 0) == 1) || (Settings.System.getInt(getContentResolver(),
                Settings.System.CARRIER_LOGO_LEFT, 0) == 1));
        logoBackgroundImage = new File(getApplicationContext().getFilesDir()+"/lg_background");
        // brightness
        mStatusBarBrightnessControl.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_BRIGHTNESS_TOGGLE, 0) == 1));
        // headset
        mStatusBarHeadset.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_HEADSET, 1) == 1));
        // hidden statusbar
        mStatusBarHidden.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.SYSTEMUI_STATUSBAR_VISIBILITY, 0) == 1));
        // 4g
        mStatusBarFourG.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_FOURG, 0) == 1));

        mStatusBarSetting.setValue(String.valueOf(Settings.System.getInt(getContentResolver(),
                Settings.System.ENABLE_SETTING_BUTTON, 0)));
        mStatusBarSetting.setOnPreferenceChangeListener(this);

        try {
            if (Settings.System.getInt(getContentResolver(), 
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                mStatusBarBrightnessControl.setEnabled(false);
                mStatusBarBrightnessControl.setSummary(R.string.ui_status_bar_toggle_info);
            } else {
                mStatusBarBrightnessControl.setEnabled(true);
            }
        } catch (SettingNotFoundException e) {
        }

        mStatusBarAmPm = (ListPreference) prefSet.findPreference(PREF_STATUS_BAR_AM_PM);
        mStatusBarBattery = (ListPreference) prefSet.findPreference(PREF_STATUS_BAR_BATTERY);
        mStatusBarBatteryStyle = (ListPreference) prefSet.findPreference(PREF_STATUS_BAR_BATTERY_STYLE);
        mStatusBarCmSignal = (ListPreference) prefSet.findPreference(PREF_STATUS_BAR_CM_SIGNAL);

        int statusBarAmPm = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_AM_PM, 2);
        mStatusBarAmPm.setValue(String.valueOf(statusBarAmPm));
        mStatusBarAmPm.setOnPreferenceChangeListener(this);

        try {
            if (Settings.System.getInt(getContentResolver(), 
                    Settings.System.TIME_12_24) == 24) {
                mStatusBarAmPm.setEnabled(false);
                mStatusBarAmPm.setSummary(R.string.ui_status_bar_am_pm_info);
            } else {
                mStatusBarAmPm.setEnabled(true);
            }
        } catch (SettingNotFoundException e) {
        }

        //mRecentAppsStatusBar = (CheckBoxPreference) prefSet.findPreference(PREF_RECENT_APPS_STATUS_BAR);
        //mRecentAppsStatusBar.setChecked((Settings.System.getInt(getContentResolver(),
        //        Settings.System.RECENT_APPS_STATUS_BAR, 1) == 1) || (Settings.System.getInt(getContentResolver(),
        //        Settings.System.EXPANDED_VIEW_WIDGET, 1) != 4));
        //mRecentAppsStatusBar.setEnabled((Settings.System.getInt(getContentResolver(),
        //        Settings.System.EXPANDED_VIEW_WIDGET, 1) != 4));

        int statusBarBattery = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_BATTERY, 0);
        mStatusBarBattery.setValue(String.valueOf(statusBarBattery));
        mStatusBarBattery.setOnPreferenceChangeListener(this);
 
        int statusBarBatteryStyle = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_BATTERY_STYLE, 0);
        mStatusBarBatteryStyle.setValue(String.valueOf(statusBarBatteryStyle));
        mStatusBarBatteryStyle.setOnPreferenceChangeListener(this);
        mStatusBarBatteryStyle.setEnabled(statusBarBattery == 1);

	mStatusBarBatteryColor.setValue(Settings.System.getString(getContentResolver(),
                Settings.System.STATUS_BAR_BATTERY_COLOR));
        mStatusBarBatteryColor.setOnPreferenceChangeListener(this);
        mStatusBarBatteryColor.setEnabled(statusBarBattery == 3 || statusBarBattery == 4 || statusBarBattery == 5 || statusBarBattery == 6);

        int signalStyle = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_CM_SIGNAL_TEXT, 0);
        mStatusBarCmSignal.setValue(String.valueOf(signalStyle));
        mStatusBarCmSignal.setOnPreferenceChangeListener(this);

        mStatusBarCarrierLabel = (ListPreference) prefSet
                .findPreference(PREF_STATUS_BAR_CARRIER_LABEL);
        mStatusBarCarrierLabelCustom = (EditTextPreference) prefSet
                .findPreference(PREF_STATUS_BAR_CARRIER_LABEL_CUSTOM);

        if (mStatusBarCarrierLabelCustom != null) {
            EditText carrierEditText = mStatusBarCarrierLabelCustom.getEditText();

            if (carrierEditText != null) {
                InputFilter lengthFilter = new InputFilter.LengthFilter(20);
                carrierEditText.setFilters(new InputFilter[]{lengthFilter});
                carrierEditText.setSingleLine(true);
            }
        }

        int statusBarCarrierLabel = Settings.System.getInt(getContentResolver(),
                Settings.System.CARRIER_LABEL_TYPE, 0);
        String statusBarCarrierLabelCustom = Settings.System.getString(getContentResolver(),
                Settings.System.CARRIER_LABEL_CUSTOM_STRING);

        if (statusBarCarrierLabelCustom == null) {
            statusBarCarrierLabelCustom = "CyanMobileX";
            Settings.System.putString(getContentResolver(),
                    Settings.System.CARRIER_LABEL_CUSTOM_STRING,
		    statusBarCarrierLabelCustom);
	}

        mStatusBarCarrierLabel.setValue(String.valueOf(statusBarCarrierLabel));
        mStatusBarCarrierLabel.setOnPreferenceChangeListener(this);

        mStatusBarCarrierLabelCustom.setText(statusBarCarrierLabelCustom);
        mStatusBarCarrierLabelCustom.setOnPreferenceChangeListener(this);
        mStatusBarCarrierLabelCustom.setEnabled(
                statusBarCarrierLabel == 3);

        int clockColor = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_CLOCKCOLOR, 0xFF33B5E5);
        mStatusBarClockColor.setSummary(Integer.toHexString(clockColor));

        int carrierColor = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_CARRIERCOLOR, 0xFF33B5E5);
        mStatusBarCarrierColor.setSummary(Integer.toHexString(carrierColor));

        int transparentStatusBarPref = Settings.System.getInt(getContentResolver(),
                Settings.System.TRANSPARENT_STATUS_BAR, 0);
	mTransparentStatusBarPref = (ListPreference) prefSet.findPreference(TRANSPARENT_STATUS_BAR_PREF);
        mTransparentStatusBarPref.setValue(String.valueOf(transparentStatusBarPref));
        mTransparentStatusBarPref.setOnPreferenceChangeListener(this);
        backBackgroundImage = new File(getApplicationContext().getFilesDir()+"/bc_background");

        int statusBarColor = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_COLOR, 0);
        mStatusBarColor.setSummary(Integer.toHexString(statusBarColor));
        mStatusBarColor.setEnabled(transparentStatusBarPref == 4);

        int transparentNotificationBackgroundPref = Settings.System.getInt(getContentResolver(),
                Settings.System.TRANSPARENT_NOTIFICATION_BACKGROUND, 0);
        mTransparentNotificationBackgroundPref = (ListPreference) prefSet.findPreference(PREF_TRANSPARENT_NOTIFICATION_BACKGROUND);
        mTransparentNotificationBackgroundPref.setValue(String.valueOf(transparentNotificationBackgroundPref));
        mTransparentNotificationBackgroundPref.setOnPreferenceChangeListener(this);

        int notificationBackgroundColor = Settings.System.getInt(getContentResolver(),
                Settings.System.NOTIFICATION_BACKGROUND_COLOR, 0);
        mNotificationBackgroundColor.setSummary(Integer.toHexString(notificationBackgroundColor));
        mNotificationBackgroundColor.setEnabled(transparentNotificationBackgroundPref == 2);

        // Set up the warning
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("CyanMobile Notice");
        alertDialog.setMessage(getResources().getString(R.string.reboot_notice_summary));
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
        if (preference == mStatusBarAmPm) {
            int statusBarAmPm = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_AM_PM,
                    statusBarAmPm);
            return true;
        } else if (preference == mStatusBarBattery) {
            int statusBarBattery = Integer.valueOf((String) newValue);
            boolean StatusStyle = (Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_BATTERY, 0) == 5);
            if (statusBarBattery == 1) {
                if (StatusStyle) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BATTERY,
                    1);
                } else {
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BATTERY,
                    1);
                }
                mStatusBarBatteryStyle.setEnabled(true);
                mStatusBarBatteryColor.setEnabled(false);
            } else if (statusBarBattery == 3) {
                if (StatusStyle) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BATTERY,
                    3);
                } else {
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BATTERY,
                    3);
                }
                mStatusBarBatteryStyle.setEnabled(false);
                mStatusBarBatteryColor.setEnabled(true);
            } else if (statusBarBattery == 6) {
                if (StatusStyle) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BATTERY,
                    6);
                } else {
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BATTERY,
                    6);
                }
                mStatusBarBatteryStyle.setEnabled(false);
                mStatusBarBatteryColor.setEnabled(true);
            } else if (statusBarBattery == 4) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BATTERY,
                    4);
                mStatusBarBatteryStyle.setEnabled(false);
                mStatusBarBatteryColor.setEnabled(true);
            } else if (statusBarBattery == 5) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                              Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BATTERY, 5);
                               mStatusBarBatteryStyle.setEnabled(false);
                                mStatusBarBatteryColor.setEnabled(true);
            } else if (statusBarBattery == 2) {
                if (StatusStyle) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BATTERY,
                    2);
                } else {
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BATTERY,
                    2);
                }
                mStatusBarBatteryStyle.setEnabled(false);
                mStatusBarBatteryColor.setEnabled(false);
            } else {
                if (StatusStyle) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BATTERY,
                    0);
                } else {
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BATTERY,
                    0);
                }
                mStatusBarBatteryStyle.setEnabled(false);
                mStatusBarBatteryColor.setEnabled(false);
            }
            return true;
        } else if (preference == mStatusBarBatteryStyle) {
            int statusBarBatteryStyle = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BATTERY_STYLE,
                    statusBarBatteryStyle);
            return true;
	} else if (preference == mStatusBarSetting) {
            int SettingsValue = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.ENABLE_SETTING_BUTTON,
                    SettingsValue);
            return true;
        } else if (preference == mClockFontsize) {
            int ClockFontSize = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.STATUSBAR_CLOCK_FONT_SIZE,
                    ClockFontSize);
            return true;
        } else if (preference == mCarrierFontsize) {
            int CarrierFontSize = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.STATUSBAR_CARRIER_FONT_SIZE,
                    CarrierFontSize);
            return true;
        } else if (preference == mIconFontsize) {
            int IconFontSize = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.STATUSBAR_ICON_FONT_SIZE,
                    IconFontSize);
            return true;
        } else if (preference == mIconsize) {
            int IconSize = Integer.valueOf((String) newValue);
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
            Settings.System.putInt(getContentResolver(), Settings.System.STATUSBAR_ICONS_SIZE,
                    IconSize);
            return true;
        } else if (preference == mStatsize) {
            int StatSize = Integer.valueOf((String) newValue);
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
            Settings.System.putInt(getContentResolver(), Settings.System.STATUSBAR_STATS_SIZE,
                    StatSize);
            return true;
        } else if (preference == mStatusBarCmSignal) {
            int signalStyle = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CM_SIGNAL_TEXT,
                    signalStyle);
            return true;
        } else if (preference == mStatusBarCarrierLabel) {
            int carrierLabelType = Integer.valueOf((String) newValue);
            mStatusBarCarrierLabelCustom.setEnabled(carrierLabelType == 3);
            Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LABEL_TYPE,
                    carrierLabelType);
            return true;
        } else if (preference == mStatusBarCarrierLabelCustom) {
            String carrierLabelCustom = String.valueOf(newValue);
            Settings.System.putString(getContentResolver(),
                    Settings.System.CARRIER_LABEL_CUSTOM_STRING,
                    carrierLabelCustom);
            return true;
        } else if (preference == mTransparentStatusBarPref) {
            int transparentStatusBarPref = Integer.parseInt(String.valueOf(newValue));
            Settings.System.putInt(getContentResolver(), Settings.System.TRANSPARENT_STATUS_BAR,
                          transparentStatusBarPref);
            mStatusBarColor.setEnabled(transparentStatusBarPref == 4);
            if (transparentStatusBarPref == 6) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
                intent.putExtra("scaleUpIfNeeded", false);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
                int width = 32;
                int height = 32;
                Rect rect = new Rect();
                Window window = getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(rect);
                int statusBarHeight = rect.top;
                int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
                int titleBarHeight = contentViewTop - statusBarHeight;
                boolean isPortrait = getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_PORTRAIT;
                intent.putExtra("aspectX", isPortrait ? (width + statusBarHeight + statusBarHeight) : statusBarHeight);
                intent.putExtra("aspectY", isPortrait ? statusBarHeight : (width + statusBarHeight + statusBarHeight));
                try {
                    backBackgroundImage.createNewFile();
                    backBackgroundImage.setReadable(true, false);
                    backBackgroundImage.setWritable(true, false);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(backBackgroundImage));
                    intent.putExtra("return-data", false);
                    startActivityForResult(intent,REQUEST_CODE_BACK_FILE);
                } catch (IOException e) {
                    Log.e("Picker", "IOException: ", e);
                } catch (ActivityNotFoundException e) {
                    Log.e("Picker", "ActivityNotFoundException: ", e);
                }
            } else {
                if (transparentStatusBarPref == 4) {
                // do nothing
                } else if (transparentStatusBarPref == 6) {
                // do nothing
                } else {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                }
            }
            return true;
        } else if (preference == mTransparentNotificationBackgroundPref) {
            int transparentNotificationBackgroundPref = Integer.parseInt(String.valueOf(newValue));
            Settings.System.putInt(getContentResolver(), Settings.System.TRANSPARENT_NOTIFICATION_BACKGROUND,
                                   transparentNotificationBackgroundPref);
            mNotificationBackgroundColor.setEnabled(transparentNotificationBackgroundPref == 2);
            if (transparentNotificationBackgroundPref == 5) {
                Intent intent = new Intent("org.openintents.action.PICK_FILE");
                intent.setData(Uri.parse("file:///sdcard/"));
                intent.putExtra("org.openintents.extra.TITLE", "CyanMobile Please select a file");
                startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
            }
            if (transparentNotificationBackgroundPref == 5) {
                //do nothings
            } else if (transparentNotificationBackgroundPref == 2) {
                //do nothings
            } else {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
            }
            return true;
	} else if (preference == mStatusBarBatteryColor) {
            String statusBarBatteryColor = (String) newValue;
                Settings.System.putString(getContentResolver(),
                        Settings.System.STATUS_BAR_BATTERY_COLOR, statusBarBatteryColor);
            if ("custom".equals(statusBarBatteryColor)) {
                int color = -1;
                String colorString = Settings.System.getString(getContentResolver(),
                        Settings.System.STATUS_BAR_BATTERY_COLOR);
                if (!TextUtils.isEmpty(colorString)) {
                    try {
                        color = Color.parseColor(colorString);
                    } catch (IllegalArgumentException e) { }
                    new ColorPickerDialog(this, mColorChangedListener, color).show();
                }
            }
		return true;
	}
        return false;
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        /* Preference Screens */
        if (preference == mStatusBarClock) {
            value = mStatusBarClock.isChecked();
            if (value) {
                mStatusBarCenterClock.setChecked(false);
                mStatusBarLeftClock.setChecked(false);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CENTERCLOCK, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_LEFTCLOCK, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CLOCK, 1);
            } else {
                mStatusBarCenterClock.setChecked(false);
                mStatusBarLeftClock.setChecked(false);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CENTERCLOCK, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_LEFTCLOCK, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CLOCK, 0);
            }
            return true;
        } else if (preference == mStatusBarCenterClock) {
            value = mStatusBarCenterClock.isChecked();
            if (value) {
                mStatusBarLeftClock.setChecked(false);
                mStatusBarClock.setChecked(false);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CENTERCLOCK, 1);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_LEFTCLOCK, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CLOCK, 0);
            } else {
                mStatusBarCenterClock.setChecked(false);
                mStatusBarLeftClock.setChecked(false);
                mStatusBarClock.setChecked(true);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CENTERCLOCK, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_LEFTCLOCK, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CLOCK, 1);
            }
            return true;
        } else if (preference == mStatusBarLeftClock) {
            value = mStatusBarLeftClock.isChecked();
            if (value) {
                mStatusBarCenterClock.setChecked(false);
                mStatusBarClock.setChecked(false);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CENTERCLOCK, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_LEFTCLOCK, 1);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CLOCK, 0);
            } else {
                mStatusBarCenterClock.setChecked(false);
                mStatusBarLeftClock.setChecked(false);
                mStatusBarClock.setChecked(true);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CENTERCLOCK, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_LEFTCLOCK, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CLOCK, 1);
            }
            return true;
        } else if (preference == mStatusBarAlarm) {
            value = mStatusBarAlarm.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_ALARM,
                    value ? 1 : 0);
            return true;
	} else if (preference == mStatusBarIcon) {
            value = mStatusBarIcon.isChecked();
            if (value) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_ALARM, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_NOTIF, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_WIFI, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BLUETOOTH, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_3G, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_GPS, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_SYNC, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_HEADSET, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CM_SIGNAL_TEXT, 4);
                mStatusBarAlarm.setChecked(false);
                mStatusBarHeadset.setChecked(false);
                mStatusBarWifi.setChecked(false);
                mStatusBarBluetooth.setChecked(false);
                mStatusBar3g.setChecked(false);
                mStatusBarGPS.setChecked(false);
                mStatusBarSync.setChecked(false);
                mStatusBarNotif.setChecked(false);
            } else {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_ALARM, 1);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_NOTIF, 1);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_WIFI, 1);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BLUETOOTH, 1);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_3G, 1);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_GPS, 1);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_SYNC, 1);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_HEADSET, 1);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CM_SIGNAL_TEXT, 0);
                mStatusBarAlarm.setChecked(true);
                mStatusBarHeadset.setChecked(true);
                mStatusBarWifi.setChecked(true);
                mStatusBarBluetooth.setChecked(true);
                mStatusBar3g.setChecked(true);
                mStatusBarGPS.setChecked(true);
                mStatusBarSync.setChecked(true);
                mStatusBarNotif.setChecked(true);
            }
            return true;
	} else if (preference == mStatusBarCmWifiPref) {
            value = mStatusBarCmWifiPref.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CM_WIFI_TEXT,
                    value ? 1 : 0);
            return true;
        } else if (preference == mStatusBarWifi) {
            value = mStatusBarWifi.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_WIFI,
                    value ? 1 : 0);
            return true;
        } else if (preference == mStatusBarBluetooth) {
            value = mStatusBarBluetooth.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BLUETOOTH,
                    value ? 1 : 0);
           return true;
        } else if (preference == mStatusBar3g) {
            value = mStatusBar3g.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_3G,
                    value ? 1 : 0);
            return true;
        } else if (preference == mStatusBarGPS) {
            value = mStatusBarGPS.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_GPS,
                    value ? 1 : 0);
           return true;
        } else if (preference == mStatusBarSync) {
            value = mStatusBarSync.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_SYNC,
                    value ? 1 : 0);
            return true;
	} else if (preference == mStatusBarDate) {
            value = mStatusBarDate.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_DATE,
                    value ? 1 : 0);
            return true;
	} else if (preference == mStatusBarNotif) {
            value = mStatusBarNotif.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_NOTIF,
                    value ? 1 : 0);
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
            return true;
	} else if (preference == mStatusBarClockColor) {
            ColorPickerDialog cp = new ColorPickerDialog(this, mClockColorListener, getClockColor());
            cp.show();
            return true;
        } else if (preference == mDateColorPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mDateFontColorListener,
                readDateFontColor());
            cp.show();
            return true;          
        } else if (preference == mNotifTickerColorPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mTickerFontColorListener,
                readTickerFontColor());
            cp.show();
            return true;       
        } else if (preference == mNoNotifColorPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mNoneFontColorListener,
                readNoneFontColor());
            cp.show();          
            return true; 
        } else if (preference == mRestartStatusBar) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
            return true;
        }else if (preference == mLatestNotifColorPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mLatestFontColorListener,
                readLatestFontColor());
            cp.show();
            return true;           
        } else if (preference == mOngoingNotifColorPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mOngoingFontColorListener,
                readOngoingFontColor());
            cp.show();
            return true;           
        } else if (preference == mClearLabelColorPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mClearFontColorListener,
                readClearFontColor());
            cp.show();          
        } else if (preference == mNotifItemTitlePref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mNotifTitleFontColorListener,
                readNotifTitleFontColor());
            cp.show();
            return true;           
        } else if (preference == mNotifItemTextPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mNotifItemFontColorListener,
                readNotifItemFontColor());
            cp.show();
            return true;           
        } else if (preference == mNotifItemTimePref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mNotifTimeFontColorListener,
                readNotifTimeFontColor());
            cp.show();   
            return true;
        } else if (preference == mStatusBarCarrierColor) {
            CRColorPickerDialog cr = new CRColorPickerDialog(this, mCarrierColorListener, getCarrierColor());
            cr.show();
            return true;
        } else if (preference == mStatusBarCarrier) {
            value = mStatusBarCarrier.isChecked();
            if (value) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER, 1);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_CENTER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_LEFT, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_HIDE_CARRIER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_COMPACT_CARRIER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LABEL_BOTTOM, 0);
                                   mStatusBarCarrierBottom.setChecked(false);
                                   mStatusBarHideCarrier.setChecked(false);
                                   mStatusBarCompactCarrier.setChecked(false);
                                   mStatusBarCarrierCenter.setChecked(false);
                                   mStatusBarCarrierLeft.setChecked(false);
            } else {
              if ((Settings.System.getInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER, 0) == 1)) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                  Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER, 0);
                      mStatusBarCarrier.setChecked(false);
              }
            }
            return true;
        } else if (preference == mStatusBarCarrierCenter) {
            value = mStatusBarCarrierCenter.isChecked();
            if (value) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_CENTER, 1);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_LEFT, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_HIDE_CARRIER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_COMPACT_CARRIER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LABEL_BOTTOM, 0);
                                   mStatusBarCarrierBottom.setChecked(false);
                                   mStatusBarHideCarrier.setChecked(false);
                                   mStatusBarCompactCarrier.setChecked(false);
                                   mStatusBarCarrier.setChecked(false);
                                   mStatusBarCarrierLeft.setChecked(false);
            } else {
              if ((Settings.System.getInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_CENTER, 0) == 1)) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                  Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_CENTER, 0);
                      mStatusBarCarrierCenter.setChecked(false);
              }
            }
            return true;
        } else if (preference == mStatusBarCarrierLeft) {
            value = mStatusBarCarrierLeft.isChecked();
            if (value) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_CENTER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_LEFT, 1);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_HIDE_CARRIER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_COMPACT_CARRIER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LABEL_BOTTOM, 0);
                                   mStatusBarCarrierBottom.setChecked(false);
                                   mStatusBarHideCarrier.setChecked(false);
                                   mStatusBarCompactCarrier.setChecked(false);
                                   mStatusBarCarrier.setChecked(false);
                                   mStatusBarCarrierCenter.setChecked(false);
            } else {
              if ((Settings.System.getInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_LEFT, 0) == 1)) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                  Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_LEFT, 0);
                      mStatusBarCarrierLeft.setChecked(false);
              }
            }
            return true;
        } else if (preference == mStatusBarHideCarrier) {
            value = mStatusBarHideCarrier.isChecked();
            if (value) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_DATE, 1);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_HIDE_CARRIER, 1);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_CENTER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_LEFT, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_COMPACT_CARRIER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO_CENTER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO_LEFT, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LABEL_BOTTOM, 0);
                                   mStatusBarCarrierBottom.setChecked(false);
                                   mStatusBarCarrier.setChecked(false);
                                   mStatusBarCarrierCenter.setChecked(false);
                                   mStatusBarCarrierLeft.setChecked(false);
                                   mStatusBarCompactCarrier.setChecked(false);
                                   mStatusBarCarrierLogo.setChecked(false);
                                   mStatusBarCarrierLogoCenter.setChecked(false);
                                   mStatusBarCarrierLogoLeft.setChecked(false);
                                   mStatusBarCarrierLogoImage.setEnabled(false);
            }
            return true;
        } else if (preference == mStatusBarCompactCarrier) {
            value = mStatusBarCompactCarrier.isChecked();
            if (value) {
               if ((Settings.System.getInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_CENTER, 0) == 1) ||
                   (Settings.System.getInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_LEFT, 0) == 1) ||
                   (Settings.System.getInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER, 0) == 1) ||
                   (Settings.System.getInt(getContentResolver(), Settings.System.CARRIER_LOGO, 0) == 1) ||
                   (Settings.System.getInt(getContentResolver(), Settings.System.CARRIER_LOGO_CENTER, 0) == 1) ||
                   (Settings.System.getInt(getContentResolver(), Settings.System.CARRIER_LOGO_LEFT, 0) == 1)) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
               }
               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_COMPACT_CARRIER, 1);
               Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LABEL_BOTTOM, 0);
               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_HIDE_CARRIER, 0);
               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER, 0);
               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_CENTER, 0);
               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_LEFT, 0);
               mStatusBarHideCarrier.setChecked(false);
               mStatusBarCarrier.setChecked(false);
               mStatusBarCarrierBottom.setChecked(false);
               mStatusBarCarrierCenter.setChecked(false);
               mStatusBarCarrierLeft.setChecked(false);
               mStatusBarCarrierLogoImage.setEnabled(false);
            } else {
               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_COMPACT_CARRIER, 0);
            }
            return true;
        } else if (preference == mStatusBarCarrierBottom) {
            value = mStatusBarCarrierBottom.isChecked();
            if (value) {
               if ((Settings.System.getInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_CENTER, 0) == 1) ||
                   (Settings.System.getInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_LEFT, 0) == 1) ||
                   (Settings.System.getInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER, 0) == 1) ||
                   (Settings.System.getInt(getContentResolver(), Settings.System.CARRIER_LOGO, 0) == 1) ||
                   (Settings.System.getInt(getContentResolver(), Settings.System.CARRIER_LOGO_CENTER, 0) == 1) ||
                   (Settings.System.getInt(getContentResolver(), Settings.System.CARRIER_LOGO_LEFT, 0) == 1)) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
               }
               Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LABEL_BOTTOM, 1);
               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_COMPACT_CARRIER, 0);
               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_HIDE_CARRIER, 0);
               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER, 0);
               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_CENTER, 0);
               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_LEFT, 0);
               mStatusBarHideCarrier.setChecked(false);
               mStatusBarCarrier.setChecked(false);
               mStatusBarCompactCarrier.setChecked(false);
               mStatusBarCarrierCenter.setChecked(false);
               mStatusBarCarrierLeft.setChecked(false);
               mStatusBarCarrierLogoImage.setEnabled(false);
            } else {
               Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LABEL_BOTTOM, 0);
            }
            return true;
        } else if (preference == mStatusBarCarrierLogoImage) {
            value = mStatusBarCarrierLogoImage.isChecked();
            if (value) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setType("image/*");
                //intent.putExtra("crop", "true");
                //intent.putExtra("scale", true);
                //intent.putExtra("scaleUpIfNeeded", false);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
                int width = 32;
                int height = 32;
                Rect rect = new Rect();
                Window window = getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(rect);
                int statusBarHeight = rect.top;
                int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
                int titleBarHeight = contentViewTop - statusBarHeight;
                boolean isPortrait = getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_PORTRAIT;
                //intent.putExtra("aspectX", isPortrait ? (statusBarHeight + statusBarHeight) : statusBarHeight);
                //intent.putExtra("aspectY", isPortrait ? statusBarHeight : (statusBarHeight + statusBarHeight));
                try {
                    logoBackgroundImage.createNewFile();
                    logoBackgroundImage.setReadable(true, false);
                    logoBackgroundImage.setWritable(true, false);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(logoBackgroundImage));
                    intent.putExtra("return-data", false);
                    startActivityForResult(intent,REQUEST_CODE_LOGO_FILE);
                } catch (IOException e) {
                    Log.e("Picker", "IOException: ", e);
                } catch (ActivityNotFoundException e) {
                    Log.e("Picker", "ActivityNotFoundException: ", e);
                }
            } else {
              if ((Settings.System.getInt(getContentResolver(), Settings.System.CARRIER_LOGO_STATUS_BAR, 0) == 1)) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                  Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO_STATUS_BAR, 0);
                      mStatusBarCarrierLogoImage.setChecked(false);
              }
            }
            return true;
        } else if (preference == mStatusBarCarrierLogo) {
            value = mStatusBarCarrierLogo.isChecked();
            if (value) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                          Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO, 1);
                          Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO_CENTER, 0);
                          Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO_LEFT, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_HIDE_CARRIER, 0);
                                   mStatusBarHideCarrier.setChecked(false);
                                   mStatusBarCarrierLogoCenter.setChecked(false);
                                   mStatusBarCarrierLogoLeft.setChecked(false);
                                   mStatusBarCarrierLogoImage.setEnabled(true);
            } else {
              if ((Settings.System.getInt(getContentResolver(), Settings.System.CARRIER_LOGO, 0) == 1)) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                  Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO, 0);
                      mStatusBarCarrierLogo.setChecked(false);
                      mStatusBarCarrierLogoImage.setEnabled(false);
              }
            }
            return true;
        } else if (preference == mStatusBarCarrierLogoCenter) {
            value = mStatusBarCarrierLogoCenter.isChecked();
            if (value) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                          Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO, 0);
                          Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO_LEFT, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_HIDE_CARRIER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO_CENTER, 1);
                                   mStatusBarHideCarrier.setChecked(false);
                                   mStatusBarCarrierLogo.setChecked(false);
                                   mStatusBarCarrierLogoLeft.setChecked(false);
                                   mStatusBarCarrierLogoImage.setEnabled(true);
            } else {
              if ((Settings.System.getInt(getContentResolver(), Settings.System.CARRIER_LOGO_CENTER, 0) == 1)) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                  Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO_CENTER, 0);
                      mStatusBarCarrierLogoCenter.setChecked(false);
                      mStatusBarCarrierLogoImage.setEnabled(false);
              }
            }
            return true;
        } else if (preference == mStatusBarCarrierLogoLeft) {
            value = mStatusBarCarrierLogoLeft.isChecked();
            if (value) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                          Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO, 0);
                          Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO_CENTER, 0);
                          Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO_LEFT, 1);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_HIDE_CARRIER, 0);
                                   mStatusBarHideCarrier.setChecked(false);;
                                   mStatusBarCarrierLogo.setChecked(false);
                                   mStatusBarCarrierLogoCenter.setChecked(false);
                                   mStatusBarCarrierLogoImage.setEnabled(true);
            }else {
              if ((Settings.System.getInt(getContentResolver(), Settings.System.CARRIER_LOGO_LEFT, 0) == 1)) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                  Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO_LEFT, 0);
                      mStatusBarCarrierLogoLeft.setChecked(false);
                      mStatusBarCarrierLogoImage.setEnabled(false);
              }
            }
            return true;
        } else if (preference == mStatusBarBrightnessControl) {
            value = mStatusBarBrightnessControl.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.STATUS_BAR_BRIGHTNESS_TOGGLE, value ? 1 : 0);
            return true;
        //} else if (preference == mRecentAppsStatusBar) {
        //    value = mRecentAppsStatusBar.isChecked();
        //    Settings.System.putInt(getContentResolver(),
        //            Settings.System.RECENT_APPS_STATUS_BAR, value ? 1 : 0);
        //    return true;
        } else if (preference == mStatusBarColor) {
            SBColorPickerDialog sbcp = new SBColorPickerDialog(this, mStatusBarColorListener, getStatusBarColor());
            sbcp.show();
            return true;
        } else if (preference == mNotificationBackgroundColor) {
            NBColorPickerDialog nbcp = new NBColorPickerDialog(this, mNotificationBackgroundColorListener, getNotificationBackgroundColor());
            nbcp.show();
            return true;
        } else if (preference == mStatusBarHeadset) {
            value = mStatusBarHeadset.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_HEADSET,
                    value ? 1 : 0);
            return true;
        } else if (preference == mStatusBarHidden) {
            value = mStatusBarHidden.isChecked();
            if (value) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                               Settings.System.putInt(getContentResolver(), Settings.System.SYSTEMUI_STATUSBAR_VISIBILITY, 1);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BATTERY, 4);
            } else {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                               Settings.System.putInt(getContentResolver(), Settings.System.SYSTEMUI_STATUSBAR_VISIBILITY, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BATTERY, 0);
            }
            return true;
        } else if (preference == mStatusBarFourG) {
            value = mStatusBarFourG.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_FOURG,
                    value ? 1 : 0);
            return true;
        } else if (preference == mStatusBarReverse) {
            value = mStatusBarReverse.isChecked();
            if (value) {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_CENTER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_STATUSBAR_CARRIER_LEFT, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_HIDE_CARRIER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_COMPACT_CARRIER, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LABEL_BOTTOM, 0);
                                   mStatusBarCarrierBottom.setChecked(false);
                mStatusBarCenterClock.setChecked(false);
                mStatusBarLeftClock.setChecked(false);
                mStatusBarClock.setChecked(true);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CENTERCLOCK, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_LEFTCLOCK, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CLOCK, 1);
                                   mStatusBarHideCarrier.setChecked(false);
                                   mStatusBarCompactCarrier.setChecked(false);
                                   mStatusBarCarrierCenter.setChecked(false);
                                   mStatusBarCarrierLeft.setChecked(false);
                                   mStatusBarCarrier.setChecked(false);
                                   mStatusBarDate.setChecked(false);
                                   mStatusBarDate.setEnabled(false);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_DATE, 0);
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_REVERSE, 1);
            } else {
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_REVERSE, 0);
            }
            return true;
        }
        return false;
    }

    private int getStatusBarColor() {
        try {
            return Settings.System.getInt(getContentResolver(),
                     Settings.System.STATUS_BAR_COLOR);
        } catch (SettingNotFoundException e) {
            return -1;
        }
    }

    SBColorPickerDialog.OnColorChangedListener mStatusBarColorListener =
        new SBColorPickerDialog.OnColorChangedListener() {
            public void SBcolorChanged(int SBcolor) {
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_COLOR, SBcolor);
                mStatusBarColor.setSummary(Integer.toHexString(SBcolor));
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
            }
            public void SBcolorUpdate(int SBcolor) {
            }
    };

    private int getNotificationBackgroundColor() {
        try {
            return Settings.System.getInt(getContentResolver(),
                     Settings.System.NOTIFICATION_BACKGROUND_COLOR);
        } catch (SettingNotFoundException e) {
            return -1;
        }
    }

    NBColorPickerDialog.OnColorChangedListener mNotificationBackgroundColorListener =
        new NBColorPickerDialog.OnColorChangedListener() {
            public void NBcolorChanged(int NBcolor) {
                Settings.System.putInt(getContentResolver(), Settings.System.NOTIFICATION_BACKGROUND_COLOR, NBcolor);
                mNotificationBackgroundColor.setSummary(Integer.toHexString(NBcolor));
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
            }
            public void NBcolorUpdate(int NBcolor) {
            }
    };

    private int getClockColor() {
        try {
            return Settings.System.getInt(getContentResolver(),
                     Settings.System.STATUS_BAR_CLOCKCOLOR);
        } catch (SettingNotFoundException e) {
            return -16777216;
        }
    }

    ColorPickerDialog.OnColorChangedListener mClockColorListener =
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CLOCKCOLOR, color);
                mStatusBarClockColor.setSummary(Integer.toHexString(color));
            }
            public void colorUpdate(int color) {
            }
    };

    private int getCarrierColor() {
        try {
            return Settings.System.getInt(getContentResolver(),
                     Settings.System.STATUS_BAR_CARRIERCOLOR);
        } catch (SettingNotFoundException e) {
            return -16777216;
        }
    }

    CRColorPickerDialog.OnColorChangedListener mCarrierColorListener =
        new CRColorPickerDialog.OnColorChangedListener() {
            public void CRcolorChanged(int CRcolor) {
                Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CARRIERCOLOR, CRcolor);
                mStatusBarCarrierColor.setSummary(Integer.toHexString(CRcolor));
            }
            public void CRcolorUpdate(int CRcolor) {
            }
    };

    private int readDateFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_DATE);
        }
        catch (SettingNotFoundException e) {
            return -1;
        }
    }

    ColorPickerDialog.OnColorChangedListener mDateFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_DATE, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readTickerFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_TICKER_TEXT);
        }
        catch (SettingNotFoundException e) {
            return -1;
        }
    }

    ColorPickerDialog.OnColorChangedListener mTickerFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_TICKER_TEXT, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readNoneFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_NONE);
        }
        catch (SettingNotFoundException e) {
            return -1;
        }
    }

    ColorPickerDialog.OnColorChangedListener mNoneFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_NONE, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readLatestFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_LATEST);
        }
        catch (SettingNotFoundException e) {
            return -1;
        }
    }

    ColorPickerDialog.OnColorChangedListener mLatestFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_LATEST, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readOngoingFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_ONGOING);
        }
        catch (SettingNotFoundException e) {
            return -1;
        }
    }

    ColorPickerDialog.OnColorChangedListener mOngoingFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_ONGOING, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readClearFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_CLEAR_BUTTON);
        }
        catch (SettingNotFoundException e) {
            return -16777216;

        }
    }

    ColorPickerDialog.OnColorChangedListener mClearFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_CLEAR_BUTTON, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readNotifTitleFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_ITEM_TITLE);
        }
        catch (SettingNotFoundException e) {
            return -16777216;
        }
    }

    ColorPickerDialog.OnColorChangedListener mNotifTitleFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_ITEM_TITLE, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readNotifItemFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_ITEM_TEXT);
        }
        catch (SettingNotFoundException e) {
            return -16777216;
        }
    }

    ColorPickerDialog.OnColorChangedListener mNotifItemFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_ITEM_TEXT, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readNotifTimeFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_ITEM_TIME);
        }
        catch (SettingNotFoundException e) {
            return -16777216;
        }
    }

    ColorPickerDialog.OnColorChangedListener mNotifTimeFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_ITEM_TIME, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private OnColorChangedListener mColorChangedListener = new OnColorChangedListener() {
        @Override
        public void colorChanged(int color) {
            String colorString = String.format("#%02x%02x%02x%02x", Color.alpha(color), Color.red(color),
                    Color.green(color), Color.blue(color));
            Settings.System.putString(getContentResolver(),
                    Settings.System.STATUS_BAR_BATTERY_COLOR, colorString);
        }

        @Override
        public void colorUpdate(int color) {
            // no-op
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Context context = getApplicationContext();
        switch (requestCode) {
            case REQUEST_CODE_PICK_FILE:
                if (resultCode == RESULT_OK && data != null) {
                    // obtain the filename
                    Uri fileUri = data.getData();
                    if (fileUri != null) {
                        String filePath = fileUri.getPath();
                        if (filePath != null) {
                            Intent mvBackgroundImage = new Intent();
                            mvBackgroundImage.setAction(MOVE_BACKGROUND_INTENT);
                            mvBackgroundImage.putExtra("fileName", filePath);
                            sendBroadcast(mvBackgroundImage);
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                        }
                    }
                }
            break;
            case REQUEST_CODE_LOGO_FILE:
                if (resultCode != RESULT_OK) {
                    Log.d("Copy_logo_Error", "Error: " + resultCode);
/*                if (resultCode == RESULT_OK) {
                    // obtain the filename
                    Uri fileUri = Uri.fromFile(wallpaperTemporary);
                    if (fileUri != null) {
                        String filePath = fileUri.getPath();
                        Log.d("FilePath = ", filePath);
                        if (filePath != null) {
                            Intent mvBackgroundImage = new Intent();
                            mvBackgroundImage.setAction(MOVE_BACKGROUND_INTENT);
                            mvBackgroundImage.putExtra("fileName", filePath);
                            sendBroadcast(mvBackgroundImage);
                        }
                    }
*/
                } else { 
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                          Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LOGO_STATUS_BAR, 1);
                    Toast.makeText(context, "CyanMobile carrier logo set to new image" ,Toast.LENGTH_LONG).show();
                }
            break;
            case REQUEST_CODE_BACK_FILE:
                if (resultCode != RESULT_OK) {
                    Log.d("Copy_logo_Error", "Error: " + resultCode);
/*                if (resultCode == RESULT_OK) {
                    // obtain the filename
                    Uri fileUri = Uri.fromFile(wallpaperTemporary);
                    if (fileUri != null) {
                        String filePath = fileUri.getPath();
                        Log.d("FilePath = ", filePath);
                        if (filePath != null) {
                            Intent mvBackgroundImage = new Intent();
                            mvBackgroundImage.setAction(MOVE_BACKGROUND_INTENT);
                            mvBackgroundImage.putExtra("fileName", filePath);
                            sendBroadcast(mvBackgroundImage);
                        }
                    }
*/
                } else { 
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                     // we're screwed here fellas
                   }
                          Settings.System.putInt(getContentResolver(), Settings.System.TRANSPARENT_STATUS_BAR, 6);
                    Toast.makeText(context, "CyanMobile carrier logo set to new image" ,Toast.LENGTH_LONG).show();
                }
            break;
        }
    }

}
