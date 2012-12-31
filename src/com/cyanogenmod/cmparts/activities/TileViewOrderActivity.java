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
import com.cyanogenmod.cmparts.utils.TileViewUtil;
import com.cyanogenmod.cmparts.widgets.TouchInterceptor;

import android.app.ListActivity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TileViewOrderActivity extends ListActivity
{
    private static final String TAG = "TileViewOrderActivity";

    private ListView mTileList;
    private TileAdapter mTileAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.order_tileview_activity);

        mTileList = getListView();
        ((TouchInterceptor) mTileList).setDropListener(mDropListener);
        mTileAdapter = new TileAdapter(this);
        setListAdapter(mTileAdapter);
    }

    @Override
    public void onDestroy() {
        ((TouchInterceptor) mTileList).setDropListener(null);
        setListAdapter(null);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        // reload our tiles and invalidate the views for redraw
        mTileAdapter.reloadTiles();
        mTileList.invalidateViews();
    }

    private TouchInterceptor.DropListener mDropListener = new TouchInterceptor.DropListener() {
            public void drop(int from, int to) {
                // get the current tile list
                ArrayList<String> tiles = TileViewUtil.getTileListFromString(
                        TileViewUtil.getCurrentTiles(TileViewOrderActivity.this));

                // move the tile
                if(from < tiles.size()) {
                    String tile = tiles.remove(from);

                    if(to <= tiles.size()) {
                        tiles.add(to, tile);

                        // save our tiles
                        TileViewUtil.saveCurrentTiles(TileViewOrderActivity.this,
                                TileViewUtil.getTileStringFromList(tiles));

                        // tell our adapter/listview to reload
                        mTileAdapter.reloadTiles();
                        mTileList.invalidateViews();
                    }
                }
            }
        };

    private class TileAdapter extends BaseAdapter {
        private Context mContext;
        private Resources mSystemUIResources = null;
        private LayoutInflater mInflater;
        private ArrayList<TileViewUtil.TileInfo> mTiles;

        public TileAdapter(Context c) {
            mContext = c;
            mInflater = LayoutInflater.from(mContext);

            PackageManager pm = mContext.getPackageManager();
            if(pm != null) {
                try {
                    mSystemUIResources = pm.getResourcesForApplication("com.android.systemui");
                } catch(Exception e) {
                    mSystemUIResources = null;
                    Log.e(TAG, "Could not load SystemUI resources", e);
                }
            }

            reloadTiles();
        }

        public void reloadTiles() {
            ArrayList<String> tiles = TileViewUtil.getTileListFromString(
                    TileViewUtil.getCurrentTiles(mContext));

            mTiles = new ArrayList<TileViewUtil.TileInfo>();
            for(String tile : tiles) {
                if(TileViewUtil.TILES.containsKey(tile)) {
                    mTiles.add(TileViewUtil.TILES.get(tile));
                }
            }
        }

        public int getCount() {
            return mTiles.size();
        }

        public Object getItem(int position) {
            return mTiles.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final View v;
            if(convertView == null) {
                v = mInflater.inflate(R.layout.order_tileview_list_item, null);
            } else {
                v = convertView;
            }

            TileViewUtil.TileInfo tile = mTiles.get(position);

            final TextView name = (TextView)v.findViewById(R.id.name);
            final ImageView icon = (ImageView)v.findViewById(R.id.icon);

            name.setText(tile.getTitleResId());

            // assume no icon first
            icon.setVisibility(View.GONE);

            // attempt to load the icon for this tile
            if(mSystemUIResources != null) {
                int resId = mSystemUIResources.getIdentifier(tile.getIcon(), null, null);
                if(resId > 0) {
                    try {
                        Drawable d = mSystemUIResources.getDrawable(resId);
                        icon.setVisibility(View.VISIBLE);
                        icon.setImageDrawable(d);
                    } catch(Exception e) {
                        Log.e(TAG, "Error retrieving icon drawable", e);
                    }
                }
            }

            return v;
        }
    }
}

