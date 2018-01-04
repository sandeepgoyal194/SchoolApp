
package transport.school.com.schoolapp.bean;
import com.google.gson.annotations.SerializedName;

import frameworks.locationmanager.Locations;


public class LocationUpdateRequest extends Locations {
    @SerializedName("routeid")
    private String mRouteid;

    public String getRouteid() {
        return mRouteid;
    }

    public void setRouteid(String routeid) {
        mRouteid = routeid;
    }
}
