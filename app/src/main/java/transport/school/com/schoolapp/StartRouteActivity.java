package transport.school.com.schoolapp;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import frameworks.appsession.AppBaseApplication;
import frameworks.basemvp.AppBaseActivity;
import frameworks.basemvp.IPresenter;
import frameworks.retrofit.ResponseResolver;
import frameworks.retrofit.RestError;
import frameworks.retrofit.WebServicesWrapper;
import retrofit2.Response;
import transport.school.com.schoolapp.bean.Route;
import transport.school.com.schoolapp.bean.RouteReply;
public class StartRouteActivity extends AppBaseActivity {
    @Override
    public int getViewToCreate() {
        return R.layout.activity_start_route;
    }

    @Override
    public IPresenter getPresenter() {
        return null;
    }


    @OnClick({R.id.btn_start_morning_route, R.id.btn_start_evening_route})
    public void onViewClicked(View view) {
        Route route = AppBaseApplication.getApplication().getRoute();
        switch (view.getId()) {
            case R.id.btn_start_morning_route:
                route.setmMorningEvening("m");
                break;
            case R.id.btn_start_evening_route:
                route.setmMorningEvening("e");
                break;
        }
        WebServicesWrapper.getInstance().startRoute(route, new ResponseResolver<RouteReply>() {
            @Override
            public void onSuccess(RouteReply routeReply, Response response) {

            }

            @Override
            public void onFailure(RestError error, String msg) {
            }
        });
    }

}
