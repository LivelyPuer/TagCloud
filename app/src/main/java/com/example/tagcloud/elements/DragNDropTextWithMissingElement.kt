package com.example.tagcloud.elements

import android.util.Log
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned

data class DragNDropTextWithMissingElement(val simpleTextMissing: TextWithMissingElement,
                                           val variants: List<Variant>, val id: Int){
    init {
        for (variant in variants){
            variant.id = id
        }
    }
}

data class Variant(val text: String, var id: Int = 0)
