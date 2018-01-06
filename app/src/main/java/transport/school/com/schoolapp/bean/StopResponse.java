
package transport.school.com.schoolapp.bean;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class StopResponse {

    @SerializedName("error")
    private Boolean mError;
    @SerializedName("route")
    private Route mRoute;
    @SerializedName("routestops")
    private List<Routestop> mRoutestops;
    @SerializedName("stop")
    private Stop mStop;

    public Boolean getError() {
        return mError;
    }

    public void setError(Boolean error) {
        mError = error;
    }

    public Route getRoute() {
        return mRoute;
    }

    public void setRoute(Route route) {
        mRoute = route;
    }

    public List<Routestop> getRoutestops() {
        return mRoutestops;
    }

    public void setRoutestops(List<Routestop> routestops) {
        mRoutestops = routestops;
    }

    public Stop getStop() {
        return mStop;
    }

    public void setStop(Stop stop) {
        mStop = stop;
    }

}
