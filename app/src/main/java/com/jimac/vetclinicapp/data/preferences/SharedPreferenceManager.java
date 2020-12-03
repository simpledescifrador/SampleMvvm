package com.jimac.vetclinicapp.data.preferences;

import static com.jimac.vetclinicapp.data.preferences.PreferenceConstant.CLIENT_DATA;
import static com.jimac.vetclinicapp.data.preferences.PreferenceConstant.LOGIN_DATETIME;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.jimac.vetclinicapp.data.models.Client;

public class SharedPreferenceManager extends PreferenceHelper {

    public static final String PREFS = "LOCAL_PREFS";

    private final SharedPreferences mSharedPreferences;

    public SharedPreferenceManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    @Override
    public Client getClientData() {
        return new Gson().fromJson(mSharedPreferences.getString(CLIENT_DATA, ""), Client.class);
    }

    @Override
    public String getLoginDateTime() {
        return mSharedPreferences.getString(LOGIN_DATETIME, "0000-00-00 00:00:00");
    }

    @Override
    public void saveLoginDateTime(final String loginDateTime) {
        mSharedPreferences.edit().putString(LOGIN_DATETIME, loginDateTime).apply();
    }

    @Override
    public void saveClientData(final Client client) {
        mSharedPreferences.edit().putString(CLIENT_DATA, new Gson().toJson(client)).apply();
    }
}
