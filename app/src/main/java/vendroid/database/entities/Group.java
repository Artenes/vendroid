package vendroid.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

@Entity
public class Group extends vendroid.database.entities.Entity {

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "code")
    private String code;

    public Group(String code, String description) {
        super();
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
