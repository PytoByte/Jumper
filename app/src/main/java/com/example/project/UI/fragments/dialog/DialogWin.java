package com.example.project.UI.fragments.dialog;

import static android.content.Context.MODE_PRIVATE;
import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.DefaultLifecycleObserver;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.R;
import com.example.project.databinding.FragmentDialogLoseBinding;
import com.example.project.databinding.FragmentDialogWinBinding;
import com.example.project.game.draw.DrawView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.function.Predicate;

public class DialogWin extends DialogFragment {
    FragmentDialogWinBinding binding;

    public DrawView drawView;
    public Fragment fragment;

    public DialogWin(DrawView drawView, Fragment fragment) {
        this.drawView = drawView;
        this.fragment = fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_win, container, false);
        binding = FragmentDialogWinBinding.bind(view);

        String nl = getNextLevel();
        if (nextLevelCheck(nl)) {
            binding.continueButton.setOnClickListener(v -> nextLevel(nl));
        } else {
            binding.getRoot().removeView(binding.continueButton);
        }
        binding.restartButton.setOnClickListener(v -> restart());
        binding.exitButton.setOnClickListener(v -> toMainFragment());

        return view;
    }

    public String getNextLevel() {
        SharedPreferences sharedPreferences = fragment.getActivity().getSharedPreferences("level", MODE_PRIVATE);
        String levelName = "";
        try {
            levelName = String.valueOf(Integer.parseInt(sharedPreferences.getString("name", null)) + 1);
        } catch (Throwable er) {
            er.printStackTrace();
            levelName = "0";
        }
        return levelName;
    }

    public boolean nextLevelCheck(String levelName) {
        AssetManager am = fragment.getActivity().getAssets();
        try {
            String[] files = am.list("levels");
            String finalLevelName = levelName;

            return Arrays.stream(files).anyMatch(new Predicate<String>() {
                @Override
                public boolean test(String s) {
                    System.out.println(s+" "+finalLevelName);
                    return s.equals(finalLevelName+".json");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void nextLevel(String levelName) {
        SharedPreferences sharedPreferences = fragment.getActivity().getSharedPreferences("level", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putString("name", levelName);
        prefEditor.apply();
        restart();
    }

    public void restart() {
        drawView.drawThread.requestStop();
        FragmentManager fm = fragment.getParentFragmentManager();
        fm.beginTransaction()
                .detach(fragment)
                .commit();
        fm.beginTransaction()
                .attach(fragment)
                .commit();
        dismissNow();
    }

    public void toMainFragment() {
        drawView.drawThread.requestStop();
        Log.e("TAG", "toMainFragment: Trying");
        findNavController(fragment).navigate(R.id.action_gameUIFragment_to_mainFragment);
        Log.e("TAG", "toMainFragment: results?");
        dismissNow();
    }
}