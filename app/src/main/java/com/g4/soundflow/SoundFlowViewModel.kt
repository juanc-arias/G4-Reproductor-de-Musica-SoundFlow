package com.g4.soundflow

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class Section(val title: String) {
    PERFIL("Perfil"), FOTOS("Fotos"), VIDEO("Video"), WEB("Web"), BOTONES("Botones")
}

/** El menú izquierdo publica la sección elegida; el panel derecho la observa. */
class SoundFlowViewModel : ViewModel() {
    private val _selectedSection = MutableStateFlow(Section.PERFIL)
    val selectedSection = _selectedSection.asStateFlow()

    fun select(section: Section) {
        _selectedSection.value = section
    }
}
