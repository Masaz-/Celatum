package tf.masaz.celatum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
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
import tf.masaz.celatum.room.AppDatabase
import tf.masaz.celatum.room.Item
import tf.masaz.celatum.secret.Secret
import java.util.concurrent.Executor
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), OnItemClickListener {
    private val tag = "celatum-main"
    private var executor: Executor? = null
    private var biometricPrompt: BiometricPrompt? = null
    private var promptInfo: PromptInfo? = null
    private var mItemList: RecyclerView? = null
    private var itemAdapter: ItemAdapter? = null
    private var db: AppDatabase? = null
    private var items: List<Item>? = null
    private var authenticated: Boolean = false
    private var secret: Secret? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_main)

        val mNoItems = findViewById<TextView>(R.id.main_no_items)
        val addItem = findViewById<FloatingActionButton>(R.id.btn_add)

        mItemList = findViewById(R.id.item_list)
        mItemList?.setLayoutManager(LinearLayoutManager(this))

        secret = Secret()

        db = databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "celatum"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        mNoItems.visibility = if (items == null || items?.isEmpty() == true) View.VISIBLE else View.GONE

        itemAdapter = ItemAdapter(this, secret!!)
        mItemList?.adapter = itemAdapter

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(
            this@MainActivity,
            executor!!,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, getString(R.string.authentication_error, errString), Toast.LENGTH_SHORT).show()
                    exitProcess(0)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    findViewById<ProgressBar>(R.id.waiting).visibility = View.GONE
                    authenticated = true
                    loadList()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, getString(R.string.authentication_failed), Toast.LENGTH_SHORT).show()
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
        if (authenticated)  {
            items = db!!.itemDao()!!.all
            itemAdapter!!.setItemList(items!!)
            findViewById<TextView>(R.id.main_no_items).visibility = if (items?.isEmpty() == true) View.VISIBLE else View.GONE
        }
        else {
            findViewById<TextView>(R.id.main_no_items).visibility = View.VISIBLE
        }
    }

    companion object {
        var ITEM_ID: String = "item_id"
    }
}