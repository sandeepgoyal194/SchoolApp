package frameworks.locationmanager.location;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.List;

import frameworks.locationmanager.LocationManagerService;
import frameworks.locationmanager.location.helper.GACHelperInterface;
/**
 * Created by naveen on 09/06/2016.
 */
public class LocationManager implements GACHelperInterface {
    private static final String TAG = "LocationManager";
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;
    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;


    /**
     * Store Device Location Single Instance for full application
     */
    protected Location mCurrentLocation = null;

    /**
     * Constant used in the location settings dialog.
     */

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;


    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    protected Activity mActivity = null;
    private static LocationManager mLocationManagerInstance = null;
    public static final int ACCESS_LOCATION = 201;
    List<LocationAndSettingChangeUpdates> mLocationandSettingChangeObserver = new ArrayList<>();
    int locationPriority = LocationRequest.PRIORITY_HIGH_ACCURACY;


    protected LocationManager() {

    }

    private LocationManager(Activity context) {
        mActivity = context;
    }

    public static LocationManager getmLocationManagerInstance(Activity activity) {
//        if (mLocationManagerInstance == null) {
//            mLocationManagerInstance = new LocationManager(activity);
//        }
        return new LocationManager(activity);// mLocationManagerInstance;
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    public void initGoogleClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void connectGoogleClient() {
        if (mGoogleApiClient == null) {
            initGoogleClient();
        }
        mGoogleApiClient.connect();

    }

    public void disconnectGoogleClient() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }

    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(locationPriority);
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    protected void checkLocationSettings() {
        buildLocationSettingsRequest();
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "Google Client Connected");
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_LOCATION);
            return;
        } else {
            createLocationRequest();
            if (!LocationManagerService.getInstance().isLocationSettingDenied())
                checkLocationSettings();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void setLocationandSettingChangeObserver(LocationAndSettingChangeUpdates observer) {
        mLocationandSettingChangeObserver.add(observer);
    }

    public void removeLocationandSettingChangeObserver(LocationAndSettingChangeUpdates observer) {
        mLocationandSettingChangeObserver.remove(observer);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocation Changed called");
        mCurrentLocation = location;
        for (LocationAndSettingChangeUpdates observer : mLocationandSettingChangeObserver) {
            observer.onLocationChanged(location);
        }
    }

    /*
    * 1. Intial Check with METHOD checkLocationSettings() to check gps setting
     * 2. onResult Method Callback if not success then reqeust for status.startResolutionForResult(mContext, REQUEST_CHECK_SETTINGS); location on
     * 3. Final onActivityResult callback  user accepted or denied
    * */

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                initCurrentLocation();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");


                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(mActivity, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
        }
    }

    public void setLocationPriority(int locationPriority) {
        this.locationPriority = locationPriority;
        createLocationRequest();
        getmCurrentLocation();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        for (LocationAndSettingChangeUpdates observer : mLocationandSettingChangeObserver) {
                            observer.onLocationSettingEnabled();
                        }
                        initCurrentLocation();
                        //startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        for (LocationAndSettingChangeUpdates observer : mLocationandSettingChangeObserver) {
                            observer.onLocationSettingEnableDenied();
                        }
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createLocationRequest();
                    //TODO need to handle from diff file
                    if (!LocationManagerService.getInstance().isLocationSettingDenied())
                        checkLocationSettings();
                    for (LocationAndSettingChangeUpdates observer : mLocationandSettingChangeObserver) {
                        observer.onLocationPermissionGranted();
                    }
                } else {
                    for (LocationAndSettingChangeUpdates observer : mLocationandSettingChangeObserver) {
                        observer.onLocationPermissionDenied();
                    }
                }
                break;
        }
    }

    void initCurrentLocation() {
        try {
            if(mGoogleApiClient == null) {
                initGoogleClient();
            }
            if (!mGoogleApiClient.isConnected()) {
                return;
            }
            Log.e(TAG, "init CurrentLocation ");
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.e(TAG, "mCurrentLcation  " + mCurrentLocation);
            if (mCurrentLocation != null) {
                for (LocationAndSettingChangeUpdates observer : mLocationandSettingChangeObserver) {
                    Log.e(TAG, "onLocationChanged called for " + observer);
                    observer.onLocationChanged(mCurrentLocation);
                }
            }
            startLocationUpdates();
        } catch (SecurityException e) {
            //startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException ex) {

        }
    }

    public Location getmCurrentLocation() {
        if (mCurrentLocation == null) {
            initCurrentLocation();
        }
        return mCurrentLocation;
    }

    interface LocationSettingUpdates {
        void onLocationPermissionGranted();

        void onLocationPermissionDenied();

        void onLocationSettingEnabled();

        void onLocationSettingEnableDenied();
    }

    interface LocationChangeObserver {
        void onLocationChanged(Location location);
    }

    public interface LocationAndSettingChangeUpdates extends LocationManager.LocationSettingUpdates, LocationManager.LocationChangeObserver {

    }
}
