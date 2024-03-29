package com.example.tagcloud.interactive.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tagcloud.R
import com.example.tagcloud.elements.*
import com.example.tagcloud.interactive.BaseContainer
import com.example.tagcloud.interactive.DragTarget
import com.example.tagcloud.interactive.DropTarget
import com.example.tagcloud.interactive.LongPressDraggable
import com.example.tagcloud.ui.theme.TagCloudTheme
import com.example.tagcloud.ui.theme.fontFamily
import com.google.accompanist.flowlayout.FlowRow

class DragNDropMissingTextActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main()
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun Main() {
        BaseContainer(onClickFloatingAction = { finish() }, color = Color.LightGray) {
            LongPressDraggable(modifier = Modifier.fillMaxSize()) {
                val dark = isSystemInDarkTheme()
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
                        .verticalScroll(scrollState)
                        .background(if (dark) MaterialTheme.colors.background else Color.LightGray)

                ) {
                    val tel = TextWithMissingElement(
                        "Пропуски",
                        "Текст {{}} несколькими {{}} и вариантами.",
                        listOf("с", "пропусками")
                    )
                    for (i in 1..n) {
                        val data = DragNDropTextWithMissingElement(
                            tel,
                            listOf(
                                Variant("с"),
                                Variant("пропусками"),
                                Variant("и"),
                                Variant("вариантами")
                            ),
                            i
                        )
                        DnDTextWithMissing(data)
                    }
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    @Composable
    fun DnDTextWithMissing(
        dragNDropTextWithMissingElement: DragNDropTextWithMissingElement,
    ) {
        val isSubmitted = remember {
            mutableStateOf(false)
        }
        val dark = isSystemInDarkTheme()
        val data = dragNDropTextWithMissingElement.simpleTextMissing
        Card(
            modifier = Modifier
                .padding(5.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = 2.dp,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    data.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = "Вставьте пропущенные слова",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    mainAxisSpacing = 10.dp,
                ) {
                    for (elem in data.content) {
                        when (elem) {
                            is TextMissing -> {
                                MissingText(
                                    text = elem.text,
                                    size = elem.size,
                                    isSubmitted,
                                    dragNDropTextWithMissingElement,
                                )
                            }
                            is SimpleText -> {
                                SimpleText(text = elem.text)
                            }
                        }
                    }
                }
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    mainAxisSpacing = 10.dp,
                ) {
                    for (variant in dragNDropTextWithMissingElement.variants) {
                        if (isSubmitted.value) {
                            Card(
                                Modifier
                                    .height(40.dp)
                                    .width((variant.text.length * 10 + 30).dp),
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(2.dp, MaterialTheme.colors.primary)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        variant.text,
                                        color = if (dark) Color.LightGray else Color.DarkGray
                                    )
                                }
                            }
                        } else {

                            DragTarget(modifier = Modifier, dataToDrop = variant) {
                                Card(
                                    Modifier
                                        .height(40.dp)
                                        .width((variant.text.length * 10 + 30).dp),
                                    shape = RoundedCornerShape(20.dp),
                                    border = BorderStroke(2.dp, MaterialTheme.colors.primary)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            variant.text,
                                            color = if (dark) Color.LightGray else Color.DarkGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Button(
                    onClick = {

                        isSubmitted.value = true
                    },
                    modifier = Modifier
                        .size(300.dp, 50.dp)
                        .padding(bottom = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (dark) MaterialTheme.colors.onBackground else Color.White),
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
    fun BowForTextMissing(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
        Box(modifier.height(30.dp), contentAlignment = Alignment.BottomCenter) {
            content()
        }
    }

    @Composable
    fun SimpleText(text: String) {
        val dark = isSystemInDarkTheme()
        BowForTextMissing {
            Text(
                text, textAlign = TextAlign.Center, fontSize = 20.sp,
                color = if (dark) Color.LightGray else Color.DarkGray
            )
        }
    }

    @Composable
    fun MissingText(
        text: String,
        size: Int,
        isSubmitted: MutableState<Boolean>,
        dragNDropTextWithMissingElement: DragNDropTextWithMissingElement,
    ) {
        var layout by remember { mutableStateOf<TextLayoutResult?>(null) }
        var textValue by remember {
            mutableStateOf("")
        }

        var columnWidthPx by remember {
            mutableStateOf(0f)
        }
        var columnStartPx by remember {
            mutableStateOf(0f)
        }
        var sizeText by remember {
            mutableStateOf(size)
        }
        DropTarget<Variant>(
            modifier = Modifier
        ) { isInBound, dropText ->
            val bgColor = if (isInBound) {
                Color.Red
            } else {
                Color.White
            }
            dropText?.let {
                Log.d("DUBUGMSG", dragNDropTextWithMissingElement.id.toString() + " " + dropText.id)
                if (isInBound && dragNDropTextWithMissingElement.id == dropText.id) {
                    textValue = dropText.text
                    sizeText = dropText.text.length
                }
            }
            BowForTextMissing {
                BasicTextField(
                    enabled = false,
                    singleLine = true,
                    value = textValue,
                    onValueChange = {
                        if (!isSubmitted.value && it.length <= sizeText) textValue = it

                    },
                    onTextLayout = { layout = it },
                    modifier = Modifier
                        .width((sizeText * 11).dp)
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
                                        if (isSubmitted.value) if (textValue == text) Color.Green else Color.Red else Color(
                                            0, 119, 255
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
                        color = if (isSubmitted.value) if (textValue == text) Color.Green else Color.Red else MaterialTheme.colors.primary,
                        textDecoration = TextDecoration.None
                    ),
                )
            }
        }
    }
}