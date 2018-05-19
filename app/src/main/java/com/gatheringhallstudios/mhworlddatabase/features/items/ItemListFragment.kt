package com.gatheringhallstudios.mhworlddatabase.features.items

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.common.Navigator
import com.gatheringhallstudios.mhworlddatabase.common.adapters.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.adapters.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.dao.ItemDao
import com.gatheringhallstudios.mhworlddatabase.data.views.ItemView
import kotlinx.android.synthetic.main.listitem_monster.view.*

// move to adapters if more classes need to use it
class ItemListDelegate(val onSelect: (ItemView) -> Unit) : SimpleListDelegate<ItemView>(ItemView::class) {
    // todo: create item listitem layout
    override fun getLayoutId() = R.layout.listitem_monster

    override fun bindItem(v: View, item : ItemView) {
        v.monster_name.text = item.name

        v.setOnClickListener { onSelect(item) }
    }
}

class ItemListFragment : Fragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ItemListFragment.ViewModel::class.java)
    }

    private val adapter = BasicListDelegationAdapter(ItemListDelegate(::handleItemSelected))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Setup RecyclerView
        val recyclerView = inflater.inflate(R.layout.list_generic, container, false) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(container!!.context)
        recyclerView.adapter = adapter

        return recyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.init()
        viewModel.items.observe(this, Observer(::setItems))
    }

    private fun setItems(items: List<ItemView>?) {
        items ?: return

        adapter.items = items
        adapter.notifyDataSetChanged()
    }

    private fun handleItemSelected(item: ItemView) {
        val nav = activity as Navigator
        nav.navigateTo(ItemDetailPagerFragment.newInstance(item.id))
    }

    // ViewModel class used by this Fragment
    class ViewModel(application : Application) : AndroidViewModel(application) {
        private val dao : ItemDao = MHWDatabase.getDatabase(application).itemDao()
        lateinit var items : LiveData<List<ItemView>> private set

        fun init() {
            items = dao.getItems("en")
        }
    }
}