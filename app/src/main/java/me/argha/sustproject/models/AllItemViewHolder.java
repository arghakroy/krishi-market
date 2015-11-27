package me.argha.sustproject.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author: ARGHA K ROY
 * Date: 11/27/2015.
 */
public class AllItemViewHolder extends RecyclerView.ViewHolder {
    protected String itemName;
    protected String itemDistrict;
    protected String itemRating;
    protected String itemPriceRange;
    public AllItemViewHolder(View itemView) {
        super(itemView);
    }
}
