package com.example.tagcloud.interactive.activity

//import com.example.tagcloud.shape.StarShape
import android.os.Bundle
import android.util.Log
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tagcloud.R
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

    @Composable
    fun Main() {
        TagCloudTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
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
    }
    @Composable
    fun StarGroup(){
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

        Surface(
            onClick = {
                rate.value = index
            },
            shape = roundedStarShape
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = if (index <= rate.value) Color(0xFFFD8B27) else
                            Color(0xFFD1D1D1)
                    )
                    .size(40.dp),
            )
        }
    }
}
