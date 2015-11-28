package me.argha.sustproject.utils;

/**
 * Author: ARGHA K ROY
 * Date: 11/27/2015.
 */
public interface AppURL {

    //String DOMAIN="http://10.0.3.2:81/efarmer/api/";
    String DOMAIN="http://argha.xyz/_efarmer/api/";
    String ASSETS="http://argha.xyz/_efarmer/assets/uploads/";
    //String ASSETS="http://10.0.3.2:81/efarmer/assets/uploads/";
    String LOGIN=DOMAIN+"login";
    String REGISTER=DOMAIN+"register";
    String CATEGORIES=DOMAIN+"categories";
    String ADD_ITEM=DOMAIN+"create_item";
    String ALL_ITEMS=DOMAIN+"all_items";
    String ITEM_RATE=DOMAIN+"rate_item";
    String USER_PROFILE=DOMAIN+"user_details";
    String UPDATE_PROFILE=DOMAIN+"update_profile";
}
