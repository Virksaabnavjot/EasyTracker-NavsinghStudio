package navsingh.org.uk.easytracker.x13112406;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint("NewApi")
public class MyPreference {
	public static String PREF_NAME = "MyPref";
	public static String QR_CODE_TAG = "qrcod";
	
	SharedPreferences pref;
	Context context;
	Editor editor;
	
	public MyPreference(Context _context){
		this.context = _context;
		pref = _context.getSharedPreferences(PREF_NAME, 1);
	}
	
	 public void setQRCODE(String value){
        editor = pref.edit();
        editor.putString(QR_CODE_TAG, value);
        editor.apply();
    }
	 
	 public String getQRCODE(){
        return pref.getString(QR_CODE_TAG, "Null");
    }
}
