package me.argha.sustproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import me.argha.sustproject.models.Item;
import me.argha.sustproject.utils.AppURL;
import me.argha.sustproject.utils.Util;

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
    @Bind(R.id.itemDetailsRatingBar) RatingBar itemRatingBar;
    @Bind(R.id.itemDetailsImageIv)ImageView itemImage;
    @Bind(R.id.itemDetailsExpireDateTv)TextView expireTv;

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
        args.putString("photo", item.getPhoto());
        args.putString("expire", item.getExpire_date());

        ItemDetailsFragment fragment = new ItemDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.item_details_layout,container,false);
        ButterKnife.bind(this, root);
        itemNameTv.setText(getArguments().getString("name"));
        itemRatingTv.setText(getArguments().getString("rating"));
        itemSubCatTv.setText(getArguments().getString("subCat"));
        itemParentCatTv.setText(getArguments().getString("mainCat"));
        itemPriceRangeTv.setText("Price Range: " + getArguments().getString("priceMin") + " - " + getArguments().getString("priceMax"));
        itemDescTv.setText(getArguments().getString("desc"));
        if(getArguments().getString("expire").length()>3){
            expireTv.setText("Expire Date: "+getArguments().getString("expire"));
        }

        Picasso.with(getActivity()).load(AppURL.ASSETS+getArguments().getString("photo")).into(itemImage);

        itemRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                updateItemRating(itemRatingBar.getRating());
                Util.showToast(getActivity(),"Rating "+itemRatingBar.getRating()+"");
            }
        });
        return root;
    }

    private void updateItemRating(float rating) {
        RequestParams params=new RequestParams();
        params.add("user_id","1");
        params.add("item_id",getArguments().getString("id"));
        params.add("rating",rating+"");
        final ProgressDialog dialog=Util.getProgressDialog(getActivity(),"Rating. Please Wait");
        AsyncHttpClient httpClient=new AsyncHttpClient();
        httpClient.post(AppURL.ITEM_RATE,params,new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Util.printDebug("Rating response",response.toString());
                try {
                    if(response.getBoolean("success")){
                        Util.showToast(getActivity(),"Item Rating Successful");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Util.printDebug("rating json error",e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Util.printDebug("Rating failed", responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.dismiss();
            }
        });
    }
}
