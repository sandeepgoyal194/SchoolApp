package transport.school.com.schoolapp;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.Duration;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import frameworks.appsession.AppBaseApplication;
import frameworks.basemvp.AppBaseFragment;
import frameworks.basemvp.IPresenter;
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
public class MapViewFragment extends AppBaseFragment implements GoogleMap.OnMarkerClickListener {
    private static final String TAG = "SchoolApp";
    MapView mMapView;
    private GoogleMap googleMap;
    private MarkerOptions mMarkerOptions = null;
    private Marker mMarker = null;
    IconGenerator iconFactory;// = new IconGenerator(this);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_fragment, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        iconFactory = new IconGenerator(getContext());// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setOnMarkerClickListener(MapViewFragment.this);
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
                           routestops = stopResponse.getRoutestops();
                           String sequence;
                           if(AppBaseApplication.getApplication().isMorningRoute()) {
                               sequence = stopResponse.getRoute().getMorningsequence();
                           }else {
                               sequence = stopResponse.getRoute().getEveningsequence();
                           }

                           String[] routes = sequence.split(",");

                            List<Routestop> routestopss = new ArrayList<>();
                           for(String route:routes) {
                               routestopss.add(routestops.get(Integer.parseInt(route)));

                               }


                            drawRoute(routestopss);
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

    @Override
    public View getView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    @Override
    public IPresenter getPresenter() {
        return null;
    }

    List<Routestop> routestops = new ArrayList<>();

    public void drawRoute(final List<Routestop> list) {

        final List<LatLng> latLngs = new ArrayList<>();
        for (Routestop routestop : list) {
            latLngs.add(new LatLng(Double.parseDouble(routestop.getLatitude()), Double.parseDouble(routestop.getLongitude())));
        }
        NotificationSender.getmInstance().setRoutestops(routestops);
        Locations location = LocationManagerService.getInstance().getCurrentLocation();
        final LatLng origin = location != null ? new LatLng(location.getLattitude(), location.getLongitude()) : new LatLng(latLngs.get(0).latitude, latLngs.get(0).longitude);
        final LatLng destination = new LatLng(latLngs.get(list.size() - 1).latitude, latLngs.get(list.size() - 1).longitude);
        GoogleDirection.withServerKey("AIzaSyAUmVRXx43uVLZomeU1tRR5OYYkGuW6bew")
                .from(origin)
                .and(latLngs)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        int i = 0;
                        if (direction.isOK()) {
                            com.akexorcist.googledirection.model.Route route = direction.getRouteList().get(0);
                            LatLng origin = new LatLng(latLngs.get(0).latitude, latLngs.get(0).longitude);
                            iconFactory.setColor(Color.GREEN);
                            addIcon(iconFactory, list.get(i).getStopname(), list.get(i++).getStopid(), origin);
                            iconFactory.setColor(Color.RED);
                            addIcon(iconFactory, list.get(list.size() - 1).getStopname(), list.get(list.size() - 1).getStopid(), destination);
                            for (LatLng position : latLngs.subList(1, list.size() - 1)) {
                                iconFactory.setColor(Color.BLUE);
                                addIcon(iconFactory, list.get(i).getStopname(), list.get(i++).getStopid(), position);
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

    private void addIcon(IconGenerator iconFactory, String title, CharSequence text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(title))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        Marker marker = googleMap.addMarker(markerOptions);
        marker.setTag(text);
        marker.setTitle(title);
        markers.add(marker);
    }

    private void setCameraWithCoordinationBounds(com.akexorcist.googledirection.model.Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    void setLocationOnMap(Locations location, float bearing) {
        if (googleMap == null) {
            Log.i(TAG, "Map is null");
            return;
        }
        LatLng currentLocationLatLong = new LatLng(location.getLattitude(), location.getLongitude());
        if (mMarker != null && !mMarker.getPosition().equals(currentLocationLatLong)) {
            if (googleMap != null) {
                googleMap.clear();
            }
        }else {
            if(mMarker != null) {
                return;
            }
        }
        mMarkerOptions = new MarkerOptions().icon(getCarMapIcon(R.drawable.car_icon))/*.rotation(bearing)*/.position(currentLocationLatLong);
        mMarker = googleMap.addMarker(mMarkerOptions);
        drawRoute(routestops);
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

    Locations prevLocations = null;

    public void onLocationChanged(Location location) {
        for(Marker marker:markers) {
            marker.remove();
        }
        markers.clear();
        Locations locations = LocationManagerService.getInstance().getCurrentLocation();
        NotificationSender.getmInstance().setCurrentLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        setLocationOnMap(locations, location.getBearing());
        addmarker(location);
    }

    private void addmarker(Location location) {
        final List<LatLng> latLngs = new ArrayList<>();
        if (routestops.size() <= 0)
            return;
        for (Routestop routestop : routestops) {
            latLngs.add(new LatLng(Double.parseDouble(routestop.getLatitude()), Double.parseDouble(routestop.getLongitude())));
        }
        NotificationSender.getmInstance().setRoutestops(routestops);
        final LatLng origin = new LatLng(latLngs.get(0).latitude, latLngs.get(0).longitude);
        final LatLng destination = new LatLng(latLngs.get(routestops.size() - 1).latitude, latLngs.get(routestops.size() - 1).longitude);
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyAUmVRXx43uVLZomeU1tRR5OYYkGuW6bew")
                .build();
        com.google.maps.model.LatLng latLng1 = new com.google.maps.model.LatLng(location.getLongitude(), location.getLatitude());
        DistanceMatrixApiRequest distanceMatrixApiRequest = DistanceMatrixApi.newRequest(context).origins(latLng1);
        com.google.maps.model.LatLng[] latLng2 = new com.google.maps.model.LatLng[routestops.size()];
        DistanceMatrix matrix = null;
        for (int i = 0; i < routestops.size() && !routestops.get(i).isMessageSent(); i++) {
            latLng2[i] = new com.google.maps.model.LatLng(Double.parseDouble(routestops.get(i).getLatitude()), Double.parseDouble(routestops.get(i).getLongitude()));
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
        for (int i = 0; i < distanceMatrixRow.length; i++) {
            int j = 0;
            DistanceMatrixRow distanceMatrixRoww = distanceMatrixRow[i];
            iconFactory.setColor(Color.GREEN);
            addIcon(iconFactory, getStopName(routestops.get(j), distanceMatrixRoww.elements[j].duration), routestops.get(j++).getStopid(), origin);
            iconFactory.setColor(Color.RED);
            addIcon(iconFactory, getStopName(routestops.get(routestops.size() - 1), distanceMatrixRoww.elements[distanceMatrixRoww.elements.length - 1].duration), routestops.get(routestops.size() - 1).getStopid(), destination);
            for (LatLng position : latLngs.subList(1, routestops.size() - 1)) {
                iconFactory.setColor(Color.BLUE);
                addIcon(iconFactory, getStopName(routestops.get(j), distanceMatrixRoww.elements[j].duration), routestops.get(j++).getStopid(), position);
            }
        }
    }

    public String getStopName(Routestop routestop, Duration duration) {
        String title = "";
        if (routestop != null) {
            title = routestop.getStopname();
        }
        if (duration != null) {
            title += ", " + duration.humanReadable;
        }
        return title;
    }

    private BitmapDescriptor getCarMapIcon(int resourceId) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, getResources().getDimensionPixelSize(R.dimen.car_marker_width), getResources().getDimensionPixelSize(R.dimen.car_marker_height), false);
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent i = new Intent(getContext(), StopStudentAttendance.class);
        i.putExtra(StopStudentAttendance.STOP_ID, marker.getTag().toString());
        i.putExtra(StopStudentAttendance.STOP_NAME, marker.getTitle());
        startActivity(i);
        return false;
    }

    List<Marker> markers = new ArrayList<>();
}