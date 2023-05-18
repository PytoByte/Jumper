package com.example.project.UI.fragments.dialog;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.R;
import com.example.project.databinding.FragmentDialogPauseBinding;
import com.example.project.databinding.FragmentGameUiBinding;
import com.example.project.game.draw.DrawThread;
import com.example.project.game.draw.DrawView;

public class DialogPause extends DialogFragment {
    FragmentDialogPauseBinding binding;

    public DrawView drawView;
    public Fragment fragment;

    public DialogPause(DrawView drawView, Fragment fragment) {
        this.drawView = drawView;
        this.fragment = fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_pause, container, false);
        binding = FragmentDialogPauseBinding.bind(view);

        binding.continueButton.setOnClickListener(v -> dismissNow());
        binding.restartButton.setOnClickListener(v -> restart());
        binding.exitButton.setOnClickListener(v -> toMainFragment());

        return view;
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