package com.assistivetouch.gesture.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.assistivetouch.gesture.data.Gesture
import com.assistivetouch.gesture.data.GestureDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomGesturesScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var gestures by remember { mutableStateOf(listOf<Gesture>()) }

    LaunchedEffect(Unit) {
        val db = GestureDatabase.getDatabase(context)
        gestures = db.gestureDao().getAllGestures()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Custom Gestures", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            items(gestures.size) { index ->
                val gesture = gestures[index]
                GestureRow(
                    gesture = gesture,
                    onDelete = {
                        scope.launch {
                            val db = GestureDatabase.getDatabase(context)
                            db.gestureDao().deleteGesture(gesture)
                            gestures = db.gestureDao().getAllGestures()
                        }
                    },
                    onRename = {
                        // TODO: Show rename dialog
                    }
                )
            }

            item {
                Text(
                    text = "Create New Gesture...",
                    color = Color(0xFF0A84FF),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("create_gesture") }
                        .padding(vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
fun GestureRow(gesture: Gesture, onDelete: () -> Unit, onRename: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(gesture.name, color = Color.White, fontSize = 16.sp)
            Text("${gesture.duration}ms", color = Color.Gray, fontSize = 14.sp)
        }
        Row {
            IconButton(onClick = onRename) {
                Icon(Icons.Default.Edit, contentDescription = "Rename", tint = Color.Gray)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}
