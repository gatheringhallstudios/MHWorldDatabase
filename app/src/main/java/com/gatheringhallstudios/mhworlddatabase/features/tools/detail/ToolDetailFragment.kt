package com.gatheringhallstudios.mhworlddatabase.features.tools.detail

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.assets.SlotEmptyRegistry
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.Tool
import com.gatheringhallstudios.mhworlddatabase.data.types.ToolType
import com.gatheringhallstudios.mhworlddatabase.features.armor.detail.ArmorDetailPagerFragment
import com.gatheringhallstudios.mhworlddatabase.features.bookmarks.BookmarksFeature
import com.gatheringhallstudios.mhworlddatabase.setActivityTitle
import com.gatheringhallstudios.mhworlddatabase.util.BundleBuilder
import com.gatheringhallstudios.mhworlddatabase.util.getDrawableCompat
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentToolSummaryBinding

class ToolDetailFragment : androidx.fragment.app.Fragment() {
    private var _binding: FragmentToolSummaryBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ARG_TOOL_ID = "TOOL"

        @JvmStatic
        fun newInstance(toolId: Int): ArmorDetailPagerFragment {
            val fragment = ArmorDetailPagerFragment()
            fragment.arguments = BundleBuilder()
                    .putInt(ARG_TOOL_ID, toolId)
                    .build()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(ToolDetailFragment.ViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentToolSummaryBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolId = requireArguments().getInt(ARG_TOOL_ID)
        viewModel.loadTool(toolId, AppSettings.dataLocale)
        viewModel.tool.observe(viewLifecycleOwner, Observer(::populateTool))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_bookmarkable, menu)
        val itemData = viewModel.tool.value
        if (itemData != null && BookmarksFeature.isBookmarked(itemData)) {
            menu.findItem(R.id.action_toggle_bookmark).icon = (requireContext().getDrawableCompat(R.drawable.ic_sys_bookmark_on))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Try to handle the bookmarks button onclick here instead of the main activity
        val id = item.itemId
        super.onOptionsItemSelected(item)
        return if (id == R.id.action_toggle_bookmark) {
            BookmarksFeature.toggleBookmark(viewModel.tool.value)
            requireActivity().invalidateOptionsMenu()
            true
        } else false
    }

    private fun populateTool(toolData: Tool?) {
        if (toolData == null) return
        setActivityTitle(toolData.name)

        //Rerender the menu bar because we are 100% sure we have the tool data now
        requireActivity().invalidateOptionsMenu()

        binding.toolHeader.setTitleText(toolData.name)
        binding.toolHeader.setSubtitleText(when (toolData.tool_type) {
            ToolType.MANTLE -> requireContext().getString(R.string.tool_mantle)
            ToolType.BOOSTER -> requireContext().getString(R.string.tool_booster)
        })
        binding.toolHeader.setIconDrawable(AssetLoader.loadIconFor(toolData))

        binding.effectDurationValue.text = if (toolData.duration_upgraded != null) String.format("%d (%d)",
                toolData.duration, toolData.duration_upgraded) else toolData.duration.toString()
        binding.rechargeTimeValue.text = toolData.recharge.toString()

        val slotImages = toolData.slots.map {
            this.requireContext().getDrawableCompat(SlotEmptyRegistry(it))
        }
        binding.slot1.setImageDrawable(slotImages[0])
        binding.slot2.setImageDrawable(slotImages[1])
        binding.slot3.setImageDrawable(slotImages[2])
    }

    class ViewModel(application: Application) : AndroidViewModel(application) {
        private val dao = MHWDatabase.getDatabase(application).toolDao()

        lateinit var tool: LiveData<Tool>

        fun loadTool(toolId: Int, langId: String) {
            tool = dao.loadTool(toolId, langId)
        }
    }
}

