package pytobyte.game.jumper.UI.fragments.dialog;

import static android.content.Context.MODE_PRIVATE;
import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
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
import pytobyte.game.jumper.databinding.FragmentDialogLoseBinding;
import pytobyte.game.jumper.databinding.FragmentDialogWinBinding;
import pytobyte.game.jumper.game.draw.DrawView;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;

public class DialogWin extends DialogFragment implements Sounds {
    FragmentDialogWinBinding binding;

    public DrawView drawView;
    public Fragment fragment;

    MediaPlayer mPlayer;

    public DialogWin(DrawView drawView, Fragment fragment) {
        this.drawView = drawView;
        this.fragment = fragment;
        mPlayer = initSound(fragment.getActivity(), R.raw.button_sound);
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

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("points", MODE_PRIVATE);

        int score = sharedPreferences.getInt("count", 0);

        binding.score.setText("Счёт: "+score);

        sharedPreferences = fragment.getActivity().getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putInt("money", sharedPreferences.getInt("money", 0)+score/10);
        prefEditor.apply();

        return view;
    }

    public String getNextLevel() {
        playSound(mPlayer, getActivity());
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
        fragment.onDestroy();
        Log.e("TAG", "toMainFragment: results?");
        dismissNow();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {}
}