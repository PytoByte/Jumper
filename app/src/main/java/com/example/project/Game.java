package com.example.project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.databinding.ActivityGameBinding;

public class Game extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ActivityGameBinding binding = ActivityGameBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);



        DrawView dv = new DrawView(getContext());
        getActivity().setContentView(dv);

    }
}