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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import frameworks.customlayout.Utils;
import frameworks.retrofit.ResponseResolver;
import frameworks.retrofit.RestError;
import frameworks.retrofit.WebServicesWrapper;
import retrofit2.Response;
import transport.school.com.schoolapp.bean.AttendanceRecord;
import transport.school.com.schoolapp.bean.AttendanceUpdateResponse;
import transport.school.com.schoolapp.bean.Student;
/**
 * Created by Naveen.Goyal on 11/29/2017.
 */
public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.MyViewHolder> {
    private Context mContext;

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
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
            Glide.with(mContext).load(student.getProfilePic()).into(thumbnail);
            this.student = student;
        }

        @OnCheckedChanged(R.id.checkbox)
        void onChecked(boolean checked) {
            AttendanceRecord attendanceRecord;
            attendanceRecord = new AttendanceRecord();
            attendanceRecord.setStudentid(student.getStudentid());
            attendanceRecord.setSchoolid(student.getSchoolid());
            attendanceRecord.setMorningevening("e");
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

    public StudentAttendanceAdapter(Context mContext) {
        this.mContext = mContext;
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
    public int getItemCount() {
        return studentList.size();
    }
}
