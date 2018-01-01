
package transport.school.com.schoolapp.bean;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ActiveRouteReply {

    @SerializedName("error")
    private Boolean mError;
    @SerializedName("students")
    private List<StudentRouteReply> mStudents;

    public Boolean getError() {
        return mError;
    }

    public void setError(Boolean error) {
        mError = error;
    }

    public List<StudentRouteReply> getStudents() {
        return mStudents;
    }

    public void setStudents(List<StudentRouteReply> students) {
        mStudents = students;
    }

}
