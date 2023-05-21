package pytobyte.game.jumper.UI.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;

import pytobyte.game.jumper.UI.items.shop.DBWorking;
import pytobyte.game.jumper.batabase.SkinEntity;
import pytobyte.game.jumper.batabase.SkinsDB;
import pytobyte.game.jumper.databinding.ActivityMainBinding;
import pytobyte.game.jumper.sprites.extensions.BitmapBank;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        try {
            DBWorking dbw = new DBWorking();
            dbw.fillDB(getApplicationContext());
            SharedPreferences sp = getSharedPreferences("shop", Context.MODE_PRIVATE);
            SharedPreferences.Editor ep = sp.edit();
            ep.putInt("active", BitmapBank.PLAYER_0);
            ep.apply();
        } catch (Exception er) {}

    }
}