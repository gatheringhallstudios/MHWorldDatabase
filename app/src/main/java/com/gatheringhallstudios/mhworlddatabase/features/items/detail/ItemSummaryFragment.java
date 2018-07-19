package com.gatheringhallstudios.mhworlddatabase.features.items.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.data.models.Item;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemSummaryFragment extends Fragment {

    //@BindView(R.id.item_icon)
    //ImageView itemIcon;

    @BindView(R.id.item_name)
    TextView itemName;

    @BindView(R.id.item_description)
    TextView itemDescription;

    @BindView(R.id.item_data_layout)
    LinearLayout itemDataLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_summary, parent, false);
        ButterKnife.bind(this, view);

        ItemDetailViewModel viewmodel = ViewModelProviders.of(getParentFragment()).get(ItemDetailViewModel.class);
        viewmodel.getItem().observe(this, this::populateItem);

        return view;
    }

    private void populateItem(Item item) {

        //Set the summary information
        //itemIcon.setIcon(setsomeicon);
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());

        // Find target views manually since we can't run ButterKnife.bind more than once.
        TextView buyPriceValue = itemDataLayout.findViewById(R.id.buy_price_value);
        TextView sellPriceValue = itemDataLayout.findViewById(R.id.sell_price_value);
        TextView carryCapacityValue = itemDataLayout.findViewById(R.id.carry_capacity_value);
        TextView rarityValue = itemDataLayout.findViewById(R.id.rarity_value);

        bindItemDataValue(buyPriceValue, item.getData().getBuy_price());
        bindItemDataValue(sellPriceValue, item.getData().getSell_price());
        bindItemDataValue(carryCapacityValue, item.getData().getCarry_limit());
        bindItemDataValue(rarityValue, item.getData().getRarity());
    }

    /**
     * Binds item data values from the itemView object to the view
     */
    private void bindItemDataValue(TextView textView, int value) {
        //Replace null/0 value with -
        if(value == 0) {
            textView.setText("-");
        } else {
            textView.setText(Integer.toString(value));

        }
    }
}
