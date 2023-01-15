package com.example.tagcloud.interactive.activity

import android.annotation.SuppressLint
import android.graphics.Paint.Align
import androidx.compose.material.TextFieldDefaults.indicatorLine
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.TextFieldDecorationBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.*
import com.example.tagcloud.TagsActivity
import com.example.tagcloud.elements.SimpleText
import com.example.tagcloud.elements.TextMissing
import com.example.tagcloud.elements.TextWithMissingElement
import com.example.tagcloud.interactive.BaseContainer
import com.example.tagcloud.ui.theme.TagCloudTheme
import com.example.tagcloud.ui.theme.fontFamily
import com.google.accompanist.flowlayout.FlowRow

class TextWithMissingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main()
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun Main() {
        BaseContainer(color = Color.LightGray, onClickFloatingAction = { finish() }) {
            val dark = isSystemInDarkTheme()
            val data = TextWithMissingElement(
                "Пропуски",
                "Текст {{}} несколькими пропусками {{}} вариантами.",
                listOf("с", "и")
            )
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
                Modifier
                    .fillMaxSize()
                    .background(if (dark) MaterialTheme.colors.background else Color.LightGray)
                    .verticalScroll(scrollState)) {
                for (i in 1..n) {
                    TextWithMissing(data)
                }
            }
        }
    }

    @Composable
    fun TextWithMissing(data: TextWithMissingElement) {
        Card(
            modifier = Modifier
                .padding(5.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = 2.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val isSubmitted = remember {
                    mutableStateOf(false)
                }
                Text(
                    data.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = "Впишите пропущенные слова",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    mainAxisSpacing = 10.dp,
                ) {

                    for (elem in data.content) {
                        when (elem) {
                            is TextMissing -> {
                                MissingText(text = elem.text, size = elem.size, isSubmitted)
                            }
                            is SimpleText -> {
                                SimpleText(text = elem.text)
                            }
                        }
                    }
                }
                Button(
                    onClick = {
                        isSubmitted.value = true
                    },
                    modifier = Modifier.size(300.dp, 50.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isSystemInDarkTheme()) MaterialTheme.colors.onBackground else Color.White),
                    border = BorderStroke(4.dp, MaterialTheme.colors.primary),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text(
                        text = "Подтвердить",
                        color = MaterialTheme.colors.primary,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    @Composable
    fun BowForTextMissing(content: @Composable BoxScope.() -> Unit) {
        Box(Modifier.height(30.dp), contentAlignment = Alignment.BottomCenter) {
            content()
        }
    }

    @Composable
    fun SimpleText(text: String) {
        BowForTextMissing {
            Text(text, textAlign = TextAlign.Center, fontSize = 20.sp,
                color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
            )
        }
    }

    @Composable
    fun MissingText(text: String, size: Int, isSubmitted: MutableState<Boolean>) {
        var layout by remember { mutableStateOf<TextLayoutResult?>(null) }

        var text_value by remember {
            mutableStateOf("")
        }
        var columnWidthPx by remember {
            mutableStateOf(0f)
        }
        var columnStartPx by remember {
            mutableStateOf(0f)
        }
        BowForTextMissing {
            BasicTextField(
                enabled = !isSubmitted.value,
                value = text_value,
                onValueChange = { if (!isSubmitted.value && it.length <= size) text_value = it },
                onTextLayout = { layout = it },
                modifier = Modifier
                    .width((size * 11).dp)
                    .onGloballyPositioned { coordinates ->
                        // Set column height using the LayoutCoordinates
                        columnWidthPx = coordinates.size.width.toFloat()
                        columnStartPx =
                            coordinates.size.center.x.toFloat() - (coordinates.size.width.toFloat() - coordinates.size.center.x.toFloat())
                    }
                    .drawBehind {
                        layout?.let {
                            val thickness = 5f
                            val spacingExtra = 4f
                            val offsetY = 6f

                            for (i in 0 until it.lineCount) {
                                drawPath(
                                    path = Path().apply {
                                        moveTo(
                                            columnStartPx,
                                            it.getLineBottom(i) - spacingExtra + offsetY
                                        )
                                        lineTo(
                                            columnWidthPx,
                                            it.getLineBottom(i) - spacingExtra + offsetY
                                        )
                                    },
                                    if (isSubmitted.value) if (text_value == text) Color.Green else Color.Red else Color(
                                        0,
                                        119,
                                        255
                                    ),
                                    style = Stroke(
                                        width = thickness,
                                        pathEffect = PathEffect.cornerPathEffect(10f),
                                    )
                                )
                            }
                        }
                    },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = if (isSubmitted.value) if (text_value == text) Color.Green else Color.Red else MaterialTheme.colors.primary,
                    textDecoration = TextDecoration.None
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            )
        }
    }

}