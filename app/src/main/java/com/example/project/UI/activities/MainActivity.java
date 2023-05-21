package com.example.project.UI.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.project.UI.items.shop.ItemSkin;
import com.example.project.databinding.ActivityMainBinding;
import com.example.project.sprites.extensions.BitmapBank;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences sp = getSharedPreferences("shop", Context.MODE_PRIVATE);
        if (!sp.getBoolean(String.valueOf(BitmapBank.PLAYER_0), true)) {
            SharedPreferences.Editor ep = sp.edit();
            ep.putInt("active", BitmapBank.PLAYER_0);
            ep.putBoolean(String.valueOf(BitmapBank.PLAYER_0), true);
            ep.apply();
        }

    }
}