package transport.school.com.schoolapp;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

import frameworks.appsession.AppBaseApplication;
import frameworks.retrofit.ResponseResolver;
import frameworks.retrofit.RestError;
import frameworks.retrofit.WebServices;
import frameworks.retrofit.WebServicesWrapper;
import retrofit2.Response;
import transport.school.com.schoolapp.bean.NotificationBean;
import transport.school.com.schoolapp.bean.Routestop;

/**
 * Created by sandeepgoyal on 02/02/18.
 */

public class NotificationSender {

    private static NotificationSender mInstance =  new NotificationSender();

    private List<Routestop> routestops = new ArrayList<>();


    private NotificationSender() {

    }

    public static NotificationSender getmInstance() {
        return  mInstance;
    }
    public void setCurrentLatLng(LatLng latLng) {
        for(int i =0 ;i< routestops.size() && !routestops.get(i).isMessageSent();i++) {
            double distance = SphericalUtil.computeDistanceBetween(latLng,new LatLng(Double.parseDouble(routestops.get(i).getLatitude()),Double.parseDouble(routestops.get(i).getLongitude())));
            if(distance <= 10 * 1000) {
                NotificationBean notificationBean = new NotificationBean();
                notificationBean.setMessage("Bus reaching within 10 Minutes");
                notificationBean.setStopid(routestops.get(i).getStopid());
                final int finalI = i;
                WebServicesWrapper.getInstance().pushNotificatione(notificationBean, new ResponseResolver<String>() {
                    @Override
                    public void onSuccess(String s, Response response) {
                        routestops.get(finalI).setMessageSent(true);
                    }

                    @Override
                    public void onFailure(RestError error, String msg) {

                    }
                });
            }
        }
    }

    public List<Routestop> getRoutestops() {
        return routestops;
    }

    public void setRoutestops(List<Routestop> routestops) {
        this.routestops = routestops;
    }
}
