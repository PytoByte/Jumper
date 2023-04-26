package com.example.project;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.project.databinding.FragmentPreviewBinding;

public class Preview extends Fragment {
    FragmentPreviewBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview, container, false);

        binding = FragmentPreviewBinding.bind(view);
        binding.play.setOnClickListener(v -> startGame());

        return view;

        //return inflater.inflate(R.layout.fragment_preview, container, false);

        //return inflater.inflate(R.layout.fragment_preview, container, false);
    }

    private void startGame() {
        if (requireActivity() instanceof MainActivity) {
            findNavController(this).navigate(R.id.action_preview2_to_game);
        }
    }
}