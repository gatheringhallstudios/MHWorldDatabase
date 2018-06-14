package com.gatheringhallstudios.mhworlddatabase.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.data.views.ArmorBasicView;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Carlos on 3/22/2018.
 */

public class ArmorAdapterDelegate extends AdapterDelegate<List<ArmorBasicView>> {
    @Override
    protected boolean isForViewType(@NonNull List<ArmorBasicView> items, int position) {
        return true;
    }

    @NonNull
    @Override
    public ArmorViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.listitem_monster, parent, false);
        return new ArmorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull List<ArmorBasicView> items,
                                 int position,
                                 @NonNull RecyclerView.ViewHolder holder,
                                 @NonNull List<Object> payloads) {
        ArmorBasicView item = items.get(position);

        ArmorViewHolder vh = (ArmorViewHolder)holder;
        vh.armorName.setText(item.getName());
    }

    class ArmorViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.monster_icon) ImageView armorIcon;
        @BindView(R.id.monster_name) TextView armorName;

        ArmorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
