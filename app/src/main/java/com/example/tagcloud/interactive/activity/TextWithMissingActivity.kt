package com.example.tagcloud.interactive.activity

import android.annotation.SuppressLint
import android.graphics.Paint.Align
import androidx.compose.material.TextFieldDefaults.indicatorLine
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.TextFieldDecorationBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tagcloud.TagsActivity
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
    @OptIn(ExperimentalMaterialApi::class)
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
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        mainAxisSpacing = 10.dp,
                    ) {
                        Box(Modifier.height(56.dp), contentAlignment = Alignment.BottomCenter) {
                            Text("Текст", textAlign = TextAlign.Center, fontSize = 20.sp)
                        }
                        var text by remember {
                            mutableStateOf("")
                        }
                        TextField(
                            value = text,
                            onValueChange = { text = it },
                            modifier = Modifier.indicatorLine(
                                enabled = true,
                                isError = false,
                                interactionSource = MutableInteractionSource(),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = Color.Black,
                                    unfocusedIndicatorColor = Color.Black,
                                    backgroundColor = Color.White
                                ),
                                focusedIndicatorLineThickness = 7.dp,
                                unfocusedIndicatorLineThickness = 4.dp,
                            ),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Black,
                                unfocusedIndicatorColor = Color.Black,
                                backgroundColor = Color.White
                            ),
                        )
                        Box(Modifier.height(56.dp), contentAlignment = Alignment.BottomCenter) {
                            Text(
                                "несколькими пропусками",
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp
                            )
                        }

                        TextField(value = text, onValueChange = { text = it })

                    }

                }
            }

        }
    }

    @ExperimentalMaterialApi
    @Composable
    fun MissingTextField(size: Int) {
        var text by remember { mutableStateOf("") }
        val singleLine = true
        val enabled = true
        val interactionSource = remember { MutableInteractionSource() }
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .indicatorLine(
                    enabled, false,
                    interactionSource,
                    TextFieldDefaults.textFieldColors(),
                    focusedIndicatorLineThickness = 0.dp,
                    unfocusedIndicatorLineThickness = 0.dp
                )
                .background(
                    TextFieldDefaults
                        .textFieldColors()
                        .backgroundColor(enabled).value,
                    TextFieldDefaults.TextFieldShape
                )
                .width(100.dp)
                .clip(RoundedCornerShape(10.dp)),
            singleLine = singleLine,
            interactionSource = interactionSource
        ) { innerTextField ->
            TextFieldDecorationBox(
                value = text,
                innerTextField = innerTextField,
                enabled = enabled,
                singleLine = singleLine,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                label = { Text("Label") }
            )
        }
    }
}