package com.gatheringhallstudios.mhworlddatabase.common.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.data.ArmorBasic;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SkillListAdapter extends RecyclerView.Adapter<SkillListAdapter.ViewHolder> {
    private List<ArmorBasic> armorList;

    public SkillListAdapter(List<ArmorBasic> armorList) {
        this.armorList = armorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.listitem_monster, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArmorBasic item = armorList.get(position);
        holder.skillName.setText(item.name);
    }

    @Override
    public int getItemCount() {
        return armorList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon_monster) ImageView skillIcon;
        @BindView(R.id.name_monster) TextView skillName;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
