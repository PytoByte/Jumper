package pytobyte.game.jumper.batabase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {SkinEntity.class}, version = 1)
public abstract class SkinsDB extends RoomDatabase {
    private static final String Database_Name = "SkinsDB.db";
    private static SkinsDB skinsDB;

    public static synchronized SkinsDB getDatabase(Context context) {
        if (skinsDB == null) {
            skinsDB = Room.databaseBuilder(context.getApplicationContext(), SkinsDB.class, Database_Name).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return skinsDB;
    }

    public abstract SkinDao getSkinDao();
}
