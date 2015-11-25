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

}
