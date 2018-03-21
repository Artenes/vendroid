package vendroid.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

@Entity
public class Code extends vendroid.database.entities.Entity {

    @ColumnInfo(name = "code")
    private String code;

    public Code(String code) {
        super();
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
