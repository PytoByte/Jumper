package com.example.project.UI.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.UI.activities.MainActivity;
import com.example.project.R;
import com.example.project.databinding.FragmentPreviewBinding;

public class PreviewFragment extends Fragment {
    FragmentPreviewBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview, container, false);

        binding = FragmentPreviewBinding.bind(view);
        binding.play.setOnClickListener(v -> startGame());

        return view;
    }

    private void startGame() {
        if (requireActivity() instanceof MainActivity) {
            findNavController(this).navigate(R.id.action_fragment_preview_to_mainFragment);
        }
    }
}