package transport.school.com.schoolapp;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import frameworks.appsession.AppBaseApplication;
import frameworks.locationmanager.LocationManagerService;
import frameworks.locationmanager.Locations;
import frameworks.retrofit.ResponseResolver;
import frameworks.retrofit.RestError;
import frameworks.retrofit.WebServicesWrapper;
import retrofit2.Response;
import transport.school.com.schoolapp.bean.Route;
import transport.school.com.schoolapp.bean.RouteReply;
import transport.school.com.schoolapp.bean.Routestop;
import transport.school.com.schoolapp.bean.Stop;
import transport.school.com.schoolapp.bean.StopResponse;

import static transport.school.com.schoolapp.Constants.ZOOM_LEVEL_STREETS;
public class MapViewFragment extends Fragment {
    private static final String TAG = "SchoolApp";
    MapView mMapView;
    private GoogleMap googleMap;
    private List<Marker> markers = new ArrayList<Marker>();
    ArrayList<LatLng> latLngs = new ArrayList<>();
    private MarkerOptions mMarkerOptions = null;
    private Marker mMarker = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_fragment, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                mMarkerOptions = null;
                String[] sequenceids = null;
                if (AppBaseApplication.getApplication().isMorningRoute()) {
                    String morningSequence = AppBaseApplication.getApplication().getSession().getTeacher().get(0).getMorningsequence();
                    if (morningSequence != null) {
                        sequenceids = morningSequence.split(",");
                    } else {
                        final Route route = AppBaseApplication.getApplication().getRoute();
                        route.setmMorningEvening("m");
                        Toast.makeText(getContext(), "Morning Route Not Available", Toast.LENGTH_LONG).show();
                        WebServicesWrapper.getInstance().stopRoute(route, new ResponseResolver<RouteReply>() {
                            @Override
                            public void onSuccess(RouteReply routeReply, Response response) {
                                startActivity(new Intent(getContext(), StartRouteActivity.class));
                            }

                            @Override
                            public void onFailure(RestError error, String msg) {
                            }
                        });
                    }
                } else {
                    String eveningSequence = AppBaseApplication.getApplication().getSession().getTeacher().get(0).getEveningsequence();
                    if (eveningSequence != null) {
                        sequenceids = eveningSequence.split(",");
                    } else {
                        final Route route = AppBaseApplication.getApplication().getRoute();
                        route.setmMorningEvening("");
                        Toast.makeText(getContext(), "Morning Route Not Available", Toast.LENGTH_LONG).show();
                        WebServicesWrapper.getInstance().stopRoute(route, new ResponseResolver<RouteReply>() {
                            @Override
                            public void onSuccess(RouteReply routeReply, Response response) {
                                startActivity(new Intent(getContext(), StartRouteActivity.class));
                            }

                            @Override
                            public void onFailure(RestError error, String msg) {
                            }
                        });
                    }
                }
                if (sequenceids != null && sequenceids.length > 0) {
                    Stop stop = new Stop();
                    stop.setStopid(sequenceids[0]);
                    WebServicesWrapper.getInstance().getRoute(stop, new ResponseResolver<StopResponse>() {
                        @Override
                        public void onSuccess(StopResponse stopResponse, Response response) {
                            latLngs.clear();
                            List<Routestop> routestops = stopResponse.getRoutestops();
                            for (Routestop routestop : routestops) {
                                latLngs.add(new LatLng(Double.parseDouble(routestop.getLatitude()), Double.parseDouble(routestop.getLongitude())));
                            }
                            drawPolyLineOnMap(latLngs);
                        }

                        @Override
                        public void onFailure(RestError error, String msg) {
                        }
                    });
                }
//                AppBaseApplication.getApplication().getSession().getTeacher().get(0).
            }
        });
        return rootView;
    }

    public void navigateToPoint(LatLng latLng) {
        CameraPosition position = new CameraPosition.Builder().target(latLng).build();
        changeCameraPosition(position);
    }

    private void changeCameraPosition(CameraPosition cameraPosition) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.moveCamera(cameraUpdate);
    }

    public void addMarkerToMap(LatLng latLng) {
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title("title")
                .snippet("snippet").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
    }

    public void drawPolyLineOnMap(List<LatLng> list) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.BLUE);
        polyOptions.width(8);
        polyOptions.addAll(list);
        googleMap.addPolyline(polyOptions);
        /*LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list) {
            builder.include(latLng);
        }
        final LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        googleMap.animateCamera(cu);*/
    }

    void setLocationOnMap(Locations location) {
        if (googleMap == null) {
            Log.i(TAG, "Map is null");
            return;
        }
        LatLng currentLocationLatLong = new LatLng(location.getLattitude(), location.getLongitude());
        if (mMarkerOptions == null || !mMarker.isVisible()) {
            mMarkerOptions = new MarkerOptions().position(currentLocationLatLong);
            mMarker = googleMap.addMarker(mMarkerOptions);
        } else if (!mMarker.getPosition().equals(currentLocationLatLong)) {
            mMarker.setPosition(currentLocationLatLong);
        }
        setZoomLevel(currentLocationLatLong);
    }

    public void setZoomLevel(LatLng currentLocationLatLong) {
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL_STREETS));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocationLatLong));
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void onLocationChanged() {
        Locations locations = LocationManagerService.getInstance().getCurrentLocation();
        setLocationOnMap(locations);
    }
}