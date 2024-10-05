package fi.masaz.celatum

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room.databaseBuilder
import fi.masaz.celatum.room.AppDatabase
import fi.masaz.celatum.room.Item
import java.util.Date

class EditActivity : AppCompatActivity() {
    private val tag = "celatum-detail"
    private var db: AppDatabase? = null
    private var mEditTitle: EditText? = null
    private var mEditDescription: EditText? = null
    private var mEditSave: Button? = null
    private var mEditDelete: Button? = null
    private var item: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        db = databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "celatum"
        ).allowMainThreadQueries().build()

        mEditTitle = findViewById(R.id.edit_title)
        mEditDescription = findViewById(R.id.edit_description)
        mEditSave = findViewById(R.id.edit_save)
        mEditDelete = findViewById(R.id.edit_delete)

        mEditSave?.setOnClickListener(View.OnClickListener { v: View? ->
            item!!.title = mEditTitle!!.getText().toString()
            item!!.description = mEditDescription!!.getText().toString()
            item!!.editedTs = Date().time

            if (item!!.id > 0) {
                db!!.itemDao().update(item)
            }
            else {
                item!!.createdTs = Date().time
                db!!.itemDao().insert(item)
            }
        })

        mEditDelete?.setOnClickListener(View.OnClickListener { v: View? ->
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_item))
                .setMessage(getString(R.string.are_you_sure_delete_item))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes) { _: DialogInterface?, _: Int ->
                    db!!.itemDao().delete(item)
                    finish()
                }
                .setNegativeButton(android.R.string.no, null).show()
        })
    }

    public override fun onResume() {
        super.onResume()
        Log.i(tag, "onResume()")

        val itemId = intent.getIntExtra(MainActivity.ITEM_ID, 0)

        if (itemId > 0) {
            item = db!!.itemDao().findById(itemId)
            mEditDelete!!.visibility = View.VISIBLE
        }
        else {
            item = Item()
            mEditDelete!!.visibility = View.INVISIBLE
        }

        mEditTitle!!.setText(item!!.title)
        mEditDescription!!.setText(item!!.description)
    }
}