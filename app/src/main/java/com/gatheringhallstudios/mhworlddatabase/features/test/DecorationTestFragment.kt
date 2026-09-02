package com.gatheringhallstudios.mhworlddatabase.features.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.models.DecorationBase
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentTestDecorationBinding

/**
 * @author Jayson Dela Cruz
 */

class DecorationTestFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentTestDecorationBinding.inflate(inflater, container, false)

        // Add variations of decorations
        binding.deco1.setImageDrawable(AssetLoader.loadIconFor(DecorationBase(1, "Test", 1, "Green")))
        binding.deco2.setImageDrawable(AssetLoader.loadIconFor(DecorationBase(2, "Test", 2, "Green")))
        binding.deco3.setImageDrawable(AssetLoader.loadIconFor(DecorationBase(3, "Test", 3, "Green")))
        binding.deco4.setImageDrawable(AssetLoader.loadIconFor(DecorationBase(4, "Test", 4, "Green")))

        // Add variations of slots
        binding.slot1Deco1.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 1, "Green"), 1))
        binding.slot2Deco1.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 1, "Green"), 2))
        binding.slot3Deco1.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 1, "Green"), 3))
        binding.slot4Deco1.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 1, "Green"), 4))

        binding.slot2Deco2.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 2, "Green"), 2))
        binding.slot3Deco2.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 2, "Green"), 3))
        binding.slot4Deco2.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 2, "Green"), 4))

        binding.slot3Deco3.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 3, "Green"), 3))
        binding.slot4Deco3.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 3, "Green"), 4))

        binding.slot4Deco4.setImageDrawable(AssetLoader.loadFilledSlotIcon(DecorationBase(0, "", 4, "Green"), 4))

        return binding.root
    }
}