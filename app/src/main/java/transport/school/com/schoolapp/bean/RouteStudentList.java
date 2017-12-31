
package transport.school.com.schoolapp.bean;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RouteStudentList {

    @SerializedName("error")
    private Boolean mError;
    @SerializedName("students")
    private List<Student> mStudents;

    public Boolean getError() {
        return mError;
    }

    public void setError(Boolean error) {
        mError = error;
    }

    public List<Student> getStudents() {
        return mStudents;
    }

    public void setStudents(List<Student> students) {
        mStudents = students;
    }

}
