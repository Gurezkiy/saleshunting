package com.gurezkiygmail.foxhunting;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by gurez on 12.08.2017.
 */
public class SessionManager {
    private static String TAG = SessionManager.class.getSimpleName();
    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Shared preferences file name
    private static final String PREF_NAME = "foxhuntingLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String USER_TOKEN = "foxhuntingToken";
    private static final String USER_PROFIE = "foxhuntingProfile";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void setToken(String _token) {
        editor.putString(USER_TOKEN, _token);
        // commit changes
        editor.commit();
        Log.d(TAG, "User token session modified!");
    }
    public String getToken(){
        String token = pref.getString(USER_TOKEN,"");
        return  token;
    }
    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        String token = getToken();
        if(token.length()==0){
            setLogin(false);
            return  false;
        }
        setLogin(true);
        return  true;
    }
}
