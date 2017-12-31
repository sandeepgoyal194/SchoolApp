package frameworks.dbhandler;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by Madhuri on 2/9/2016.
 */
public class PrefManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "StudentApp";

    // All Shared Preferences Keys

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

   public void putString(String id, String value) {
        editor.putString(id, value);
        editor.commit();
    }


    public String getString(String id) {
        return pref.getString(id, null);
    }


}