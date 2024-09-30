package fi.masaz.celatum;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.Date;

import fi.masaz.celatum.room.AppDatabase;
import fi.masaz.celatum.room.Item;

public class EditActivity extends AppCompatActivity {
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "celatum").allowMainThreadQueries().build();

        EditText mEditTitle = findViewById(R.id.edit_title);
        EditText mEditDescription = findViewById(R.id.edit_description);
        Button mEditSave = findViewById(R.id.edit_save);

        Item item;
        int itemId = getIntent().getIntExtra(MainActivity.ITEM_ID, 0);

        if (itemId > 0) {
            item = db.itemDao().findById(itemId);
            mEditTitle.setText(item.title);
            mEditDescription.setText(item.description);
        }
        else {
            item = new Item();
        }

        mEditSave.setOnClickListener(v -> {
            item.title = mEditTitle.getText().toString();
            item.description = mEditDescription.getText().toString();
            item.editedTs = new Date().getTime();

            if (item.id > 0) {
                db.itemDao().update(item);
            }
            else {
                db.itemDao().insert(item);
            }
        });
    }
}