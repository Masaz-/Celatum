package tf.masaz.celatum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room.databaseBuilder
import tf.masaz.celatum.room.AppDatabase
import tf.masaz.celatum.room.Item
import tf.masaz.celatum.secret.Secret
import java.text.DateFormat
import java.util.Date

class DetailActivity : AppCompatActivity() {
    private val tag = "celatum-detail"
    private var item: Item? = null
    private var secret: Secret? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_detail)

        val mDetailEdit = findViewById<Button>(R.id.detail_edit)
        mDetailEdit?.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra(MainActivity.ITEM_ID, item!!.id)
            startActivity(intent)
        }

        secret = Secret()
    }

    public override fun onResume() {
        super.onResume()
        Log.i(tag, "onResume()")

        val mDetailIcon = findViewById<ImageView>(R.id.detail_icon)
        val mDetailTitle = findViewById<TextView>(R.id.detail_title)
        val mDetailDescription = findViewById<TextView>(R.id.detail_description)
        val mDetailEdited = findViewById<TextView>(R.id.detail_edited)

        val db = databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "celatum"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        val itemId = intent.getIntExtra(MainActivity.ITEM_ID, 0)
        item = db.itemDao()?.findById(itemId)

        if (item == null) {
            finish()
            return
        }

        val edited = Date(item!!.editedTs!!)

        mDetailIcon!!.setImageResource(Tools.getIcon(item!!.icon))
        mDetailTitle!!.text = secret?.decryptText(item!!.title!!, item!!.titleIV!!)
        mDetailDescription!!.text = secret?.decryptText(item!!.description!!, item!!.descriptionIV!!)
        mDetailEdited!!.text = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(edited)
    }
}