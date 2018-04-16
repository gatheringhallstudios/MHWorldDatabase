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
import com.gatheringhallstudios.mhworlddatabase.data.views.Reward;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;

import java.util.List;

public class RewardAdapterDelegate extends AdapterDelegate<List<Object>> {

    private Consumer<Object> onSelected;

    public RewardAdapterDelegate(Consumer<Object> onSelected) {
        this.onSelected = onSelected;
    }

    @Override
    protected boolean isForViewType(@NonNull List<Object> items, int position) {
        return items.get(position) instanceof Reward;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.listitem_reward, parent, false);

        return new RewardViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull List<Object> items,
                                    int position,
                                    @NonNull RecyclerView.ViewHolder holder,
                                    @NonNull List<Object> payloads) {
        Reward reward = (Reward) items.get(position);

        RewardViewHolder vh = (RewardViewHolder) holder;
        // TODO Set monster image
        vh.rewardName.setText(reward.name);

        String stack = "x " + Integer.toString(reward.stackSize);
        vh.rewardStack.setText(stack);

        String percent = Integer.toString(reward.percentage) + "%";
        vh.rewardPercent.setText(percent);

        holder.itemView.setOnClickListener((View v) -> onSelected.accept(items.get(position)));

    }

    class RewardViewHolder extends RecyclerView.ViewHolder {
        ImageView rewardIcon;
        TextView rewardName;
        TextView rewardStack;
        TextView rewardPercent;

        RewardViewHolder(View itemView) {
            super(itemView);
            rewardIcon = itemView.findViewById(R.id.reward_icon);
            rewardName = itemView.findViewById(R.id.reward_name);
            rewardStack = itemView.findViewById(R.id.reward_stack);
            rewardPercent = itemView.findViewById(R.id.reward_percent);
        }
    }
}
