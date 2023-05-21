package pytobyte.game.jumper.UI.items.shop;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pytobyte.game.jumper.R;
import pytobyte.game.jumper.databinding.FragmentItemSkinBinding;

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