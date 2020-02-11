package com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.list

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.util.RecyclerViewFragment
import com.gatheringhallstudios.mhworlddatabase.data.models.UserEquipmentSet
import com.gatheringhallstudios.mhworlddatabase.features.userequipmentsetbuilder.UserEquipmentSetViewModel
import com.gatheringhallstudios.mhworlddatabase.getRouter
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator

class UserEquipmentSetListFragment : RecyclerViewFragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(UserEquipmentSetViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getEquipmentSets()

        recyclerView.itemAnimator = SlideInRightAnimator(OvershootInterpolator(1f))
        viewModel.userEquipmentSets.observe(this, Observer<MutableList<UserEquipmentSet>> {
            // Setup recycler list adapter and the on-selected
            if (!containsEmptyElement(it)) {
                it.add(UserEquipmentSet.createEmptySet())
            }

            val adapter = UserEquipmentSetAdapterDelegate(it,
                    onSelect = { itr ->
                        if (itr.id == 0) { //Set has not yet been created
                            val newSet = viewModel.createEquipmentSet()
                            getRouter().navigateUserEquipmentSetDetail(newSet.id)

                        } else {
                            getRouter().navigateUserEquipmentSetDetail(itr.id)
                        }
                    },
                    onDelete = { itr, idx, adapter ->
                        viewModel.userEquipmentSets.value?.remove(itr)

                        //Show UNDO snackbar
                        val snackBar = Snackbar.make(view, getString(R.string.user_equipment_set_deleted, itr.name), Snackbar.LENGTH_LONG).setAction(R.string.action_undo) {
                            viewModel.userEquipmentSets.value?.add(idx, itr)
                            adapter.notifyItemInserted(idx)
                        }
                        snackBar.addCallback(object: Snackbar.Callback() {
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                //If snackbar was dismissed by anything other than the undo button
                                if (event != DISMISS_EVENT_ACTION) {
                                    viewModel.deleteEquipmentSet(itr)
                                }
                            }
                        })
                        snackBar.show()
                    })

            this.setAdapter(adapter)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.title_armor_set_builder)
    }

    private fun containsEmptyElement(list: MutableList<UserEquipmentSet>): Boolean {
        if (list.size == 0) {
            return false
        }
        return list.last().id == 0
    }
}
