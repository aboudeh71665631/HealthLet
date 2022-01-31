package com.example.firstproject.Database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_PHONENO = "phoneNo";

    public SessionManager(Context context) {
        this.context = context;
        userSession = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createLoginSession(String phoneNo) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_PHONENO, phoneNo);

        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> userData = new HashMap<String, String>();
        userData.put(KEY_PHONENO, userSession.getString(KEY_PHONENO, null));
        return userData;
    }

    public boolean checkLogin() {
        if (userSession.getBoolean(IS_LOGIN, true)) {
            return true;
        } else return false;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }
}
