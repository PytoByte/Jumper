package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.project.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {
    ActivityGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        DrawView dv = new DrawView(this);

        setContentView(dv);
    }
}