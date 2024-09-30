package fi.masaz.celatum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.Executor;

import fi.masaz.celatum.room.AppDatabase;
import fi.masaz.celatum.room.Item;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    public static String ITEM_ID = "item_id";

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private RecyclerView mItemList;
    private ItemAdapter itemAdapter;
    private AppDatabase db;
    private String tag = "celatum-main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);

        TextView mNoItems = findViewById(R.id.main_no_items);

        mItemList = findViewById(R.id.item_list);
        mItemList.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "celatum").allowMainThreadQueries().build();
        List<Item> items = db.itemDao().getAll();

        mNoItems.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);

        itemAdapter = new ItemAdapter(items, this);
        mItemList.setAdapter(itemAdapter);

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        //promptInfo = new BiometricPrompt.PromptInfo.Builder()
        //        .setTitle("Biometric login for my app")
        //        .setSubtitle("Log in using your biometric credential")
        //        .setNegativeButtonText("Use account password")
        //        .build();
        //biometricPrompt.authenticate(promptInfo);

        FloatingActionButton addItem = findViewById(R.id.btn_add);
        addItem.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra(ITEM_ID, 0);
            startActivity(intent);
        });
    }

    @Override
    public void onItemClick(Item item) {
        Log.i(tag, "Item " + item.id + ":" + item.title + ":" + item.description + " clicked");

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(ITEM_ID, item.id);
        startActivity(intent);
    }
}