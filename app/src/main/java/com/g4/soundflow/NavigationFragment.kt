package com.g4.soundflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class NavigationFragment : Fragment() {
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
                NavigationMenu(selected) { sharedModel.select(it) }
            }
        }
    }
}

@Composable
private fun NavigationMenu(selected: Section, onSelect: (Section) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF172337)).padding(7.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(Modifier.height(18.dp))
        Text("SoundFlow", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("MÚSICA", fontSize = 10.sp, color = Color(0xFF9CACBF))
        Spacer(Modifier.height(18.dp))
        Section.entries.forEach { section ->
            val active = section == selected
            Text(
                text = section.title,
                modifier = Modifier.fillMaxWidth()
                    .background(if (active) Color(0xFF315A98) else Color.Transparent, RoundedCornerShape(9.dp))
                    .clickable { onSelect(section) }
                    .padding(horizontal = 7.dp, vertical = 14.dp),
                fontSize = 12.sp,
                lineHeight = 15.sp,
                color = if (active) Color.White else Color(0xFFD3DFEF),
                fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Start
            )
        }
    }
}
