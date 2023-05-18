package com.example.project.UI.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.R;
import com.example.project.UI.activities.MainActivity;
import com.example.project.databinding.FragmentNavigationBinding;

public class NavigationFragment extends Fragment {

    FragmentNavigationBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        binding = FragmentNavigationBinding.bind(view);

        binding.arcade.setOnClickListener(v -> openAcrade());
        binding.levels.setOnClickListener(v -> openLevels());

        return view;
    }

    public void openAcrade() {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.pageContent, new ArcadeFragment())
                .commit();
    }

    public void openLevels() {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.pageContent, new LevelsFragment())
                .commit();
    }
}