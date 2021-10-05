package tw.com.gbtech.checkhelper.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import tw.com.gbtech.checkhelper.Dao.CheckDao;
import tw.com.gbtech.checkhelper.Entity.Check;


@Database(entities = {Check.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CheckDao checkDao();
}
