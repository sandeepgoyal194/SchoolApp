
package transport.school.com.schoolapp.bean;

import com.google.gson.annotations.SerializedName;

public class AttendanceUpdateResponse {

    @SerializedName("attendance")
    private String mAttendance;
    @SerializedName("error")
    private Boolean mError;

    public String getAttendance() {
        return mAttendance;
    }

    public void setAttendance(String attendance) {
        mAttendance = attendance;
    }

    public Boolean getError() {
        return mError;
    }

    public void setError(Boolean error) {
        mError = error;
    }

}
