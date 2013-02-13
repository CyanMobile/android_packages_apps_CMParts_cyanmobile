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
    private static final String NAMESPACE = "com.cyanogenmod.cmparts";
    private static final String CONFIG_BACKUP_FILENAME = "backupsettings.db";

    static Context mContext;

    private boolean shouldRestart = false;

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

        /*mExportAllPref.setEnabled(false);
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
        
        alertDialog.show();*/

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

    private class ExportPrefsTask extends AsyncTask<Void, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(UIExportActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(getResources().getString(R.string.xml_export_dialog));
            this.dialog.show();
        }

        @Override
        protected String doInBackground(final Void... args) {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return getResources().getString(R.string.import_export_sdcard_unmounted);
            }

            File dis = new File("/sdcard/CMobile_backup");
	    if(!dis.exists()) {
		new CMDProcessor().su.runWaitFor("busybox mkdir /sdcard/CMobile_backup");
            }

            new CMDProcessor().su.runWaitFor("busybox cp /data/data/com.cyanogenmod.cmparts/shared_prefs/com.cyanogenmod.cmparts_preferences.xml /sdcard/CMobile_backup/com.cyanogenmod.cmparts_preferences.xml");
            return getResources().getString(R.string.xml_export_success);
        }

        @Override
        protected void onPostExecute(final String msg) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            new ExportDatabaseTask().execute();
            Toast.makeText(UIExportActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private class ImportPrefsTask extends AsyncTask<Void, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(UIExportActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(getResources().getString(R.string.xml_import_dialog));
            this.dialog.show();
        }

        @Override
        protected String doInBackground(final Void... args) {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return getResources().getString(R.string.import_export_sdcard_unmounted);
            }

            File prefBackupFile = new File("/sdcard/CMobile_backup/com.cyanogenmod.cmparts_preferences.xml");

            if (!prefBackupFile.exists()) {
                return getResources().getString(R.string.xml_file_not_found);
            }

            new CMDProcessor().su.runWaitFor("busybox rm -r /data/data/com.cyanogenmod.cmparts/shared_prefs/com.cyanogenmod.cmparts_preferences.xml");
            new CMDProcessor().su.runWaitFor("busybox cp /sdcard/CMobile_backup/com.cyanogenmod.cmparts_preferences.xml /data/data/com.cyanogenmod.cmparts/shared_prefs/com.cyanogenmod.cmparts_preferences.xml");
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

    private class ExportDatabaseTask extends AsyncTask<Void, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(UIExportActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(getResources().getString(R.string.dbfile_export_dialog));
            this.dialog.show();
        }

        @Override
        protected String doInBackground(final Void... args) {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return getResources().getString(R.string.import_export_sdcard_unmounted);
            }

            try {
                new CMDProcessor().su.runWaitFor("busybox cp /data/data/com.android.providers.settings/databases/settings.db /sdcard/CMobile_backup/backupsettings.db");
                exportCategories();
                exportImgCategories();
                return getResources().getString(R.string.dbfile_export_success);
            } catch (IOException e) {
                return getResources().getString(R.string.dbfile_export_error);
            }
        }

        private void exportCategories() throws IOException {
            File prefFolder = new File(Environment.getDataDirectory() + "/data/" + NAMESPACE + "/shared_prefs");
            String[] list = prefFolder.list();
            for (String fileName : list) {
                if (fileName.startsWith("led_packages")) {
                    new CMDProcessor().su.runWaitFor("busybox cp /data/data/com.cyanogenmod.cmparts/shared_prefs/led_packages.xml /sdcard/CMobile_backup/led_packages.xml");
                }
            }
        }

        private void exportImgCategories() throws IOException {
            File prefFolder = new File(Environment.getDataDirectory() + "/data/" + NAMESPACE + "/files");
            String[] list = prefFolder.list();
            for (String fileName : list) {
                if (fileName.startsWith("aps_background")) {
                    new CMDProcessor().su.runWaitFor("busybox cp /data/data/com.cyanogenmod.cmparts/files/aps_background /sdcard/CMobile_backup/aps_background");
                } else if (fileName.startsWith("navb_background")) {
                    new CMDProcessor().su.runWaitFor("busybox cp /data/data/com.cyanogenmod.cmparts/files/navb_background /sdcard/CMobile_backup/navb_background");
                } else if (fileName.startsWith("lg_background")) {
                    new CMDProcessor().su.runWaitFor("busybox cp /data/data/com.cyanogenmod.cmparts/files/lg_background /sdcard/CMobile_backup/lg_background");
                } else if (fileName.startsWith("bc_background")) {
                    new CMDProcessor().su.runWaitFor("busybox cp /data/data/com.cyanogenmod.cmparts/files/bc_background /sdcard/CMobile_backup/bc_background");
                } else if (fileName.startsWith("nb_background")) {
                    new CMDProcessor().su.runWaitFor("busybox cp /data/data/com.cyanogenmod.cmparts/files/nb_background /sdcard/CMobile_backup/nb_background");
                } else if (fileName.startsWith("lockwallpaper")) {
                    new CMDProcessor().su.runWaitFor("busybox cp /data/data/com.cyanogenmod.cmparts/files/lockwallpaper /sdcard/CMobile_backup/lockwallpaper");
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

    private class ImportDatabaseTask extends AsyncTask<Void, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(UIExportActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(getResources().getString(R.string.dbfile_import_dialog));
            this.dialog.show();
        }

        @Override
        protected String doInBackground(final Void... args) {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return getResources().getString(R.string.import_export_sdcard_unmounted);
            }

            File dbBackupFile = new File("/sdcard/CMobile_backup/backupsettings.db");

            if (!dbBackupFile.exists()) {
                return getResources().getString(R.string.dbfile_not_found);
            }

            try {
                new CMDProcessor().su.runWaitFor("busybox rm -r /data/data/com.android.providers.settings/databases/settings.db");
                new CMDProcessor().su.runWaitFor("busybox cp /sdcard/CMobile_backup/backupsettings.db /data/data/com.android.providers.settings/databases/settings.db");
                new CMDProcessor().su.runWaitFor("busybox chmod 660 /data/data/com.android.providers.settings/databases/settings.db");
                importCategories();
                shouldRestart = true;
                return getResources().getString(R.string.dbfile_import_success);
            } catch (IOException e) {
                return getResources().getString(R.string.dbfile_import_error);
            }
        }

        private void importCategories() throws IOException {
            File prefFolder = new File(Environment.getExternalStorageDirectory() + "/CMobile_backup");
            String[] list = prefFolder.list();
            for (String fileName : list) {
                if (fileName.startsWith("led_packages")) {
                    new CMDProcessor().su.runWaitFor("busybox cp /sdcard/CMobile_backup/led_packages.xml /data/data/com.cyanogenmod.cmparts/shared_prefs/led_packages.xml");
                    new CMDProcessor().su.runWaitFor("busybox chmod 660 /data/data/com.cyanogenmod.cmparts/shared_prefs/led_packages.xml");
                } else if (fileName.startsWith("aps_background")) {
                    new CMDProcessor().su.runWaitFor("busybox cp /sdcard/CMobile_backup/aps_background /data/data/com.cyanogenmod.cmparts/files/aps_background");
                    new CMDProcessor().su.runWaitFor("busybox chmod 664 /data/data/com.cyanogenmod.cmparts/files/aps_background");
                } else if (fileName.startsWith("navb_background")) {
                    new CMDProcessor().su.runWaitFor("busybox cp /sdcard/CMobile_backup/navb_background /data/data/com.cyanogenmod.cmparts/files/navb_background");
                    new CMDProcessor().su.runWaitFor("busybox chmod 664 /data/data/com.cyanogenmod.cmparts/files/navb_background");
                } else if (fileName.startsWith("lg_background")) {
                    new CMDProcessor().su.runWaitFor("busybox cp /sdcard/CMobile_backup/lg_background /data/data/com.cyanogenmod.cmparts/files/lg_background");
                    new CMDProcessor().su.runWaitFor("busybox chmod 664 /data/data/com.cyanogenmod.cmparts/files/lg_background");
                } else if (fileName.startsWith("bc_background")) {
                    new CMDProcessor().su.runWaitFor("busybox cp /sdcard/CMobile_backup/bc_background /data/data/com.cyanogenmod.cmparts/files/bc_background");
                    new CMDProcessor().su.runWaitFor("busybox chmod 664 /data/data/com.cyanogenmod.cmparts/files/bc_background");
                } else if (fileName.startsWith("nb_background")) {
                    new CMDProcessor().su.runWaitFor("busybox cp /sdcard/CMobile_backup/nb_background /data/data/com.cyanogenmod.cmparts/files/nb_background");
                    new CMDProcessor().su.runWaitFor("busybox chmod 664 /data/data/com.cyanogenmod.cmparts/files/nb_background");
                } else if (fileName.startsWith("lockwallpaper")) {
                    new CMDProcessor().su.runWaitFor("busybox cp /sdcard/CMobile_backup/lockwallpaper /data/data/com.cyanogenmod.cmparts/files/lockwallpaper");
                    new CMDProcessor().su.runWaitFor("busybox chmod 664 /data/data/com.cyanogenmod.cmparts/files/lockwallpaper");
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
