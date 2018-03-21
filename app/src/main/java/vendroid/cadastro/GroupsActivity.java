package vendroid.cadastro;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import dipro.vendasandroid.R;
import vendroid.database.AppDatabase;
import vendroid.database.entities.Code;
import vendroid.database.entities.Group;
import vendroid.database.repositories.CodeRepository;

public class GroupsActivity extends Activity {

    private EditText mEdtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups_layout);
        mEdtDescription = (EditText) findViewById(R.id.edtDescription);
        setTitle(R.string.groups);
    }

    public boolean save() {

        if (!isValid())
            return false;

        insertToDB();

        Toast.makeText(this, getString(R.string.register_saved), Toast.LENGTH_SHORT).show();

        return true;

    }

    public boolean isValid() {

        if (mEdtDescription.getText().length() == 0) {
            mEdtDescription.setError(getString(R.string.required_field));
            return false;
        }

        return true;

    }

    public void insertToDB() {

        Code code = CodeRepository.makeCode(this);
        Group group = new Group(code.getCode(), mEdtDescription.getText().toString());
        AppDatabase.getInstance(this).groupDAO().insert(group);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crud, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemSave:
                if (save()) {
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppDatabase.getInstance(this).close();
    }

}
