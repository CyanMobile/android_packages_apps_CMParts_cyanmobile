package com.cyanogenmod.cmparts.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.cyanogenmod.cmparts.R;
import com.cyanogenmod.cmparts.utils.SurfaceFlingerUtils;
import com.cyanogenmod.cmparts.utils.SurfaceFlingerUtils.RenderEffectSettings;

public class RenderColorPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {
    private String mValue;
    private SeekBar mRedSeekBar;
    private SeekBar mGreenSeekBar;
    private SeekBar mBlueSeekBar;

    public RenderColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.render_colors_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedString(mValue) : (String) defaultValue);
    }

    public void setValue(String value) {
        mValue = value;
        persistString(value);
    }

    public String getValue() {
        return mValue;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setNeutralButton(R.string.render_colors_reset, null);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        RenderEffectSettings settings = new RenderEffectSettings();
        settings.setColorDefinition(getValue());

        mRedSeekBar = (SeekBar) view.findViewById(R.id.render_color_red);
        mGreenSeekBar = (SeekBar) view.findViewById(R.id.render_color_green);
        mBlueSeekBar = (SeekBar) view.findViewById(R.id.render_color_blue);

        initSeekBarValues(settings);

        mRedSeekBar.setOnSeekBarChangeListener(this);
        mGreenSeekBar.setOnSeekBarChangeListener(this);
        mBlueSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        // We need to do this here instead of onPrepareDialogBuilder because
        // we want the button not to close the dialog.
        installResetButtonListener();

        updateColors();
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            RenderEffectSettings settings = new RenderEffectSettings();
            settings.renderColorR = mRedSeekBar.getProgress();
            settings.renderColorG = mGreenSeekBar.getProgress();
            settings.renderColorB = mBlueSeekBar.getProgress();

            String value = settings.getColorDefinition();
            if (callChangeListener(value)) {
                setValue(value);
            }
        }

        SurfaceFlingerUtils.setRenderColors(getContext(), getValue());
        super.onDialogClosed(positiveResult);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.value = getValue();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        setValue(myState.value);
    }

    private static class SavedState extends BaseSavedState {
        String value;

        public SavedState(Parcel source) {
            super(source);
            value = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public void onStartTrackingTouch(SeekBar seek) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seek) {
    }

    @Override
    public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
        updateColors();
    }

    private void initSeekBarValues(RenderEffectSettings settings) {
        mRedSeekBar.setProgress(settings.renderColorR);
        mGreenSeekBar.setProgress(settings.renderColorG);
        mBlueSeekBar.setProgress(settings.renderColorB);
    }

    private void installResetButtonListener() {
        AlertDialog dialog = (AlertDialog) getDialog();
        Button resetButton = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                SurfaceFlingerUtils.resetRenderColorsToDefaults(context);
                RenderEffectSettings settings = SurfaceFlingerUtils.getRenderEffectSettings(context);
                initSeekBarValues(settings);
            }
        });
    }

    private void updateColors() {
        SurfaceFlingerUtils.setRenderColors(getContext(),
                mRedSeekBar.getProgress(),
                mGreenSeekBar.getProgress(),
                mBlueSeekBar.getProgress());
    }
}
