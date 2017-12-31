
package transport.school.com.schoolapp.bean;

import com.google.gson.annotations.SerializedName;

public class Teacher {

    @SerializedName("busnumber")
    private String mBusnumber;
    @SerializedName("driver")
    private String mDriver;
    @SerializedName("drivermobile")
    private String mDrivermobile;
    @SerializedName("eveningsequence")
    private String mEveningsequence;
    @SerializedName("helper")
    private String mHelper;
    @SerializedName("helpermobile")
    private String mHelpermobile;
    @SerializedName("mobile")
    private String mMobile;
    @SerializedName("morningsequence")
    private String mMorningsequence;
    @SerializedName("routeid")
    private String mRouteid;
    @SerializedName("routenumber")
    private String mRoutenumber;
    @SerializedName("teacher")
    private String mTeacher;

    public String getBusnumber() {
        return mBusnumber;
    }

    public void setBusnumber(String busnumber) {
        mBusnumber = busnumber;
    }

    public String getDriver() {
        return mDriver;
    }

    public void setDriver(String driver) {
        mDriver = driver;
    }

    public String getDrivermobile() {
        return mDrivermobile;
    }

    public void setDrivermobile(String drivermobile) {
        mDrivermobile = drivermobile;
    }

    public String getEveningsequence() {
        return mEveningsequence;
    }

    public void setEveningsequence(String eveningsequence) {
        mEveningsequence = eveningsequence;
    }

    public String getHelper() {
        return mHelper;
    }

    public void setHelper(String helper) {
        mHelper = helper;
    }

    public String getHelpermobile() {
        return mHelpermobile;
    }

    public void setHelpermobile(String helpermobile) {
        mHelpermobile = helpermobile;
    }

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public String getMorningsequence() {
        return mMorningsequence;
    }

    public void setMorningsequence(String morningsequence) {
        mMorningsequence = morningsequence;
    }

    public String getRouteid() {
        return mRouteid;
    }

    public void setRouteid(String routeid) {
        mRouteid = routeid;
    }

    public String getRoutenumber() {
        return mRoutenumber;
    }

    public void setRoutenumber(String routenumber) {
        mRoutenumber = routenumber;
    }

    public String getTeacher() {
        return mTeacher;
    }

    public void setTeacher(String teacher) {
        mTeacher = teacher;
    }

}
