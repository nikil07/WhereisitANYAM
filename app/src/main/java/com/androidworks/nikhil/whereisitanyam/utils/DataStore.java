package com.androidworks.nikhil.whereisitanyam.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.androidworks.nikhil.whereisitanyam.TimeZoneItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Nikhil on 25-Sep-16.
 */
public class DataStore {

    static Gson gson = new Gson();
    static ArrayList<TimeZoneItem> timeZones;
    static ArrayList<TimeZoneItem> temp;
    private static DataStore instance;
    private static SharedPreferences sharedPreferences;

    private DataStore() {
        // private constructor to enforce singleton
    }

    public static DataStore getInstance(Context context) {
        if (instance == null) {
            instance = new DataStore();
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            timeZones = new ArrayList<>();
            temp = new ArrayList<>();
        }
        return instance;
    }

    public void storeTimeZoneItems(TimeZoneItem item) {

        timeZones = gson.fromJson(sharedPreferences.getString(Constants.SHARED_PREF_TIMEZONES, timeZones.toString()), new TypeToken<ArrayList<TimeZoneItem>>() {
        }.getType());
        timeZones.add(item);
        sharedPreferences.edit().putString(Constants.SHARED_PREF_TIMEZONES, gson.toJson(timeZones)).apply();
    }

    public void updateUserMessage(TimeZoneItem item) {
        timeZones = gson.fromJson(sharedPreferences.getString(Constants.SHARED_PREF_TIMEZONES, timeZones.toString()), new TypeToken<ArrayList<TimeZoneItem>>() {
        }.getType());
        for (int i = 0; i < timeZones.size(); i++) {
            if (timeZones.get(i).getCity().equalsIgnoreCase(item.getCity()))
                timeZones.get(i).setMessage(item.getMessage());
        }
        sharedPreferences.edit().putString(Constants.SHARED_PREF_TIMEZONES, gson.toJson(timeZones)).apply();
    }

    public ArrayList<TimeZoneItem> getTimeZones() {

        Log.d("nikhil", "timezone items list " + gson.fromJson(sharedPreferences.getString(Constants.SHARED_PREF_TIMEZONES, timeZones.toString())
                , new TypeToken<ArrayList<TimeZoneItem>>() {
                }.getType()).toString());

        return gson.fromJson(sharedPreferences.getString(Constants.SHARED_PREF_TIMEZONES, timeZones.toString())
                , new TypeToken<ArrayList<TimeZoneItem>>() {
                }.getType());
    }

    public void deleteTimeZone(TimeZoneItem item) {
        timeZones = gson.fromJson(sharedPreferences.getString(Constants.SHARED_PREF_TIMEZONES, timeZones.toString()), new TypeToken<ArrayList<TimeZoneItem>>() {
        }.getType());
        for (int i = 0; i < timeZones.size(); i++) {
            if (timeZones.get(i).getCity().equalsIgnoreCase(item.getCity()))
                timeZones.remove(i);
        }
        sharedPreferences.edit().putString(Constants.SHARED_PREF_TIMEZONES, gson.toJson(timeZones)).apply();
    }

    public void deleteAll() {
        temp.clear();
        temp.addAll(timeZones);
        timeZones.clear();
        sharedPreferences.edit().putString(Constants.SHARED_PREF_TIMEZONES, gson.toJson(timeZones)).apply();
    }

    public void restore() {
        timeZones.addAll(temp);
        sharedPreferences.edit().putString(Constants.SHARED_PREF_TIMEZONES, gson.toJson(timeZones)).apply();
        temp.clear();
    }

    public void storeIfIconNeeded(boolean bool) {
        sharedPreferences.edit().putBoolean(Constants.SHARED_PREF_ICON_NEEDED, bool).apply();
    }

    public boolean isIconNeeded() {
        return sharedPreferences.getBoolean(Constants.SHARED_PREF_ICON_NEEDED, true);
    }

    public void storeVersion(int verCode) {
        sharedPreferences.edit().putInt(Constants.SHARED_PREF_VERSION_NUMBER, verCode).apply();
    }

    public int getVersion() {
        return sharedPreferences.getInt(Constants.SHARED_PREF_VERSION_NUMBER, 1);
    }
}
