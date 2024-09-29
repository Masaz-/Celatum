package fi.masaz.celatum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fi.masaz.celatum.room.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final List<Item> itemList;
    private final OnItemClickListener onItemClickListener;

    public ItemAdapter(List<Item> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.mItemTitle.setText(item.title);

        int icon;

        switch (item.icon) {
            case "credit_card":
                icon = R.drawable.baseline_credit_card_24;
                break;
            case "key":
                icon = R.drawable.baseline_key_24;
                break;
            case "person":
                icon = R.drawable.baseline_person_24;
                break;
            case "link":
                icon = R.drawable.baseline_link_24;
                break;
            default:
                icon = R.drawable.baseline_note_24;
                break;
        }

        holder.mImageView.setImageResource(icon);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mItemTitle;
        ImageView mImageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mItemTitle = itemView.findViewById(R.id.item_title);
            mImageView = itemView.findViewById(R.id.item_icon);
        }
    }
}
