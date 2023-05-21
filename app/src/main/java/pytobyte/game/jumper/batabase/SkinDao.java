package pytobyte.game.jumper.batabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SkinDao {
    // Добавление Person в бд
    @Insert
    void insertAll(SkinEntity... people);

    // Удаление Person из бд
    @Delete
    void delete(SkinEntity person);

    // Получение всех Person из бд
    @Query("SELECT * FROM SkinEntity")
    List<SkinEntity> getAllSkin();

    @Query("SELECT * FROM SkinEntity WHERE skinID = :id")
    SkinEntity getSkin(int id);

    @Update
    void update(SkinEntity skin);
}
