package com.example.project.UI.fragments.dialog;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.R;
import com.example.project.databinding.FragmentDialogOptionsBinding;
import com.example.project.databinding.FragmentDialogPauseBinding;
import com.example.project.game.draw.DrawView;

public class DialogOptions extends Fragment {
    FragmentDialogOptionsBinding binding;

    public Fragment fragment;

    public DialogOptions(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_pause, container, false);
        binding = FragmentDialogOptionsBinding.bind(view);

        return view;
    }
}