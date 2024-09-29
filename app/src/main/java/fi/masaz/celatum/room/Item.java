package fi.masaz.celatum.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Item {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "icon")
    public String icon;

    @ColumnInfo(name = "created_ts")
    public Long createdTs;

    @ColumnInfo(name = "edited_ts")
    public Long editedTs;
}
