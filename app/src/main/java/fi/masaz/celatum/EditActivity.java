package fi.masaz.celatum;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.Date;

import fi.masaz.celatum.room.AppDatabase;
import fi.masaz.celatum.room.Item;

public class EditActivity extends AppCompatActivity {
    private String tag = "celatum-detail";
    private AppDatabase db;
    private EditText mEditTitle;
    private EditText mEditDescription;
    private Button mEditSave;
    private Button mEditDelete;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "celatum").allowMainThreadQueries().build();

        mEditTitle = findViewById(R.id.edit_title);
        mEditDescription = findViewById(R.id.edit_description);
        mEditSave = findViewById(R.id.edit_save);
        mEditDelete = findViewById(R.id.edit_delete);

        mEditSave.setOnClickListener(v -> {
            item.title = mEditTitle.getText().toString();
            item.description = mEditDescription.getText().toString();
            item.editedTs = new Date().getTime();

            if (item.id > 0) {
                db.itemDao().update(item);
            }
            else {
                item.createdTs = new Date().getTime();
                db.itemDao().insert(item);
            }
        });

        mEditDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete_item))
                    .setMessage(getString(R.string.are_you_sure_delete_item))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                        db.itemDao().delete(item);
                        finish();
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(tag, "onResume()");

        int itemId = getIntent().getIntExtra(MainActivity.ITEM_ID, 0);

        if (itemId > 0) {
            item = db.itemDao().findById(itemId);
            mEditDelete.setVisibility(View.VISIBLE);
        }
        else {
            item = new Item();
            mEditDelete.setVisibility(View.INVISIBLE);
        }

        mEditTitle.setText(item.title);
        mEditDescription.setText(item.description);
    }
}