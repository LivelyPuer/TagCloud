package com.example.tagcloud

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tagcloud.interactive.activity.*
import com.example.tagcloud.ui.theme.TagCloudTheme
import com.example.tagcloud.ui.theme.fontFamily

class SelectInteractiveActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main()
        }
    }


    @Composable
    fun Main() {
        val context = LocalContext.current
        TagCloudTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                LazyColumn(
                    Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    contentPadding = PaddingValues(top = 50.dp, bottom = 50.dp)
                ) {

//                    Spacer(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(20.dp)
//                    )

                    item {
                        ToInteractiveButton(QuizActivity::class.java, "Многоступенчатые опросы")
                    }
                    item {
                        ToInteractiveButton(
                            ComparisonListActivity::class.java,
                            "Сопоставление элементов"
                        )
                    }
                    item {
                        ToInteractiveButton(
                            DragNDropMissingTextActivity::class.java,
                            "Перетаскивание вариантов в пропуски"
                        )
                    }
                    item {
                        ToInteractiveButton(
                            TextWithMissingActivity::class.java,
                            "Заполнение пропуска"
                        )
                    }
                    item {
                        ToInteractiveButton(StarsActivity::class.java, "Оценка статьи")
                    }
                    item {
                        ToInteractiveButton(LikeActivity::class.java, "Поставить лайк и поделиться")
                    }
                }
            }
        }
    }

    @Composable
    fun ToInteractiveButton(cls: Class<*>?, text: String) {
        val context = LocalContext.current

        Button(
            onClick = {
                startActivity(Intent(context, cls))
            },
            modifier = Modifier
                .size(300.dp, 50.dp),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text(
                text = text,
                color = MaterialTheme.colors.secondary,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
