package com.example.tagcloud.interactive.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.tagcloud.R
import com.example.tagcloud.interactive.BaseContainer

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
            val scrollState = rememberScrollState()
            val endReached by remember {
                derivedStateOf {
                    scrollState.value == scrollState.maxValue
                }
            }
            var n by remember {
                mutableStateOf(5)
            }
            if (endReached) {
                n += 5
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .background(Color.LightGray),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (i in 1..n) {
                    UnderPanel()
                }
            }
        }
    }

    @Composable
    fun UnderPanel() {
        Card(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .height(50.dp),
            backgroundColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            elevation = 2.dp,
        ) {
            Row(horizontalArrangement = Arrangement.Start) {
                Like()
                Share()
            }
        }
    }

    @Composable
    fun Share() {
        val clicked = remember { mutableStateOf(false) }
        val count = remember { mutableStateOf((0..100).random()) }
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.press_share))

        Box(
            Modifier
                .height(50.dp)
                .width(45.dp)
        ) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Поделитесь этой статьей с друзьями")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            val context = LocalContext.current
            LottieAnimation(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    clicked.value = true
                    count.value += 1
                    context.startActivity(shareIntent)
                },
                composition = composition,
                isPlaying = clicked.value,
                clipSpec = LottieClipSpec.Progress(0f, 1f),
                speed = 2f,
            )
        }
        Box(
            Modifier
                .height(50.dp)
                .width(30.dp), contentAlignment = Alignment.CenterStart) {
            Text(text = count.value.toString())
        }
    }

    @Composable
    fun Like() {
        var liked by remember { mutableStateOf(false) }
        val count = remember { mutableStateOf((0..100).random()) }
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.like))
        val first = remember { mutableStateOf(true) }
        Box(
            Modifier
                .height(50.dp)
                .width(50.dp)
        ) {
            val progress = remember { Animatable(initialValue = 0f) }
            LaunchedEffect(liked) {
                progress.animateTo(
                    targetValue = if (liked) 1f else 0.4f,
                    animationSpec = tween(durationMillis = 1000),
                )
            }
            LottieAnimation(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) { liked = !liked },
                composition = composition,
                progress = {progress.value}
            )
        }
        Box(
            Modifier
                .height(50.dp)
                .width(30.dp), contentAlignment = Alignment.CenterStart) {
            Text(text = (count.value + if (liked) 1 else 0).toString())
        }
    }
}