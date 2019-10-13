package com.gatheringhallstudios.mhworlddatabase.features.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.DecorationBase
import kotlinx.android.synthetic.main.fragment_test_decoration.view.*

/**
 * @author Jayson Dela Cruz
 */

class DecorationTestFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_test_decoration, container, false)

        // Add variations of decorations
        view.deco1.setImageDrawable(AssetLoader.loadIconFor(DecorationBase(1, "Test", 1, "Green")))
        view.deco2.setImageDrawable(AssetLoader.loadIconFor(DecorationBase(2, "Test", 2, "Green")))
        view.deco3.setImageDrawable(AssetLoader.loadIconFor(DecorationBase(3, "Test", 3, "Green")))
        view.deco4.setImageDrawable(AssetLoader.loadIconFor(DecorationBase(4, "Test", 4, "Green")))

        // Add variations of slots
        view.slot1_deco1.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 1, "Green"), 1))
        view.slot2_deco1.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 1, "Green"), 2))
        view.slot3_deco1.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 1, "Green"), 3))
        view.slot4_deco1.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 1, "Green"), 4))

        view.slot2_deco2.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 2, "Green"), 2))
        view.slot3_deco2.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 2, "Green"), 3))
        view.slot4_deco2.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 2, "Green"), 4))

        view.slot3_deco3.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 3, "Green"), 3))
        view.slot4_deco3.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 3, "Green"), 4))

        view.slot4_deco4.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 4, "Green"), 4))

        return view
    }
}