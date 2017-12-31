
package transport.school.com.schoolapp.bean;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@SuppressWarnings("unused")
public class LoginResponse {
    @SerializedName("error")
    private Boolean mError;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("teacher")
    private List<Teacher> mTeacher;
    @SerializedName("token")
    private String mToken;

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

    public List<Teacher> getTeacher() {
        return mTeacher;
    }

    public void setTeacher(List<Teacher> teacher) {
        mTeacher = teacher;
    }

    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }
}
