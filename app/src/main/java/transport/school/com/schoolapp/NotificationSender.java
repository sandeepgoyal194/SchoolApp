package transport.school.com.schoolapp;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import frameworks.retrofit.ResponseResolver;
import frameworks.retrofit.RestError;
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
    public void setCurrentLatLng(com.google.android.gms.maps.model.LatLng latLng) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyAUmVRXx43uVLZomeU1tRR5OYYkGuW6bew")
                .build();
        LatLng latLng1 = new LatLng(latLng.latitude,latLng.longitude);

        DistanceMatrixApiRequest distanceMatrixApiRequest = DistanceMatrixApi.newRequest(context).origins(latLng1);
        LatLng[] latLng2 = new LatLng[routestops.size()];
        DistanceMatrix matrix = null;
        for(int i =0 ;i< routestops.size() && !routestops.get(i).isMessageSent();i++) {
            latLng2[i] = new LatLng(Double.parseDouble(routestops.get(i).getLatitude()),Double.parseDouble(routestops.get(i).getLongitude()));
        }
            distanceMatrixApiRequest.destinations(latLng2).mode(TravelMode.DRIVING);
        try {
            matrix = distanceMatrixApiRequest.await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DistanceMatrixRow[] distanceMatrixRow = matrix.rows;
        for(int i =0;i<distanceMatrixRow.length;i++) {
            DistanceMatrixRow distanceMatrixRoww = distanceMatrixRow[i];
            if(distanceMatrixRoww.elements[i].duration.inSeconds < 60*10) {
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