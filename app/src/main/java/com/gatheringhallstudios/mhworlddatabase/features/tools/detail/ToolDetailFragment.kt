package com.gatheringhallstudios.mhworlddatabase.features.tools.detail

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
import kotlinx.android.synthetic.main.fragment_tool_summary.*

class ToolDetailFragment : androidx.fragment.app.Fragment() {
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
        ViewModelProviders.of(this).get(ToolDetailFragment.ViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tool_summary, parent, false)
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

        tool_header.setTitleText(toolData.name)
        tool_header.setSubtitleText(when (toolData.tool_type) {
            ToolType.MANTLE -> requireContext().getString(R.string.tool_mantle)
            ToolType.BOOSTER -> requireContext().getString(R.string.tool_booster)
        })
        tool_header.setIconDrawable(AssetLoader.loadIconFor(toolData))

        effect_duration_value.text = if (toolData.duration_upgraded != null) String.format("%d (%d)",
                toolData.duration, toolData.duration_upgraded) else toolData.duration.toString()
        recharge_time_value.text = toolData.recharge.toString()

        val slotImages = toolData.slots.map {
            this.requireContext().getDrawableCompat(SlotEmptyRegistry(it))
        }
        slot1.setImageDrawable(slotImages[0])
        slot2.setImageDrawable(slotImages[1])
        slot3.setImageDrawable(slotImages[2])
    }

    class ViewModel(application: Application) : AndroidViewModel(application) {
        private val dao = MHWDatabase.getDatabase(application).toolDao()

        lateinit var tool: LiveData<Tool>

        fun loadTool(toolId: Int, langId: String) {
            tool = dao.loadTool(toolId, langId)
        }
    }
}

