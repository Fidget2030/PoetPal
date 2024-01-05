package com.example.poetpal.ui.screens.reading

import com.example.poetpal.domain.Poem
import com.example.poetpal.domain.PoemMeta

data class MetaState(val metas: List<PoemMeta> = listOf())

data class SelectedState(val selectedPoem: Poem? = null)
