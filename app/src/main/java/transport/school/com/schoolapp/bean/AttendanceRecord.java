
package transport.school.com.schoolapp.bean;

import com.google.gson.annotations.SerializedName;

public class AttendanceRecord {

    @SerializedName("attendancedate")
    private String mAttendancedate;
    @SerializedName("morningevening")
    private String mMorningevening;
    @SerializedName("schoolid")
    private String mSchoolid;
    @SerializedName("studentid")
    private String mStudentid;

    public String getAttendancedate() {
        return mAttendancedate;
    }

    public void setAttendancedate(String attendancedate) {
        mAttendancedate = attendancedate;
    }

    public String getMorningevening() {
        return mMorningevening;
    }

    public void setMorningevening(String morningevening) {
        mMorningevening = morningevening;
    }

    public String getSchoolid() {
        return mSchoolid;
    }

    public void setSchoolid(String schoolid) {
        mSchoolid = schoolid;
    }

    public String getStudentid() {
        return mStudentid;
    }

    public void setStudentid(String studentid) {
        mStudentid = studentid;
    }

}
