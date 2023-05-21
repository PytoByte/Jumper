package pytobyte.game.jumper.UI.fragments.dialog;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pytobyte.game.jumper.R;
import pytobyte.game.jumper.UI.Sounds;
import pytobyte.game.jumper.databinding.FragmentDialogPauseBinding;
import pytobyte.game.jumper.databinding.FragmentGameUiBinding;
import pytobyte.game.jumper.game.draw.DrawView;

public class DialogPause extends DialogFragment implements Sounds {
    FragmentDialogPauseBinding binding;

    public DrawView drawView;
    public Fragment fragment;

    MediaPlayer mPlayer;

    public DialogPause(DrawView drawView, Fragment fragment) {
        this.drawView = drawView;
        this.fragment = fragment;
        mPlayer = initSound(fragment.getActivity(), R.raw.button_sound);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_pause, container, false);
        binding = FragmentDialogPauseBinding.bind(view);
        drawView.drawThread.requestPause();
        binding.continueButton.setOnClickListener(v -> resume());
        binding.restartButton.setOnClickListener(v -> restart());
        binding.exitButton.setOnClickListener(v -> toMainFragment());

        return view;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        drawView.drawThread.requestResume();
        super.onCancel(dialog);
    }

    public void resume() {
        playSound(mPlayer, getActivity());
        drawView.drawThread.requestResume();
        dismissNow();
    }

    public void restart() {
        playSound(mPlayer, getActivity());
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