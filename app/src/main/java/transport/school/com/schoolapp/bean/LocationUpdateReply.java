
package transport.school.com.schoolapp.bean;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class LocationUpdateReply {

    @SerializedName("error")
    private Boolean mError;
    @SerializedName("latitude")
    private String mLatitude;
    @SerializedName("longitude")
    private String mLongitude;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("routeid")
    private String mRouteid;

    public Boolean getError() {
        return mError;
    }

    public void setError(Boolean error) {
        mError = error;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getRouteid() {
        return mRouteid;
    }

    public void setRouteid(String routeid) {
        mRouteid = routeid;
    }

}
