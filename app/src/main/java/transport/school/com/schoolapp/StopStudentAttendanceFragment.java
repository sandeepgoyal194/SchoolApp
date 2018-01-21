package transport.school.com.schoolapp;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import frameworks.customlayout.EmptyLayout;
import frameworks.retrofit.ResponseResolver;
import frameworks.retrofit.RestError;
import frameworks.retrofit.WebServices;
import frameworks.retrofit.WebServicesWrapper;
import retrofit2.Response;
import transport.school.com.schoolapp.bean.RouteStudentList;
import transport.school.com.schoolapp.bean.Stop;
import transport.school.com.schoolapp.bean.StopResponse;
import transport.school.com.schoolapp.bean.Student;
/**
 */
public class StopStudentAttendanceFragment extends StudentAttendanceFragment {
    private static final String STOP_ID = "STOP_ID";
    private String mStopId;

    public StopStudentAttendanceFragment() {
        // Required empty public constructor
    }

    public static StopStudentAttendanceFragment newInstance(String param1) {
        StopStudentAttendanceFragment fragment = new StopStudentAttendanceFragment();
        Bundle args = new Bundle();
        args.putString(STOP_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStopId = getArguments().getString(STOP_ID);
        }
        Stop stop = new Stop();
        stop.setStopid(mStopId);
        WebServicesWrapper.getInstance().getRoute(stop, new ResponseResolver<StopResponse>() {
            @Override
            public void onSuccess(StopResponse stopResponse, Response response) {
                setTitle(stopResponse.getStop().getStopname());
            }

            @Override
            public void onFailure(RestError error, String msg) {
            }
        });
    }

    public void setStudentList(RouteStudentList routeStudentList) {
        List<Student> studentList = new ArrayList<>();
        if(routeStudentList.getStudents() != null) {
            for (Student student : routeStudentList.getStudents()) {
                if(student.getStopid().equals(mStopId)) {
                    studentList.add(student);
                }
            }
        }
        if(studentList.size() <= 0) {
            addEmptyLayout();
        }else {
            adapter.setStudentList(studentList);
        }
    }

    @Override
    public EmptyLayout getEmptyLayout() {
        return new NoStudentLayout(getContext());
    }
}
