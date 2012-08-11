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
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;
import android.util.Log;
import com.cyanogenmod.cmparts.utils.CMDProcessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import com.cyanogenmod.cmparts.R;

public class UIExportActivity extends PreferenceActivity implements OnPreferenceChangeListener {

    private static final String PREF_SQUADZONE = "squadkeys";

    private static final String EXPORTALL_PREF = "pref_exportall";
    private static final String IMPORTALL_PREF = "pref_importall";

    private static final String PREF_BACKUP_FILENAME = "com.cyanogenmod.cmparts_preferences.xml";
    private static final String CONFIG_BACKUP_FILENAME = "webview.db";
    private static final String CONFIG_BACKUP_FILENAMESE = "webviewCache.db";
    private static final String NAMESPACE = "com.cyanogenmod.cmparts";
    private static final String LAUNCHER_DB_BASE = "/data/" + NAMESPACE + "/databases/webview.db";
    private static final String LAUNCHER_DB_BASESE = "/data/" + NAMESPACE + "/databases/webviewCache.db";

    static Context mContext;

    private boolean shouldRestart=false;

    private Preference mExportAllPref;
    private Preference mImportAllPref;
    private Preference mSquadzone;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.ui_utilities_title_head);
        addPreferencesFromResource(R.xml.ui_exportimport);

		mContext = this.getBaseContext();

        PreferenceScreen prefSet = getPreferenceScreen();

        mSquadzone = (Preference) prefSet.findPreference(PREF_SQUADZONE);
        mSquadzone.setSummary("CyanMobile");

        mExportAllPref = (Preference) prefSet.findPreference(EXPORTALL_PREF);
        mExportAllPref.setOnPreferenceChangeListener(this);
        mImportAllPref = (Preference) prefSet.findPreference(IMPORTALL_PREF);
        mImportAllPref.setOnPreferenceChangeListener(this);

        mExportAllPref.setEnabled(false);
        mImportAllPref.setEnabled(false);

        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("CyanMobile Notice");
        alertDialog.setMessage("Not working yet, now disable!");
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
        return false;
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        /* Preference Screens */
        if (preference == mExportAllPref) {
                AlertDialog alertDialog = new AlertDialog.Builder(UIExportActivity.this).create();
                alertDialog.setTitle("CyanMobile Backup");
                alertDialog.setMessage("Backup CM Settings?");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new ExportPrefsTask().execute();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            return true;
        } else if (preference == mImportAllPref) {
                AlertDialog alertDialog = new AlertDialog.Builder(UIExportActivity.this).create();
                alertDialog.setTitle("CyanMobile Restore");
                alertDialog.setMessage("Restore CM Settings?");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new ImportPrefsTask().execute();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            return true;
        }
        return false;
    }

    // Wysie: Adapted from http://code.google.com/p/and-examples/source/browse/#svn/trunk/database/src/com/totsp/database
    private class ExportPrefsTask extends AsyncTask<Void, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(UIExportActivity.this);

        // can use UI thread here
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(getResources().getString(R.string.xml_export_dialog));
            this.dialog.show();
        }

      // automatically done on worker thread (separate from UI thread)
        @Override
        protected String doInBackground(final Void... args) {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return getResources().getString(R.string.import_export_sdcard_unmounted);
            }

            new CMDProcessor().su.runWaitFor("busybox cp /data/data/com.cyanogenmod.cmparts/shared_prefs/com.cyanogenmod.cmparts_preferences.xml /sdcard/com.cyanogenmod.cmparts_preferences.xml");
            return getResources().getString(R.string.xml_export_success);
        }

        // can use UI thread here
        @Override
        protected void onPostExecute(final String msg) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            new ExportDatabaseTask().execute();
            Toast.makeText(UIExportActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    // Wysie: Adapted from http://code.google.com/p/and-examples/source/browse/#svn/trunk/database/src/com/totsp/database
    private class ImportPrefsTask extends AsyncTask<Void, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(UIExportActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(getResources().getString(R.string.xml_import_dialog));
            this.dialog.show();
        }

        // could pass the params used here in AsyncTask<String, Void, String> - but not being re-used
        @Override
        protected String doInBackground(final Void... args) {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return getResources().getString(R.string.import_export_sdcard_unmounted);
            }

            File prefBackupFile = new File(Environment.getExternalStorageDirectory(), PREF_BACKUP_FILENAME);

            if (!prefBackupFile.exists()) {
                return getResources().getString(R.string.xml_file_not_found);
            }

            new CMDProcessor().su.runWaitFor("busybox rm -r /data/data/com.cyanogenmod.cmparts/shared_prefs/com.cyanogenmod.cmparts_preferences.xml");
            new CMDProcessor().su.runWaitFor("busybox cp /sdcard/com.cyanogenmod.cmparts_preferences.xml /data/data/com.cyanogenmod.cmparts/shared_prefs/com.cyanogenmod.cmparts_preferences.xml");
            new CMDProcessor().su.runWaitFor("busybox chmod 660 /data/data/com.cyanogenmod.cmparts/shared_prefs/com.cyanogenmod.cmparts_preferences.xml");
            return getResources().getString(R.string.xml_import_success);
        }

        @Override
        protected void onPostExecute(final String msg) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            new ImportDatabaseTask().execute();
            Toast.makeText(UIExportActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    // Wysie: Adapted from http://code.google.com/p/and-examples/source/browse/#svn/trunk/database/src/com/totsp/database
    private class ExportDatabaseTask extends AsyncTask<Void, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(UIExportActivity.this);

        // can use UI thread here
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(getResources().getString(R.string.dbfile_export_dialog));
            this.dialog.show();
        }

      // automatically done on worker thread (separate from UI thread)
        @Override
        protected String doInBackground(final Void... args) {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return getResources().getString(R.string.import_export_sdcard_unmounted);
            }

            try {
                new CMDProcessor().su.runWaitFor("busybox cp /data/data/com.cyanogenmod.cmparts/databases/webview.db /sdcard/webview.db");
                new CMDProcessor().su.runWaitFor("busybox cp /data/data/com.cyanogenmod.cmparts/databases/webviewCache.db /sdcard/webviewCache.db");
                exportCategories();
                return getResources().getString(R.string.dbfile_export_success);
            } catch (IOException e) {
                return getResources().getString(R.string.dbfile_export_error);
            }
        }

        private void exportCategories() throws IOException
        {
            File prefFolder = new File(Environment.getDataDirectory() + "/data/" + NAMESPACE + "/shared_prefs");
            String[] list = prefFolder.list();
            for (String fileName : list)
            {
                if ( fileName.startsWith("led_packages") )
                {
                    new CMDProcessor().su.runWaitFor("busybox cp /data/data/com.cyanogenmod.cmparts/shared_prefs/led_packages.xml /sdcard/led_packages.xml");
                }
            }
        }

        // can use UI thread here
        @Override
        protected void onPostExecute(final String msg) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            Toast.makeText(UIExportActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    // Wysie: Adapted from http://code.google.com/p/and-examples/source/browse/#svn/trunk/database/src/com/totsp/database
    private class ImportDatabaseTask extends AsyncTask<Void, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(UIExportActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(getResources().getString(R.string.dbfile_import_dialog));
            this.dialog.show();
        }

        // could pass the params used here in AsyncTask<String, Void, String> - but not being re-used
        @Override
        protected String doInBackground(final Void... args) {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return getResources().getString(R.string.import_export_sdcard_unmounted);
            }

            File dbBackupFile = new File(Environment.getExternalStorageDirectory(), CONFIG_BACKUP_FILENAME);
            File dbBackupFiler = new File(Environment.getExternalStorageDirectory(), CONFIG_BACKUP_FILENAMESE);

            if (!dbBackupFile.exists() || !dbBackupFiler.exists()) {
                return getResources().getString(R.string.dbfile_not_found);
            }

            new CMDProcessor().su.runWaitFor("busybox rm -r /data/data/com.cyanogenmod.cmparts/databases/webview.db");
            new CMDProcessor().su.runWaitFor("busybox rm -r /data/data/com.cyanogenmod.cmparts/databases/webviewCache.db");
            new CMDProcessor().su.runWaitFor("busybox rm -r /data/data/com.cyanogenmod.cmparts/databases/webview.db-shm");
            new CMDProcessor().su.runWaitFor("busybox rm -r /data/data/com.cyanogenmod.cmparts/databases/webviewCache.db-shm");
            new CMDProcessor().su.runWaitFor("busybox rm -r /data/data/com.cyanogenmod.cmparts/databases/webview.db-wal");
            new CMDProcessor().su.runWaitFor("busybox rm -r /data/data/com.cyanogenmod.cmparts/databases/webviewCache.db-wal");

            try {
                new CMDProcessor().su.runWaitFor("busybox cp /sdcard/webview.db /data/data/com.cyanogenmod.cmparts/databases/webview.db");
                new CMDProcessor().su.runWaitFor("busybox cp /sdcard/webviewCache.db /data/data/com.cyanogenmod.cmparts/databases/webviewCache.db");
                new CMDProcessor().su.runWaitFor("busybox chmod 660 /data/data/com.cyanogenmod.cmparts/databases/webview.db");
                new CMDProcessor().su.runWaitFor("busybox chmod 660 /data/data/com.cyanogenmod.cmparts/databases/webviewCache.db");
                importCategories();
                shouldRestart = true;
                return getResources().getString(R.string.dbfile_import_success);
            } catch (IOException e) {
                return getResources().getString(R.string.dbfile_import_error);
            }
        }

        private void importCategories() throws IOException
        {
            File prefFolder = Environment.getExternalStorageDirectory();
            String[] list = prefFolder.list();
            for (String fileName : list)
            {
                if ( fileName.startsWith("led_packages") )
                {
                    new CMDProcessor().su.runWaitFor("busybox cp /sdcard/led_packages.xml /data/data/com.cyanogenmod.cmparts/shared_prefs/led_packages.xml");
                    new CMDProcessor().su.runWaitFor("busybox chmod 660 /data/data/com.cyanogenmod.cmparts/shared_prefs/led_packages.xml");
                }
            }
        }

        @Override
        protected void onPostExecute(final String msg) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            Toast.makeText(UIExportActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause(){
        if(shouldRestart){
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        super.onPause();
    }
}
