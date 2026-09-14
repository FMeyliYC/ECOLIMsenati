package com.ecolim.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(int userId, String userName, String role, boolean rememberMe) {
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
        editor.putInt(Constants.KEY_USER_ID, userId);
        editor.putString(Constants.KEY_USER_NAME, userName);
        editor.putString(Constants.KEY_USER_ROLE, role);
        editor.putBoolean(Constants.KEY_REMEMBER_ME, rememberMe);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(Constants.KEY_IS_LOGGED_IN, false) && pref.getBoolean(Constants.KEY_REMEMBER_ME, false);
    }

    public int getUserId() {
        return pref.getInt(Constants.KEY_USER_ID, 1);
    }

    public String getUserName() {
        return pref.getString(Constants.KEY_USER_NAME, "Operario");
    }

    public String getUserRole() {
        return pref.getString(Constants.KEY_USER_ROLE, "operario");
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
