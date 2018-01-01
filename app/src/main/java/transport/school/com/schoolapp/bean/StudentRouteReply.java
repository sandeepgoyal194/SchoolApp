package transport.school.com.schoolapp.bean;
import com.google.gson.annotations.SerializedName;
/**
 * Created by sandeep on 1/1/18.
 */
public class StudentRouteReply {
    @SerializedName("e")
    private String mE;
    @SerializedName("m")
    private String mM;
    @SerializedName("routeid")
    private String mRouteid;

    public String getE() {
        return mE;
    }

    public void setE(String e) {
        mE = e;
    }

    public String getM() {
        return mM;
    }

    public void setM(String m) {
        mM = m;
    }

    public String getRouteid() {
        return mRouteid;
    }

    public void setRouteid(String routeid) {
        mRouteid = routeid;
    }
}
