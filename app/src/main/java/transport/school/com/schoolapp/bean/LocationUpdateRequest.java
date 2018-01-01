
package transport.school.com.schoolapp.bean;

import com.google.gson.annotations.SerializedName;

public class LocationUpdateRequest {

    @SerializedName("latitude")
    private String mLatitude;
    @SerializedName("longitude")
    private String mLongitude;
    @SerializedName("routeid")
    private String mRouteid;

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

    public String getRouteid() {
        return mRouteid;
    }

    public void setRouteid(String routeid) {
        mRouteid = routeid;
    }

}
