package ua.forposttest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static final String SHAREDPREFS = "ua.forposttest";
    public static final String FIGHTER_RECORD = "fighter_record";

    public static List<Fighter> fighters;
    public static List<FighterRec> fightRecord = new ArrayList<>();

    private DatabaseReference mDatabase;
    private long currTime;
    private long currTimeTMP;

    private Gson gson;

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gson = new Gson();

        showFragments();

        getDataFromFB();

//        startPlay();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void getDataFromFB() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("fighters").addValueEventListener(eventListener);
    }

    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            GenericTypeIndicator<List<Fighter>> indicator = new GenericTypeIndicator<List<Fighter>>() {
            };
            fighters = dataSnapshot.getValue(indicator);

            if (fighters == null) return;

            currTime = System.currentTimeMillis();
            if (currTimeTMP == 0)
                currTimeTMP = currTime;

            FighterRec fighterRec = new FighterRec(fighters, currTime - currTimeTMP);
            fightRecord.add(fighterRec);

            currTimeTMP = currTime;

            checkFragments();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void checkFragments() {
        Fragment fragment1 = getSupportFragmentManager().findFragmentById(R.id.container_top);
        if (fragment1 != null && fragment1 instanceof MapFragment) {
            ((MapFragment) fragment1).addMarkers();
        }

        Fragment fragment2 = getSupportFragmentManager().findFragmentById(R.id.container_bottom);
        if (fragment2 != null && fragment2 instanceof ChronologyFragment) {
            ((ChronologyFragment) fragment2).initAdapter();
        }

        Fragment fragment3 = getSupportFragmentManager().findFragmentById(R.id.container_left);
        if (fragment3 != null && fragment3 instanceof MapFragment) {
            ((MapFragment) fragment3).addMarkers();
        }

        Fragment fragment4 = getSupportFragmentManager().findFragmentById(R.id.container_right);
        if (fragment4 != null && fragment4 instanceof ChronologyFragment) {
            ((ChronologyFragment) fragment4).initAdapter();
        }
    }

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

    private void startPlay() {
        mDatabase.child("fighters").removeEventListener(eventListener);
        getRec();

        Object lock = new Object();

        for (FighterRec fighterRec : fightRecord) {
            fighters = fighterRec.getFighters();

            synchronized (lock) {
                try {
                    lock.wait(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            checkFragments();
//            mTimer = new Timer();
//            mMyTimerTask = new MyTimerTask();
//
//            mTimer.schedule(mMyTimerTask, 500); // fighterRec.getTimeBetweenSteps());
        }
    }

    private void getRec() {
        SharedPreferences sp = getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);
        String json = sp.getString(FIGHTER_RECORD, "Unknown");

        if (!json.equals("Unknown")) {
            Type listType = new TypeToken<List<FighterRec>>() {
            }.getType();

            fightRecord = gson.fromJson(json, listType);
        }
    }

    private void setRec() {
        SharedPreferences sp = getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(FIGHTER_RECORD, gson.toJson(fightRecord));
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.child("fighters").removeEventListener(eventListener);
        setRec();
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(() -> {
                checkFragments();
            });
        }
    }
}
