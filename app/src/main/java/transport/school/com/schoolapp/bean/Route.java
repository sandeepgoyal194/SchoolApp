
package transport.school.com.schoolapp.bean;

import com.google.gson.annotations.SerializedName;

public class Route {

    @SerializedName("routeid")
    private String mRouteid;
    @SerializedName("morningevening")
    private String mMorningEvening;

    public String getRouteid() {
        return mRouteid;
    }

    public void setRouteid(String routeid) {
        mRouteid = routeid;
    }

    public String getmMorningEvening() {
        return mMorningEvening;
    }

    public void setmMorningEvening(String mMorningEvening) {
        this.mMorningEvening = mMorningEvening;
    }
}
