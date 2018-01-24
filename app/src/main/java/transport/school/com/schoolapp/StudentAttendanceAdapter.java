package transport.school.com.schoolapp;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import frameworks.appsession.AppBaseApplication;
import frameworks.customlayout.Utils;
import frameworks.retrofit.ResponseResolver;
import frameworks.retrofit.RestError;
import frameworks.retrofit.WebServicesWrapper;
import frameworks.retrofit.imageloader.GlideImageLoaderImpl;
import retrofit2.Response;
import transport.school.com.schoolapp.bean.AttendanceRecord;
import transport.school.com.schoolapp.bean.AttendanceUpdateResponse;
import transport.school.com.schoolapp.bean.Stop;
import transport.school.com.schoolapp.bean.StopResponse;
import transport.school.com.schoolapp.bean.Student;
/**
 * Created by Naveen.Goyal on 11/29/2017.
 */
public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.MyViewHolder> implements StickyRecyclerHeadersAdapter<StudentAttendanceAdapter.StudentListHeaderHolder> {
    private Context mContext;
    private String stopName;

    onStudentClick onStudentClick;


    public void setStudentList(List<Student> studentList, String stopName) {
        Collections.sort(studentList);
        this.studentList = studentList;
        this.stopName = stopName;
        notifyDataSetChanged();
    }


    private List<Student> studentList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.thumbnail)
        ImageView thumbnail;
        @BindView(R.id.student_name)
        TextView studentName;
        @BindView(R.id.checkbox)
        CheckBox checkbox;
        @BindView(R.id.card_view)
        CardView cardView;
        Student student;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.setDebug(true);
            ButterKnife.bind(this, view);
        }

        public void setStudent(Student student) {
            studentName.setText(student.getStudentname());
            new GlideImageLoaderImpl(mContext).loadImage(student.getProfilePic(), thumbnail, R.drawable.blank_person);
            this.student = student;
            if(student.getmAttendance() != 0) {
                checkbox.setChecked(true);
            }
        }

        @OnCheckedChanged(R.id.checkbox)
        void onChecked(boolean checked) {
            onStudentClick.onStudentClick();
            AttendanceRecord attendanceRecord;
            attendanceRecord = new AttendanceRecord();
            attendanceRecord.setStudentid(student.getStudentid());
            attendanceRecord.setSchoolid(student.getSchoolid());
            attendanceRecord.setMorningevening(AppBaseApplication.getApplication().getRoute().getmMorningEvening());
            attendanceRecord.setAttendancedate(Utils.getCurrentDate());
            if (checked) {
                WebServicesWrapper.getInstance().postStudentAttendence(attendanceRecord, new ResponseResolver<AttendanceUpdateResponse>() {
                    @Override
                    public void onSuccess(AttendanceUpdateResponse attendanceUpdateResponse, Response response) {

                    }

                    @Override
                    public void onFailure(RestError error, String msg) {
                    }
                });
            } else {
                WebServicesWrapper.getInstance().postStudentAbsent(attendanceRecord, new ResponseResolver<AttendanceUpdateResponse>() {
                    @Override
                    public void onSuccess(AttendanceUpdateResponse attendanceUpdateResponse, Response response) {
                    }

                    @Override
                    public void onFailure(RestError error, String msg) {
                    }
                });
            }
        }
    }

    public class StudentListHeaderHolder extends RecyclerView.ViewHolder {
        public StudentListHeaderHolder(View itemView) {
            super(itemView);
        }
    }

    public StudentAttendanceAdapter(Context mContext,onStudentClick onStudentClick) {
        this.mContext = mContext;
        this.onStudentClick = onStudentClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.setStudent(studentList.get(position));
    }

    @Override
    public long getHeaderId(int position) {
        return Long.parseLong(studentList.get(position).getStudentid());
    }

    @Override
    public StudentListHeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_header, parent, false);
        return new StudentAttendanceAdapter.StudentListHeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(StudentListHeaderHolder holder, int position) {
        final TextView textView = (TextView) holder.itemView;
        Stop stop = new Stop();
        stop.setStopid(studentList.get(position).getStopid());
        WebServicesWrapper.getInstance().getRoute(stop, new ResponseResolver<StopResponse>() {
            @Override
            public void onSuccess(StopResponse stopResponse, Response response) {
                textView.setText(stopResponse.getStop().getStopname()/*String.valueOf(studentList.get(position).getStudentid())*/);
            }

            @Override
            public void onFailure(RestError error, String msg) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public interface onStudentClick {
        public void onStudentClick();
    }
}
