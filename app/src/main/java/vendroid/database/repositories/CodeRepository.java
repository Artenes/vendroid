package vendroid.database.repositories;

import android.content.Context;

import vendroid.database.AppDatabase;
import vendroid.database.dao.CodeDAO;
import vendroid.database.entities.Code;

public class CodeRepository {

    public static Code makeCode(Context context) {

        CodeDAO dao = AppDatabase.getInstance(context).codeDAO();

        String code;
        Code codeInstance;
        do {
            code = Integer.toString((int)Math.ceil(100000 + Math.random() * 899999));
            codeInstance = dao.findByCode(code);
        } while (codeInstance != null);

        codeInstance = new Code(code);
        dao.insert(codeInstance);

        return codeInstance;

    }

}
