package com.example.tagcloud.interactive.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tagcloud.HomeActivity
import com.example.tagcloud.SelectInteractiveActivity
import com.example.tagcloud.TagsActivity
import com.example.tagcloud.elements.ComparisonListElement
import com.example.tagcloud.interactive.DragTarget
import com.example.tagcloud.ui.theme.TagCloudTheme
import com.example.tagcloud.ui.theme.fontFamily
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.*
import kotlin.math.roundToInt

class ComparisonListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main()
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    @Preview
    @Composable
    fun Main() {
        TagCloudTheme {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            finish()
                        },
                        backgroundColor = MaterialTheme.colors.primary,
                    ) {
                        Icon(Icons.Filled.Home, contentDescription = "Home")
                    }
                },
                // Defaults to false
                isFloatingActionButtonDocked = true
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    val scrollState = rememberScrollState()
                    val endReached by remember {
                        derivedStateOf {
                            scrollState.value == scrollState.maxValue
                        }
                    }
                    var n by remember {
                        mutableStateOf(5)
                    }
                    if (endReached){
                        n += 5
                    }
                    Column(Modifier.verticalScroll(scrollState)) {
                        val data = ComparisonListElement(
                            "Арабские - Римские",
                            listOf("Один", "Два", "Три", "Четыре"),
                            listOf("I", "II", "III", "IV")
                        )

                        for (i in 1..n){
                            Comparison(data)

                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Comparison(data: ComparisonListElement) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val isSubmitted = remember {
                mutableStateOf(false)
            }

            val tasks = remember {
                BooleanArray(data.first.size).toMutableList().toMutableStateList()
            }
            Text(
                data.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colors.primary
            )
            Text(
                text = "Сопоставьте две колонки перетаскивая элементы правой ",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            Row(
                modifier = Modifier.padding(
                    start = 20.dp, top = 10.dp, bottom = 5.dp
                ), horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {


                VerticalStaticList(Modifier.weight(0.5f), data, tasks, isSubmitted)
//                                    VerticalStaticList(Modifier.weight(0.5f), data, tasks, isSubmitted)
                VerticalReorderList(Modifier.weight(0.5f), data, tasks, isSubmitted)
                Spacer(modifier = Modifier.size(10.dp))
            }
            val number =
                if (isSubmitted.value) "Верно ${tasks.count { it }}/${tasks.size}" else " "
            Text(number, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.size(5.dp))
            Button(
                onClick = {
                    isSubmitted.value = true
                },
                modifier = Modifier.size(300.dp, 50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
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

    @Composable
    fun VerticalStaticList(
        modifier: Modifier = Modifier,
        dataElements: ComparisonListElement,
        tasks: SnapshotStateList<Boolean>,
        isSubmitted: MutableState<Boolean>,
    ) {
        val data = dataElements.first
        Column(
            modifier = modifier, verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            for (item in data) {
                var scale = remember { Animatable(initialValue = 1f) }
                Card(
                    modifier = Modifier
                        .height(50.dp)
                        .scale(scale.value),
                    shape = RoundedCornerShape(0.dp),
                    border = BorderStroke(
                        4.dp,
                        if (isSubmitted.value) if (tasks[data.indexOf(item)]) Color.Green else Color.Red else MaterialTheme.colors.primary
                    ),
                    elevation = 0.dp
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = item, color = MaterialTheme.colors.primary
                        )
                    }

                }
            }
        }
    }

    @Composable
    fun VerticalReorderList(
        modifier: Modifier = Modifier,
        dataElements: ComparisonListElement,
        tasks: SnapshotStateList<Boolean>,
        isSubmitted: MutableState<Boolean>,
    ) {
        val data = remember { mutableStateOf(dataElements.second.shuffled()) }
        val state = rememberReorderableLazyListState(onMove = { from, to ->
            if (!isSubmitted.value) {
                data.value = data.value.toMutableList().apply {
                    add(to.index, removeAt(from.index))
                }
            }
        })
        LazyColumn(
            state = state.listState,
            modifier = modifier
                .reorderable(state)
                .detectReorderAfterLongPress(state)
                .height(60.dp * dataElements.first.size - 10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(data.value, { it }) { item ->
                ReorderableItem(state, key = item) { isDragging ->
                    val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                    var scale = remember { Animatable(initialValue = 1f) }
                    if (!isDragging) {
                        tasks[dataElements.second.indexOf(item)] =
                            dataElements.second.indexOf(item) == data.value.indexOf(item)
                        Log.d("DUBUGMSG", tasks.toString())
                    }
                    LaunchedEffect(isDragging) {
                        scale.animateTo(
                            targetValue = if (isDragging) 1.05f else 1f,
                            animationSpec = tween(durationMillis = 500),
                        )
                    }
                    Card(
                        modifier = Modifier
                            .height(50.dp)
                            .scale(scale.value),
                        backgroundColor = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(4.dp, MaterialTheme.colors.primary),
                        elevation = elevation.value
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = item,
                                modifier = Modifier.detectReorderAfterLongPress(state),
                                color = Color.White
                            )
                        }

                    }
                }
            }
        }
    }
}