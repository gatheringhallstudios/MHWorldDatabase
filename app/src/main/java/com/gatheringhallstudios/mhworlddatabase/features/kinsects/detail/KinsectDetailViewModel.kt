package com.gatheringhallstudios.mhworlddatabase.features.kinsects.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.models.Kinsect
import com.gatheringhallstudios.mhworlddatabase.data.models.KinsectFull
import com.gatheringhallstudios.mhworlddatabase.util.tree.TreeNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KinsectFamilyData(
        val familyPath: TreeNode<Kinsect>,
        val finalKinsects: List<Kinsect>
)

class KinsectDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = MHWDatabase.getDatabase(application).kinsectDao()

    var kinsectId: Int = -1
        private set

    val kinsectData = MutableLiveData<KinsectFull>()
    val kinsectFamilyData = MutableLiveData<KinsectFamilyData>()

    /**
     * The current value of the stored kinsect data.
     * Alias for kinsectData.value
     */
    val kinsect: KinsectFull? get() = kinsectData.value

    /**
     * Sets the kinsect id to be loaded.
     */
    fun loadKinsect(kinsectId: Int) {
        if (this.kinsectId == kinsectId) return

        this.kinsectId = kinsectId

        GlobalScope.launch(Dispatchers.Main) {
            val langId = AppSettings.dataLocale

            val kinsect = withContext(Dispatchers.IO) {
                dao.loadKinsectFullSync(langId, kinsectId)
            }

            val node = withContext(Dispatchers.IO) {
                val trees = dao.loadKinsectTrees(langId)
                trees.getModel(kinsectId)
            }

            // Update values if still relevant
            if (node != null && kinsect.entityId == kinsectId) {
                kinsectData.value = kinsect
                kinsectFamilyData.value = KinsectFamilyData(
                        familyPath = node.getPathSubtree(),
                        finalKinsects = node.leaves.map { it.value }
                )
            }
        }
    }
}
