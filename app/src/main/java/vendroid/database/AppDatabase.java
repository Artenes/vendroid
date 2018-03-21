package vendroid.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import vendroid.database.dao.CodeDAO;
import vendroid.database.dao.GroupDAO;
import vendroid.database.entities.Code;
import vendroid.database.entities.Group;

@Database(entities = {Code.class, Group.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public static final String NAME = "new_vendroid.db";

    public static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        //TODO: remove allow main thread queries and fallback to destructive migration
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract CodeDAO codeDAO();

    public abstract GroupDAO groupDAO();

}
