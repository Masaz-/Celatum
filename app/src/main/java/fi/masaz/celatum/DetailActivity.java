package fi.masaz.celatum;

import static fi.masaz.celatum.MainActivity.ITEM_ID;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.text.DateFormat;
import java.util.Date;

import fi.masaz.celatum.room.AppDatabase;
import fi.masaz.celatum.room.Item;

public class DetailActivity extends AppCompatActivity {
    private String tag = "celatum-detail";
    private AppDatabase db;
    private ImageView mDetailIcon;
    private TextView mDetailTitle;
    private TextView mDetailDescription;
    private TextView mDetailEdited;
    private Button mDetailEdit;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "celatum").allowMainThreadQueries().build();

        mDetailIcon = findViewById(R.id.detail_icon);
        mDetailTitle = findViewById(R.id.detail_title);
        mDetailDescription = findViewById(R.id.detail_description);
        mDetailEdited = findViewById(R.id.detail_edited);
        mDetailEdit = findViewById(R.id.detail_edit);

        mDetailEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra(ITEM_ID, item.id);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(tag, "onResume()");

        int itemId = getIntent().getIntExtra(ITEM_ID, 0);
        item = db.itemDao().findById(itemId);

        if (item == null) {
            finish();
            return;
        }

        Date edited = new Date(item.editedTs);

        mDetailIcon.setImageResource(Tools.getIcon(item.icon));
        mDetailTitle.setText(item.title);
        mDetailDescription.setText(item.description);
        mDetailEdited.setText(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(edited));
    }
}