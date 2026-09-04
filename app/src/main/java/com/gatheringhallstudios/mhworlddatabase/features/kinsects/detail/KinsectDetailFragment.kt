package com.gatheringhallstudios.mhworlddatabase.features.kinsects.detail

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.components.IconType
import com.gatheringhallstudios.mhworlddatabase.data.models.ItemQuantity
import com.gatheringhallstudios.mhworlddatabase.data.models.Kinsect
import com.gatheringhallstudios.mhworlddatabase.data.models.KinsectFull
import com.gatheringhallstudios.mhworlddatabase.data.types.KinsectAttackType
import com.gatheringhallstudios.mhworlddatabase.data.types.KinsectDustEffect
import com.gatheringhallstudios.mhworlddatabase.features.bookmarks.BookmarksFeature
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import com.gatheringhallstudios.mhworlddatabase.databinding.ViewWeaponRecipeBinding
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentKinsectSummaryBinding

class  KinsectDetailFragment : androidx.fragment.app.Fragment() {
    private var _binding: FragmentKinsectSummaryBinding? = null
    private val binding get() = _binding!!

    /**
     * Returns the viewmodel owned by the parent fragment
     */
    private val viewModel: KinsectDetailViewModel by lazy {
        ViewModelProvider(parentFragment!!).get(KinsectDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentKinsectSummaryBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.kinsectData.observe(viewLifecycleOwner, Observer(::populateKinsect))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_bookmarkable, menu)
        val kinsectData = viewModel.kinsectData.value
        if (kinsectData != null && BookmarksFeature.isBookmarked(kinsectData)) {
            menu.findItem(R.id.action_toggle_bookmark).icon = (context!!.getDrawableCompat(R.drawable.ic_sys_bookmark_on))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Try to handle the bookmarks button onclick here instead of the main activity
        val id = item.itemId
        super.onOptionsItemSelected(item)
        return if (id == R.id.action_toggle_bookmark) {
            BookmarksFeature.toggleBookmark(viewModel.kinsectData.value)
            activity!!.invalidateOptionsMenu()
            true
        } else false
    }

    private fun populateKinsect(kinsectData: KinsectFull?) {
        if (kinsectData == null) return

        //Rerender the menu bar because we are 100% sure we have the kinsect data now
        activity!!.invalidateOptionsMenu()

        setActivityTitle(kinsectData.kinsect.name)
        populateKinsectBasic(kinsectData.kinsect)
        populateComponents(kinsectData.recipe)
    }

    private fun populateKinsectBasic(kinsect: Kinsect) {
        binding.kinsectHeader.setIconType(IconType.ZEMBELLISHED)
        binding.kinsectHeader.setIconDrawable(AssetLoader.loadIconFor(kinsect))
        binding.kinsectHeader.setTitleText(kinsect.name)
        binding.kinsectHeader.setSubtitleText(getString(R.string.format_rarity, kinsect.rarity))
        binding.kinsectHeader.setSubtitleColor(AssetLoader.loadRarityColor(kinsect.rarity))

        binding.attackType.text = when (kinsect.attack_type) {
            KinsectAttackType.SEVER -> getString(R.string.kinsect_attack_type_sever)
            KinsectAttackType.BLUNT -> getString(R.string.kinsect_attack_type_blunt)
        }

        binding.dustEffect.text = when (kinsect.dust_effect) {
            KinsectDustEffect.POISON -> getString(R.string.kinsect_dust_effect_poison)
            KinsectDustEffect.PARALYSIS -> getString(R.string.kinsect_dust_effect_paralysis)
            KinsectDustEffect.HEAL -> getString(R.string.kinsect_dust_effect_heal)
            KinsectDustEffect.BLAST -> getString(R.string.kinsect_dust_effect_blast)
        }

        binding.dustEffectIcon.setImageDrawable(AssetLoader.loadKinsectDustIcon(kinsect.dust_effect))

        binding.powerValue.text = getString(R.string.level_short_qty,  kinsect.power)

        binding.speedValue.text = getString(R.string.level_short_qty, kinsect.speed)

        binding.healValue.text = getString(R.string.level_short_qty, kinsect.heal)
    }

    private fun populateComponents(recipe: List<ItemQuantity>?) {
        if (recipe == null || recipe.isEmpty()) {
            binding.kinsectRecipes.visibility = View.GONE
            return
        }

        binding.kinsectRecipes.visibility = View.VISIBLE

        val recipeBinding = ViewWeaponRecipeBinding.inflate(layoutInflater, binding.kinsectRecipes, false)

        recipeBinding.weaponComponentsListTitle.setLabelText(getString(R.string.header_required_materials))

        for (component in recipe) {
            val itemView = IconLabelTextCell(context)
            val icon = AssetLoader.loadIconFor(component.item)

            itemView.setLeftIconDrawable(icon)
            itemView.setLabelText(component.item.name)
            itemView.setValueText(getString(R.string.format_quantity_none, component.quantity))
            itemView.setOnClickListener {
                getRouter().navigateItemDetail(component.item.id)
            }

            recipeBinding.weaponComponentsList.addView(itemView)
        }

        binding.kinsectRecipes.removeAllViews()
        binding.kinsectRecipes.addView(recipeBinding.root)
    }
}
