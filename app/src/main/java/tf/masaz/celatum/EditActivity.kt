package tf.masaz.celatum

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room.databaseBuilder
import tf.masaz.celatum.MainActivity.Companion.ITEM_ID
import tf.masaz.celatum.room.AppDatabase
import tf.masaz.celatum.room.Item
import tf.masaz.celatum.secret.Secret
import java.util.Date

class EditActivity : AppCompatActivity() {
    private val tag = "celatum-edit"
    private var db: AppDatabase? = null
    private var mEditTitle: EditText? = null
    private var mEditDescription: EditText? = null
    private var item: Item? = null
    private var selected: Int? = null
    private var secret: Secret? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_edit)

        mEditTitle = findViewById(R.id.edit_title)
        mEditDescription = findViewById(R.id.edit_description)

        val mEditSave = findViewById<Button>(R.id.edit_save)
        val mEditDelete = findViewById<Button>(R.id.edit_delete)

        secret = Secret()

        db = databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "celatum"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        mEditSave.setOnClickListener {
            val titlePair:Pair<String, ByteArray> = secret?.encryptText(mEditTitle!!.getText().toString())!!
            val descPair:Pair<String, ByteArray> = secret?.encryptText(mEditDescription!!.getText().toString())!!

            item!!.icon = findViewById<ImageButton>(selected!!).tag.toString()
            item!!.title = titlePair.first
            item!!.titleIV = titlePair.second
            item!!.description = descPair.first
            item!!.descriptionIV = descPair.second
            item!!.editedTs = Date().time

            if (item!!.id > 0) {
                db!!.itemDao()?.update(item!!)

                finish()
            }
            else {
                item!!.createdTs = Date().time
                db!!.itemDao()?.insert(item!!)

                finish()

                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra(ITEM_ID, item!!.id)
                startActivity(intent)
            }
        }

        mEditDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_item))
                .setMessage(getString(R.string.are_you_sure_delete_item))
                .setIcon(R.drawable.round_warning_amber_24)
                .setPositiveButton(R.string.yes) { _: DialogInterface?, _: Int ->
                    val itemTitle = secret?.decryptText(item!!.title!!, item!!.titleIV!!)
                    db!!.itemDao()?.delete(item!!)

                    Toast.makeText(this, String.format(getString(R.string.item_deleted), itemTitle), Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton(R.string.no, null).show()
        }
    }

    public override fun onResume() {
        super.onResume()
        Log.i(tag, "onResume()")

        val itemId = intent.getIntExtra(ITEM_ID, 0)

        if (itemId > 0) {
            item = db!!.itemDao()?.findById(itemId)
            findViewById<Button>(R.id.edit_delete).visibility = View.VISIBLE

            mEditTitle!!.setText(secret?.decryptText(item!!.title!!, item!!.titleIV!!))
            mEditDescription!!.setText(secret?.decryptText(item!!.description!!, item!!.descriptionIV!!))
        }
        else {
            item = Item()
            findViewById<Button>(R.id.edit_delete).visibility = View.INVISIBLE

            mEditTitle!!.setText("")
            mEditDescription!!.setText("")
        }

        selected = getTypeButtonId(item?.icon)
        onClickIconButton(findViewById(selected!!))
    }

    fun onClickIconButton(view:View) {
        val btn = view as ImageButton

        findViewById<ImageButton>(selected!!).setBackgroundResource(R.drawable.toggle_button_bg)

        when (btn.id) {
            R.id.type_note -> btn.setBackgroundResource(R.drawable.toggle_button_active_bg)
            R.id.type_card -> btn.setBackgroundResource(R.drawable.toggle_button_active_bg)
            R.id.type_key -> btn.setBackgroundResource(R.drawable.toggle_button_active_bg)
            R.id.type_account -> btn.setBackgroundResource(R.drawable.toggle_button_active_bg)
            R.id.type_link -> btn.setBackgroundResource(R.drawable.toggle_button_active_bg)
        }

        selected = btn.id
    }

    private fun getTypeButtonId(iconStr: String?): Int {
        var id = R.id.type_note

        if (iconStr != null) {
            when (iconStr) {
                "credit_card" -> id = R.id.type_card
                "key" -> id = R.id.type_key
                "person" -> id = R.id.type_account
                "link" -> id = R.id.type_link
            }
        }

        return id
    }
}