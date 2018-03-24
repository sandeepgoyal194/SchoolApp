package frameworks.locationmanager;

import android.location.Location;

/**
 * Created by naveen on 15/07/2017.
 */

public class LocationManagerService {

    Locations currentLocation;
    boolean isLocationPermissionGiven;
    boolean isLocationSettingEnabled;
    boolean isLocationSettingDenied;

    private static final LocationManagerService ourInstance = new LocationManagerService();

    public static LocationManagerService getInstance() {
        return ourInstance;
    }

    private LocationManagerService() {
    }

    public Locations getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Locations currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        Locations locations = new Locations();
        locations.setLattitude(currentLocation.getLatitude());
        locations.setLongitude(currentLocation.getLongitude());
        this.currentLocation = locations;
    }

    public boolean isLocationPermissionGiven() {
        return isLocationPermissionGiven;
    }

    public void setLocationPermissionGiven(boolean locationPermissionGiven) {
        isLocationPermissionGiven = locationPermissionGiven;
    }

    public boolean isLocationSettingEnabled() {
        return isLocationSettingEnabled;
    }

    public void setLocationSettingEnabled(boolean locationSettingEnabled) {
        isLocationSettingEnabled = locationSettingEnabled;
    }

    public boolean isLocationSettingDenied() {
        return isLocationSettingDenied;
    }

    public void setLocationSettingDenied(boolean locationSettingDenied) {
        isLocationSettingDenied = locationSettingDenied;
    }
}
