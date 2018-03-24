package frameworks.locationmanager.location;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
/**
 * Created by naveen on 15/07/2017.
 */

public class LocationManagerExtended extends LocationManager {
    Context mContext;

    public LocationManagerExtended(Context context) {
        this.mContext = context;
    }

    public void initGoogleClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        createLocationRequest();
    }

    void initCurrentLocation() {
        try {
            if (!mGoogleApiClient.isConnected()) {
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mCurrentLocation != null) {
                for (LocationAndSettingChangeUpdates observer : mLocationandSettingChangeObserver) {
                    observer.onLocationChanged(mCurrentLocation);
                }
            }
        } catch (SecurityException e) {
            //startLocationUpdates();
        }
    }


}
