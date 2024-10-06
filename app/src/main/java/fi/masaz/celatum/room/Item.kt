package fi.masaz.celatum.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Item {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "description")
    var description: String? = null

    @ColumnInfo(name = "icon")
    var icon: String? = null

    @ColumnInfo(name = "created_ts")
    var createdTs: Long? = null

    @ColumnInfo(name = "edited_ts")
    var editedTs: Long? = null
}
