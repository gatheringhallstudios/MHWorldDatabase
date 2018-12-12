package com.gatheringhallstudios.mhworlddatabase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.michaelflisar.changelog.ChangelogBuilder
import kotlinx.android.synthetic.main.fragment_changelog.*

class ChangelogActivity : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_changelog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ChangelogBuilder()
                .withTitle(getString(R.string.title_changelog))
                .withOkButtonLabel(getString(R.string.action_ok))
                .withRateButton(true)
                .withRateButtonLabel(getString(R.string.label_rate_app))
                .buildAndSetup(changelog_recycler)
    }
}