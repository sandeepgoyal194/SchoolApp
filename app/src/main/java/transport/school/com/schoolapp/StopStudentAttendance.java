package transport.school.com.schoolapp;
import android.os.Bundle;
import android.support.annotation.Nullable;

import frameworks.basemvp.AppBaseActivity;
import frameworks.basemvp.IPresenter;
public class StopStudentAttendance extends AppBaseActivity {
    public static final String STOP_ID = "STOP_ID";
    public static final String STOP_NAME = "STOP_NAME";

    @Override
    public int getViewToCreate() {
        return R.layout.activity_stop_student_attendance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFrgment(StopStudentAttendanceFragment.newInstance(getIntent().getStringExtra(STOP_ID), getIntent().getStringExtra(STOP_NAME)), "Student_Fragment", R.id.container, false);
    }

    @Override
    public IPresenter getPresenter() {
        return null;
    }
}
