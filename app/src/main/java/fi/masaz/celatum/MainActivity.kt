package fi.masaz.celatum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room.databaseBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fi.masaz.celatum.room.AppDatabase
import fi.masaz.celatum.room.Item
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity(), OnItemClickListener {
    private val tag = "celatum-main"
    private var executor: Executor? = null
    private var biometricPrompt: BiometricPrompt? = null
    private var promptInfo: PromptInfo? = null
    private var mItemList: RecyclerView? = null
    private var itemAdapter: ItemAdapter? = null
    private var db: AppDatabase? = null
    private var items: List<Item>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_main)

        val mNoItems = findViewById<TextView>(R.id.main_no_items)
        val addItem = findViewById<FloatingActionButton>(R.id.btn_add)

        mItemList = findViewById(R.id.item_list)
        mItemList?.setLayoutManager(LinearLayoutManager(this))

        db = databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "celatum"
        ).allowMainThreadQueries().build()

        items = db!!.itemDao()!!.all

        mNoItems.visibility = if (items!!.isEmpty()) View.VISIBLE else View.GONE

        itemAdapter = ItemAdapter(this)
        mItemList?.adapter = itemAdapter

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(
            this@MainActivity,
            executor!!,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext, "Authentication succeeded!", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            })

        promptInfo = PromptInfo.Builder()
            .setTitle(getString(R.string.app_name))
            .setSubtitle(getString(R.string.use_biometric_to_access_data))
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()

        biometricPrompt!!.authenticate(promptInfo!!)

        addItem.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra(ITEM_ID, 0)
            startActivity(intent)
        }
    }

    public override fun onResume() {
        super.onResume()
        Log.i(tag, "onResume()")

        loadList()
    }

    override fun onItemClick(item: Item) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(ITEM_ID, item.id)
        startActivity(intent)
    }

    private fun loadList() {
        items = db!!.itemDao()!!.all
        itemAdapter!!.setItemList(items!!)
        findViewById<TextView>(R.id.main_no_items).visibility = if (items!!.isEmpty()) View.VISIBLE else View.GONE
    }

    companion object {
        var ITEM_ID: String = "item_id"
    }
}