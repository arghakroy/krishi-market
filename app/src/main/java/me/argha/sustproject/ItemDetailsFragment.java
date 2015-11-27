package me.argha.sustproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.argha.sustproject.models.Item;

/**
 * Author: ARGHA K ROY
 * Date: 11/27/2015.
 */
public class ItemDetailsFragment extends Fragment {

    @Bind(R.id.itemDetailsNameTv) TextView itemNameTv;
    @Bind(R.id.itemDetailsRatingTv) TextView itemRatingTv;
    @Bind(R.id.itemDetailsSubCatTv) TextView itemSubCatTv;
    @Bind(R.id.itemDetailsParentCatTv) TextView itemParentCatTv;
    @Bind(R.id.itemDetailsPriceRange) TextView itemPriceRangeTv;
    @Bind(R.id.itemDetailsDescriptionTv) TextView itemDescTv;

    public static ItemDetailsFragment newInstance(Item item) {
        Bundle args = new Bundle();
        args.putString("id",item.getItemId());
        args.putString("name",item.getName());
        args.putString("mainCat",item.getMain_category());
        args.putString("subCat",item.getSub_category());
        args.putString("priceMin",item.getRange_min());
        args.putString("priceMax",item.getRange_max());
        args.putString("desc",item.getDescription());
        args.putString("rating", item.getRating());

        ItemDetailsFragment fragment = new ItemDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.item_details_layout,container,false);
        ButterKnife.bind(this,root);
        itemNameTv.setText(getArguments().getString("name"));
        itemRatingTv.setText(getArguments().getString("rating"));
        itemSubCatTv.setText(getArguments().getString("subCat"));
        itemParentCatTv.setText(getArguments().getString("mainCat"));
        itemPriceRangeTv.setText("Price Range: "+getArguments().getString("priceMin")+" - "+getArguments().getString("priceMax"));
        itemDescTv.setText(getArguments().getString("desc"));
        return root;
    }
}
