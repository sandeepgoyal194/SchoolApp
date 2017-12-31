
package transport.school.com.schoolapp.bean;

import com.google.gson.annotations.SerializedName;

public class Route {

    @SerializedName("routeid")
    private String mRouteid;

    public String getRouteid() {
        return mRouteid;
    }

    public void setRouteid(String routeid) {
        mRouteid = routeid;
    }

}
