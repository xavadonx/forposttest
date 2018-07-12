package ua.forposttest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

public class ChronologyFragment extends Fragment {

    //    private static List<Fighter> fighters;
    private ListView fightersList;
    private FighterAdapter adapter;

    public static ChronologyFragment newInstance() {
//        fighters = list;
        return new ChronologyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chronology, container, false);
        fightersList = root.findViewById(R.id.fc_fighters_list);

//        while (MainActivity.fighters == null) {
//            try {
//                wait(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        adapter = new FighterAdapter(getContext(), MainActivity.fighters);
        fightersList.setAdapter(adapter);

        return root;
    }
}
