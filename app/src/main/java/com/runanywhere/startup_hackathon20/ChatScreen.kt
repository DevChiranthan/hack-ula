package com.runanywhere.startup_hackathon20

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.runanywhere.sdk.models.ModelInfo

// This is the composable that MainActivity was looking for
@Composable
fun ChatScreen(chatViewModel: ChatViewModel) {
    val messages by chatViewModel.messages.collectAsState()
    val isLoading by chatViewModel.isLoading.collectAsState()
    val availableModels by chatViewModel.availableModels.collectAsState()
    val downloadProgress by chatViewModel.downloadProgress.collectAsState()
    val currentModelId by chatViewModel.currentModelId.collectAsState()
    val statusMessage by chatViewModel.statusMessage.collectAsState()

    var textState by remember { mutableStateOf("") }
    var showModels by remember { mutableStateOf(true) } // Start by showing models

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
            .padding(16.dp)
    ) {
        // --- Top Bar ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (showModels) "Manage Models" else "Test Chat",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { showModels = !showModels }) {
                Text(if (showModels) "Go to Chat" else "Manage Models", color = Color(0xFFE94560))
            }
        }

        Spacer(Modifier.height(8.dp))
        Text(statusMessage, color = Color.Gray, fontSize = 12.sp)
        Spacer(Modifier.height(16.dp))

        if (showModels) {
            // --- Model Management UI ---
            ModelManagementUI(
                availableModels = availableModels,
                currentModelId = currentModelId,
                downloadProgress = downloadProgress,
                onDownload = { chatViewModel.downloadModel(it) },
                onLoad = { chatViewModel.loadModel(it) },
                onRefresh = { chatViewModel.refreshModels() }
            )
        } else {
            // --- Chat UI ---
            ChatUI(
                messages = messages,
                isLoading = isLoading,
                textState = textState,
                onTextChange = { textState = it },
                onSend = {
                    chatViewModel.sendMessage(textState)
                    textState = ""
                }
            )
        }
    }
}

// --- Model Management UI ---
@Composable
fun ModelManagementUI(
    availableModels: List<ModelInfo>,
    currentModelId: String?,
    downloadProgress: Float?,
    onDownload: (String) -> Unit,
    onLoad: (String) -> Unit,
    onRefresh: () -> Unit
) {
    Button(onClick = onRefresh, modifier = Modifier.fillMaxWidth()) {
        Text("Refresh Model List")
    }
    Spacer(Modifier.height(16.dp))

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(availableModels) { model ->
            ModelInfoCard(
                modelInfo = model,
                // --- FIX: Use .id ---
                isLoaded = model.id == currentModelId,
                downloadProgress = downloadProgress,
                // --- FIX: Use .id ---
                onDownload = { onDownload(model.id) },
                // --- FIX: Use .id ---
                onLoad = { onLoad(model.id) }
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun ModelInfoCard(
    modelInfo: ModelInfo,
    isLoaded: Boolean,
    downloadProgress: Float?,
    onDownload: () -> Unit,
    onLoad: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F3460))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(modelInfo.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
            Spacer(Modifier.height(4.dp))
            // --- FIX: Use .modelType ---
            // --- QUICK FIX: Hardcoded string to check UI ---
            Text("Type: ${"LLM Model"}", color = Color.LightGray, fontSize = 14.sp)
            //Text("Size: ${modelInfo.sizeInMb} MB", color = Color.LightGray, fontSize = 14.sp) // ModelInfo might not have this directly

            Spacer(Modifier.height(16.dp))

            if (isLoaded) {
                Text("✅ Model Loaded", color = Color.Green, fontWeight = FontWeight.Bold)
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = onDownload, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF533483))) {
                        Text("Download")
                    }
                    Button(onClick = onLoad, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE94560))) {
                        Text("Load")
                    }
                }
            }

            downloadProgress?.let {
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { it }, // Updated for new API
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// --- Chat UI ---
@Composable
fun ColumnScope.ChatUI(
    messages: List<ChatMessage>,
    isLoading: Boolean,
    textState: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        reverseLayout = true
    ) {
        items(messages.reversed()) { message ->
            MessageBubble(message)
        }
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = textState,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message...") },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF0F3460),
                unfocusedContainerColor = Color(0xFF0F3460),
                disabledContainerColor = Color(0xFF0F3460),
            )
        )
        Spacer(Modifier.width(8.dp))
        Button(onClick = onSend, enabled = !isLoading) {
            Text("Send")
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser) Color(0xFF533483) else Color(0xFF0F3460)
            )
        ) {
            Text(
                text = message.text,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
    }
}