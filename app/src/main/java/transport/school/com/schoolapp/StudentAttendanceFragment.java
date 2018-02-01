package transport.school.com.schoolapp;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import frameworks.appsession.AppBaseApplication;
import frameworks.basemvp.AppBaseFragment;
import frameworks.basemvp.IPresenter;
import frameworks.customlayout.Utils;
import frameworks.retrofit.ResponseResolver;
import frameworks.retrofit.RestError;
import frameworks.retrofit.WebServicesWrapper;
import retrofit2.Response;
import transport.school.com.schoolapp.bean.ActiveRouteReply;
import transport.school.com.schoolapp.bean.AttendanceRecord;
import transport.school.com.schoolapp.bean.RouteStudentList;
import transport.school.com.schoolapp.bean.Student;
/**
 * Created by Naveen.Goyal on 11/29/2017.
 */
public class StudentAttendanceFragment extends AppBaseFragment implements StudentAttendanceAdapter.onStudentClick {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    protected StudentAttendanceAdapter adapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new StudentAttendanceAdapter(getContext(),this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(adapter));
    }

    public void imVisible() {
        prepareStudentList();
    }
    @Override
    public void onResume() {
        super.onResume();
        prepareStudentList();
    }


    @Override
    public View getView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_attendance, container, false);
    }

    @Override
    public IPresenter getPresenter() {
        return null;
    }

    private void prepareStudentList() {
        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setmRouteId(AppBaseApplication.getApplication().getRoute().getRouteid());
        attendanceRecord.setAttendancedate(Utils.getCurrentDate());
        attendanceRecord.setMorningevening(AppBaseApplication.getApplication().getRoute().getmMorningEvening());
        WebServicesWrapper.getInstance().getAttendence(attendanceRecord, new ResponseResolver<RouteStudentList>() {
            @Override
            public void onSuccess(RouteStudentList routeStudentList, Response response) {
                setStudentList(routeStudentList);
            }

            @Override
            public void onFailure(RestError error, String msg) {
            }
        });
    }

    public void setStudentList(RouteStudentList routeStudentList) {
        List<Student> studentList = new ArrayList<>();
        for(int i=0;i<routeStudentList.getStudents().size();i++ ) {
            if(routeStudentList.getStudents().get(i).getmAttendance() == 0) {
                studentList.add(routeStudentList.getStudents().get(i));
            }
        }
        adapter.setStudentList(studentList, "");
    }

    @Override
    public void onStudentClick() {
        prepareStudentList();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
