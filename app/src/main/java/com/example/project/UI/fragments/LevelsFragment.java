package com.example.project.UI.fragments;

import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.UI.items.level.Level;
import com.example.project.UI.items.level.LevelAdapter;
import com.example.project.R;
import com.example.project.databinding.FragmentLevelsBinding;

import java.io.IOException;
import java.util.ArrayList;

public class LevelsFragment extends Fragment {

    FragmentLevelsBinding binding;
    ArrayList<Level> levels = new ArrayList<Level>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_levels, container, false);
        binding = FragmentLevelsBinding.bind(view);

        fillLevels();

        RecyclerView recyclerView = binding.levelsList;
        LevelAdapter adapter = new LevelAdapter(getContext(), levels, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void fillLevels() {
        levels.clear();
        AssetManager assetManager = getActivity().getAssets();
        try {
            for (String s : assetManager.list("levels")) {
                levels.add( new Level(s.substring(0, (s.length()-5))) );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}