package com.example.sahil.androidpersonalassistant;

/**
 * Created by ravichandran on 19/04/17.
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utility {
    private static Pattern pattern;
    private static Matcher matcher;
    //Email Pattern
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";


    public static boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }

    public static boolean isNull(String txt){
        return ( txt==null || txt.trim().length() == 0 ) ? true: false;
    }

    public static String jsonRecurseKeys(JSONObject jObj, String findKey) throws JSONException {

        Iterator<?> keys = jObj.keys();
        String key = "";

        while (keys.hasNext() && !key.equalsIgnoreCase(findKey)) {
            key = (String) keys.next();

            if (key.equalsIgnoreCase(findKey)) {
                return jObj.getString(key);
            }
            if (jObj.get(key) instanceof JSONObject) {
                return jsonRecurseKeys((JSONObject)jObj.get(key), findKey);
            }
        }

        return "";
    }

}