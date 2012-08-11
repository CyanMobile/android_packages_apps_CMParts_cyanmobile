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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//
// CPU Related Settings
//
public class CPUActivity extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {

    public static final String FREQ_CUR_PREF = "pref_cpu_freq_cur";
    public static final String SCALE_CUR_FILE = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
    public static final String FREQINFO_CUR_FILE = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_cur_freq";
    private static String FREQ_CUR_FILE = SCALE_CUR_FILE;

    public static final String GOV_PREF = "pref_cpu_gov";
    public static final String GOVERNORS_LIST_FILE = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors";
    public static final String GOVERNOR = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
    public static final String MIN_FREQ_PREF = "pref_freq_min";
    public static final String MAX_FREQ_PREF = "pref_freq_max";
    public static final String SO_MAX_FREQ_PREF = "pref_screenoff_freq_max";
    public static final String FREQ_LIST_FILE = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies";
    public static final String FREQ_MAX_FILE = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq";
    public static final String FREQ_MIN_FILE = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq";
    public static final String SOB_PREF = "pref_set_on_boot";

    private static final String TAG = "CPUSettings";

    private String mGovernorFormat;
    private String mMinFrequencyFormat;
    private String mMaxFrequencyFormat;
    private String mMaxSoFrequencyFormat;

    private Preference mCurFrequencyPref;
    private ListPreference mGovernorPref;
    private ListPreference mMinFrequencyPref;
    private ListPreference mMaxFrequencyPref;
    private ListPreference mMaxSoFrequencyPref;

    private class CurCPUThread extends Thread {
        private boolean mInterrupt = false;

        public void interrupt() {
            mInterrupt = true;
        }

        @Override
        public void run() {
            try {
                while (!mInterrupt) {
                    sleep(500);
                    final String curFreq = readOneLine(FREQ_CUR_FILE);
                    if (curFreq != null)
                        mCurCPUHandler.sendMessage(mCurCPUHandler.obtainMessage(0, curFreq));
                    /* mCurCPUHandler.sendMessage(mCurCPUHandler.obtainMessage(0, curFreq)); */
               }
            } catch (InterruptedException e) {
            }
        }
    };

    private CurCPUThread mCurCPUThread = new CurCPUThread();
	
    private Handler mCurCPUHandler = new Handler() {
        public void handleMessage(Message msg) {
            mCurFrequencyPref.setSummary(toMHzS((String) msg.obj));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mGovernorFormat = getString(R.string.cpu_governors_summary);
        mMinFrequencyFormat = getString(R.string.cpu_min_freq_summary);
        mMaxFrequencyFormat = getString(R.string.cpu_max_freq_summary);
        mMaxSoFrequencyFormat = getString(R.string.screenoff_cpu_max_freq_summary);

        String[] availableGovernors = readOneLine(GOVERNORS_LIST_FILE).split(" ");
        String[] availableFrequencies = new String[0];
        String availableFrequenciesLine = readOneLine(FREQ_LIST_FILE);
        if (availableFrequenciesLine != null)
             availableFrequencies = availableFrequenciesLine.split(" ");
        String[] frequencies;
        String temp;

        frequencies = new String[availableFrequencies.length];
        for (int i = 0; i < frequencies.length; i++) {
            frequencies[i] = toMHz(availableFrequencies[i]);
        }

        setTitle(R.string.cpu_title);
        addPreferencesFromResource(R.xml.cpu_settings);

        PreferenceScreen PrefScreen = getPreferenceScreen();

        temp = readOneLine(GOVERNOR);

        mGovernorPref = (ListPreference) PrefScreen.findPreference(GOV_PREF);
        mGovernorPref.setEntryValues(availableGovernors);
        mGovernorPref.setEntries(availableGovernors);
        mGovernorPref.setValue(temp);
        mGovernorPref.setSummary(String.format(mGovernorFormat, temp));
        mGovernorPref.setOnPreferenceChangeListener(this);

        /* Some systems might not use governors */
        if (temp == null) {
            PrefScreen.removePreference(mGovernorPref);
        }

        if (!fileExists(FREQ_CUR_FILE)) {
            FREQ_CUR_FILE = FREQINFO_CUR_FILE;
        }

        temp = readOneLine(FREQ_CUR_FILE);

        mCurFrequencyPref = (Preference) PrefScreen.findPreference(FREQ_CUR_PREF);
        mCurFrequencyPref.setSummary(toMHz(temp));

        temp = readOneLine(FREQ_MIN_FILE);

        mMinFrequencyPref = (ListPreference) PrefScreen.findPreference(MIN_FREQ_PREF);
        mMinFrequencyPref.setEntryValues(availableFrequencies);
        mMinFrequencyPref.setEntries(frequencies);
        mMinFrequencyPref.setValue(temp);
        mMinFrequencyPref.setSummary(String.format(mMinFrequencyFormat, toMHz(temp)));
        mMinFrequencyPref.setOnPreferenceChangeListener(this);

        temp = prefs.getString(MAX_FREQ_PREF, null);

        mMaxFrequencyPref = (ListPreference) PrefScreen.findPreference(MAX_FREQ_PREF);
        mMaxFrequencyPref.setEntryValues(availableFrequencies);
        mMaxFrequencyPref.setEntries(frequencies);
        mMaxFrequencyPref.setValue(temp);
        mMaxFrequencyPref.setSummary(String.format(mMaxFrequencyFormat, toMHz(temp)));
        mMaxFrequencyPref.setOnPreferenceChangeListener(this);
	
        temp = prefs.getString(SO_MAX_FREQ_PREF, null);

        mMaxSoFrequencyPref = (ListPreference) PrefScreen.findPreference(SO_MAX_FREQ_PREF);
        mMaxSoFrequencyPref.setEntryValues(availableFrequencies);
        mMaxSoFrequencyPref.setEntries(frequencies);
        mMaxSoFrequencyPref.setValue(temp);
        mMaxSoFrequencyPref.setSummary(String.format(mMaxSoFrequencyFormat, toMHz(temp)));
        mMaxSoFrequencyPref.setOnPreferenceChangeListener(this);

        if (availableFrequenciesLine == null) {
            mMinFrequencyPref.setEnabled(false);
            mMaxFrequencyPref.setEnabled(false);
            mMaxSoFrequencyPref.setEnabled(false);
        }

        mCurCPUThread.start();

    }

    @Override
    public void onResume() {
        String temp;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        super.onResume();

        temp = prefs.getString(MAX_FREQ_PREF, null);

        if (temp == null) {
            temp = readOneLine(FREQ_MAX_FILE);
            mMaxFrequencyPref.setValue(temp);
            mMaxFrequencyPref.setSummary(String.format(mMaxFrequencyFormat, toMHz(temp)));
        }

        temp = readOneLine(FREQ_MIN_FILE);
        mMinFrequencyPref.setValue(temp);
        mMinFrequencyPref.setSummary(String.format(mMinFrequencyFormat, toMHz(temp)));

        temp = readOneLine(GOVERNOR);
        mGovernorPref.setSummary(String.format(mGovernorFormat, temp));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCurCPUThread.interrupt();
        try {
            mCurCPUThread.join();
        } catch (InterruptedException e) {
        }
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String fname = "";

        if (newValue != null) {
            if (preference == mGovernorPref) {
                fname = GOVERNOR;
            } else if (preference == mMinFrequencyPref) {
                fname = FREQ_MIN_FILE;
            } else if (preference == mMaxFrequencyPref) {
                fname = FREQ_MAX_FILE;
            } else if (preference == mMaxSoFrequencyPref) {
                mMaxSoFrequencyPref.setSummary(String.format(mMaxSoFrequencyFormat,
                        toMHz((String) newValue)));
                return true;
            }

            if (writeOneLine(fname, (String) newValue)) {
                if (preference == mGovernorPref) {
                    mGovernorPref.setSummary(String.format(mGovernorFormat, (String) newValue));
                } else if (preference == mMinFrequencyPref) {
                    mMinFrequencyPref.setSummary(String.format(mMinFrequencyFormat,
                            toMHz((String) newValue)));
                } else if (preference == mMaxFrequencyPref) {
                    mMaxFrequencyPref.setSummary(String.format(mMaxFrequencyFormat,
                            toMHz((String) newValue)));
                }
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static String readOneLine(String fname) {
        BufferedReader br;
        String line = null;

        try {
            br = new BufferedReader(new FileReader(fname), 512);
            try {
                line = br.readLine();
            } finally {
                br.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "IO Exception when reading /sys/ file", e);
        }
        return line;
    }

    public static boolean writeOneLine(String fname, String value) {
        try {
            FileWriter fw = new FileWriter(fname);
            try {
                fw.write(value);
            } finally {
                fw.close();
            }
        } catch (IOException e) {
            String Error = "Error writing to " + fname + ". Exception: ";
            Log.e(TAG, Error, e);
            return false;
        }
        return true;
    }

    public static boolean fileExists(String filename) {
        return new File(filename).exists();
    }

    private String toMHz(String mhzString) {
        if (mhzString == null)
            return "-";
        return new StringBuilder().append(Integer.valueOf(mhzString) / 1000).append(" MHz").toString();
    }

    private String toMHzS(String mhzsString) {
        if (mhzsString == null)
            return "-";
        return new StringBuilder().append(Integer.valueOf(mhzsString) / 1000).append(" MHz CMobile").toString();
    }
}
