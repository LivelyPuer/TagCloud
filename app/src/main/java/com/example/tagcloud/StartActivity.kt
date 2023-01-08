package com.example.tagcloud

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tagcloud.ui.theme.TagCloudTheme
import com.example.tagcloud.ui.theme.fontFamily

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main()
        }
    }

    @Composable
    fun Main() {
        val context = LocalContext.current
        var animated by remember { mutableStateOf(true) }
        val rotation = remember { Animatable(initialValue = 360f) }
        val scale = remember { Animatable(initialValue = 0f) }
        val scaleButton = remember { Animatable(initialValue = 0f) }
        val offsetY = remember { Animatable(initialValue = 0f) }
        TagCloudTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                LaunchedEffect(animated) {
                    scale.animateTo(
                        targetValue = if (animated) 0.7f else 0f,
                        animationSpec = tween(durationMillis = 1000),
                    )
                    rotation.animateTo(
                        targetValue = if (animated) 0f else 360f,
                        animationSpec = tween(durationMillis = 1000),

                        )
                    offsetY.animateTo(
                        targetValue = if (animated) 300f else 0f,
                        animationSpec = tween(durationMillis = 1000),
                    )
                    scaleButton.animateTo(
                        targetValue = if (animated) 1f else 0f,
                        animationSpec = tween(durationMillis = 1000),
                    )
                }
                Column(
                    Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val image: Painter = painterResource(id = R.drawable.news)
                    Image(modifier = Modifier
                        .graphicsLayer { rotationY = rotation.value }
                        .scale(scale.value)
                        .absoluteOffset(y = -offsetY.value.dp),
                        painter = image,
                        contentDescription = "Desc")
                    Text(
                        modifier = Modifier
                            .scale(scaleButton.value)
                            .offset(y = (-200).dp),
                        color = MaterialTheme.colors.primary,
                        fontSize = 18.sp,
                        text = "Добро пожаловать!",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        modifier = Modifier
                            .scale(scaleButton.value)
                            .offset(y = (-200).dp),
                        color = MaterialTheme.colors.primary,
                        fontSize = 17.sp,
                        text = "Самые персонализированные новости",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Button(
                        onClick = {
                                  startActivity(Intent(context, TagsActivity::class.java))
                        },
                        modifier = Modifier
                            .size(300.dp, 50.dp)
                            .scale(scaleButton.value),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Text(
                            text = "Продолжить",
                            color = MaterialTheme.colors.secondary,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))
                    Button(
                        onClick = {
                            startActivity(Intent(context, SelectInteractiveActivity::class.java))
                        },
                        modifier = Modifier
                            .size(300.dp, 50.dp)
                            .scale(scaleButton.value),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Text(
                            text = "Тест интерактивных элементов",
                            color = MaterialTheme.colors.secondary,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }

                }

            }
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        Main()
    }
}