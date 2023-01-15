package com.example.tagcloud.interactive.activity

//import com.example.tagcloud.shape.StarShape
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.tagcloud.R
import com.example.tagcloud.interactive.BaseContainer
import com.example.tagcloud.shape.StarShape
import com.example.tagcloud.ui.theme.TagCloudTheme
import com.pz64.shape.RoundedStarShape

class StarsActivity : ComponentActivity() {
    companion object {
        val roundedStarShape = RoundedStarShape(
            sides = 5,
            curve = 0.2,
            rotation = 17f,
            iterations = 360

        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main()
        }
    }

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
                    Rate()
                }
            }

        }
    }

    @Composable
    fun Rate() {
        Card(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .height(60.dp),
            backgroundColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            elevation = 2.dp,
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Оцените статью", color = MaterialTheme.colors.primary)
                Row(
                    modifier = Modifier.fillMaxHeight(),

                    horizontalArrangement = Arrangement.Center
                ) {
                    StarGroup()
                }
            }

        }
    }

    @Composable
    fun StarGroup() {
        val rate = remember { mutableStateOf(0) }
        Star(1, rate)
        Star(2, rate)
        Star(3, rate)
        Star(4, rate)
        Star(5, rate)
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Star(index: Int, rate: MutableState<Int>) {

        val progress = remember { Animatable(initialValue = 0f) }
        val selected = index <= rate.value
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.star_fav_animation))
        LaunchedEffect(selected) {
            progress.animateTo(
                targetValue = if (selected) 0.6f else 0.2f,
                animationSpec = tween(durationMillis = 1000 + 100 * index),
            )
        }
        Box(Modifier.size(width = 40.dp, height = 60.dp)) {
            LottieAnimation(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { rate.value = index },
                composition = composition,
                progress = { progress.value }
            )
        }
    }
}
