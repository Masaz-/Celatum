package fi.masaz.celatum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room.databaseBuilder
import fi.masaz.celatum.room.AppDatabase
import fi.masaz.celatum.room.Item
import java.text.DateFormat
import java.util.Date

class DetailActivity : AppCompatActivity() {
    private val tag = "celatum-detail"
    private var db: AppDatabase? = null
    private var mDetailIcon: ImageView? = null
    private var mDetailTitle: TextView? = null
    private var mDetailDescription: TextView? = null
    private var mDetailEdited: TextView? = null
    private var mDetailEdit: Button? = null
    private var item: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        db = databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "celatum"
        ).allowMainThreadQueries().build()

        mDetailIcon = findViewById(R.id.detail_icon)
        mDetailTitle = findViewById(R.id.detail_title)
        mDetailDescription = findViewById(R.id.detail_description)
        mDetailEdited = findViewById(R.id.detail_edited)
        mDetailEdit = findViewById(R.id.detail_edit)

        mDetailEdit?.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra(MainActivity.ITEM_ID, item!!.id)
            startActivity(intent)
        })
    }

    public override fun onResume() {
        super.onResume()
        Log.i(tag, "onResume()")

        val itemId = intent.getIntExtra(MainActivity.ITEM_ID, 0)
        item = db!!.itemDao().findById(itemId)

        if (item == null) {
            finish()
            return
        }

        val edited = Date(item!!.editedTs)

        mDetailIcon!!.setImageResource(Tools.getIcon(item!!.icon))
        mDetailTitle!!.text = item!!.title
        mDetailDescription!!.text = item!!.description
        mDetailEdited!!.text = DateFormat.getDateTimeInstance(
            DateFormat.LONG,
            DateFormat.SHORT
        ).format(edited)
    }
}