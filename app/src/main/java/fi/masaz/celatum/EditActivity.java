package fi.masaz.celatum;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import fi.masaz.celatum.room.AppDatabase;
import fi.masaz.celatum.room.Item;

public class EditActivity extends AppCompatActivity {
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "celatum").allowMainThreadQueries().build();

        int itemId = getIntent().getIntExtra(MainActivity.ITEM_ID, 0);

        if (itemId > 0) {
            Item item = db.itemDao().findById(itemId);
        }
    }
}