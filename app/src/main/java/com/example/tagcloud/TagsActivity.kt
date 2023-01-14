package com.example.tagcloud

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import com.example.tagcloud.ui.theme.TagCloudTheme
import com.example.tagcloud.ui.theme.fontFamily
import com.google.accompanist.flowlayout.FlowRow
import java.util.*
import kotlin.collections.ArrayList


class TagsActivity : ComponentActivity() {
    companion object {
        private const val TAGS_KEYS = "tags_keys"
        private const val TAGS_VALUES = "tags_values"
        private val tagsLst = listOf(
            "Юмор", "Еда", "Кино", "Рестораны", "Прогулки", "Политика", "Новости", "Автомобили",
            "Сериалы", "Рецепты", "Работа", "Своими руками", "Отдых", "Спорт",
            "Искусственный интелект", "Новые технологии", "Музыка", "Анимации", "Аниме", "Вязание",
            "Разработка", "Строительство", "Наука", "Автомобили", "Динозавры", "Бизнес",
            "Шоу", "Телевиденье", "Интернет", "Копирайтинг", "Заработок",
        )
        var tags: MutableMap<String, Boolean> =
            tagsLst.associateWith { false } as MutableMap<String, Boolean>
        var tagsPositions: MutableMap<String, IntOffset> =
            tagsLst.associateWith { IntOffset.Zero } as MutableMap<String, IntOffset>
        var isAny = false;

    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main()
        }

        val keysArray = savedInstanceState?.getStringArrayList(TAGS_KEYS) ?: ArrayList<String>(0)
        val valuesArray = savedInstanceState?.getBooleanArray(TAGS_VALUES) ?: BooleanArray(0)
        for (i in 0 until keysArray.size) {
            tags[keysArray[i]] = valuesArray[i]
        }

    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putStringArrayList(TAGS_KEYS, ArrayList(tags.keys.toList()))
        savedInstanceState.putBooleanArray(TAGS_VALUES, tags.values.toBooleanArray())
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("MutableCollectionMutableState", "UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun Main() {
        var isNotContinue by remember { mutableStateOf(false) }
        var isAnyRem = remember { mutableStateOf(isAny) }
        isNotContinue = true
        val context = LocalContext.current

        val positions = remember {
            mutableStateOf(tagsPositions)
        }
        val tagsBoolean = remember {
            mutableStateOf(tags)
        }
        TagCloudTheme {
            val context = LocalContext.current
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {startActivity(Intent(context, StartActivity::class.java))
                                  finish()},
                        backgroundColor = MaterialTheme.colors.primary
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Home")
                    }
                },
                // Defaults to false
                isFloatingActionButtonDocked = true
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LazyColumn(Modifier.fillMaxHeight()) {
                        item {
                            UpperView()
                        }
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateContentSize(
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                            ) {
                                TagsView(isAnyRem = isAnyRem, animated = isNotContinue)
                            }

                        }
                    }
                    Column(
                        Modifier
                            .fillMaxHeight()
                            .padding(bottom = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        if (isAnyRem.value) {
                            Button(
                                onClick = {
                                    if (isNotContinue) {
                                        isNotContinue = false
                                        Timer().schedule(object : TimerTask() {
                                            override fun run() {
                                                val intent =
                                                    Intent(context, HomeActivity::class.java)
                                                intent.putExtra("first", true)
                                                startActivity(intent)
                                            }
                                        }, 2000)
                                    }

                                },
                                modifier = Modifier.size(300.dp, 50.dp),
                                shape = RoundedCornerShape(30.dp)
                            ) {
                                Text(
                                    text = "Продолжить",
                                    color = MaterialTheme.colors.secondary,
                                    fontFamily = fontFamily,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                    }

                    if (!isNotContinue) {
                        Log.d("DUBUGMSG", "END")
                        for (tag in positions.value.keys) {
                            if (tagsBoolean.value[tag] == true) {
                                var offsetY =
                                    remember { Animatable(initialValue = positions.value[tag]!!.y.toFloat()) }

                                LaunchedEffect(isAnyRem) {
                                    offsetY.animateTo(
                                        targetValue = if (isAnyRem.value) 400f else positions.value[tag]!!.y.toFloat(),
                                        animationSpec = tween(durationMillis = 5000),
                                    )
                                }
                                Popup(offset = positions.value[tag]!!) {
                                    OutlinedButton(
                                        onClick = { /*TODO*/ },
                                        Modifier
                                            .scale(1.05f),
                                        shape = RoundedCornerShape(20.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = MaterialTheme.colors.primary)
                                    ) {
                                        Text(
                                            text = tag,
                                            color = MaterialTheme.colors.secondary
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TagsButton(text: String, isAnyRem: MutableState<Boolean>) {
        val isChoice = remember { mutableStateOf(tags[text]) }
        val scale = animateFloatAsState(if (tags[text] == true) 1.05f else 1f)
        OutlinedButton(
            onClick = {
                tags[text] = !isChoice.value!!
                isAny = tags.values.any { it }
                isAnyRem.value = tags.values.any { it }
                isChoice.value = !isChoice.value!!
                Log.d("DUBUGMSG", isAny.toString())
            },
            modifier = Modifier
                .scale(scale.value)
                .onGloballyPositioned { cord ->
                    tagsPositions[text] = IntOffset(
                        cord.positionInRoot().x.toInt() + 8,
                        cord.positionInRoot().y.toInt() + 4
                    )
                },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (!isChoice.value!!) MaterialTheme.colors.onBackground else MaterialTheme.colors.primary
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    color = if (!isChoice.value!!) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.secondary
                )
            }
        }

    }

    @Composable
    fun UpperView() {
        Box(
            Modifier
                .padding()
                .fillMaxWidth()
                .background(MaterialTheme.colors.onSurface)
                .padding(start = 20.dp, end = 20.dp)
                .height(80.dp), contentAlignment = Alignment.Center
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Отметьте то, что Вам интересно,\nчтобы настроить приложение",
                    color = Color.White
                )
                OutlinedButton(
                    onClick = { /*TODO*/ },
                    border = BorderStroke(1.dp, MaterialTheme.colors.secondary),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.outlinedButtonColors(backgroundColor = MaterialTheme.colors.onSurface)
                ) {
                    Text(text = "Позже", color = MaterialTheme.colors.secondary)
                }

            }
        }
    }

    @Composable
    fun TagsView(isAnyRem: MutableState<Boolean>, animated: Boolean) {
        val alpha = remember { Animatable(initialValue = 0f) }
        LaunchedEffect(animated) {
            alpha.animateTo(
                targetValue = if (animated) 1f else 0f,
                animationSpec = tween(durationMillis = 1000),
            )
        }
        Box(
            Modifier
                .padding(start = 10.dp, end = 10.dp)
                .fillMaxWidth()
                .alpha(alpha.value)
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                mainAxisSpacing = 10.dp,
            ) {
                for (tag in tags.keys)
                    TagsButton(text = tag, isAnyRem = isAnyRem)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        Main()
    }

    fun anyList(lst: List<Any?>): Boolean {
        return lst.any { it == true }
    }
}