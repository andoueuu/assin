package com.assistivetouch.gesture.recorder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.assistivetouch.gesture.data.Gesture
import com.assistivetouch.gesture.data.GestureDatabase
import com.assistivetouch.gesture.data.GestureEvent
import kotlinx.coroutines.launch

class CreateGestureActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateGestureScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGestureScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    var isRecording by remember { mutableStateOf(false) }
    var events by remember { mutableStateOf(listOf<GestureEvent>()) }
    var startTime by remember { mutableStateOf(0L) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var gestureName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create New Gesture", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { (context as? android.app.Activity)?.finish() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                actions = {
                    if (events.isNotEmpty()) {
                        IconButton(onClick = { showSaveDialog = true }) {
                            Icon(Icons.Default.Check, contentDescription = "Save", tint = Color(0xFF0A84FF))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Orientation indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (isLandscape) "Landscape Mode" else "Portrait Mode",
                    color = if (isRecording) Color.Red else Color.Gray,
                    fontSize = 14.sp
                )
            }

            // Recording area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color(0xFF1C1C1E), RoundedCornerShape(16.dp))
                    .pointerInput(isRecording) {
                        if (isRecording) {
                            detectTapGestures(
                                onPress = { offset ->
                                    val timestamp = System.currentTimeMillis() - startTime
                                    events = events + GestureEvent(
                                        x = offset.x,
                                        y = offset.y,
                                        timestamp = timestamp,
                                        action = GestureEvent.ACTION_DOWN
                                    )
                                    tryAwaitRelease()
                                    events = events + GestureEvent(
                                        x = offset.x,
                                        y = offset.y,
                                        timestamp = System.currentTimeMillis() - startTime,
                                        action = GestureEvent.ACTION_UP
                                    )
                                }
                            )
                        }
                    }
                    .pointerInput(isRecording) {
                        if (isRecording) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    val timestamp = System.currentTimeMillis() - startTime
                                    events = events + GestureEvent(
                                        x = offset.x,
                                        y = offset.y,
                                        timestamp = timestamp,
                                        action = GestureEvent.ACTION_DOWN
                                    )
                                },
                                onDrag = { change, _ ->
                                    val timestamp = System.currentTimeMillis() - startTime
                                    events = events + GestureEvent(
                                        x = change.position.x,
                                        y = change.position.y,
                                        timestamp = timestamp,
                                        action = GestureEvent.ACTION_MOVE
                                    )
                                },
                                onDragEnd = {
                                    val lastEvent = events.lastOrNull()
                                    if (lastEvent != null) {
                                        events = events + GestureEvent(
                                            x = lastEvent.x,
                                            y = lastEvent.y,
                                            timestamp = System.currentTimeMillis() - startTime,
                                            action = GestureEvent.ACTION_UP
                                        )
                                    }
                                }
                            )
                        }
                    }
            ) {
                // Draw recorded path
                Canvas(modifier = Modifier.fillMaxSize()) {
                    if (events.size > 1) {
                        val path = Path()
                        events.forEachIndexed { index, event ->
                            when (event.action) {
                                GestureEvent.ACTION_DOWN -> path.moveTo(event.x, event.y)
                                GestureEvent.ACTION_MOVE -> path.lineTo(event.x, event.y)
                                GestureEvent.ACTION_UP -> path.lineTo(event.x, event.y)
                            }
                        }
                        drawPath(
                            path = path,
                            color = Color.Cyan,
                            style = Stroke(width = 8f, cap = StrokeCap.Round)
                        )
                    }
                }

                if (events.isEmpty() && !isRecording) {
                    Text(
                        text = "Draw your gesture here",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            // Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        if (isRecording) {
                            isRecording = false
                        } else {
                            events = emptyList()
                            startTime = System.currentTimeMillis()
                            isRecording = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRecording) Color.Red else Color(0xFF0A84FF)
                    )
                ) {
                    Text(if (isRecording) "Stop Recording" else "Start Recording")
                }

                if (events.isNotEmpty()) {
                    OutlinedButton(
                        onClick = { events = emptyList() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text("Clear")
                    }
                }
            }
        }
    }

    // Save dialog
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save Gesture", color = Color.White) },
            text = {
                TextField(
                    value = gestureName,
                    onValueChange = { gestureName = it },
                    label = { Text("Gesture Name") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1C1C1E),
                        unfocusedContainerColor = Color(0xFF1C1C1E)
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (gestureName.isNotBlank()) {
                            scope.launch {
                                val db = GestureDatabase.getDatabase(context)
                                val duration = events.lastOrNull()?.timestamp ?: 0L
                                val gesture = Gesture.fromEvents(
                                    name = gestureName,
                                    events = events,
                                    duration = duration,
                                    orientation = if (isLandscape) 1 else 0,
                                    screenWidth = context.resources.displayMetrics.widthPixels,
                                    screenHeight = context.resources.displayMetrics.heightPixels
                                )
                                db.gestureDao().insertGesture(gesture)
                                (context as? android.app.Activity)?.finish()
                            }
                        }
                    }
                ) {
                    Text("Save", color = Color(0xFF0A84FF))
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = Color(0xFF1C1C1E)
        )
    }
}
