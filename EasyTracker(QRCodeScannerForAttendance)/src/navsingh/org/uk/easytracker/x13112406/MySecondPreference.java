package navsingh.org.uk.easytracker.x13112406;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint("NewApi")
public class MySecondPreference {
	public static String SPREF_NAME = "MySPref";
	public static String SID_TAG = "sid";
	SharedPreferences spref;
	Context secondcontext;
	Editor editor2;
	
	public MySecondPreference(Context scontext){
		this.secondcontext = scontext;
		spref = scontext.getSharedPreferences(SPREF_NAME, 1);
	}
	
	 public void setSID(String value){
        editor2 = spref.edit();
        editor2.putString(SID_TAG, value);
        editor2.apply();
    }
	 
	 public String getSID(){
        return spref.getString(SID_TAG, "Null");
    }
}