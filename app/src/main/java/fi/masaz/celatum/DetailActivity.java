package fi.masaz.celatum;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import fi.masaz.celatum.room.AppDatabase;
import fi.masaz.celatum.room.Item;

public class DetailActivity extends AppCompatActivity {
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "celatum").allowMainThreadQueries().build();

        int itemId = getIntent().getIntExtra(MainActivity.ITEM_ID, 0);
        Item item = db.itemDao().findById(itemId);
    }
}