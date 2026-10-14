package com.g4.soundflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ContentFragment : Fragment() {
    private val sharedModel: SoundFlowViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            val selected by sharedModel.selectedSection.collectAsState()
            MaterialTheme {
                when (selected) {
                    Section.PERFIL -> ProfileScreen()
                    Section.FOTOS -> PhotosScreen()
                    Section.VIDEO -> VideoScreen()
                    Section.WEB -> WebScreen()
                    Section.BOTONES -> MusicScreen()
                }
            }
        }
    }
}
