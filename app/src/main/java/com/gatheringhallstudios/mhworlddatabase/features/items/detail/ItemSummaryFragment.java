package com.gatheringhallstudios.mhworlddatabase.features.items.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gatheringhallstudios.mhworlddatabase.R;
import com.gatheringhallstudios.mhworlddatabase.data.views.ItemView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class ItemSummaryFragment extends Fragment {

    //@BindView(R.id.item_icon)
    //ImageView itemIcon;

    TextView itemName;
    TextView itemDescription;
    LinearLayout itemDataLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_summary, parent, false);

        // Bind views
        itemName = view.findViewById(R.id.item_name);
        itemDescription = view.findViewById(R.id.item_description);
        itemDataLayout = view.findViewById(R.id.item_data_layout);

        ItemDetailViewModel viewmodel = ViewModelProviders.of(getParentFragment()).get(ItemDetailViewModel.class);
        viewmodel.getItem().observe(this, this::populateItem);

        return view;
    }

    private void populateItem(ItemView item) {

        //Set the summary information
        //itemIcon.setIcon(setsomeicon);
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());

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
