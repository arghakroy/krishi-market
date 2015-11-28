package me.argha.sustproject.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import me.argha.sustproject.utils.AppConst;

public class PrefHelper {
    SharedPreferences preferences;

    public PrefHelper(Context context){
        preferences=context.getSharedPreferences(AppConst.APP_PREF, Context.MODE_PRIVATE);
    }

    public SharedPreferences.Editor getEditor(){
        return preferences.edit();
    }

    public void saveUserId(String userId){
        getEditor().putString("pref_user_id",userId).apply();
    }

    public void saveUserName(String username){
        getEditor().putString("pref_user_name",username).apply();
    }

    public void saveUserFullName(String fullName){
        getEditor().putString("pref_user_full_name",fullName).apply();
    }

    public String getUserId(){
        return preferences.getString("pref_user_id",null);
    }

    public String getUserName(){
        return preferences.getString("pref_user_name",null);
    }

    public String getUserFullName(){
        return preferences.getString("pref_user_full_name",null);
    }

    public void logOut(){
        saveUserId(null);
        saveUserName(null);
        saveUserFullName(null);
    }
}
