package com.gatheringhallstudios.mhworlddatabase.common.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.common.Consumer;
import com.gatheringhallstudios.mhworlddatabase.data.views.Monster;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;

import java.util.List;

public class MonsterAdapterDelegate extends AdapterDelegate<List<Monster>> {

    Consumer<Monster> onSelected;

    public MonsterAdapterDelegate(Consumer<Monster> onSelected) {
        this.onSelected = onSelected;
    }

    @Override
    protected boolean isForViewType(@NonNull List<Monster> items, int position) {
        return true;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.listitem_monster, parent, false);

        return new MonsterViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull List<Monster> items,
                                    int position,
                                    @NonNull RecyclerView.ViewHolder holder,
                                    @NonNull List<Object> payloads) {
        Monster monster = items.get(position);

        MonsterViewHolder monVH = (MonsterViewHolder)holder;
        monVH.monsterName.setText(monster.name);

        holder.itemView.setOnClickListener((View v) -> {
            onSelected.accept(items.get(position));
        });

        // TODO Set monster image
    }

    class MonsterViewHolder extends RecyclerView.ViewHolder {
        ImageView monsterIcon;
        TextView monsterName;

        MonsterViewHolder(View itemView) {
            super(itemView);
            monsterIcon = itemView.findViewById(R.id.monster_icon);
            monsterName = itemView.findViewById(R.id.monster_name);
        }
    }
}
