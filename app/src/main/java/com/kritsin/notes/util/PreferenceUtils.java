package com.kritsin.notes.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kritsin.notes.model.User;


public class PreferenceUtils {

    private static final String ID="id", SERVER_ID="server_id",
            NAME="name", LOGIN="login", PASSWORD="password";

    public static void saveUser(Context context, User user){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ID, user.getId());
        editor.putInt(SERVER_ID, user.getServerId());
        editor.putString(NAME, user.getName());
        editor.putString(LOGIN, user.getLogin());
        editor.putString(PASSWORD, user.getPassword());
        editor.commit();
    }

    public static void removeUser(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("id");
        editor.remove("server_id");
        editor.remove("secretkey");
        editor.remove("avatar");
        editor.remove("password");
        editor.remove("phone");
        editor.commit();
    }

    public static User getUser(Context context){
        User result = new User();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        result.setId(preferences.getInt(ID, -1));
        result.setServerId(preferences.getInt(SERVER_ID, -1));
        result.setName(preferences.getString(NAME, ""));
        result.setLogin(preferences.getString(LOGIN, ""));
        result.setPassword(preferences.getString(PASSWORD, ""));
        return result;
    }

}
