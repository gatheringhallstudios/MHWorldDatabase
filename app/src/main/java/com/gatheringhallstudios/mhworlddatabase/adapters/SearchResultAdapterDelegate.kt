package com.gatheringhallstudios.mhworlddatabase.adapters

import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.adapters.common.SimpleListDelegate
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.data.types.DataType
import com.gatheringhallstudios.mhworlddatabase.data.views.SearchResult
import com.gatheringhallstudios.mhworlddatabase.getRouter
import kotlin.reflect.KClass

class SearchResultAdapterDelegate : SimpleListDelegate<SearchResult, IconLabelTextCell>() {
    override fun getDataClass(): KClass<SearchResult> {
        return SearchResult::class
    }

    override fun onCreateView(parent: ViewGroup): View {
        return IconLabelTextCell(parent.context)
    }

    override fun bindView(view: IconLabelTextCell, data: SearchResult) {
        view.setLabelText(data.name)

        view.setOnClickListener {
            val router = view.getRouter()
            when (data.data_type) {
                DataType.LOCATION -> router.navigateLocationDetail(data.id)
                DataType.ITEM -> router.navigateItemDetail(data.id)
                DataType.MONSTER -> router.navigateMonsterDetail(data.id)
                DataType.SKILL -> router.navigateSkillDetail(data.id)
                else -> throw UnsupportedOperationException("Non existing endpoint")
            }
        }
    }
}