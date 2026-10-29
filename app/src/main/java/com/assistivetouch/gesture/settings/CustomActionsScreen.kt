package com.assistivetouch.gesture.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.assistivetouch.gesture.data.CustomAction
import com.assistivetouch.gesture.data.Gesture
import com.assistivetouch.gesture.data.GestureDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomActionsScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var gestures by remember { mutableStateOf(listOf<Gesture>()) }
    var selectedAction by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val db = GestureDatabase.getDatabase(context)
        gestures = db.gestureDao().getAllGestures()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Custom Actions", color = Color.White) },
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
            item {
                Text(
                    text = "Custom actions allow you to interact directly with the AssistiveTouch icon without opening the menu.",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            // Action types
            item {
                ActionTypeRow("Single-Tap", "Open Menu", isExpanded = selectedAction == "single_tap") {
                    selectedAction = "single_tap"
                }
            }
            item {
                ActionTypeRow("Double-Tap", "None", isExpanded = selectedAction == "double_tap") {
                    selectedAction = "double_tap"
                }
            }
            item {
                ActionTypeRow("Long Press", "None", isExpanded = selectedAction == "long_press") {
                    selectedAction = "long_press"
                }
            }

            // Gesture selection
            selectedAction?.let { actionType ->
                item {
                    Text(
                        text = "Select Gesture for ${actionType.replace("_", " ").replaceFirstChar { it.uppercase() }}",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                items(gestures.size) { index ->
                    val gesture = gestures[index]
                    GestureSelectRow(gesture) {
                        scope.launch {
                            val db = GestureDatabase.getDatabase(context)
                            db.customActionDao().insertAction(
                                CustomAction(
                                    actionType = actionType,
                                    gestureId = gesture.id,
                                    actionName = gesture.name
                                )
                            )
                            navController.popBackStack()
                        }
                    }
                }

                item {
                    Text(
                        text = "None",
                        color = Color(0xFF0A84FF),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch {
                                    val db = GestureDatabase.getDatabase(context)
                                    db.customActionDao().insertAction(
                                        CustomAction(
                                            actionType = actionType,
                                            gestureId = null,
                                            actionName = "None"
                                        )
                                    )
                                    navController.popBackStack()
                                }
                            }
                            .padding(vertical = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ActionTypeRow(
    title: String,
    currentValue: String,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, color = Color.White, fontSize = 16.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(currentValue, color = Color.Gray, fontSize = 16.sp)
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun GestureSelectRow(gesture: Gesture, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(gesture.name, color = Color.White, fontSize = 16.sp)
        Text("${gesture.duration}ms", color = Color.Gray, fontSize = 14.sp)
    }
}
