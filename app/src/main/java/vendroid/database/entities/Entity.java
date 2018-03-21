package vendroid.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;

@android.arch.persistence.room.Entity
public abstract class Entity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    @ColumnInfo(name = "updated_at")
    private long updatedAt;

    public Entity() {
        fillTimestamps();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void fillTimestamps() {
        Calendar now = Calendar.getInstance();
        setCreatedAt(now.getTimeInMillis());
        setUpdatedAt(now.getTimeInMillis());
    }

}
