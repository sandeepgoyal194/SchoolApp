package transport.school.com.schoolapp;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import frameworks.basemvp.AppBaseFragment;
import frameworks.basemvp.IPresenter;
import transport.school.com.schoolapp.bean.RouteStudentList;
import transport.school.com.schoolapp.bean.Student;
/**
 * Created by sandeep on 22/1/18.
 */
public class StudentAbsentFragment extends StudentAttendanceFragment {
    public void setStudentList(RouteStudentList routeStudentList) {
        List<Student> studentList = new ArrayList<>();
        for(int i=0;i<routeStudentList.getStudents().size();i++ ) {
            if(routeStudentList.getStudents().get(i).getmAttendance() != 0) {
                studentList.add(routeStudentList.getStudents().get(i));
            }
        }
        adapter.setStudentList(studentList, "");
    }
}
