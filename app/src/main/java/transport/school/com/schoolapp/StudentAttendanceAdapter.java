package transport.school.com.schoolapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Naveen.Goyal on 11/29/2017.
 */

public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.MyViewHolder> {

    private Context mContext;
    private List<StudentModel> studentList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView studentName;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            studentName = (TextView) view.findViewById(R.id.student_name);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public StudentAttendanceAdapter(Context mContext, List<StudentModel> studentList) {
        this.mContext = mContext;
        this.studentList = studentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        StudentModel studentModel = studentList.get(position);
        holder.studentName.setText(studentModel.getName());

        // loading album cover using Glide library
        Glide.with(mContext).load(studentModel.getThumbnail()).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}
