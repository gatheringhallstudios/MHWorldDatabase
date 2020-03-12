package com.gatheringhallstudios.mhworlddatabase.features.kinsects.detail

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
import kotlinx.android.synthetic.main.fragment_kinsect_summary.*
import kotlinx.android.synthetic.main.view_weapon_recipe.view.*

class  KinsectDetailFragment : androidx.fragment.app.Fragment() {
    /**
     * Returns the viewmodel owned by the parent fragment
     */
    private val viewModel: KinsectDetailViewModel by lazy {
        ViewModelProviders.of(parentFragment!!).get(KinsectDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_kinsect_summary, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.kinsectData.observe(this, Observer(::populateKinsect))
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
        kinsect_header.setIconType(IconType.ZEMBELLISHED)
        kinsect_header.setIconDrawable(AssetLoader.loadIconFor(kinsect))
        kinsect_header.setTitleText(kinsect.name)
        kinsect_header.setSubtitleText(getString(R.string.format_rarity, kinsect.rarity))
        kinsect_header.setSubtitleColor(AssetLoader.loadRarityColor(kinsect.rarity))

        attack_type.text = when (kinsect.attack_type) {
            KinsectAttackType.SEVER -> getString(R.string.kinsect_attack_type_sever)
            KinsectAttackType.BLUNT -> getString(R.string.kinsect_attack_type_blunt)
        }

        dust_effect.text = when (kinsect.dust_effect) {
            KinsectDustEffect.POISON -> getString(R.string.kinsect_dust_effect_poison)
            KinsectDustEffect.PARALYSIS -> getString(R.string.kinsect_dust_effect_paralysis)
            KinsectDustEffect.HEAL -> getString(R.string.kinsect_dust_effect_heal)
            KinsectDustEffect.BLAST -> getString(R.string.kinsect_dust_effect_blast)
        }

        dust_effect_icon.setImageDrawable(AssetLoader.loadKinsectDustIcon(kinsect.dust_effect))

        power_value.text = getString(R.string.level_short_qty,  kinsect.power)

        speed_value.text = getString(R.string.level_short_qty, kinsect.speed)

        heal_value.text = getString(R.string.level_short_qty, kinsect.heal)
    }

    private fun populateComponents(recipe: List<ItemQuantity>?) {
        if (recipe == null || recipe.isEmpty()) {
            kinsect_recipes.visibility = View.GONE
            return
        }

        kinsect_recipes.visibility = View.VISIBLE

        val view = layoutInflater.inflate(R.layout.view_weapon_recipe, kinsect_recipes, false)

        view.weapon_components_list_title.setLabelText(getString(R.string.header_required_materials))

        for (component in recipe) {
            val itemView = IconLabelTextCell(context)
            val icon = AssetLoader.loadIconFor(component.item)

            itemView.setLeftIconDrawable(icon)
            itemView.setLabelText(component.item.name)
            itemView.setValueText(getString(R.string.format_quantity_none, component.quantity))
            itemView.setOnClickListener {
                getRouter().navigateItemDetail(component.item.id)
            }

            view.weapon_components_list.addView(itemView)
        }

        kinsect_recipes.removeAllViews()
        kinsect_recipes.addView(view)
    }
}