package com.gatheringhallstudios.mhworlddatabase.features.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentAboutBinding

class AboutFragment : androidx.fragment.app.Fragment() {
    private var _binding: FragmentAboutBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val aboutLayout = _binding!!.aboutLayout

        // Look for IconLabelTextCells and hook up their ClickListeners
        for (i in 0..aboutLayout.childCount) {
            if (aboutLayout.getChildAt(i) is IconLabelTextCell) {
                val cell = aboutLayout.getChildAt(i) as IconLabelTextCell

                cell.setOnClickListener {
                    val href = cell.tag as? String
                    if (!href.isNullOrBlank()) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(href))
                        startActivity(intent)
                    }
                }
            }
        }
    }
}