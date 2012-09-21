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
import android.provider.MediaStore;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.CmSystem;
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

public class TabletTweaksActivity extends PreferenceActivity implements OnPreferenceChangeListener{
    private static final String PREF_STATUS_BAR_BOTTOM = "pref_status_bar_bottom";
    private static final String PREF_STATUS_BAR_NAVI = "pref_status_bar_navi";
    private static final String PREF_NAVI_BAR = "pref_navi_bar";
    private static final String PREF_STATUS_BAR_SOFT = "pref_status_bar_soft";
    private static final String PREF_SQUADZONE = "squadkeys";
    private static final String PREF_NAVISIZE = "status_bar_navisize";
    private static final String TRANSPARENT_NAVI_BAR_PREF = "pref_transparent_navi_bar";
    private static final String PREF_NAVI_BAR_COLOR = "pref_navi_bar_color";
    private static final String PREF_NAVI_BUTTON_COLOR = "pref_navi_button_color";
    private static final String PREF_STATUS_BAR_DEAD_ZONE = "pref_status_bar_dead_zone";
    private static final String PREF_SOFT_BUTTONS_LEFT = "pref_soft_buttons_left";
    private static final String PREF_DISABLE_LOCKSCREEN = "pref_disable_lockscreen";
    private static final String PREF_DISABLE_FULLSCREEN = "pref_disable_fullscreen";
    private static final String PREF_UNHIDE_BUTTON = "pref_unhide_button";
    private static final String PREF_EXTEND_PM = "pref_extend_pm";
    private static final String PREF_ENABLE_OVERICON = "pref_enable_overicon";
    // cm71 nightlies: will be re-enabled there
    //private static final String PREF_REVERSE_VOLUME_BEHAVIOR = "pref_reverse_volume_behavior";
    private static final String PREF_GENERAL_CATEGORY = "pref_general_category";
    private static final String PREF_INTERFACE_CATEGORY = "pref_interface_category";
    private static final String PREF_BUTTON_CATEGORY = "pref_button_category";
    private static final String PREF_EXTEND_PM_LIST = "pref_extend_pm_list";
    private static final String PREF_SOFT_BUTTON_LIST = "pref_soft_button_list";

    private static final int REQUEST_CODE_BACK_IMAGE = 998;

    private CheckBoxPreference mStatusBarBottom;
    private CheckBoxPreference mStatusBarNavi;
    private CheckBoxPreference mNaviBar;
    private CheckBoxPreference mStatusBarSoft;
    private CheckBoxPreference mStatusBarDeadZone;
    private CheckBoxPreference mSoftButtonsLeft;
    private CheckBoxPreference mDisableLockscreen;
    private CheckBoxPreference mDisableFullscreen;
    private CheckBoxPreference mExtendPm;
    private CheckBoxPreference mNaviButtonColorEnable;
    private Preference mNaviBarColor;
    private Preference mNaviButtonColor;
    private Preference mSquadzone;
    private ListPreference mNavisize;
    private ListPreference mTransparentNaviBarPref;
    // cm71 nightlies: will be re-enabled there
    //private CheckBoxPreference mReverseVolumeBehavior;
    private ListPreference mUnhideButton;
    private AlertDialog alertDialog;

    private File navBackgroundImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.tablet_tweaks_title_head);
        addPreferencesFromResource(R.xml.tablet_settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        mSquadzone = (Preference) prefSet.findPreference(PREF_SQUADZONE);
        mSquadzone.setSummary("CyanMobile");

        mStatusBarBottom = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_BOTTOM);
        mStatusBarNavi = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_NAVI);
        mNaviBar = (CheckBoxPreference) prefSet.findPreference(PREF_NAVI_BAR);
        mStatusBarSoft = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_SOFT);
        mStatusBarDeadZone = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_DEAD_ZONE);
        mSoftButtonsLeft = (CheckBoxPreference) prefSet.findPreference(PREF_SOFT_BUTTONS_LEFT);
        mDisableLockscreen = (CheckBoxPreference) prefSet.findPreference(PREF_DISABLE_LOCKSCREEN);
        mDisableFullscreen = (CheckBoxPreference) prefSet.findPreference(PREF_DISABLE_FULLSCREEN);
        mUnhideButton = (ListPreference) prefSet.findPreference(PREF_UNHIDE_BUTTON);
        mExtendPm = (CheckBoxPreference) prefSet.findPreference(PREF_EXTEND_PM);
        mNaviButtonColorEnable = (CheckBoxPreference) prefSet.findPreference(PREF_ENABLE_OVERICON);
        mNaviButtonColorEnable.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.ENABLE_OVERICON_COLOR, 1) == 1));

        mNaviBarColor = (Preference) prefSet.findPreference(PREF_NAVI_BAR_COLOR);
        mNaviBarColor.setOnPreferenceChangeListener(this);

        mNaviButtonColor = (Preference) prefSet.findPreference(PREF_NAVI_BUTTON_COLOR);
        mNaviButtonColor.setOnPreferenceChangeListener(this);

        mNavisize = (ListPreference) prefSet.findPreference(PREF_NAVISIZE);
        mNavisize.setOnPreferenceChangeListener(this);
        mNavisize.setValue(Integer.toString(Settings.System.getInt(getContentResolver(),
                Settings.System.STATUSBAR_NAVI_SIZE, 25)));

        int transparentNaviBarPref = Settings.System.getInt(getContentResolver(),
                Settings.System.TRANSPARENT_NAVI_BAR, 0);
	mTransparentNaviBarPref = (ListPreference) prefSet.findPreference(TRANSPARENT_NAVI_BAR_PREF);
        mTransparentNaviBarPref.setValue(String.valueOf(transparentNaviBarPref));
        mTransparentNaviBarPref.setOnPreferenceChangeListener(this);
        navBackgroundImage = new File(getApplicationContext().getFilesDir()+"/navb_background");

        int naviBarColor = Settings.System.getInt(getContentResolver(),
                Settings.System.NAVI_BAR_COLOR, 0xFF38FF00);
        mNaviBarColor.setSummary(Integer.toHexString(naviBarColor));
        mNaviBarColor.setEnabled(transparentNaviBarPref == 4);

        int naviButtonColor = Settings.System.getInt(getContentResolver(),
                Settings.System.OVERICON_COLOR, 0xFF38FF00);
        mNaviButtonColor.setSummary(Integer.toHexString(naviButtonColor));
        mNaviButtonColor.setEnabled((Settings.System.getInt(getContentResolver(),
                Settings.System.ENABLE_OVERICON_COLOR, 1) == 1));

        int defValue;

        defValue=CmSystem.getDefaultBool(getBaseContext(), CmSystem.CM_DEFAULT_BOTTOM_STATUS_BAR)==true ? 1 : 0;
        mStatusBarBottom.setChecked((Settings.System.getInt(getContentResolver(),
               Settings.System.STATUS_BAR_BOTTOM, 0) == 1));
        mStatusBarNavi.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.SHOW_NAVI_BUTTONS, 1) == 1));
        mNaviBar.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.NAVI_BUTTONS, 1) == 1));
        mNaviBar.setEnabled((Settings.System.getInt(getContentResolver(),
                Settings.System.NAVI_BUTTONS, 1) != 2));
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

        defValue=CmSystem.getDefaultInt(getBaseContext(), CmSystem.CM_DEFAULT_UNHIDE_BUTTON_INDEX);
        mUnhideButton.setOnPreferenceChangeListener(this);
        mUnhideButton.setValueIndex(Settings.System.getInt(getContentResolver(),
                Settings.System.UNHIDE_BUTTON, defValue));

        if (Settings.System.getInt(getContentResolver(),
                            Settings.System.EXPANDED_VIEW_WIDGET, 1) == 4) {
            new AlertDialog.Builder(this)
            .setTitle("Changing Status Bar Layout")
            .setMessage("System has detect you are using Tab layout.\nneed change to default before enable tablet tweaks.\nRestart now?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET, 1);
                        try {
                            Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                        } catch (IOException e) {
                           // we're screwed here fellas
                        }
                    }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                         mStatusBarBottom.setEnabled(false);
                    }
             })
            .show();
        } else if (Settings.System.getInt(getContentResolver(),
                            Settings.System.EXPANDED_VIEW_WIDGET, 1) == 3) {
            new AlertDialog.Builder(this)
            .setTitle("Changing Status Bar Layout")
            .setMessage("System has detect you are using Grid layout.\nneed change to default before enable tablet tweaks.\nDisable now?")
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
                     mStatusBarNavi.setChecked(false);
                     updateDependencies();
                     try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                     } catch (IOException e) {
                       // we're screwed here fellas
                     }
                 } else {
                     Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BOTTOM, 1);
                     mStatusBarNavi.setChecked(false);
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
               try {
                  Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
               } catch (IOException e) {
                  // we're screwed here fellas
               }
            } else {
               Settings.System.putInt(getContentResolver(), Settings.System.USE_SOFT_BUTTONS, 0);
               try {
                  Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
               } catch (IOException e) {
                  // we're screwed here fellas
               }
            }
            return true;
        } else if (preference == mStatusBarNavi) {
            value = mStatusBarNavi.isChecked();
            if (value)  {
               try {
                  Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
               } catch (IOException e) {
                  // we're screwed here fellas
               }
               Settings.System.putInt(getContentResolver(), Settings.System.NAVI_BUTTONS, 1);
               Settings.System.putInt(getContentResolver(), Settings.System.SHOW_NAVI_BUTTONS, 1);
               Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BOTTOM, 0);
               mStatusBarBottom.setChecked(false);
               updateDependencies();
            } else {
               Settings.System.putInt(getContentResolver(), Settings.System.NAVI_BUTTONS, 0);
               Settings.System.putInt(getContentResolver(), Settings.System.SHOW_NAVI_BUTTONS, 0);
               try {
                  Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
               } catch (IOException e) {
                  // we're screwed here fellas
               }
            }
            return true;
        } else if (preference == mNaviBar) {
            value = mNaviBar.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.NAVI_BUTTONS,
                   value ? 1 : 0);
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
        } else if (preference == mNaviButtonColorEnable) {
            value = mNaviButtonColorEnable.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.ENABLE_OVERICON_COLOR,
                    value ? 1 : 0);
            mNaviButtonColor.setEnabled(value ? true : false);
            return true;
        } else if (preference == mNaviBarColor) {
            ColorPickerDialog cp = new ColorPickerDialog(this, mNaviBarColorListener, getNaviBarColor());
            cp.show();
            return true;
        } else if (preference == mNaviButtonColor) {
            ColorPickerDialog cp = new ColorPickerDialog(this, mNaviButtonColorListener, getNaviButtonColor());
            cp.show();
            return true;
        }

        return false;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mUnhideButton) {
            int value = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.UNHIDE_BUTTON, value);
            return true;
        } else if (preference == mNavisize) {
            int NaviSize = Integer.valueOf((String) newValue);
            if (Settings.System.getInt(getContentResolver(),
                             Settings.System.NAVI_BUTTONS, 0) == 1) {
                try {
                   Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                } catch (IOException e) {
                   // we're screwed here fellas
                }
                Settings.System.putInt(getContentResolver(), Settings.System.STATUSBAR_NAVI_SIZE, NaviSize);
            }
            return true;
        } else if (preference == mTransparentNaviBarPref) {
            int transparentNaviBarPref = Integer.parseInt(String.valueOf(newValue));
            Settings.System.putInt(getContentResolver(), Settings.System.TRANSPARENT_NAVI_BAR,
                          transparentNaviBarPref);
            mNaviBarColor.setEnabled(transparentNaviBarPref == 4);
            if (transparentNaviBarPref == 6) {
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
                    navBackgroundImage.createNewFile();
                    navBackgroundImage.setReadable(true, false);
                    navBackgroundImage.setWritable(true, false);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(navBackgroundImage));
                    intent.putExtra("return-data", false);
                    startActivityForResult(intent,REQUEST_CODE_BACK_IMAGE);
                } catch (IOException e) {
                    Log.e("Picker", "IOException: ", e);
                } catch (ActivityNotFoundException e) {
                    Log.e("Picker", "ActivityNotFoundException: ", e);
                }
            } else {
                if (transparentNaviBarPref == 4) {
                // do nothing
                } else {
                  if (Settings.System.getInt(getContentResolver(),
                             Settings.System.NAVI_BUTTONS, 0) == 1) {
                    try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                    } catch (IOException e) {
                      // we're screwed here fellas
                    }
                  }
                }
            }
            return true;
        }
        return false;
    }

    private int getNaviBarColor() {
        try {
            return Settings.System.getInt(getContentResolver(),
                     Settings.System.NAVI_BAR_COLOR);
        } catch (SettingNotFoundException e) {
            return -16777216;
        }
    }

    ColorPickerDialog.OnColorChangedListener mNaviBarColorListener =
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.NAVI_BAR_COLOR, color);
                mNaviBarColor.setSummary(Integer.toHexString(color));
                    if (Settings.System.getInt(getContentResolver(),
                              Settings.System.NAVI_BUTTONS, 0) == 1) {
                       try {
                          Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                       } catch (IOException e) {
                         // we're screwed here fellas
                       }
                    }
            }
            public void colorUpdate(int color) {
            }
    };

    private int getNaviButtonColor() {
        try {
            return Settings.System.getInt(getContentResolver(),
                     Settings.System.OVERICON_COLOR);
        } catch (SettingNotFoundException e) {
            return -16777216;
        }
    }

    ColorPickerDialog.OnColorChangedListener mNaviButtonColorListener =
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.OVERICON_COLOR, color);
                mNaviButtonColor.setSummary(Integer.toHexString(color));
            }
            public void colorUpdate(int color) {
            }
    };

    private void updateDependencies() {
        if(!mStatusBarBottom.isChecked()){
            mStatusBarDeadZone.setChecked(false);
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_DEAD_ZONE, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Context context = getApplicationContext();
        switch (requestCode) {
            case REQUEST_CODE_BACK_IMAGE:
                if (resultCode != RESULT_OK) {
                    Log.d("Copy_image_Error", "Error: " + resultCode);
                } else { 
                   try {
                       Runtime.getRuntime().exec("pkill -TERM -f  com.android.systemui");
                   } catch (IOException e) {
                       // we're screwed here fellas
                   }
                   Settings.System.putInt(getContentResolver(), Settings.System.TRANSPARENT_NAVI_BAR, 3);
                   Toast.makeText(context, "Success set to new image" ,Toast.LENGTH_LONG).show();
                }
            break;
        }
    }
}
