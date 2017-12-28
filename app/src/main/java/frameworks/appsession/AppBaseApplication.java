package frameworks.appsession;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import transport.school.com.schoolapp.LoginActivity;
/**
 * Created by Sandeep on 21/01/2017.
 */

public class AppBaseApplication extends Application {
    private static AppBaseApplication mApplication;
    private UserInfo mUser = null;
    private LoginResponse mLoginResponse = null;
    AppUserManager mAppUserManager = null;
    AppSessionManager mAppSessionManager = null;

    private boolean mCallPermission;
    private boolean mMicroPhonePermission;
    private boolean mContactPermission;
    private boolean mCameraPermission;

    public static String TAG = AppBaseApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
      //  JodaTimeAndroid.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        //MultiDex.install(base);
        super.attachBaseContext(base);
        mAppUserManager = new AppUserManager(this);
        mAppSessionManager = new AppSessionManager(this);
        initSession();
    }

    public static AppBaseApplication getApplication() {
        return mApplication;
    }

    public void initSession() {
        if (mAppSessionManager.isRunningSession()) {
            //mLoginResponse = mAppSessionManager.getSession();
        }
    }

    public void saveUser(UserInfo user) {
        mAppUserManager.saveUser(user);
        mUser = user;
    }

    public UserInfo getUser() {
        if (mUser == null) {
            mUser = mAppUserManager.getUser();
        }
        return mUser;

    }

    public void setSession(LoginResponse loginResponse) {
        mAppSessionManager.saveSession(loginResponse);
        mLoginResponse = loginResponse;
    }


    public boolean isUserLogin() {
        /*if (mLoginResponse == null) {
            return false;
        }*/
        return true;
    }

    public String getmAuthID() {
       if (mLoginResponse == null) {
            startLogin();
            return null;
        }
        return mLoginResponse.getApi_key();
    }

    public void onLogout() {
        clearSession();
        clearUser();
        startLogin();
    }

    public void startLogin() {
        Intent i = new Intent(mApplication.getBaseContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mApplication.getBaseContext().startActivity(i);
    }

    private void clearUser() {
        mAppUserManager.clearUser();
       // mUser = null;
    }

    private void clearSession() {
        mAppSessionManager.clearSession();
        //mLoginResponse = null;
    }

    /*public boolean isUserTypeDefined() {
        return !(mLoginResponse.getUser_type() == null);
    }

    public String getUserType() {
        return mLoginResponse.getUser_type();
    }

    public void setUserType(String userType) {
        mLoginResponse.setUser_type(userType);
    //    setSession(mLoginResponse);*/
//    }

    public boolean ismCallPermission() {
        return mCallPermission;
    }

    public void setmCallPermission(boolean mCallPermission) {
        this.mCallPermission = mCallPermission;
    }

    public boolean ismMicroPhonePermission() {
        return mMicroPhonePermission;
    }

    public void setmMicroPhonePermission(boolean mMicroPhonePermission) {
        this.mMicroPhonePermission = mMicroPhonePermission;
    }

    public boolean ismContactPermission() {
        return mContactPermission;
    }

    public void setmContactPermission(boolean mContactPermission) {
        this.mContactPermission = mContactPermission;
    }

    public boolean ismCameraPermission() {
        return mCameraPermission;
    }

    public void setmCameraPermission(boolean mCameraPermission) {
        this.mCameraPermission = mCameraPermission;
    }
}
