
package transport.school.com.schoolapp.bean;

import com.google.gson.annotations.SerializedName;

public class RouteReply {

    @SerializedName("error")
    private Boolean mError;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("morningevening")
    private String mMorningevening;
    @SerializedName("routeid")
    private String mRouteid;

    public Boolean getError() {
        return mError;
    }

    public void setError(Boolean error) {
        mError = error;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getMorningevening() {
        return mMorningevening;
    }

    public void setMorningevening(String morningevening) {
        mMorningevening = morningevening;
    }

    public String getRouteid() {
        return mRouteid;
    }

    public void setRouteid(String routeid) {
        mRouteid = routeid;
    }

}
