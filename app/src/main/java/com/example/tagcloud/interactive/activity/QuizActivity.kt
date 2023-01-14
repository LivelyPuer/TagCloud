package com.example.tagcloud.interactive.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.MainThread
import androidx.compose.animation.Animatable
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tagcloud.elements.InterviewData
import com.example.tagcloud.interactive.BaseContainer
import com.example.tagcloud.ui.theme.TagCloudTheme
import java.util.*

class QuizActivity : ComponentActivity() {
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
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .background(Color.LightGray)
            ) {
                for (i in 1..n) {
                    Interview(
                        InterviewData(
                            "Топ опрос $i", listOf(
                                Pair(
                                    "Вопрос 1",
                                    listOf("Ответ 1", "Ответ 2", "Ответ 3", "Ответ 4")
                                ),
                                Pair("Вопрос 2", listOf("Ответ 1", "Ответ 2", "Ответ 3")),
                                Pair("Вопрос 3", listOf("Ответ 1", "Ответ 2")),
                                Pair(
                                    "Вопрос 4",
                                    listOf("Ответ 1", "Ответ 2", "Ответ 3", "Ответ 4")
                                ),
                            ), mutableListOf(
                                mutableListOf(1, 2, 3, 4),
                                mutableListOf(1, 2, 3),
                                mutableListOf(1, 2),
                                mutableListOf(1, 2, 3, 4),
                            )
                        )
                    )
                }

            }
        }
    }


    @SuppressLint("MutableCollectionMutableState")
    @Composable
    fun Interview(interview: InterviewData) {
        Card(
            modifier = Modifier
                .padding(5.dp),
            backgroundColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            elevation = 2.dp,
        ) {
            Surface(
                Modifier
                    .fillMaxWidth()
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF5F5F5)
            )
            {
                Column(Modifier.padding(top = 10.dp, start = 5.dp, end = 5.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text(
                            interview.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colors.primary
                        )
                    }
                    val answers = remember {
                        BooleanArray(interview.getCountQuestions()).toMutableList()
                            .toMutableStateList()
                    }

                    for (j in 0 until interview.getCountQuestions()) {
                        Log.d("DUBUGMSG", answers.toString())
                        if (j == 0 || answers[j - 1]) {
                            val number = "Вопрос " + (j + 1) + "/" + interview.getCountQuestions()
                            Text(number, fontSize = 12.sp, color = Color.Gray)
                            interview.getTitle(j)?.let { Text(it) }

                            val selectedField = remember {
                                mutableStateOf(-1)
                            }
                            for (i in 0 until interview.getCountInBlock(j)) {
                                Field(
                                    i,
                                    j,
                                    selectedField,
                                    answers,
                                    interview
                                )
                            }
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    "Ответили ${interview.sumVote(j)} чел.",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }

                    }

                }
            }
        }

    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Field(
        index: Int,
        questionIndex: Int,
        fieldBoolean: MutableState<Int>,
        answered: SnapshotStateList<Boolean>,
        interview: InterviewData

    ) {
        val color = remember { Animatable(Color(0xFFE0E0E0)) }
        LaunchedEffect(index == fieldBoolean.value) {
            color.animateTo(
                targetValue = if (index == fieldBoolean.value) Color(0xFF8BC34A) else Color(
                    0xFFE0E0E0
                ),
                animationSpec = tween(500)
            )
        }
        rememberRipple(color = Color.Green)
        Card({
            if (!answered[questionIndex]) {
                fieldBoolean.value = index
                answered[questionIndex] = true
                interview.addVote(questionIndex, index)
                Log.d("DUBUGMSG", answered.toString())
            }
        },
            Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 40.dp),
            !answered[questionIndex],
            RoundedCornerShape(10.dp),
            color.value,
            contentColorFor(backgroundColor),
            null,
            1.dp,
            remember { MutableInteractionSource() }) {
            Row(
                Modifier.padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    interview.getAnswerInBlock(questionIndex, index),
                    modifier = Modifier.weight(0.8f),
                    color = Color.White
                )
                Text(
                    if (answered[questionIndex]) "${
                        interview.voteInAnswerPercent(
                            questionIndex,
                            index
                        )
                    }%" else "",
                    modifier = Modifier.weight(0.2f),
                    color = Color.White
                )
            }

        }

    }

    @Composable
    fun CustomProgressBar(
        modifier: Modifier,
        width: Dp,
        backgroundColor: Color,
        foregroundColor: Brush,
        percent: Int,
        isShownText: Boolean
    ) {
        Box(
            modifier = modifier
                .background(backgroundColor)
                .width(width)
        ) {
            Box(
                modifier = modifier
                    .background(foregroundColor)
                    .width(width * percent / 100)
            )
        }
    }
}