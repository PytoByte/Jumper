package com.example.project.UI.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.R;
import com.example.project.UI.Sounds;
import com.example.project.UI.fragments.dialog.DialogPause;
import com.example.project.databinding.FragmentGameUiBinding;
import com.example.project.game.draw.DrawView;

public class GameUIFragment extends Fragment implements Sounds {

    DrawView drawView;
    FragmentGameUiBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_game_ui, container, false);
        binding = FragmentGameUiBinding.bind(view);
        binding.optionsButton.setOnClickListener(v -> summonInGameMenu());

        MediaPlayer startSound = initSound(getActivity(), R.raw.start_sound);
        playSound(startSound);

        this.drawView = new DrawView(binding.gameSurface.getHolder(), getContext(), this);
        Log.e("&", "onCreateView: hello?");

        return view;
    }

    public void summonInGameMenu() {
        DialogFragment dialogPause = new DialogPause(drawView, this);
        dialogPause.show(getParentFragmentManager(), "igm");
    }
}