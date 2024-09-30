package fi.masaz.celatum;

import static fi.masaz.celatum.MainActivity.ITEM_ID;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "celatum").allowMainThreadQueries().build();

        int itemId = getIntent().getIntExtra(ITEM_ID, 0);
        Item item = db.itemDao().findById(itemId);

        ImageView mDetailIcon = findViewById(R.id.detail_icon);
        TextView mDetailTitle = findViewById(R.id.detail_title);
        TextView mDetailDescription = findViewById(R.id.detail_description);
        TextView mDetailEdited = findViewById(R.id.detail_edited);
        Button mDetailEdit = findViewById(R.id.detail_edit);

        Date edited = new Date(item.editedTs);

        mDetailIcon.setImageResource(Tools.getIcon(item.icon));
        mDetailTitle.setText(item.title);
        mDetailDescription.setText(item.description);
        mDetailEdited.setText(DateFormat.getDateInstance(DateFormat.LONG).format(edited));
        mDetailEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra(ITEM_ID, item.id);
            startActivity(intent);
        });
    }
}