
package transport.school.com.schoolapp.bean;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Stop {

    @SerializedName("latitude")
    private String mLatitude;
    @SerializedName("longitude")
    private String mLongitude;
    @SerializedName("stopid")
    private String mStopid;
    @SerializedName("stopname")
    private String mStopname;

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

    public String getStopid() {
        return mStopid;
    }

    public void setStopid(String stopid) {
        mStopid = stopid;
    }

    public String getStopname() {
        return mStopname;
    }

    public void setStopname(String stopname) {
        mStopname = stopname;
    }

}
