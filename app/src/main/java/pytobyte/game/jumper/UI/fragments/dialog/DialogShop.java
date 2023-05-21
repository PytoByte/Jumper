package pytobyte.game.jumper.UI.fragments.dialog;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pytobyte.game.jumper.R;
import pytobyte.game.jumper.UI.Sounds;
import pytobyte.game.jumper.UI.items.shop.ShopAdapter;
import pytobyte.game.jumper.UI.items.shop.Skin;
import pytobyte.game.jumper.databinding.FragmentDialogPauseBinding;
import pytobyte.game.jumper.databinding.FragmentDialogShopBinding;
import pytobyte.game.jumper.sprites.extensions.BitmapBank;

import java.util.ArrayList;

public class DialogShop extends DialogFragment implements Sounds {
    FragmentDialogShopBinding binding;
    ArrayList<Skin> skins = new ArrayList<>();
    Fragment parentFragment;
    MediaPlayer mPlayer;

    public DialogShop(Fragment parentFragment) {
        this.parentFragment = parentFragment;
        mPlayer = initSound(parentFragment.getActivity(), R.raw.button_sound);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_shop, container, false);
        binding = FragmentDialogShopBinding.bind(view);

        fillSkins();

        ShopAdapter adapter = new ShopAdapter(getContext(), skins, parentFragment);
        binding.skinShop.setAdapter(adapter);
        binding.closeShop.setOnClickListener(v -> close());

        return view;
    }

    public void close() {
        playSound(mPlayer, getActivity());
        dismissNow();
    }

    public void fillSkins() {
        skins.clear();
        skins.add(new Skin(BitmapBank.PLAYER_0));
        skins.add(new Skin(BitmapBank.PLAYER_1));
        skins.add(new Skin(BitmapBank.PLAYER_2));
        skins.add(new Skin(BitmapBank.PLAYER_3));
        skins.add(new Skin(BitmapBank.PLAYER_4));
        skins.add(new Skin(BitmapBank.PLAYER_5));
        skins.add(new Skin(BitmapBank.PLAYER_6));
    }
}