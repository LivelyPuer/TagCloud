package com.example.tagcloud

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.tagcloud.ui.theme.TagCloudTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main()
        }
    }

    @Composable
    fun Main() {
        Log.d("DEBGMSG", intent.getBooleanExtra("first", false).toString())
        if (intent.getBooleanExtra("first", false)) {
            val toast = Toast.makeText(
                applicationContext,
                "Вы можете изменить интересы в настройках (позже)",
                Toast.LENGTH_LONG
            )
            toast.show()
        }
        TagCloudTheme() {
            TagCloudTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, Alignment.CenterHorizontally) {
                        Text(text = "Здесь пока ничего нет....")

                    }
                }
            }
        }
    }
}