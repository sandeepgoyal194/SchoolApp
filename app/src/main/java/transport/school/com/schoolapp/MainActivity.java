package transport.school.com.schoolapp;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import frameworks.appsession.AppBaseApplication;
import frameworks.basemvp.AppBaseActivity;
import frameworks.basemvp.AppBaseFragment;
import frameworks.basemvp.IPresenter;
import frameworks.retrofit.ResponseResolver;
import frameworks.retrofit.RestError;
import frameworks.retrofit.WebServices;
import frameworks.retrofit.WebServicesWrapper;
import retrofit2.Response;
import transport.school.com.schoolapp.bean.ActiveRouteReply;
import transport.school.com.schoolapp.bean.Route;
import transport.school.com.schoolapp.bean.RouteReply;
public class MainActivity extends AppBaseActivity {
    SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private MapViewFragment mapFragment;
    @BindView(R.id.tabs)
    TabLayout mMainTab;

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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(3);
    }

    void loadFragments() {

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mMainTab.setupWithViewPager(mViewPager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageSelected(int pageNumber) {
                // Just define a callback method in your fragment and call it like this!
                mSectionsPagerAdapter.getItem(pageNumber).imVisible();

            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();


        /*WebServicesWrapper.getInstance().getActiveRoute(AppBaseApplication.getApplication().getRoute(), new ResponseResolver<ActiveRouteReply>() {
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
        });*/
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
 StudentAttendanceFragment studentAttendanceFragment = new StudentAttendanceFragment();
        StudentAbsentFragment studentAbsentFragment = new StudentAbsentFragment();
        @Override
        public AppBaseFragment getItem(int position) {
            switch (position) {
                case 0:
                    return mapFragment;
                case 1:
                    return studentAttendanceFragment;

                case 2:
                    return studentAbsentFragment;
            }
            return new StudentAttendanceFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Navigation";
                case 1:
                    return "Attendence";
                case 2:
                    return "Pick Up";
            }
            return "Attendence";
        }
    }

    @Override
    public boolean isLocationNeeded() {
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        if (mapFragment != null) {
            mapFragment.onLocationChanged(location);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.endroute_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.end_route:
                Route route = new Route();
                route.setRouteid(AppBaseApplication.getApplication().getRoute().getRouteid());
                route.setmMorningEvening(AppBaseApplication.getApplication().getUser().getmMorningEvening());
                WebServicesWrapper.getInstance().stopRoute(route, new ResponseResolver<RouteReply>() {
                    @Override
                    public void onSuccess(RouteReply routeReply, Response response) {
                        startActivity(new Intent(getContext(), StartRouteActivity.class));
                    }

                    @Override
                    public void onFailure(RestError error, String msg) {
                    }
                });
                return super.onOptionsItemSelected(item);
            case R.id.sendMsg:
                startActivity(new Intent(this,SendMessage.class));
                break;
            case R.id.logout:
                WebServicesWrapper.getInstance().logout(AppBaseApplication.getApplication().getSession(), new ResponseResolver<String>() {
                    @Override
                    public void onSuccess(String s, Response response) {
                        finish();
                        AppBaseApplication.getApplication().onLogout();
                    }

                    @Override
                    public void onFailure(RestError error, String msg) {

                    }
                });

        }
        return true;
    }



        }
