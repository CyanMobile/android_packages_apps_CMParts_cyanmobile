/*
 * Copyright (C) 2012 The CyanogenMod Project
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

package com.cyanogenmod.cmparts.utils;

import android.content.Context;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

public class SurfaceFlingerUtils {
    private static final String TAG = "SurfaceFlingerUtils";

    private SurfaceFlingerUtils() {
    }

    public static class RenderEffectSettings {
        public int effectId;
        public int renderColorR;
        public int renderColorG;
        public int renderColorB;

        public RenderEffectSettings() {
            effectId = 0;
            setColorDefinition(null);
        }
        public String getColorDefinition() {
            StringBuilder builder = new StringBuilder();
            builder.append(renderColorR);
            builder.append(';');
            builder.append(renderColorG);
            builder.append(';');
            builder.append(renderColorB);
            return builder.toString();
        }
        public void setColorDefinition(String definition) {
            if (definition != null) {
                String[] colors = definition.split(";");
                if (colors.length == 3) {
                    renderColorR = Integer.valueOf(colors[0]);
                    renderColorG = Integer.valueOf(colors[1]);
                    renderColorB = Integer.valueOf(colors[2]);
                    return;
                }
            }
            renderColorR = renderColorG = renderColorB = 1000;
        }
    }

    public static RenderEffectSettings getRenderEffectSettings(Context context) {
        RenderEffectSettings settings = new RenderEffectSettings();

        // Taken from DevelopmentSettings
        // magic communication with surface flinger.
        try {
            IBinder flinger = ServiceManager.getService("SurfaceFlinger");
            if (flinger != null) {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                data.writeInterfaceToken("android.ui.ISurfaceComposer");
                flinger.transact(1010, data, reply, 0);
                // boolean: show CPU load
                reply.readInt();
                // boolean: enable GL ES
                reply.readInt();
                // boolean: show updates
                reply.readInt();
                // boolean: show background
                reply.readInt();
                // int: render effect id
                settings.effectId = reply.readInt();
                // int: render colors
                settings.renderColorR = reply.readInt();
                settings.renderColorG = reply.readInt();
                settings.renderColorB = reply.readInt();
                reply.recycle();
                data.recycle();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Could not get render effect settings", e);
        }

        return settings;
    }

    public static void setRenderEffect(Context context, int effectId) {
        doIntTransaction(1014, effectId);
    }

    public static void setRenderColors(Context context, String colorDefinition) {
        RenderEffectSettings settings = new RenderEffectSettings();
        settings.setColorDefinition(colorDefinition);
        setRenderColors(context, settings.renderColorR, settings.renderColorG, settings.renderColorB);
    }

    public static void setRenderColors(Context context, int r, int g, int b) {
        doIntTransaction(1015, r);
        doIntTransaction(1016, g);
        doIntTransaction(1017, b);
    }

    public static void resetRenderColorsToDefaults(Context context) {
        doIntTransaction(1018, 0);
    }

    private static boolean doIntTransaction(int transactionId, int data) {
        try {
            IBinder flinger = ServiceManager.getService("SurfaceFlinger");
            if (flinger != null) {
                Parcel parcel = Parcel.obtain();
                parcel.writeInterfaceToken("android.ui.ISurfaceComposer");
                parcel.writeInt(data);
                flinger.transact(transactionId, parcel, null, 0);
                parcel.recycle();
                return true;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "SurfaceFlinger transaction " + transactionId + " failed", e);
        }

        return false;
    }
}
