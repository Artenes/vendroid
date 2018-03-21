package vendroid.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import vendroid.database.entities.Group;

@Dao
public interface GroupDAO {

    @Insert
    void insert(Group group);

    @Query("SELECT * FROM `group`")
    List<Group> getAll();

}
