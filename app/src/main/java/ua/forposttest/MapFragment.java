package ua.forposttest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.List;
import java.util.Objects;

public class MapFragment extends Fragment { // implements MainActivity.DataFighters {

    private MapView mapView;
    private DatabaseReference mDatabase;
    private List<Marker> markers;
//    private List<Fighter> fighters;

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
        mapView.onResume();
//        getDataFromFB();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

//    private void getDataFromFB() {
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("fighters").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                GenericTypeIndicator<List<Fighter>> indicator = new GenericTypeIndicator<List<Fighter>>() {
//                };
//                MainActivity.fighters = dataSnapshot.getValue(indicator);
//                addMarkers();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    //    @Override
    public void addMarkers() {
//        for (Fighter fighter : fighters) {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                markers = mapboxMap.getMarkers();

                for (Fighter fighter : MainActivity.fighters) {
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
                                .snippet(fighter.team)
                        );
                    }
                }
            }
        });
    }


//                    boolean sign = true;
//                    markers = mapboxMap.getMarkers();
//                    for (Marker m : markers) {
//                        if (Integer.parseInt(m.getTitle()) == fighter.id) {
//                            sign = false;
//                            break;
//                        }
//                    }
//
//                    if (sign) {
//                        mapboxMap.addMarker(new MarkerOptions()
//                                .position(new LatLng(fighter.position_lat, fighter.position_lon))
//                                .title(String.valueOf(fighter.id))
//                                .snippet(fighter.team)
//
//                        );
//                    } else {
//                        for (Marker m : markers) {
//                            if (Integer.parseInt(m.getTitle()) == fighter.id) {
//                                m.setPosition(new LatLng(fighter.position_lat, fighter.position_lon));
//                                break;
//                            }
//                        }
//                    }
//    }
//});
//        }
//        }
}
