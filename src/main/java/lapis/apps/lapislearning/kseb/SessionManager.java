package lapis.apps.lapislearning.kseb;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Pref";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void setuid(String uid) {
        editor.putString("uid", uid).commit();
    }

    public String getuid() {
        String uid = pref.getString("uid","");
        return uid;
    }

    public void setuserrole(String userrole) {
        editor.putString("userrole", userrole).commit();
    }

    public String getuserrole() {
        String userrole = pref.getString("userrole","");
        return userrole;
    }
    public void setNameUser(String name) {
        editor.putString("name", name).commit();
    }

    public String getNameUser() {
        String name = pref.getString("name","");
        return name;
    }
    public void setMobileUser(String mobile) {
        editor.putString("mobile", mobile).commit();
    }

    public String getMobileUser() {
        String mobile = pref.getString("mobile","");
        return mobile;
    }
 public void setPincode(String pincode) {
        editor.putString("pincode", pincode).commit();
    }

    public String getPincode() {
        String pincode = pref.getString("pincode","");
        return pincode;
    }
    public void setLm(String lm) {
        editor.putString("lm", lm).commit();
    }

    public String getLm() {
        String lm = pref.getString("lm","");
        return lm;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }

}