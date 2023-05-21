package com.example.project.UI.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.R;
import com.example.project.UI.Sounds;
import com.example.project.databinding.FragmentMainBinding;

public class MainFragment extends Fragment implements Sounds {

    FragmentMainBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        binding = FragmentMainBinding.bind(view);
        SharedPreferences sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String money = String.valueOf(sp.getInt("money", 0));
        binding.moneyCount.setText(money);

        MediaPlayer sound = initSound(getActivity(), R.raw.enter_game);
        playSound(sound);

        return view;
    }
}