package frameworks.appsession;

import android.content.Context;

import frameworks.dbhandler.PrefManager;

import frameworks.retrofit.GsonFactory;
import transport.school.com.schoolapp.bean.LoginResponse;
/**
 * Created by Sandeep on 04/12/2016.
 */

class AppSessionManager {
    private static final String KEY_SESSION_ID = "SESSIONID";
    Context mContext;

    public AppSessionManager(Context mContext) {
        this.mContext = mContext;
    }

    public void saveSession(LoginResponse sessionId) {
        PrefManager mPrefManager;
        mPrefManager = new PrefManager(mContext);
        mPrefManager.putString(KEY_SESSION_ID, GsonFactory.getGson().toJson(sessionId));

    }

    public LoginResponse getSession() {
        PrefManager mPrefManager;
        mPrefManager = new PrefManager(mContext);
        return GsonFactory.getGson().fromJson(mPrefManager.getString(KEY_SESSION_ID), LoginResponse.class);
    }

    public void clearSession() {
        PrefManager mPrefManager;
        mPrefManager = new PrefManager(mContext);
        mPrefManager.putString(KEY_SESSION_ID, null);

    }

    public boolean isRunningSession() {
        if (getSession() == null) {
            return false;
        }
        return true;
    }
}
