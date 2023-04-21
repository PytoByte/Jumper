package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import android.view.View;

import com.example.project.databinding.ActivityMainBinding;
import com.example.project.databinding.FragmentPreviewBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        binding.exit.setOnClickListener(v -> finish());
        binding.play.setOnClickListener(v -> startGame());
    }

    private void startGame() {
        FragmentPreviewBinding fragmentPreviewBinding;

        fragmentPreviewBinding = FragmentPreviewBinding.inflate(getLayoutInflater());

        Fragment fragment = new Game();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(fragmentPreviewBinding.getRoot().getId(), fragment);
        ft.commit();
    }
}