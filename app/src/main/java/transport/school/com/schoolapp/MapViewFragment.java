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

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
                        Toast.makeText(getContext(), "Evening Route Not Available", Toast.LENGTH_LONG).show();
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
                            drawRoute(latLngs);
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

    public void drawRoute(final List<LatLng> list) {
        final LatLng origin = new LatLng(list.get(0).latitude, list.get(0).longitude);
        final LatLng destination = new LatLng(list.get(list.size() - 1).latitude, list.get(list.size() - 1).longitude);
        GoogleDirection.withServerKey("AIzaSyAUmVRXx43uVLZomeU1tRR5OYYkGuW6bew")
                .from(origin)
                .and(list)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            com.akexorcist.googledirection.model.Route route = direction.getRouteList().get(0);
                            googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(origin));
                            googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(destination));
                            for (LatLng position : list.subList(1,list.size()-1)) {
                                googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).position(position));
                            }
                            for (Leg leg : route.getLegList()) {
                                //List<Step> stepList = leg.getStepList();
                                PolylineOptions polylineOptions = DirectionConverter.createPolyline(getContext(), leg.getDirectionPoint(), 5, Color.RED);
                                googleMap.addPolyline(polylineOptions);
                            }
                            setCameraWithCoordinationBounds(route);
                        } else {
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                    }
                });
    }

    private void setCameraWithCoordinationBounds(com.akexorcist.googledirection.model.Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
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
        //setZoomLevel(currentLocationLatLong);
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