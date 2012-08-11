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
import android.graphics.Color;
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
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

import com.cyanogenmod.cmparts.R;
import com.cyanogenmod.cmparts.activities.ColorPickerDialog.OnColorChangedListener;

public class UIActivity extends PreferenceActivity implements OnPreferenceChangeListener {

    /* Preference Screens */
    private static final String NOTIFICATION_SCREEN = "notification_settings";

    private static final String NOTIFICATION_TRACKBALL = "trackball_notifications";

    private static final String PREF_TEXT_FULL_OF_COLOR = "pref_text_full_of_color";

    private static final String PREF_TEXT_GLOBAL_OF_COLOR = "pref_text_global_of_color";

    private static final String PREF_SQUADZONE = "squadkeys";

    private static final String EXTRAS_SCREEN = "tweaks_extras";

    private static final String GENERAL_CATEGORY = "general_category";

    private static final String PREF_PROFILE = "profile_dialog_prompt";

    private static final String PREF_SILENT = "silent_dialog_prompt";

    private static final String PREF_AIRPLANE = "airplane_dialog_prompt";

    private static final String PREF_POWER_SAVER = "powersaver_dialog_prompt";

    private static final String PREF_SCREENSHOT = "screenshot_dialog_prompt";

    private static final String PREF_SUSPEND = "suspend_dialog_prompt";

    private static final String PREF_HIBERNATE = "hibernate_dialog_prompt";

    /* private static final String HIDE_AVATAR_MESSAGE_PREF = "pref_hide_avatar_message"; */

    private PreferenceScreen mStatusBarScreen;

    private PreferenceScreen mNotificationScreen;

    private PreferenceScreen mTrackballScreen;;

    private PreferenceScreen mExtrasScreen;

    private CheckBoxPreference mShowProfile;

    private CheckBoxPreference mShowSilent;

    private CheckBoxPreference mShowAirplane;

    private CheckBoxPreference mShowScreenshot;

    private CheckBoxPreference mShowPowersaver;

    private CheckBoxPreference mShowSuspend;

    private CheckBoxPreference mShowHibernate;

    /* private CheckBoxPreference mHideAvatarMessage; */

    private CheckBoxPreference mTextGlobalOfColor;

    private Preference mTextFullOfColor;

    /* Other */
    private static final String BOOTANIMATION_PREF = "pref_bootanimation";

    private static final String BOOTSOUND_PREF = "pref_bootsound";

    private static final String SHUTDOWNANIMATION_PREF = "pref_shutdownanimation";

    private static final String BOOTSOUND_RESET_PREF = "pref_bootsound_reset";

    private static final String BOOTANIMATION_RESET_PREF = "pref_bootanimation_reset";

    private static final String SHUTDOWNANIMATION_RESET_PREF = "pref_shutdownanimation_reset";

    private static final String BOOTANIMATION_PREVIEW_PREF = "pref_bootanimation_preview";

    private static final String PINCH_REFLOW_PREF = "pref_pinch_reflow";

    private static final String RENDER_EFFECT_PREF = "pref_render_effect";

    private static final String POWER_PROMPT_PREF = "power_dialog_prompt";

    private static final String SHARE_SCREENSHOT_PREF = "pref_share_screenshot";

    private static final String OVERSCROLL_PREF = "pref_overscroll_effect";

    private static final String OVERSCROLL_WEIGHT_PREF = "pref_overscroll_weight";

    private static final String OVERSCROLL_COLOR = "pref_overscroll_color";

    private static final String KEY_FONT_FONT_SETINGS = "fontsettings";

    private CheckBoxPreference mPinchReflowPref;

    private CheckBoxPreference mPowerPromptPref;

    private ListPreference mRenderEffectPref;

    private CheckBoxPreference mShareScreenshotPref;

    private ListPreference mOverscrollPref;

    private ListPreference mOverscrollColor;

    private ListPreference mOverscrollWeightPref;

    private PreferenceScreen mBootPref;

    private PreferenceScreen mBootsoundPref;

    private PreferenceScreen mShutPref;

    private PreferenceScreen mBootReset;

    private PreferenceScreen mBootSoundReset;

    private PreferenceScreen mShutReset;

    private PreferenceScreen mBootPreview;

    private PreferenceScreen mFontsPref;

    private static final int REQUEST_CODE_PICK_FILESHUT = 997;

    private static final int REQUEST_CODE_PICK_FILESOUND = 998;

    private static final int REQUEST_CODE_PICK_FILE = 999;

    private static final int REQUEST_CODE_MOVE_FILE = 1000;

    private static final int REQUEST_CODE_PREVIEW_FILE = 1001;

    private static boolean mBootPreviewRunning;

    private static int prevOrientation;

    private Preference mSquadzone;

    private static final String MOVE_BOOT_INTENT = "com.cyanogenmod.cmbootanimation.MOVE_BOOTANIMATION";

    private static final String MOVE_BOOTSOUND_INTENT = "com.cyanogenmod.cmbootsound.COPY_BOOTSOUND";

    private static final String MOVE_SHUT_INTENT = "com.cyanogenmod.cmshutdownanimation.MOVE_SHUTDOWNANIMATION";

    private static final String BOOT_RESET = "com.cyanogenmod.cmbootanimation.RESET_DEFAULT";

    private static final String BOOTSOUND_RESET = "com.cyanogenmod.cmbootsound.RESET_SOUND";

    private static final String SHUT_RESET = "com.cyanogenmod.cmshutdownanimation.RESET_SHUT";

    private static final String BOOT_PREVIEW_FILE = "preview_bootanim";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.interface_settings_title_head);
        addPreferencesFromResource(R.xml.ui_settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        mSquadzone = (Preference) prefSet.findPreference(PREF_SQUADZONE);
        mSquadzone.setSummary("CyanMobile");

        /* Preference Screens */
        mNotificationScreen = (PreferenceScreen) prefSet.findPreference(NOTIFICATION_SCREEN);
        mTrackballScreen = (PreferenceScreen) prefSet.findPreference(NOTIFICATION_TRACKBALL);
        mExtrasScreen = (PreferenceScreen) prefSet.findPreference(EXTRAS_SCREEN);

        boolean hasLed = getResources().getBoolean(R.bool.has_rgb_notification_led)
                || getResources().getBoolean(R.bool.has_dual_notification_led)
                || getResources().getBoolean(R.bool.has_single_notification_led);

        if (!hasLed) {
            ((PreferenceCategory) prefSet.findPreference(GENERAL_CATEGORY))
                    .removePreference(mTrackballScreen);
        }

        mFontsPref = (PreferenceScreen) prefSet.findPreference(KEY_FONT_FONT_SETINGS);

        /* Boot Animation Chooser */
        mBootPref = (PreferenceScreen) prefSet.findPreference(BOOTANIMATION_PREF);
        mBootReset = (PreferenceScreen) prefSet.findPreference(BOOTANIMATION_RESET_PREF);
        mBootPreview = (PreferenceScreen) prefSet.findPreference(BOOTANIMATION_PREVIEW_PREF);
        mBootsoundPref = (PreferenceScreen) prefSet.findPreference(BOOTSOUND_PREF);
        mBootSoundReset = (PreferenceScreen) prefSet.findPreference(BOOTSOUND_RESET_PREF);
        mShutPref = (PreferenceScreen) prefSet.findPreference(SHUTDOWNANIMATION_PREF);
        mShutReset = (PreferenceScreen) prefSet.findPreference(SHUTDOWNANIMATION_RESET_PREF);

        /* Pinch reflow */
        mPinchReflowPref = (CheckBoxPreference) prefSet.findPreference(PINCH_REFLOW_PREF);
        mPinchReflowPref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.WEB_VIEW_PINCH_REFLOW, 0) == 1);

        mPowerPromptPref = (CheckBoxPreference) prefSet.findPreference(POWER_PROMPT_PREF);
        mRenderEffectPref = (ListPreference) prefSet.findPreference(RENDER_EFFECT_PREF);
        mRenderEffectPref.setOnPreferenceChangeListener(this);
        updateFlingerOptions();

        mTextGlobalOfColor = (CheckBoxPreference) prefSet.findPreference(PREF_TEXT_GLOBAL_OF_COLOR);
        mTextGlobalOfColor.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.TEXT_GLOBALOFCOLOR, 1) == 1));

	mTextFullOfColor = (Preference) prefSet.findPreference(PREF_TEXT_FULL_OF_COLOR);
        mTextFullOfColor.setOnPreferenceChangeListener(this);

        mShowProfile = (CheckBoxPreference) findPreference(PREF_PROFILE);
        mShowProfile.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_DIALOG_SHOW_PROFILE, 0) == 1);

        mShowSilent = (CheckBoxPreference) findPreference(PREF_SILENT);
        mShowSilent.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_DIALOG_SHOW_SILENT, 0) == 1);

        mShowAirplane = (CheckBoxPreference) findPreference(PREF_AIRPLANE);
        mShowAirplane.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_DIALOG_SHOW_AIRPLANE, 0) == 1);

        mShowScreenshot = (CheckBoxPreference) findPreference(PREF_SCREENSHOT);
        mShowScreenshot.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_DIALOG_SHOW_SCREENSHOT, 0) == 1);

        mShowSuspend = (CheckBoxPreference) findPreference(PREF_SUSPEND);
        mShowSuspend.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_DIALOG_SHOW_SUSPEND, 1) == 1);

        mShowHibernate = (CheckBoxPreference) findPreference(PREF_HIBERNATE);
        mShowHibernate.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_DIALOG_SHOW_HIBERNATE, 1) == 1);

        /* mHideAvatarMessage = (CheckBoxPreference) findPreference(HIDE_AVATAR_MESSAGE_PREF);
        mHideAvatarMessage.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.HIDE_AVATAR_MESSAGE, 0) == 1); */

        mShowPowersaver = (CheckBoxPreference) findPreference(PREF_POWER_SAVER);
        mShowPowersaver.setChecked(Settings.Secure.getInt(getContentResolver(),
                Settings.Secure.POWER_DIALOG_SHOW_POWER_SAVER, 0) == 1);

        /* Share Screenshot */
        mShareScreenshotPref = (CheckBoxPreference) prefSet.findPreference(SHARE_SCREENSHOT_PREF);
        mShareScreenshotPref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.SHARE_SCREENSHOT, 0) == 1);

        /* Overscroll Effect */
        mOverscrollPref = (ListPreference) prefSet.findPreference(OVERSCROLL_PREF);
        int overscrollEffect = Settings.System.getInt(getContentResolver(),
                Settings.System.OVERSCROLL_EFFECT, 1);
        mOverscrollPref.setValue(String.valueOf(overscrollEffect));
        mOverscrollPref.setOnPreferenceChangeListener(this);

        mOverscrollColor = (ListPreference) prefSet.findPreference(OVERSCROLL_COLOR);
        mOverscrollColor.setOnPreferenceChangeListener(this);
        int overscrollColor = Settings.System.getInt(getContentResolver(),
                Settings.System.OVERSCROLL_COLOR,0);
        mOverscrollColor.setValue(String.valueOf(overscrollColor == 0 ? 0 : 1));

        mOverscrollWeightPref = (ListPreference) prefSet.findPreference(OVERSCROLL_WEIGHT_PREF);
        int overscrollWeight = Settings.System.getInt(getContentResolver(),
                Settings.System.OVERSCROLL_WEIGHT, 5);
        mOverscrollWeightPref.setValue(String.valueOf(overscrollWeight));
        mOverscrollWeightPref.setOnPreferenceChangeListener(this);

    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        /* Preference Screens */
        if (preference == mStatusBarScreen) {
            startActivity(mStatusBarScreen.getIntent());
            return true;
        } else if (preference == mNotificationScreen) {
            startActivity(mNotificationScreen.getIntent());
            return true;
        } else if (preference == mTrackballScreen) {
            startActivity(mTrackballScreen.getIntent());
            return true;
        } else if (preference == mExtrasScreen) {
            startActivity(mExtrasScreen.getIntent());
            return true;
        } else if (preference == mPinchReflowPref) {
            value = mPinchReflowPref.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.WEB_VIEW_PINCH_REFLOW,
                    value ? 1 : 0);
            return true;
        } else if (preference == mShareScreenshotPref) {
            value = mShareScreenshotPref.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.SHARE_SCREENSHOT,
                    value ? 1 : 0);
            return true;
        } else if (preference == mTextGlobalOfColor) {
            value = mTextGlobalOfColor.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.TEXT_GLOBALOFCOLOR,
                    value ? 1 : 0);
            return true;
        } else if (preference == mTextFullOfColor) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
            mTextFullOfColorListener,
            readTextFullOfColor());
            cp.show();
	    return true;
        } else if (preference == mPowerPromptPref) {
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
        /* } else if (preference == mHideAvatarMessage) {
            value = mHideAvatarMessage.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.HIDE_AVATAR_MESSAGE,
                    value ? 1 : 0);
            return true; */
        } else if (preference == mShowHibernate) {
            value = mShowHibernate.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.POWER_DIALOG_SHOW_HIBERNATE,
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
        } else if (preference == mBootPref) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setDataAndType(Uri.fromFile(new File("/sdcard/download")), "file/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
            return true;
        } else if (preference == mBootsoundPref) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setDataAndType(Uri.fromFile(new File("/sdcard/download")), "file/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_FILESOUND);
            return true;
        } else if (preference == mShutPref) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setDataAndType(Uri.fromFile(new File("/sdcard/download")), "file/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_FILESHUT);
            return true;
        } else if (preference == mBootReset) {
            Intent intent = new Intent(BOOT_RESET);
            sendBroadcast(intent);
            return true;
        } else if (preference == mBootSoundReset) {
            Intent intent = new Intent(BOOTSOUND_RESET);
            sendBroadcast(intent);
            return true;
        } else if (preference == mShutReset) {
            Intent intent = new Intent(SHUT_RESET);
            sendBroadcast(intent);
            return true;
        }  else if (preference == mBootPreview) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setDataAndType(Uri.fromFile(new File("/sdcard/download")), "file/*");
            startActivityForResult(intent, REQUEST_CODE_PREVIEW_FILE);
            return true;
        }  else if (preference == mFontsPref) {
              final String key = preference.getKey();
              if (KEY_FONT_FONT_SETINGS.equals(key)) {
                  return false;
              }
            return true;
        }
        return false;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String val = newValue.toString();
        if (preference == mRenderEffectPref) {
            writeRenderEffect(Integer.valueOf((String) newValue));
            return true;
        } else if (preference == mOverscrollPref) {
            int overscrollEffect = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.OVERSCROLL_EFFECT,
                    overscrollEffect);
            return true;
        } else if (preference == mOverscrollWeightPref) {
            int overscrollWeight = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.OVERSCROLL_WEIGHT,
                    overscrollWeight);
            return true;
        } else if (preference == mOverscrollColor){
            if (mOverscrollColor.findIndexOfValue(val)==0){
                Settings.System.putInt(getContentResolver(), Settings.System.OVERSCROLL_COLOR,0);
            }else{
                int overscrollColor = Settings.System.getInt(getContentResolver(),
                        Settings.System.OVERSCROLL_COLOR,0);
                ColorPickerDialog cp = new ColorPickerDialog(this,mPackageColorListener,
                        overscrollColor);
                cp.show();
            }
            return true;
        }
        return false;
    }

    ColorPickerDialog.OnColorChangedListener mPackageColorListener = new
    ColorPickerDialog.OnColorChangedListener() {
        public void colorChanged(int color) {
            Settings.System.putInt(getContentResolver(), Settings.System.OVERSCROLL_COLOR,color);
        }
        @Override
        public void colorUpdate(int color) {
        }
    };

    // Taken from DevelopmentSettings
    private void updateFlingerOptions() {
        // magic communication with surface flinger.
        try {
            IBinder flinger = ServiceManager.getService("SurfaceFlinger");
            if (flinger != null) {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                data.writeInterfaceToken("android.ui.ISurfaceComposer");
                flinger.transact(1010, data, reply, 0);
                int v;
                v = reply.readInt();
                // mShowCpuCB.setChecked(v != 0);
                v = reply.readInt();
                // mEnableGLCB.setChecked(v != 0);
                v = reply.readInt();
                // mShowUpdatesCB.setChecked(v != 0);
                v = reply.readInt();
                // mShowBackgroundCB.setChecked(v != 0);

                v = reply.readInt();
                mRenderEffectPref.setValue(String.valueOf(v));

                reply.recycle();
                data.recycle();
            }
        } catch (RemoteException ex) {
        }

    }

    private void writeRenderEffect(int id) {
        try {
            IBinder flinger = ServiceManager.getService("SurfaceFlinger");
            if (flinger != null) {
                Parcel data = Parcel.obtain();
                data.writeInterfaceToken("android.ui.ISurfaceComposer");
                data.writeInt(id);
                flinger.transact(1014, data, null, 0);
                data.recycle();
            }
        } catch (RemoteException ex) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Context context = getApplicationContext();
        switch (requestCode) {
            case REQUEST_CODE_PICK_FILESHUT:
                if (resultCode == RESULT_OK && data != null) {
                    // obtain the filename
                    Uri fileUri = data.getData();
                    if (fileUri != null) {
                        String filePath = fileUri.getPath();
                        if (filePath != null) {
                            Intent mvShutIntent = new Intent();
                            mvShutIntent.setAction(MOVE_SHUT_INTENT);
                            mvShutIntent.putExtra("fileName", filePath);
                            sendBroadcast(mvShutIntent);
                        }
                    }
                }
            break;
            case REQUEST_CODE_PICK_FILESOUND:
                if (resultCode == RESULT_OK && data != null) {
                    // obtain the filename
                    Uri fileUri = data.getData();
                    if (fileUri != null) {
                        String filePath = fileUri.getPath();
                        if (filePath != null) {
                            Intent mvBootsoundIntent = new Intent();
                            mvBootsoundIntent.setAction(MOVE_BOOTSOUND_INTENT);
                            mvBootsoundIntent.putExtra("fileName", filePath);
                            sendBroadcast(mvBootsoundIntent);
                        }
                    }
                }
            break;
            case REQUEST_CODE_PICK_FILE:
                if (resultCode == RESULT_OK && data != null) {
                    // obtain the filename
                    Uri fileUri = data.getData();
                    if (fileUri != null) {
                        String filePath = fileUri.getPath();
                        if (filePath != null) {
                            Intent mvBootIntent = new Intent();
                            mvBootIntent.setAction(MOVE_BOOT_INTENT);
                            mvBootIntent.putExtra("fileName", filePath);
                            sendBroadcast(mvBootIntent);
                        }
                    }
                }
            break;
            case REQUEST_CODE_PREVIEW_FILE:
                if (resultCode == RESULT_OK && data != null) {
                    Uri fileUri = data.getData();
                    if (fileUri != null) {
                        String filePath = fileUri.getPath();
                        if (filePath != null) {
                            try {
                                FileOutputStream outfile = context.openFileOutput(BOOT_PREVIEW_FILE, Context.MODE_WORLD_READABLE);
                                outfile.write(filePath.getBytes());
                                outfile.close();
                            } catch (Exception e) { }
                            mBootPreviewRunning = true;
                            prevOrientation = getRequestedOrientation();
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            SystemProperties.set("ctl.start", "bootanim");
                        }
                    }
                }
            break;
        }
    }

    private int readTextFullOfColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.TEXT_FULLOFCOLOR);
        }
        catch (SettingNotFoundException e) {
            return -1;
        }
    }

    ColorPickerDialog.OnColorChangedListener mTextFullOfColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.TEXT_FULLOFCOLOR, color);
            }
            public void colorUpdate(int color) {
            }
    };

    @Override
    public void onPause() {
        super.onPause();
        Context context = getApplicationContext();
        if(mBootPreviewRunning) {
	    File rmFile = new File(context.getFilesDir(), BOOT_PREVIEW_FILE);
            if (rmFile.exists()) {
                try {
                    rmFile.delete();
                } catch (Exception e) { }
            }
            setRequestedOrientation(prevOrientation);
            SystemProperties.set("ctl.stop", "bootanim");
            mBootPreviewRunning = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        Context context = getApplicationContext();
        if (keyCode == KeyEvent.KEYCODE_BACK && mBootPreviewRunning) {
            File rmFile = new File(context.getFilesDir(), BOOT_PREVIEW_FILE);
            if (rmFile.exists()) {
                try {
                    rmFile.delete();
                } catch (Exception e) { }
            }
            SystemProperties.set("ctl.stop", "bootanim");
            mBootPreviewRunning = false;
            setRequestedOrientation(prevOrientation);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
