package ua.forposttest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import static ua.forposttest.MainActivity.fighters;

public class ChronologyFragment extends Fragment {

    private ListView fightersList;
    private FighterAdapter adapter;

    public static ChronologyFragment newInstance() {
        return new ChronologyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chronology, container, false);
        fightersList = root.findViewById(R.id.fc_fighters_list);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        initAdapter();
    }

    public void initAdapter() {
        if (fighters == null) return;

        adapter = new FighterAdapter(getContext(), fighters);
        fightersList.setAdapter(adapter);
    }
}
