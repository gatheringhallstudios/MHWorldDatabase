package com.gatheringhallstudios.mhworlddatabase.adapters.common;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;

import java.util.List;

/**
 * Adapter delegate to handle displaying SectionHeader objects inside RecyclerViews.
 */
public class SectionHeaderAdapterDelegate extends AdapterDelegate<List<Object>> {
    public SectionHeaderAdapterDelegate() {
    }

    @Override
    protected boolean isForViewType(@NonNull List<Object> items, int position) {
        return items.get(position) instanceof SectionHeader;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.listitem_section_header, parent, false);

        return new HeaderViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull List<Object> items,
                                    int position,
                                    @NonNull RecyclerView.ViewHolder holder,
                                    @NonNull List<Object> payloads) {
        SectionHeader sectionHeader = (SectionHeader) items.get(position);

        HeaderViewHolder vh = (HeaderViewHolder) holder;

        vh.labelText.setText(sectionHeader.text);
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView labelText;

        HeaderViewHolder(View itemView) {
            super(itemView);
            labelText = itemView.findViewById(R.id.label_text);
        }
    }
}
