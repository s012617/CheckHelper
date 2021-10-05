package tw.com.gbtech.checkhelper.db;

import android.content.Context;

import androidx.room.Room;


public class DBHelper {
    AppDatabase appDatabase;

    public DBHelper(Context context) {
        appDatabase = Room.databaseBuilder(context,AppDatabase.class,"Check").allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
