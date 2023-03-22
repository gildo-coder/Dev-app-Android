package com.example.dgild;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class PrefDialog extends DialogPreference implements ColorPickerView.OnColorChangedListener {
    int mColor=0;
    //constructeur
    public PrefDialog(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    @Override
    public void onPrepareDialogBuilder(AlertDialog.Builder builder){
        int oldColor= getSharedPreferences().getInt(getKey(), Color.BLACK);
        builder.setView(new ColorPickerView(getContext(),this, oldColor));
        super.onPrepareDialogBuilder(builder);
    }
    @Override
    public void onDialogClosed(boolean positiveResult){
        if(positiveResult)
            persistInt(mColor);
           getSharedPreferences().edit().putInt(getKey(), mColor).apply();
        super.onDialogClosed(positiveResult);
    }
    @Override
    public void colorChanged(int color){
        mColor=color;
    }
}
