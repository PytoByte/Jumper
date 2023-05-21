package pytobyte.game.jumper.UI.items.shop;

import android.content.Context;

import java.util.ArrayList;

import pytobyte.game.jumper.R;
import pytobyte.game.jumper.batabase.SkinEntity;
import pytobyte.game.jumper.batabase.SkinsDB;
import pytobyte.game.jumper.sprites.extensions.BitmapBank;

public class DBWorking implements BitmapBank {
    public void fillDB(Context context) {
        SkinsDB db = SkinsDB.getDatabase(context.getApplicationContext());

        try {db.getSkinDao().insertAll((new SkinEntity(BitmapBank.PLAYER_0, true, 0)));} catch (Exception er) {}
        try {db.getSkinDao().insertAll((new SkinEntity(BitmapBank.PLAYER_1, false, 10)));} catch (Exception er) {}
        try {db.getSkinDao().insertAll((new SkinEntity(BitmapBank.PLAYER_2, false, 25)));} catch (Exception er) {}
        try {db.getSkinDao().insertAll((new SkinEntity(BitmapBank.PLAYER_3, false, 50)));} catch (Exception er) {}
        try {db.getSkinDao().insertAll((new SkinEntity(BitmapBank.PLAYER_4, false, 75)));} catch (Exception er) {}
        try {db.getSkinDao().insertAll((new SkinEntity(BitmapBank.PLAYER_5, false, 100)));} catch (Exception er) {}
        try {db.getSkinDao().insertAll((new SkinEntity(BitmapBank.PLAYER_6, false, 1000)));} catch (Exception er) {}

    }
}
