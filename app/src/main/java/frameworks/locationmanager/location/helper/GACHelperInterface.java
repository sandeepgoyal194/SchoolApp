package frameworks.locationmanager.location.helper;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationSettingsResult;
/**
 * Created by naveen on 09/06/2016.
 */
public interface GACHelperInterface extends GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener,
        ResultCallback<LocationSettingsResult> {
}
