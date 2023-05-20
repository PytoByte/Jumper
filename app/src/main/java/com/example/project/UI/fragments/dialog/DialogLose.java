package com.example.project.UI.fragments.dialog;

import static android.content.Context.MODE_PRIVATE;
import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.R;
import com.example.project.databinding.FragmentDialogLoseBinding;
import com.example.project.game.draw.DrawView;

public class DialogLose extends DialogFragment {

    FragmentDialogLoseBinding binding;

    public DrawView drawView;
    public Fragment fragment;

    public DialogLose(DrawView drawView, Fragment fragment) {
        this.drawView = drawView;
        this.fragment = fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_lose, container, false);
        binding = FragmentDialogLoseBinding.bind(view);

        binding.continueButton.setOnClickListener(v -> restart());
        binding.exitButton.setOnClickListener(v -> toMainFragment());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("points", MODE_PRIVATE);
        binding.score.setText("Счёт: "+sharedPreferences.getInt("count", 0));

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