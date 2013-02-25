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
import android.content.ActivityNotFoundException;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.Window;
import android.widget.Toast;
import java.util.ArrayList;
import android.os.Handler;
import android.provider.MediaStore;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.net.Uri;
import android.util.Log;
import java.util.logging.Level;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

import com.cyanogenmod.cmparts.R;
import com.cyanogenmod.cmparts.activities.ColorPickerDialog.OnColorChangedListener;

public class UINavbarActivity extends PreferenceActivity implements OnPreferenceChangeListener {

    private static final String PREF_SQUADZONE = "squadkeys";
    private static final String PREF_STATUS_BAR_NAVI = "pref_status_bar_navi";
    private static final String PREF_NAVI_BAR = "pref_navi_bar";
    private static final String PREF_NAVISIZE = "status_bar_navisize";
    private static final String TRANSPARENT_NAVI_BAR_PREF = "pref_transparent_navi_bar";
    private static final String PREF_NAVI_BAR_COLOR = "pref_navi_bar_color";
    private static final String PREF_NAVI_BUTTON_COLOR = "pref_navi_button_color";
    private static final String PREF_NAVI_GLOW_COLOR = "pref_navi_glow_color";
    private static final String PREF_NAVI_BAR_ANI = "pref_navi_bar_ani";
    private static final String PREF_NAVI_BAR_SWIPERIGHT = "pref_navi_bar_swiperight";
    private static final String PREF_NAVI_BAR_SWIPELEFT = "pref_navi_bar_swipeleft";
    private static final String PREF_ENABLE_OVERICON = "pref_enable_overicon";

    static Context mContext;

    private Preference mSquadzone;
    private CheckBoxPreference mStatusBarNavi;
    private CheckBoxPreference mNaviBar;
    private CheckBoxPreference mNaviButtonColorEnable;
    private Preference mNaviBarColor;
    private Preference mNaviButtonColor;
    private Preference mNaviGlowColor;
    private ListPreference mNavisize;
    private ListPreference mNaviAnimate;
    private ListPreference mNaviSwipeRight;
    private ListPreference mNaviSwipeLeft;
    private ListPreference mTransparentNaviBarPref;

    private static final int REQUEST_CODE_BACK_IMAGE = 998;
    private File navBackgroundImage;
    private File navBackgroundImageTmp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.ui_navbar_title);
        addPreferencesFromResource(R.xml.ui_navbar);

		mContext = this.getBaseContext();

        PreferenceScreen prefSet = getPreferenceScreen();

        mSquadzone = (Preference) prefSet.findPreference(PREF_SQUADZONE);
        mSquadzone.setSummary("CyanMobile");

        int defValuesNaviSize = getResources().getInteger(com.android.internal.R.integer.config_navibarsize_default_cyanmobile);

        mStatusBarNavi = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_NAVI);
        mNaviBar = (CheckBoxPreference) prefSet.findPreference(PREF_NAVI_BAR);

        mNaviButtonColorEnable = (CheckBoxPreference) prefSet.findPreference(PREF_ENABLE_OVERICON);
        mNaviButtonColorEnable.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.ENABLE_OVERICON_COLOR, 1) == 1));

        mNaviBarColor = (Preference) prefSet.findPreference(PREF_NAVI_BAR_COLOR);
        mNaviBarColor.setOnPreferenceChangeListener(this);

        mNaviButtonColor = (Preference) prefSet.findPreference(PREF_NAVI_BUTTON_COLOR);
        mNaviButtonColor.setOnPreferenceChangeListener(this);

        mNaviGlowColor = (Preference) prefSet.findPreference(PREF_NAVI_GLOW_COLOR);
        mNaviGlowColor.setOnPreferenceChangeListener(this);

        mNavisize = (ListPreference) prefSet.findPreference(PREF_NAVISIZE);
        mNavisize.setOnPreferenceChangeListener(this);
        mNavisize.setValue(Integer.toString(Settings.System.getInt(getContentResolver(),
                Settings.System.STATUSBAR_NAVI_SIZE, defValuesNaviSize)));

        int transparentNaviBarPref = Settings.System.getInt(getContentResolver(),
                Settings.System.TRANSPARENT_NAVI_BAR, 0);
	mTransparentNaviBarPref = (ListPreference) prefSet.findPreference(TRANSPARENT_NAVI_BAR_PREF);
        mTransparentNaviBarPref.setValue(String.valueOf(transparentNaviBarPref));
        mTransparentNaviBarPref.setOnPreferenceChangeListener(this);
        navBackgroundImage = new File(getApplicationContext().getFilesDir()+"/navb_background");
        navBackgroundImageTmp = new File(getApplicationContext().getFilesDir()+"/navb_background.tmp");

        int naviAnimatePref = Settings.System.getInt(getContentResolver(),
                Settings.System.NAVI_BUTTONS_ANIMATE, 20000);
	mNaviAnimate = (ListPreference) prefSet.findPreference(PREF_NAVI_BAR_ANI);
        mNaviAnimate.setValue(String.valueOf(naviAnimatePref));
        mNaviAnimate.setOnPreferenceChangeListener(this);

        int naviSwipeRightPref = Settings.System.getInt(getContentResolver(),
                Settings.System.WATCH_IS_NEXT, 0);
	mNaviSwipeRight = (ListPreference) prefSet.findPreference(PREF_NAVI_BAR_SWIPERIGHT);
        mNaviSwipeRight.setValue(String.valueOf(naviSwipeRightPref));
        mNaviSwipeRight.setOnPreferenceChangeListener(this);

        int naviSwipeLeftPref = Settings.System.getInt(getContentResolver(),
                Settings.System.WATCH_IS_PREVIOUS, 1);
	mNaviSwipeLeft = (ListPreference) prefSet.findPreference(PREF_NAVI_BAR_SWIPELEFT);
        mNaviSwipeLeft.setValue(String.valueOf(naviSwipeLeftPref));
        mNaviSwipeLeft.setOnPreferenceChangeListener(this);

        int naviBarColor = Settings.System.getInt(getContentResolver(),
                Settings.System.NAVI_BAR_COLOR, defValuesColor());
        mNaviBarColor.setSummary(Integer.toHexString(naviBarColor));
        mNaviBarColor.setEnabled(transparentNaviBarPref == 4);

        int naviButtonColor = Settings.System.getInt(getContentResolver(),
                Settings.System.OVERICON_COLOR, defValuesColor());
        mNaviButtonColor.setSummary(Integer.toHexString(naviButtonColor));
        mNaviButtonColor.setEnabled((Settings.System.getInt(getContentResolver(),
                Settings.System.ENABLE_OVERICON_COLOR, 1) == 1));

        int naviGlowColor = Settings.System.getInt(getContentResolver(),
                Settings.System.NAVBAR_GLOWING_COLOR, defValuesColor());
        mNaviGlowColor.setSummary(Integer.toHexString(naviGlowColor));
        mNaviGlowColor.setEnabled((Settings.System.getInt(getContentResolver(),
                Settings.System.ENABLE_OVERICON_COLOR, 1) == 1));

        mStatusBarNavi.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.SHOW_NAVI_BUTTONS, 1) == 1));
        mNaviBar.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.NAVI_BUTTONS, 1) == 1));
        mNaviBar.setEnabled((Settings.System.getInt(getContentResolver(),
                Settings.System.NAVI_BUTTONS, 1) != 2));
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mNavisize) {
            int NaviSize = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.STATUSBAR_NAVI_SIZE, NaviSize);
            restartStatusBar();
            return true;
        } else if (preference == mNaviAnimate) {
            int NaviAni = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.NAVI_BUTTONS_ANIMATE, NaviAni);
            return true;
        } else if (preference == mNaviSwipeRight) {
            int Naviswipe = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.WATCH_IS_NEXT, Naviswipe);
            return true;
        } else if (preference == mNaviSwipeLeft) {
            int Naviswipe = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.WATCH_IS_PREVIOUS, Naviswipe);
            return true;
        } else if (preference == mTransparentNaviBarPref) {
            int transparentNaviBarPref = Integer.parseInt(String.valueOf(newValue));
            if (transparentNaviBarPref != 6) {
                Settings.System.putInt(getContentResolver(), Settings.System.TRANSPARENT_NAVI_BAR,
                          transparentNaviBarPref);
            }
            mNaviBarColor.setEnabled(transparentNaviBarPref == 4);
            if (transparentNaviBarPref == 6) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
                intent.putExtra("scaleUpIfNeeded", false);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
                int width = getWindowManager().getDefaultDisplay().getWidth();
                Rect rect = new Rect();
                Window window = getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(rect);
                int naviBarsHeight = rect.bottom;
                int contentViewBottom = window.findViewById(Window.ID_ANDROID_CONTENT).getBottom();
                int naviBarHeight = contentViewBottom - naviBarsHeight;
                intent.putExtra("aspectX", width);
                intent.putExtra("aspectY", naviBarHeight);
                try {
                    navBackgroundImageTmp.createNewFile();
                    navBackgroundImageTmp.setWritable(true, false);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(navBackgroundImageTmp));
                    intent.putExtra("return-data", false);
                    startActivityForResult(intent,REQUEST_CODE_BACK_IMAGE);
                } catch (IOException e) {
                    Log.e("Picker", "IOException: ", e);
                } catch (ActivityNotFoundException e) {
                    Log.e("Picker", "ActivityNotFoundException: ", e);
                }
            }
            return true;
        }
        return false;
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        if (preference == mStatusBarNavi) {
            value = mStatusBarNavi.isChecked();
            if (value)  {
               restartStatusBar();
               Settings.System.putInt(getContentResolver(), Settings.System.NAVI_BUTTONS, 1);
               Settings.System.putInt(getContentResolver(), Settings.System.SHOW_NAVI_BUTTONS, 1);
               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BOTTOM, 0);
            } else {
               Settings.System.putInt(getContentResolver(), Settings.System.NAVI_BUTTONS, 0);
               Settings.System.putInt(getContentResolver(), Settings.System.SHOW_NAVI_BUTTONS, 0);
               restartStatusBar();
            }
            return true;
        } else if (preference == mNaviBar) {
            value = mNaviBar.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.NAVI_BUTTONS,
                   value ? 1 : 0);
            return true;
        } else if (preference == mNaviButtonColorEnable) {
            value = mNaviButtonColorEnable.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.ENABLE_OVERICON_COLOR,
                    value ? 1 : 0);
            mNaviButtonColor.setEnabled(value ? true : false);
            mNaviGlowColor.setEnabled(value ? true : false);
            return true;
        } else if (preference == mNaviBarColor) {
            ColorPickerDialog cp = new ColorPickerDialog(this, mNaviBarColorListener, getNaviBarColor());
            cp.show();
            return true;
        } else if (preference == mNaviButtonColor) {
            ColorPickerDialog cp = new ColorPickerDialog(this, mNaviButtonColorListener, getNaviButtonColor());
            cp.show();
            return true;
        } else if (preference == mNaviGlowColor) {
            ColorPickerDialog cp = new ColorPickerDialog(this, mNaviGlowColorListener, getNaviGlowColor());
            cp.show();
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

    private int getNaviBarColor() {
        try {
            return Settings.System.getInt(getContentResolver(),
                     Settings.System.NAVI_BAR_COLOR);
        } catch (SettingNotFoundException e) {
            return defValuesColor();
        }
    }

    ColorPickerDialog.OnColorChangedListener mNaviBarColorListener =
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.NAVI_BAR_COLOR, color);
                mNaviBarColor.setSummary(Integer.toHexString(color));
            }
            public void colorUpdate(int color) {
            }
    };

    private int getNaviButtonColor() {
        try {
            return Settings.System.getInt(getContentResolver(),
                     Settings.System.OVERICON_COLOR);
        } catch (SettingNotFoundException e) {
            return defValuesColor();
        }
    }

    private int getNaviGlowColor() {
        try {
            return Settings.System.getInt(getContentResolver(),
                     Settings.System.NAVBAR_GLOWING_COLOR);
        } catch (SettingNotFoundException e) {
            return defValuesColor();
        }
    }

    ColorPickerDialog.OnColorChangedListener mNaviButtonColorListener =
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Handler h = new Handler();
                if (Settings.System.getInt(getContentResolver(), Settings.System.ENABLE_OVERICON_COLOR, 1) == 1) {
                    Settings.System.putInt(getContentResolver(), Settings.System.ENABLE_OVERICON_COLOR, 2);
                }
                Settings.System.putInt(getContentResolver(), Settings.System.OVERICON_COLOR, color);
                mNaviButtonColor.setSummary(Integer.toHexString(color));
                h.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                    if (Settings.System.getInt(getContentResolver(), Settings.System.ENABLE_OVERICON_COLOR, 1) == 2) {
                       Settings.System.putInt(getContentResolver(), Settings.System.ENABLE_OVERICON_COLOR, 1);
                    }
                   }
                }, 100);
            }
            public void colorUpdate(int color) {
            }
    };

    ColorPickerDialog.OnColorChangedListener mNaviGlowColorListener =
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Handler h = new Handler();
                if (Settings.System.getInt(getContentResolver(), Settings.System.ENABLE_OVERICON_COLOR, 1) == 1) {
                    Settings.System.putInt(getContentResolver(), Settings.System.ENABLE_OVERICON_COLOR, 2);
                }
                Settings.System.putInt(getContentResolver(), Settings.System.NAVBAR_GLOWING_COLOR, color);
                mNaviGlowColor.setSummary(Integer.toHexString(color));
                h.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                    if (Settings.System.getInt(getContentResolver(), Settings.System.ENABLE_OVERICON_COLOR, 1) == 2) {
                       Settings.System.putInt(getContentResolver(), Settings.System.ENABLE_OVERICON_COLOR, 1);
                    }
                   }
                }, 100);
            }
            public void colorUpdate(int color) {
            }
    };

    private int defValuesColor() {
        return getResources().getInteger(com.android.internal.R.color.color_default_cyanmobile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Context context = getApplicationContext();
        switch (requestCode) {
            case REQUEST_CODE_BACK_IMAGE:
                if (resultCode != RESULT_OK) {
                    if (navBackgroundImageTmp.exists()) {
                        navBackgroundImageTmp.delete();
                    }
                    int transparentsNaviBarPref = Settings.System.getInt(getContentResolver(),
                         Settings.System.TRANSPARENT_NAVI_BAR, 0);
                    mTransparentNaviBarPref.setValue(String.valueOf(transparentsNaviBarPref));
                    Toast.makeText(context, "CyanMobile navibar background not set" ,Toast.LENGTH_LONG).show();
                } else {
                   if (navBackgroundImageTmp.exists()) {
                       navBackgroundImageTmp.renameTo(navBackgroundImage);
                   }
                   navBackgroundImage.setReadOnly();
                   Settings.System.putInt(getContentResolver(), Settings.System.TRANSPARENT_NAVI_BAR, 3);
                   Toast.makeText(context, "CyanMobile navibar background set to new image" ,Toast.LENGTH_LONG).show();
                }
            break;
        }
    }
}
