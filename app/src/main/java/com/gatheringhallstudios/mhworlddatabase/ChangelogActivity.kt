package com.gatheringhallstudios.mhworlddatabase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.michaelflisar.changelog.ChangelogBuilder
import com.gatheringhallstudios.mhworlddatabase.databinding.FragmentChangelogBinding

class ChangelogActivity : Fragment() {
    private var _binding: FragmentChangelogBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChangelogBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ChangelogBuilder()
                .withTitle(getString(R.string.title_changelog))
                .withOkButtonLabel(getString(R.string.action_ok))
                .withRateButton(true)
                .withRateButtonLabel(getString(R.string.label_rate_app))
                .buildAndSetup(_binding!!.changelogRecycler)
    }
}