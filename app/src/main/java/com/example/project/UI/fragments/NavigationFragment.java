package com.example.project.UI.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.UI.Sounds;
import com.example.project.UI.activities.MainActivity;
import com.example.project.UI.fragments.dialog.DialogShop;
import com.example.project.databinding.FragmentNavigationBinding;

public class NavigationFragment extends Fragment implements Sounds {

    FragmentNavigationBinding binding;
    MediaPlayer mPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        binding = FragmentNavigationBinding.bind(view);

        binding.levels.setBackgroundColor(getResources().getColor(R.color.gray, getResources().newTheme()));

        binding.shop.setOnClickListener(v -> openShop());
        binding.arcade.setOnClickListener(v -> openAcrade());
        binding.levels.setOnClickListener(v -> openLevels());

        mPlayer = initSound(getActivity(), R.raw.button_sound);

        return view;
    }

    public void openShop() {
        playSound(mPlayer);
        DialogShop shop = new DialogShop(getParentFragment());
        shop.show(getParentFragmentManager(), "shop");
    }

    public void openAcrade() {
        playSound(mPlayer);
        binding.levels.setBackgroundColor(Color.BLACK);
        binding.arcade.setBackgroundColor(getResources().getColor(R.color.gray, getResources().newTheme()));
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.pageContent, new ArcadeFragment())
                .commit();
    }

    public void openLevels() {
        playSound(mPlayer);
        binding.levels.setBackgroundColor(getResources().getColor(R.color.gray, getResources().newTheme()));
        binding.arcade.setBackgroundColor(Color.BLACK);
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.pageContent, new LevelsFragment())
                .commit();
    }
}