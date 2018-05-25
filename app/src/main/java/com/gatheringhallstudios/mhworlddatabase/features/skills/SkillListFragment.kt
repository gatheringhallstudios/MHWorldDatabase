package com.gatheringhallstudios.mhworlddatabase.features.skills

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.adapters.BasicListDelegationAdapter
import com.gatheringhallstudios.mhworlddatabase.common.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.views.SkillTreeView
import kotlinx.android.synthetic.main.list_generic.*
import kotlinx.android.synthetic.main.listitem_monster.view.*

class SkillListDelegate(private val onSelect: (SkillTreeView) -> Unit) :
        SimpleListDelegate<SkillTreeView>(SkillTreeView::class) {
    override fun getLayoutId() = R.layout.listitem_monster

    override fun bindListItem(v: View, item: SkillTreeView) {
        v.monster_name.text = item.name
    }
}

/**
 * Created by Carlos on 3/22/2018.
 */
class SkillListFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SkillListFragment.ViewModel::class.java)
    }

    val adapter = BasicListDelegationAdapter(SkillListDelegate({
        // todo: implement
    }))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter(adapter)

        viewModel.skills.observe(this, Observer({
            adapter.items = it
            adapter.notifyDataSetChanged()
        }))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity!!.title = getString(R.string.skills_title)
    }

    class ViewModel(application: Application) : AndroidViewModel(application) {
        private val dao = MHWDatabase.getDatabase(application).skillDao()
        val skills = dao.loadSkillTrees("en")
    }
}
