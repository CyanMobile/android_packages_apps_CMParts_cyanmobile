
package com.cyanogenmod.cmparts.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.server.PowerSaverService;
import com.android.internal.telephony.Phone;
import android.util.Log;

import com.cyanogenmod.cmparts.R;

public class PowerSaverActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private static final String TAG = "PowerSaverActivity";

    private static final String GENERAL_CATEGORY = "general_category";
    private static final String PREF_SQUADZONE = "squadkeys";
    private static final String PREF_MODE = "pref_mode";
    private static final String PREF_DATA_MODE = "pref_powersaving_data_screen_off";
    private static final String PREF_DATA_DELAY = "pref_powersaving_data_screen_off_delay";
    private static final String PREF_SYNC_MODE = "pref_powersaving_sync_screen_off";
    private static final String PREF_SYNC_INTERVAL = "pref_powersaving_sync_screen_off_interval";
    private static final String PREF_SCREEN_OFF_WIFI = "screen_off_wifi_action";
    private static final String PREF_SYNC_DATA_MODE = "sync_data_mode";
    private static final String PREF_SYNC_MOBILE_DATA_MODE = "sync_data_mobile_mode";

    private CheckBoxPreference mPowerSaverEnabled;
    private ListPreference mDataMode;
    private ListPreference mDataDelay;
    private ListPreference mSyncMode;
    private ListPreference mSyncInterval;
    private ListPreference mScreenOffWifiMode;
    private ListPreference mSyncDataAction;
    private ListPreference mSyncDataMobilePreference;
    private Preference mSquadzone;

    Handler handler = new Handler();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setTitle(R.string.powersaver_settings_title_subhead);
            addPreferencesFromResource(R.xml.prefs_powersaver);
        
            PreferenceCategory generalCategory = (PreferenceCategory) findPreference(GENERAL_CATEGORY);

            mSquadzone = (Preference)findPreference(PREF_SQUADZONE);
            mSquadzone.setSummary("CyanMobile");

            mPowerSaverEnabled = (CheckBoxPreference)findPreference(PREF_MODE);
            mPowerSaverEnabled
                    .setChecked(Settings.Secure.getInt(
                            getContentResolver(), Settings.Secure.POWER_SAVER_MODE,
                            PowerSaverService.POWER_SAVER_MODE_OFF) == PowerSaverService.POWER_SAVER_MODE_ON);

            mDataMode = (ListPreference) findPreference(PREF_DATA_MODE);
            mDataMode.setOnPreferenceChangeListener(this);
            int dataModeValue = Settings.Secure.getInt(
                    getContentResolver(), Settings.Secure.POWER_SAVER_DATA_MODE,
                    PowerSaverService.DATA_UNTOUCHED);
            Log.i(TAG, "data mode value onCreate: " + dataModeValue);
            mDataMode.setValue(Integer.toString(dataModeValue));

            final TelephonyManager telephony = (TelephonyManager) getSystemService(
                    Context.TELEPHONY_SERVICE);
            if (telephony.getPhoneType() == Phone.PHONE_TYPE_CDMA) {
                mDataMode.setEntries(R.array.pref_powersaving_data_screen_off_entries_cdma);
                mDataMode.setEntryValues(R.array.pref_powersaving_data_screen_off_values_cdma);
            }

            mDataDelay = (ListPreference) findPreference(PREF_DATA_DELAY);
            mDataDelay.setOnPreferenceChangeListener(this);
            mDataDelay.setValue(Settings.Secure.getInt(
                    getContentResolver(), Settings.Secure.POWER_SAVER_DATA_DELAY,
                    0) + "");

            mSyncMode = (ListPreference) findPreference(PREF_SYNC_MODE);
            mSyncMode.setOnPreferenceChangeListener(this);
            String mSyncModeValue = Settings.Secure.getInt(
                    getContentResolver(), Settings.Secure.POWER_SAVER_SYNC_MODE,
                    PowerSaverService.SYNC_UNTOUCHED) + "";
            // Log.i(TAG, "sync mode value onCreate: " + mSyncModeValue);
            mSyncMode.setValue(mSyncModeValue);

            mSyncInterval = (ListPreference) findPreference(PREF_SYNC_INTERVAL);
            mSyncInterval.setOnPreferenceChangeListener(this);
            mSyncInterval.setValue(Settings.Secure.getInt(
                    getContentResolver(), Settings.Secure.POWER_SAVER_SYNC_INTERVAL,
                    3600) + "");

            mScreenOffWifiMode = (ListPreference) findPreference(PREF_SCREEN_OFF_WIFI);
            mScreenOffWifiMode.setOnPreferenceChangeListener(this);
            mScreenOffWifiMode.setValue(Settings.Secure.getInt(
                    getContentResolver(), Settings.Secure.POWER_SAVER_WIFI_MODE,
                    PowerSaverService.WIFI_UNTOUCHED) + "");

            mSyncDataAction = (ListPreference) findPreference(PREF_SYNC_DATA_MODE);
            mSyncDataAction.setOnPreferenceChangeListener(this);
            mSyncDataAction.setValue(Settings.Secure.getInt(
                    getContentResolver(), Settings.Secure.POWER_SAVER_SYNC_DATA_MODE,
                    PowerSaverService.WIFI_UNTOUCHED) + "");

            mSyncDataMobilePreference = (ListPreference) findPreference(PREF_SYNC_MOBILE_DATA_MODE);
            mSyncDataMobilePreference.setOnPreferenceChangeListener(this);
            mSyncDataMobilePreference.setValue(Settings.Secure.getInt(
                    getContentResolver(),
                    Settings.Secure.POWER_SAVER_SYNC_MOBILE_PREFERENCE,
                    PowerSaverService.WIFI_UNTOUCHED) + "");
        }

        @Override
        public void onResume() {
            super.onResume();
            refreshSettings();
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                Preference preference) {
            if (preference == mPowerSaverEnabled) {
                boolean checked = ((CheckBoxPreference) preference).isChecked();

                int newVal = checked ? PowerSaverService.POWER_SAVER_MODE_ON
                        : PowerSaverService.POWER_SAVER_MODE_OFF;

                Log.i(TAG, "putting: " + newVal);
                Settings.Secure.putInt(getContentResolver(),
                        Settings.Secure.POWER_SAVER_MODE, newVal);
                return true;
            }
            refreshSettings();
            return false;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            boolean result = false;

            if (preference == mDataMode) {
                final int val = Integer.parseInt((String) newValue);
                Log.i(TAG, "new value: " + val);
                final TelephonyManager phone = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                if (phone.getPhoneType() == Phone.PHONE_TYPE_CDMA
                        && val == PowerSaverService.DATA_2G) {
                    AlertDialog.Builder b = new AlertDialog.Builder(this);
                    b.setTitle("Read me!");
                    b.setMessage("LTE-off function is not yet tested for CDMA/LTE devices. It can cause an infinite loop of errors and won't go away until you wipe data! Only choose this option if you have a nandroid backup!\n\nYou've been warned!!!");
                    b.setCancelable(false);

                    b.setPositiveButton("Do it. I have a Nandroid.",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Settings.Secure.putInt(getContentResolver(),
                                            Settings.Secure.POWER_SAVER_DATA_MODE, PowerSaverService.DATA_2G);

                                }
                            });
                    b.setNegativeButton("Return to safety",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    b.show();
                } else {
                    result = Settings.Secure.putInt(getContentResolver(),
                            Settings.Secure.POWER_SAVER_DATA_MODE, val);
                }

            } else if (preference == mDataDelay) {
                int val = Integer.parseInt((String) newValue);
                Log.i(TAG, "new mDataDelay value: " + val);
                result = Settings.Secure.putInt(getContentResolver(),
                        Settings.Secure.POWER_SAVER_DATA_DELAY, val);

            } else if (preference == mSyncMode) {
                int val = Integer.parseInt((String) newValue);
                Log.i(TAG, "new value: " + val);

                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        refreshSettings();
                    }
                }, 500);

                result = Settings.Secure.putInt(getContentResolver(),
                        Settings.Secure.POWER_SAVER_SYNC_MODE, val);

            } else if (preference == mSyncInterval) {
                int val = Integer.parseInt((String) newValue);
                Log.i(TAG, "new value: " + val);
                result = Settings.Secure.putInt(getContentResolver(),
                        Settings.Secure.POWER_SAVER_SYNC_INTERVAL, val);

            } else if (preference == mScreenOffWifiMode) {
                int val = Integer.parseInt((String) newValue);
                Log.i(TAG, "new value: " + val);
                result = Settings.Secure.putInt(getContentResolver(),
                        Settings.Secure.POWER_SAVER_WIFI_MODE, val);

            } else if (preference == mSyncDataAction) {
                int val = Integer.parseInt((String) newValue);
                Log.i(TAG, "new value: " + val);
                result = Settings.Secure.putInt(getContentResolver(),
                        Settings.Secure.POWER_SAVER_SYNC_DATA_MODE, val);

            } else if (preference == mSyncDataMobilePreference) {
                int val = Integer.parseInt((String) newValue);
                Log.i(TAG, "new value: " + val);
                result = Settings.Secure.putInt(getContentResolver(),
                        Settings.Secure.POWER_SAVER_SYNC_MOBILE_PREFERENCE, val);
            }
            refreshSettings();
            return result;
        }

        private void refreshSettings() {
            // Log.i(TAG, "sync mode val: " + mSyncMode.getValue());
            if (mSyncMode.getValue().equals(Integer.toString(PowerSaverService.SYNC_INTERVAL)))
                mSyncInterval.setEnabled(true);
            else
                mSyncInterval.setEnabled(false);
        }
    }
