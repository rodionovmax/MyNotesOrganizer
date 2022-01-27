package com.gb.mynoteorganizer.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SharedPref {
    private static SharedPreferences mSharedPref;

    public SharedPref() {}

    public static void init(Context context) {
        if (mSharedPref == null) {
            mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public static String read(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

}


