package com.example.project.UI.items.shop;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.UI.activities.MainActivity;
import com.example.project.databinding.FragmentItemLevelBinding;
import com.example.project.databinding.FragmentItemSkinBinding;

public class ItemSkin extends Fragment implements Prices {

    FragmentItemSkinBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_skin, container, false);

        binding = FragmentItemSkinBinding.bind(view);

        return view;
    }
}