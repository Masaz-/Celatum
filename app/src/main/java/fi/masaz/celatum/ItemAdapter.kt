package fi.masaz.celatum

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fi.masaz.celatum.ItemAdapter.ItemViewHolder
import fi.masaz.celatum.room.Item

class ItemAdapter(
    private var itemList: List<Item>,
    private val onItemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.mItemTitle.text = item.title
        holder.mImageView.setImageResource(Tools.getIcon(item.icon))

        holder.itemView.setOnClickListener { v: View? ->
            onItemClickListener?.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setItemList(newItemList: List<Item>) {
        this.itemList = newItemList
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mItemTitle: TextView = itemView.findViewById(R.id.item_title)
        var mImageView: ImageView = itemView.findViewById(R.id.item_icon)
    }
}
