package com.example.tagcloud.interactive.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tagcloud.R
import com.example.tagcloud.interactive.BaseContainer
import com.example.tagcloud.interactive.activity.StarsActivity.Companion.roundedStarShape

class LikeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main()
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun Main() {
        BaseContainer(onClickFloatingAction = { finish() }) {
            Surface(
                onClick = {},
                shape = roundedStarShape,
            ) {
                Box(
                    modifier = Modifier
                        .background(color = Color(0x70205C47))
                        .size(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.like_svgrepo_com),
                        contentDescription = "",
                        modifier = Modifier.size(56.dp)
                    )
                }
            }
        }
    }
}