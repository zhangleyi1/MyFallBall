package com.huxq17.example.floatball;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

public class ListPreference extends ListActivity implements Preference.OnPreferenceChangeListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwitchPreference test = new SwitchPreference(this);
        test.setOnPreferenceChangeListener(ListPreference.this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}
