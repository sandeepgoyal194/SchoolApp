package transport.school.com.schoolapp;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import frameworks.appsession.AppBaseApplication;
import frameworks.basemvp.AppBaseActivity;
import frameworks.basemvp.IPresenter;
import frameworks.retrofit.ResponseResolver;
import frameworks.retrofit.RestError;
import frameworks.retrofit.WebServicesWrapper;
import retrofit2.Response;
import transport.school.com.schoolapp.bean.ActiveRouteReply;
import transport.school.com.schoolapp.bean.Route;
public class MainActivity extends AppBaseActivity {
    SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private MapViewFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mapFragment = new MapViewFragment();
        WebServicesWrapper.getInstance().getActiveRoute(AppBaseApplication.getApplication().getRoute(), new ResponseResolver<ActiveRouteReply>() {
            @Override
            public void onSuccess(ActiveRouteReply activeRouteReply, Response response) {
                if (activeRouteReply.getStudents().get(0).getE().equals("0") && activeRouteReply.getStudents().get(0).getM().equals("0")) {
                    startActivity(new Intent(getContext(), StartRouteActivity.class));
                } else {
                    Route route = new Route();
                    if (activeRouteReply.getStudents().get(0).getE().equals("1")) {
                        route.setmMorningEvening("e");
                    } else if (activeRouteReply.getStudents().get(0).getM().equals("1")) {
                        route.setmMorningEvening("m");
                    }
                    AppBaseApplication.getApplication().saveUser(route);
                    loadFragments();
                }
            }

            @Override
            public void onFailure(RestError error, String msg) {
            }
        });
        super.onCreate(savedInstanceState);
    }

    void loadFragments() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WebServicesWrapper.getInstance().getActiveRoute(AppBaseApplication.getApplication().getRoute(), new ResponseResolver<ActiveRouteReply>() {
            @Override
            public void onSuccess(ActiveRouteReply activeRouteReply, Response response) {
                if (activeRouteReply.getStudents().get(0).getE().equals("0") && activeRouteReply.getStudents().get(0).getM().equals("0")) {
                    startActivity(new Intent(getContext(), StartRouteActivity.class));
                } else {
                    Route route = new Route();
                    if (activeRouteReply.getStudents().get(0).getE().equals("1")) {
                        route.setmMorningEvening("e");
                    } else if (activeRouteReply.getStudents().get(0).getM().equals("1")) {
                        route.setmMorningEvening("m");
                    }
                    AppBaseApplication.getApplication().saveUser(route);
                    loadFragments();
                }
            }

            @Override
            public void onFailure(RestError error, String msg) {
            }
        });
    }

    @Override
    public int getViewToCreate() {
        return R.layout.app_bar_main;
    }

    @Override
    public IPresenter getPresenter() {
        return null;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mapFragment;
                case 1:
                    return new StudentAttendanceFragment();
            }
            return new StudentAttendanceFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public boolean isLocationNeeded() {
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        if(mapFragment != null) {
            mapFragment.onLocationChanged();
        }
    }
}
