
package com.cyanogenmod.cmparts.activities;

import com.cyanogenmod.cmparts.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;	
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.view.View;	
import android.widget.Toast;
	
import com.android.internal.util.weather.HttpRetriever;
import com.android.internal.util.weather.WeatherInfo;
import com.android.internal.util.weather.WeatherXmlParser;	
import com.android.internal.util.weather.YahooPlaceFinder;

public class UIWeatherActivity extends PreferenceActivity implements
    OnPreferenceChangeListener {

    private static final String TAG = "Weather";
    private static final String KEY_USE_METRIC = "use_metric";
    private static final String KEY_USE_CUSTOM_LOCATION = "use_custom_location";
    private static final String KEY_CUSTOM_LOCATION = "custom_location";
    private static final String KEY_SHOW_LOCATION = "show_location";
    private static final String KEY_SHOW_TIMESTAMP = "show_timestamp";
    private static final String KEY_ENABLE_WEATHER = "enable_weather";
    private static final String KEY_REFRESH_INTERVAL = "refresh_interval";
    private static final String KEY_INVERT_LOWHIGH = "invert_lowhigh";
    private static final int WEATHER_CHECK = 0;

    private CheckBoxPreference mEnableWeather;
    private CheckBoxPreference mUseCustomLoc;
    private CheckBoxPreference mShowLocation;
    private CheckBoxPreference mShowTimestamp;
    private CheckBoxPreference mUseMetric;
    private CheckBoxPreference mInvertLowHigh;
    private ListPreference mWeatherSyncInterval;
    private EditTextPreference mCustomWeatherLoc;

    private static final int LOC_WARNING = 101;
    private static final int SYNCS_WARNING = 102;
    private ProgressDialog mProgressDialog;
    private int mWeatherInterval;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.weather_prefs);

        PreferenceScreen prefSet = getPreferenceScreen();

        // Setup the preferences
        mEnableWeather = (CheckBoxPreference) prefSet.findPreference(KEY_ENABLE_WEATHER);

        mUseCustomLoc = (CheckBoxPreference) prefSet.findPreference(KEY_USE_CUSTOM_LOCATION);
        mUseCustomLoc.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.WEATHER_USE_CUSTOM_LOCATION, 0) == 1);
        mCustomWeatherLoc = (EditTextPreference) prefSet.findPreference(KEY_CUSTOM_LOCATION);
        updateLocationSummary();

        mInvertLowHigh = (CheckBoxPreference) prefSet.findPreference(KEY_INVERT_LOWHIGH);
        mInvertLowHigh.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.WEATHER_INVERT_LOWHIGH, 0) == 1);

        mShowLocation = (CheckBoxPreference) prefSet.findPreference(KEY_SHOW_LOCATION);
        mShowLocation.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.WEATHER_SHOW_LOCATION, 1) == 1);

        mShowTimestamp = (CheckBoxPreference) prefSet.findPreference(KEY_SHOW_TIMESTAMP);
        mShowTimestamp.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.WEATHER_SHOW_TIMESTAMP, 1) == 1);

        mUseMetric = (CheckBoxPreference) prefSet.findPreference(KEY_USE_METRIC);
        mUseMetric.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.WEATHER_USE_METRIC, 1) == 1);

        mWeatherSyncInterval = (ListPreference) prefSet.findPreference(KEY_REFRESH_INTERVAL);
        mWeatherInterval = Settings.System.getInt(getContentResolver(),
                Settings.System.WEATHER_UPDATE_INTERVAL, 0);
        mEnableWeather.setChecked((mWeatherInterval != 0) && (Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_WEATHER, 0) == 1));
        mEnableWeather.setEnabled((mWeatherInterval != 0) && (Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_STYLE_PREF, 11) >= 6));
        mWeatherSyncInterval.setValue(String.valueOf(mWeatherInterval));
        mWeatherSyncInterval.setSummary(mapUpdateValue(mWeatherInterval));
        mWeatherSyncInterval.setOnPreferenceChangeListener(this);

        if (!Settings.Secure.isLocationProviderEnabled(getContentResolver(),
                LocationManager.NETWORK_PROVIDER)
                && !mUseCustomLoc.isChecked()) {
            showDialog(LOC_WARNING);
        }
    }

    private void updateLocationSummary() {
        if (mUseCustomLoc.isChecked()) {
            String location = Settings.System.getString(getContentResolver(),
                    Settings.System.WEATHER_CUSTOM_LOCATION);
            if (location != null) {
                mCustomWeatherLoc.setSummary(location);
            } else {
                mCustomWeatherLoc.setSummary(R.string.unknown);
            }
        } else {
            mCustomWeatherLoc.setSummary(R.string.weather_geolocated);
        }
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mEnableWeather) {
            Settings.System.putInt(getContentResolver(), Settings.System.LOCKSCREEN_WEATHER,
                    mEnableWeather.isChecked() ? 1 : 0);
            if (mEnableWeather.isChecked()) {
                Settings.System.putInt(getContentResolver(), Settings.System.WEATHER_UPDATE_INTERVAL, 60);
            }
            return true;

        } else if (preference == mUseCustomLoc) {
            Settings.System.putInt(getContentResolver(), Settings.System.WEATHER_USE_CUSTOM_LOCATION,
                    mUseCustomLoc.isChecked() ? 1 : 0);
            updateLocationSummary();
            return true;

        } else if (preference == mShowLocation) {
            Settings.System.putInt(getContentResolver(), Settings.System.WEATHER_SHOW_LOCATION,
                    mShowLocation.isChecked() ? 1 : 0);
            return true;

        } else if (preference == mUseMetric) {
            Settings.System.putInt(getContentResolver(), Settings.System.WEATHER_USE_METRIC,
                    mUseMetric.isChecked() ? 1 : 0);
            return true;
        } else if (preference == mShowTimestamp) {
            Settings.System.putInt(getContentResolver(), Settings.System.WEATHER_SHOW_TIMESTAMP,
                    mShowTimestamp.isChecked() ? 1 : 0);
            return true;
        } else if (preference == mInvertLowHigh) {
            Settings.System.putInt(getContentResolver(), Settings.System.WEATHER_INVERT_LOWHIGH,
                    mInvertLowHigh.isChecked() ? 1 : 0);
            return true;
        } else if (preference == mCustomWeatherLoc) {
            String location = Settings.System.getString(getContentResolver(),
                    Settings.System.WEATHER_CUSTOM_LOCATION);
            if (location != null) {
                mCustomWeatherLoc.getEditText().setText(location);
                mCustomWeatherLoc.getEditText().setSelection(location.length());
            } else {
                mCustomWeatherLoc.getEditText().setText("");
            }
            mCustomWeatherLoc.getDialog().findViewById(android.R.id.button1)
            .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mProgressDialog = new ProgressDialog(UIWeatherActivity.this);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setMessage(getString(R.string.weather_progress_title));
                    mProgressDialog.show();
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            String woeid = null;
                            try {
                                woeid = YahooPlaceFinder.GeoCode(UIWeatherActivity.this,
                                        mCustomWeatherLoc.getEditText().getText().toString());
                            } catch (Exception e) {
                            }
                            Message msg = Message.obtain();
                            msg.what = WEATHER_CHECK;
                            msg.obj = woeid;
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                }
            });
            return true;
        }

        return false;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mWeatherSyncInterval) {
            int newVal = Integer.parseInt((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.WEATHER_UPDATE_INTERVAL, newVal);
            mWeatherSyncInterval.setValue((String) newValue);
            mWeatherSyncInterval.setSummary(mapUpdateValue(newVal));
            if (newVal == 0) {
                showDialog(SYNCS_WARNING);
            } else {
                mEnableWeather.setEnabled(true);
            }
            return true;
        }

        return false;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case WEATHER_CHECK:
                if (msg.obj == null) {
                    Toast.makeText(UIWeatherActivity.this, getString(R.string.weather_retrieve_location_dialog_title),
                            Toast.LENGTH_SHORT).show();
                } else {
                    String cLoc = mCustomWeatherLoc.getEditText().getText().toString();
                    mCustomWeatherLoc.setText(cLoc);
                    Settings.System.putString(getContentResolver(), Settings.System.WEATHER_CUSTOM_LOCATION, cLoc);
                    mCustomWeatherLoc.setSummary(cLoc);
                    mCustomWeatherLoc.getDialog().dismiss();
                }
                mProgressDialog.dismiss();
                break;
            }
        }
    };

    /**
     * Utility classes and supporting methods
     */

    private String mapUpdateValue(Integer time) {

        String[] timeNames = getResources().getStringArray(R.array.weather_interval_entries);
        String[] timeValues = getResources().getStringArray(R.array.weather_interval_values);

        for (int i = 0; i < timeValues.length; i++) {
            if (Integer.decode(timeValues[i]).equals(time)) {
                return timeNames[i];
            }
        }

        return getString(R.string.unknown);
    }

    @Override
    public Dialog onCreateDialog(int dialogId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Dialog dialog;

        switch (dialogId) {
            case LOC_WARNING:
                builder.setTitle(R.string.weather_retrieve_location_dialog_title);
                builder.setMessage(R.string.weather_retrieve_location_dialog_message);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.weather_retrieve_location_dialog_enable_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Settings.Secure.setLocationProviderEnabled(getContentResolver(),
                                        LocationManager.NETWORK_PROVIDER, true);
                            }
                        });
                builder.setNegativeButton(R.string.cancel, null);
                dialog = builder.create();
                break;
            case SYNCS_WARNING:
                builder.setTitle(R.string.weather_sync_dialog_title);
                builder.setMessage(R.string.weather_sync_dialog_message);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.common_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                               Settings.System.putInt(getContentResolver(), Settings.System.LOCKSCREEN_WEATHER, 0);
                               mEnableWeather.setChecked(false);
                               mEnableWeather.setEnabled(false);
                            }
                        });
                builder.setNegativeButton(R.string.cancel, 
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                               Settings.System.putInt(getContentResolver(), Settings.System.WEATHER_UPDATE_INTERVAL, mWeatherInterval);
                               mWeatherSyncInterval.setValue(String.valueOf(mWeatherInterval));
                               mWeatherSyncInterval.setSummary(mapUpdateValue(mWeatherInterval));
                            }
                        });
                dialog = builder.create();
                break;
            default:
                dialog = null;
        }
        return dialog;
    }
}
