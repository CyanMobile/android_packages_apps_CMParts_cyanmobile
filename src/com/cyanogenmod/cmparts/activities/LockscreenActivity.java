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

import android.os.Bundle;
import android.os.SystemProperties;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class LockscreenActivity extends TabActivity {

	private static TabHost mTabHost;
	private Intent intent;
    private ActionBar mActionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        if ("1".equals(SystemProperties.get("ro.squadzone.build", "0"))) {
           setContentView(R.xml.lockscreen_settings);
        } else {
           return;
        }
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        mActionBar = (ActionBar) findViewById(R.id.actionBar);
        mActionBar.setTitle(R.string.lockscreen_settings_title_subhead);
        mActionBar.setHomeLogo(R.drawable.cm_icon, new OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        LockscreenActivity.this.finish();
                  }
        });

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.getTabWidget().setDividerDrawable(null);
		Intent intent; // Reusable Intent for each tab

		intent = new Intent().setClass(LockscreenActivity.this, LockscreenStyleActivity.class);
		setupTab(new TextView(this), getString(R.string.pref_lockscreen_stylecfg_title), intent);

		intent = new Intent().setClass(LockscreenActivity.this, LockscreenWidgetsActivity.class);
		setupTab(new TextView(this), getString(R.string.pref_lockscreen_widgets_title), intent);

		intent = new Intent().setClass(LockscreenActivity.this, LockscreenUnlockActivity.class);
		setupTab(new TextView(this), getString(R.string.pref_lockscreen_unlock_title), intent);

		intent = new Intent().setClass(LockscreenActivity.this, GestureMenuActivity.class);
		setupTab(new TextView(this), getString(R.string.pref_lockscreen_gesture_title), intent);

		intent = new Intent().setClass(LockscreenActivity.this, LockscreenTimeoutActivity.class);
		setupTab(new TextView(this), getString(R.string.pref_lockscreen_timeout_title), intent);
    }

	private void setupTab(final View view, final String tag, final Intent myIntent) {

		View tabview = createTabView(mTabHost.getContext(), tag);
		TabSpec setContent =  mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(myIntent);
		mTabHost.addTab(setContent);
	}

	private static View createTabView(final Context context, final String text) {

		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bglock, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}
}
