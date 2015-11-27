package me.argha.sustproject.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import me.argha.sustproject.R;

/**
 * Author: ARGHA K ROY
 * Date: 11/27/2015.
 */
public class AllItemViewHolder extends RecyclerView.ViewHolder {
    public TextView itemName;
    public TextView itemCategory;
    public TextView itemRating;
    public TextView itemPriceRange;
    public ImageView itemImage;
    public AllItemViewHolder(View itemView) {
        super(itemView);
        itemName= (TextView) itemView.findViewById(R.id.listSingleItemName);
        itemCategory= (TextView) itemView.findViewById(R.id.listSingleItemCategory);
        itemRating= (TextView) itemView.findViewById(R.id.listSingleItemRating);
        itemPriceRange= (TextView) itemView.findViewById(R.id.listSingleItemPriceRange);
        itemImage= (ImageView) itemView.findViewById(R.id.listItemImageIv);
    }
}
