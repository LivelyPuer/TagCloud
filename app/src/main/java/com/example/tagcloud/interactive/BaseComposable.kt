package com.example.tagcloud.interactive

import android.annotation.SuppressLint
import android.view.Surface
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tagcloud.ui.theme.TagCloudTheme


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BaseContainer(onClickFloatingAction: () -> Unit, color: Color=MaterialTheme.colors.primary, content: @Composable () -> Unit) {
    TagCloudTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onClickFloatingAction,
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(Icons.Filled.Home, contentDescription = "Home")
                }
            },
            // Defaults to false
            isFloatingActionButtonDocked = true
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                content()
            }
        }
    }
}