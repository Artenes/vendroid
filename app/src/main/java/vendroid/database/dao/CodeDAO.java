package vendroid.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import vendroid.database.entities.Code;

@Dao
public interface CodeDAO {

    @Insert
    void insert(Code code);

    @Query("SELECT * FROM code WHERE code = :code")
    Code findByCode(String code);

}
