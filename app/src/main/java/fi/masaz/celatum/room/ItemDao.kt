package fi.masaz.celatum.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ItemDao {
    @get:Query("SELECT * FROM item")
    val all: List<Item>?

    @Query("SELECT * FROM item WHERE id = :id")
    fun findById(id: Int): Item?

    @Insert
    fun insert(item: Item)

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item)
}
