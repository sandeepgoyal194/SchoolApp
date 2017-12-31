
package transport.school.com.schoolapp.bean;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("mobile")
    private String mMobile;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("user_device_id")
    private String mUserDeviceId;
    @SerializedName("user_device_type")
    private String mUserDeviceType;

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getUserDeviceId() {
        return mUserDeviceId;
    }

    public void setUserDeviceId(String userDeviceId) {
        mUserDeviceId = userDeviceId;
    }

    public String getUserDeviceType() {
        return mUserDeviceType;
    }

    public void setUserDeviceType(String userDeviceType) {
        mUserDeviceType = userDeviceType;
    }

}
