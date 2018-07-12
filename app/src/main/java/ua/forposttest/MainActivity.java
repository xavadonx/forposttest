package ua.forposttest;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private MapView mapView;
public static List<Fighter> fighters;

    private DatabaseReference mDatabase;
//    private DataFighters dataFighters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showFragments();
        getDataFromFB();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void getDataFromFB() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("fighters").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Fighter>> indicator = new GenericTypeIndicator<List<Fighter>>() {
                };
                fighters = dataSnapshot.getValue(indicator);
//                dataFighters.addMarkers();

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_top);
                if (fragment != null && fragment instanceof MapFragment) {
                    ((MapFragment) fragment).addMarkers();
                } else {
                    showFragments();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

//    public interface DataFighters {
//        void addMarkers();
//    }

    private void showFragments() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_top, MapFragment.newInstance())
                    .commit();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_bottom, ChronologyFragment.newInstance())
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_left, MapFragment.newInstance())
                    .commit();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_right, ChronologyFragment.newInstance())
                    .commit();
        }
    }

//    private void test() {
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("fighters").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                GenericTypeIndicator<List<Fighter>> indicator = new GenericTypeIndicator<List<Fighter>>() {
//                };
//                fighters = dataSnapshot.getValue(indicator);
//                addMarkers();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

//    private List<Marker> markers;

//    private void addMarkers() {
//
//
//        for (Fighter fighter : fighters) {
//            mapView.getMapAsync(new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(MapboxMap mapboxMap) {
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
//                }
//            });
//        }
//    }
}
