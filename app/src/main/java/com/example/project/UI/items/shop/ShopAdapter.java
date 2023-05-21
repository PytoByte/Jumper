package com.example.project.UI.items.shop;

import static android.content.Context.MODE_PRIVATE;
import static androidx.navigation.fragment.FragmentKt.findNavController;

import static com.example.project.UI.items.shop.ShopAdapter.ViewHolder.holders;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.UI.Sounds;
import com.example.project.UI.activities.MainActivity;
import com.example.project.UI.items.level.Level;

import java.util.ArrayList;
import java.util.List;

public class ShopAdapter  extends RecyclerView.Adapter<ShopAdapter.ViewHolder> implements Sounds {
    private final LayoutInflater inflater;
    private final List<Skin> skins;
    private final Fragment fragment;
    MediaPlayer mPlayer;
    MediaPlayer mPlayerBuy;

    public ShopAdapter(Context context, List<Skin> skins, Fragment fragment) {
        holders.clear();
        this.skins = skins;
        this.inflater = LayoutInflater.from(context);
        this.fragment = fragment;
        mPlayer = initSound(fragment.getActivity(), R.raw.button_sound);
        mPlayerBuy = initSound(fragment.getActivity(), R.raw.buy_sound);
    }

    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_item_skin, parent, false);
        return new ShopAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShopAdapter.ViewHolder holder, int position) {
        holders.add(holder);
        Skin skin = skins.get(position);
        holder.skinImage.setImageResource(skin.skinID);
        holder.skinImage.setTag(skin.skinID);
        updateStatus(skin.skinID, holder);
    }

    @Override
    public int getItemCount() {
        return skins.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public static ArrayList<ViewHolder> holders = new ArrayList<>();
        final LinearLayout base;
        final ImageView skinImage;
        final Button buyUseButton;
        final TextView moneyCount;
        ViewHolder(View view){
            super(view);
            skinImage = view.findViewById(R.id.skin);
            buyUseButton = view.findViewById(R.id.buy_use_button);
            base = view.findViewById(R.id.base);
            moneyCount = view.findViewById(R.id.moneyCount);
        }
    }

    public void buy(int id, ViewHolder holder) {
        String tag = String.valueOf(id);
        SharedPreferences sp = fragment.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        int money = sp.getInt("money", 0);
        int cost = Prices.getPrice(id);
        if (money>=cost) {
            playSound(mPlayerBuy);
            SharedPreferences.Editor ep = sp.edit();
            ep.putInt("money", money-cost);
            ep.apply();

            TextView moneyCount = fragment.getActivity().findViewById(R.id.moneyCount);
            moneyCount.setText(String.valueOf(money-cost));

            sp = fragment.getActivity().getSharedPreferences("shop", Context.MODE_PRIVATE);
            ep = sp.edit();
            ep.putBoolean(tag, true);
            ep.apply();
            updateStatus(id, holder);
        } else {
            Toast.makeText(fragment.getContext(), "Недостаточно средств", Toast.LENGTH_SHORT);
        }
    }

    public void use(int id, ViewHolder holder) {
        playSound(mPlayer);
        String tag = String.valueOf(id);
        SharedPreferences sp = fragment.getActivity().getSharedPreferences("shop", Context.MODE_PRIVATE);
        SharedPreferences.Editor pe = sp.edit();
        pe.putInt("active", id);
        pe.apply();
        updateAll();
    }

    public void updateStatus(int id, ViewHolder holder) {
        String tag = String.valueOf(id);
        SharedPreferences sp = fragment.getActivity().getSharedPreferences("shop", Context.MODE_PRIVATE);
        if (sp.getInt("active", -1)==id) {
            holder.base.setBackgroundColor(fragment.getResources().getColor(R.color.gray, fragment.getResources().newTheme()));
            holder.buyUseButton.setText("Используется");
            holder.buyUseButton.setActivated(false);
        } else {
            holder.base.setBackgroundColor(Color.BLACK);
            holder.buyUseButton.setActivated(true);

            if (sp.getBoolean(tag, false)) {
                holder.buyUseButton.setText("Использовать");
                holder.buyUseButton.setOnClickListener(v -> use(id, holder));
            } else {
                int price = Prices.getPrice(id);
                holder.buyUseButton.setText("Купить: "+price);
                holder.buyUseButton.setOnClickListener(v -> buy(id, holder));
            }
        }
    }

    public void updateAll() {
        for (ShopAdapter.ViewHolder holder : holders) {
            updateStatus((int)holder.skinImage.getTag(), holder);
        }
    }
}
