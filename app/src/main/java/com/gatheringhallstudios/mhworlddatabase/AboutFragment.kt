package com.gatheringhallstudios.mhworlddatabase

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gatheringhallstudios.mhworlddatabase.components.IconLabelTextCell
import kotlinx.android.synthetic.main.cell_icon_label_text.view.*
import kotlinx.android.synthetic.main.fragment_about.*

class AboutDialogFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Look for IconLabelTextCells and hook up their ClickListeners
        for (i in 0..about_layout.childCount) {
            if (about_layout.getChildAt(i) is IconLabelTextCell) {
                val cell = about_layout.getChildAt(i) as IconLabelTextCell
                val label = cell.label_text

                cell.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(label.text.toString())))
                }
            }
        }
    }
}