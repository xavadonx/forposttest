package ua.forposttest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.List;
import java.util.Objects;

import static ua.forposttest.MainActivity.fighters;

public class MapFragment extends Fragment {

    private MapView mapView;
    private DatabaseReference mDatabase;
    private List<Marker> markers;
    private CameraPosition position;
    private MapboxMap mapboxMap;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        Mapbox.getInstance(Objects.requireNonNull(getContext()), getString(R.string.access_token));
        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (fighters != null)
            addMarkers();
    }

    private void getDialog() {
        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                for (Fighter fighter : fighters) {
                    if (Integer.parseInt(marker.getTitle()) == fighter.id) {
                        String message = "health " + fighter.health + "\n" +
                                "type " + fighter.type + "\n" +
                                "clips " + fighter.clips + "\n" +
                                "ammo " + fighter.ammo;

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(message)
                                .setTitle("team " + fighter.team);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return false;
                    }
                }
                return false;
            }
        });
    }

    public void addMarkers() {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                MapFragment.this.mapboxMap = mapboxMap;
                getDialog();
                markers = mapboxMap.getMarkers();

                if (fighters != null) {
                    for (Fighter fighter : fighters) {
                        boolean sign = true;

                        for (Marker m : markers) {
                            if (Integer.parseInt(m.getTitle()) == fighter.id) {
                                m.setPosition(new LatLng(fighter.position_lat, fighter.position_lon));

                                sign = false;
                                break;
                            }
                        }

                        if (sign) {
                            mapboxMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(fighter.position_lat, fighter.position_lon))
                                    .title(String.valueOf(fighter.id))
                            );
                        }
                    }

                    position = new CameraPosition.Builder()
                            .target(new LatLng(fighters.get(0).position_lat, fighters.get(0).position_lon))
                            .zoom(13)
                            .tilt(20)
                            .build();
                } else {
                    position = new CameraPosition.Builder()
                            .target(new LatLng(49.989822, 36.356495))
                            .zoom(13)
                            .tilt(20)
                            .build();
                }
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            }
        });
    }
}
