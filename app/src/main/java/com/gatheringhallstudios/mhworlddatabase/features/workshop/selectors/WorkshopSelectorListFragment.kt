package com.gatheringhallstudios.mhworlddatabase.features.workshop.selectors

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.components.SpacesItemDecoration
import com.gatheringhallstudios.mhworlddatabase.data.models.*
import com.gatheringhallstudios.mhworlddatabase.data.types.ArmorType
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.features.workshop.UserEquipmentCard
import com.gatheringhallstudios.mhworlddatabase.features.weapons.list.WeaponTreePagerFragment.Companion.FILTER_RESULT_CODE
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentWorkshopSelectorBinding

class WorkshopSelectorListFragment : Fragment() {
    private var _binding: FragmentWorkshopSelectorBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ARG_ACTIVE_EQUIPMENT = "ACTIVE_EQUIPMENT"
        const val ARG_SET_ID = "ACTIVE_SET_ID" //The equipment set that is currently being handled when in builder mode
        const val ARG_ARMOR_FILTER = "ACTIVE_ARMOR_FILTER" //What class armor to limit the selector to
        const val ARG_SELECTOR_MODE = "SELECTOR_MODE"
        const val ARG_DECORATION_CONFIG = "DECORATION_CONFIG"
        const val ARG_ORDER_ID = "ORDER_ID" //For types of equipment that have repeated entries, e.g. tools

        enum class SelectorMode {
            ARMOR,
            DECORATION,
            CHARM,
            WEAPON,
            TOOL,
            NONE
        }

        class DecorationsConfig(val targetEquipmentId: Int, val targetEquipmentSlot: Int,
                                val targetEquipmentType: DataType, val decorationLevelFilter: Int) : Serializable
    }

    private val viewModel: WorkshopSelectorViewModel by lazy {
        ViewModelProvider(this).get(WorkshopSelectorViewModel::class.java)
    }

    private lateinit var card: UserEquipmentCard
    private var mode: SelectorMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.action_search).isVisible = false
        inflater.inflate(R.menu.menu_weapon_tree, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val filterIcon = menu.findItem(R.id.action_filter)
        viewModel.isFilterActive.observe(this, Observer { isFiltered ->
            filterIcon?.setIcon(when (isFiltered) {
                true -> R.drawable.ic_sys_filter_on
                false -> R.drawable.ic_sys_filter_off
            })
        })
    }

    /**
     * Handled when a menu item is clicked. True is returned if handled.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                val state = viewModel.filterState
                val filterFragment = EquipmentFilterFragment.newInstance(this.mode!!, state)
                filterFragment.setTargetFragment(this, FILTER_RESULT_CODE)
                filterFragment.show(fragmentManager!!, "Filter")
                true
            }

            // fallback to parent behavior if unhandled
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentWorkshopSelectorBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.mode = arguments?.getSerializable(ARG_SELECTOR_MODE) as? SelectorMode
        val filter = arguments?.getSerializable(ARG_ARMOR_FILTER) as? ArmorType
        val activeEquipment = arguments?.getSerializable(ARG_ACTIVE_EQUIPMENT) as? UserEquipment
        val activeEquipmentSetId = arguments?.getInt(ARG_SET_ID)
        val orderId = arguments?.getInt(ARG_ORDER_ID)
        val decorationsConfig = arguments?.getSerializable(ARG_DECORATION_CONFIG) as? DecorationsConfig
        //Remove the userEquipment from arguments to prevent it from being serialized onPause
        arguments?.putSerializable(ARG_ACTIVE_EQUIPMENT, null)

        card = UserEquipmentCard(binding.activeEquipmentSlot)

        when (mode) {
            SelectorMode.ARMOR -> initArmorSelector(filter, activeEquipment as? UserArmorPiece, activeEquipmentSetId)
            SelectorMode.CHARM -> initCharmSelector(activeEquipment as? UserCharm, activeEquipmentSetId)
            SelectorMode.DECORATION -> initDecorationSelector(activeEquipment as? UserDecoration, activeEquipmentSetId, decorationsConfig!!)
            SelectorMode.WEAPON -> initWeaponSelector(activeEquipment as? UserWeapon, activeEquipmentSetId)
            SelectorMode.TOOL -> initToolSelector(activeEquipment as? UserTool, activeEquipmentSetId, orderId)
            SelectorMode.NONE, null -> Unit
        }
    }

    override fun onPause() {
        super.onPause()
        val listState = binding.equipmentList.layoutManager?.onSaveInstanceState()
        if (listState != null) {
            viewModel.listState = listState
        }
    }

    /**
     * Receives a dialog result. Currently the only supported dialog is the filter fragment.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != FILTER_RESULT_CODE) {
            return
        }

        val state = data?.getSerializableExtra(EquipmentFilterFragment.FILTER_STATE) as? EquipmentFilterState
        if (state != null) {
            viewModel.filterState = state
        }
    }

    private fun initArmorSelector(armorType: ArmorType?, activeArmorPiece: UserArmorPiece?, activeEquipmentSetId: Int?) {
        setActivityTitle(getString(R.string.title_workshop_armor_selector))

        if (armorType != null) {
            viewModel.loadArmor(AppSettings.dataLocale, armorType)
            card.bindActiveArmor(activeArmorPiece, armorType)
            card.populateSlots(activeArmorPiece?.armor?.armor?.slots)
        }

        val adapter = WorkshopArmorSelectorAdapter {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    viewModel.updateEquipmentForEquipmentSet(it.entityId, it.entityType, activeEquipmentSetId!!, activeArmorPiece?.armor?.entityId, 0)
                }

                getRouter().goBack()
            }
        }

        binding.equipmentList.adapter = adapter
        binding.equipmentList.addItemDecoration(SpacesItemDecoration(32))
        viewModel.armor.observe(this, Observer {
            adapter.items = it
            if (viewModel.islistStateInitialized()) {
                binding.equipmentList.layoutManager?.onRestoreInstanceState(viewModel.listState)
            }

            if (it.isEmpty()) {
                binding.emptyView.root.visibility = View.VISIBLE
            } else {
                binding.emptyView.root.visibility = View.GONE
            }
        })
    }

    private fun initCharmSelector(activeCharm: UserCharm?, activeEquipmentSetId: Int?) {
        setActivityTitle(getString(R.string.title_workshop_charm_selector))
        viewModel.loadCharms(AppSettings.dataLocale)

        val adapter = WorkshopCharmSelectorAdapter {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    viewModel.updateEquipmentForEquipmentSet(it.entityId, it.entityType, activeEquipmentSetId!!, activeCharm?.entityId(), 0)
                }

                getRouter().goBack()
            }
        }

        card.bindActiveCharm(activeCharm)

        binding.equipmentList.adapter = adapter
        binding.equipmentList.addItemDecoration(SpacesItemDecoration(32))

        viewModel.charms.observe(this, Observer {
            adapter.items = it
            if (viewModel.islistStateInitialized()) {
                binding.equipmentList.layoutManager?.onRestoreInstanceState(viewModel.listState)
            }

            if (it.isEmpty()) {
                binding.emptyView.root.visibility = View.VISIBLE
            } else {
                binding.emptyView.root.visibility = View.GONE
            }
        })
    }

    private fun initDecorationSelector(activeDecoration: UserDecoration?, activeEquipmentSetId: Int?, decorationsConfig: DecorationsConfig) {
        setActivityTitle(getString(R.string.title_workshop_decoration_selector))
        viewModel.loadDecorations(AppSettings.dataLocale)

        val adapter = WorkshopDecorationSelectorAdapter {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    viewModel.updateDecorationForEquipmentSet(it.id, decorationsConfig.targetEquipmentId,
                            decorationsConfig.targetEquipmentSlot, decorationsConfig.targetEquipmentType, activeEquipmentSetId!!, activeDecoration?.entityId())
                }
                getRouter().goBack()
            }
        }

        card.bindDecoration(activeDecoration, decorationsConfig.decorationLevelFilter)

        binding.equipmentList.adapter = adapter
        binding.equipmentList.addItemDecoration(SpacesItemDecoration(32))

        viewModel.decorations.observe(this, Observer {
            val filteredCollection = it.filter { decoration ->
                decoration.slot <= decorationsConfig.decorationLevelFilter
            }
            if (viewModel.islistStateInitialized()) {
                binding.equipmentList.layoutManager?.onRestoreInstanceState(viewModel.listState)
            }

            adapter.items = filteredCollection
            if (filteredCollection.isEmpty()) {
                binding.emptyView.root.visibility = View.VISIBLE
            } else {
                binding.emptyView.root.visibility = View.GONE
            }
        })
    }

    private fun initWeaponSelector(activeWeapon: UserWeapon?, activeEquipmentSetId: Int?) {
        setActivityTitle(getString(R.string.title_workshop_tool_selector))
        viewModel.loadWeapons(AppSettings.dataLocale)

        val adapter = WorkshopWeaponSelectorAdapter {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    viewModel.updateEquipmentForEquipmentSet(it.entityId, it.entityType, activeEquipmentSetId!!, activeWeapon?.entityId(), 0)
                }
                getRouter().goBack()
            }
        }

        card.bindActiveWeapon(activeWeapon)
        card.populateSlots(activeWeapon?.weapon?.weapon?.slots)

        binding.equipmentList.adapter = adapter
        binding.equipmentList.addItemDecoration(SpacesItemDecoration(32))

        viewModel.weapons.observe(this, Observer {
            adapter.items = it
            if (viewModel.islistStateInitialized()) {
                binding.equipmentList.layoutManager?.onRestoreInstanceState(viewModel.listState)
            }

            if (it.isEmpty()) {
                binding.emptyView.root.visibility = View.VISIBLE
            } else {
                binding.emptyView.root.visibility = View.GONE
            }
        })
    }

    private fun initToolSelector(activeTool: UserTool?, activeEquipmentSetId: Int?, orderId: Int?) {
        setActivityTitle(getString(R.string.title_workshop_tool_selector))
        viewModel.loadTools(AppSettings.dataLocale)

        val adapter = WorkshopToolSelectorAdapter {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    viewModel.updateEquipmentForEquipmentSet(it.entityId, it.entityType, activeEquipmentSetId!!, activeTool?.entityId(), orderId ?: 0)
                }
                getRouter().goBack()
            }
        }

        card.bindActiveTool(activeTool)
        binding.equipmentList.adapter = adapter

        binding.equipmentList.addItemDecoration(SpacesItemDecoration(32))

        viewModel.tools.observe(this, Observer {
            adapter.items = it
            if (viewModel.islistStateInitialized()) {
                binding.equipmentList.layoutManager?.onRestoreInstanceState(viewModel.listState)
            }

            if (it.isEmpty()) {
                binding.emptyView.root.visibility = View.VISIBLE
            }
        })
    }
}
