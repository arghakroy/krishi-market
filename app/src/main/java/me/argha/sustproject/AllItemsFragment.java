package me.argha.sustproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.argha.sustproject.models.AllItemViewHolder;

/**
 * Author: ARGHA K ROY
 * Date: 11/27/2015.
 */
public class AllItemsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.item_list_layout,container,false);
        RecyclerView recList = (RecyclerView) root.findViewById(R.id.itemList);
        recList.setHasFixedSize(true);
        GridLayoutManager llm = new GridLayoutManager(getActivity(),2);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setAdapter(new ItemAdapter());
        return root;
    }

    class ItemAdapter extends RecyclerView.Adapter<AllItemViewHolder>{

        @Override
        public AllItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.single_list_item, parent, false);
            return new AllItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(AllItemViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }
}
