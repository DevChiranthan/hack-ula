package com.runanywhere.startup_hackathon20

// --- FIX: Added missing import ---
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runanywhere.sdk.public.RunAnywhere
import com.runanywhere.sdk.public.extensions.listAvailableModels
import com.runanywhere.sdk.models.ModelInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Simple Message Data Class
data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

// ViewModel
class ChatViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _availableModels = MutableStateFlow<List<ModelInfo>>(emptyList())
    val availableModels: StateFlow<List<ModelInfo>> = _availableModels

    private val _downloadProgress = MutableStateFlow<Float?>(null)
    val downloadProgress: StateFlow<Float?> = _downloadProgress

    private val _currentModelId = MutableStateFlow<String?>(null)
    val currentModelId: StateFlow<String?> = _currentModelId

    private val _statusMessage = MutableStateFlow<String>("Initializing...")
    val statusMessage: StateFlow<String> = _statusMessage

    init {
        loadAvailableModels()
    }

    private fun loadAvailableModels() {
        viewModelScope.launch {
            try {
                val models = listAvailableModels()
                _availableModels.value = models
                _statusMessage.value = "Ready - Please download and load a model"
            } catch (e: Exception) {
                _statusMessage.value = "Error loading models: ${e.message}"
            }
        }
    }

    fun downloadModel(modelId: String) {
        viewModelScope.launch {
            try {
                _statusMessage.value = "Downloading model..."
                RunAnywhere.downloadModel(modelId).collect { progress ->
                    _downloadProgress.value = progress
                    _statusMessage.value = "Downloading: ${(progress * 100).toInt()}%"
                }
                _downloadProgress.value = null
                _statusMessage.value = "Download complete! Please load the model."
            } catch (e: Exception) {
                _statusMessage.value = "Download failed: ${e.message}"
                _downloadProgress.value = null
            }
        }
    }

    // ADD THIS NEW FUNCTION INSIDE ChatViewModel
    /**
     * Gets a single, non-streaming response from the on-device LLM.
     * This is perfect for utility prompts (like crisis detection or script generation).
     */
    suspend fun getAIResponse(prompt: String): String {
        if (_currentModelId.value == null) {
            _statusMessage.value = "Please load a model first"
            return "ERROR: MODEL NOT LOADED"
        }

        return try {
            val responseBuilder = StringBuilder()
            // Generate the response and collect all tokens into one string
            RunAnywhere.generateStream(prompt).collect { token ->
                responseBuilder.append(token)
            }
            // Return the final, complete response
            responseBuilder.toString()
        } catch (e: Exception) {
            Log.e("ChatViewModel", "getAIResponse failed", e)
            "ERROR: ${e.message}"
        }
    }
    fun loadModel(modelId: String) {
        viewModelScope.launch {
            try {
                _statusMessage.value = "Loading model..."
                val success = RunAnywhere.loadModel(modelId)
                if (success) {
                    _currentModelId.value = modelId
                    _statusMessage.value = "Model loaded! Ready to chat."
                } else {
                    _statusMessage.value = "Failed to load model"
                }
            } catch (e: Exception) {
                _statusMessage.value = "Error loading model: ${e.message}"
            }
        }
    }

    fun sendMessage(text: String) {
        if (_currentModelId.value == null) {
            _statusMessage.value = "Please load a model first"
            return
        }

        // Add user message
        _messages.value += ChatMessage(text, isUser = true)

        viewModelScope.launch {
            _isLoading.value = true

            try {
                // Generate response with streaming
                var assistantResponse = ""
                RunAnywhere.generateStream(text).collect { token ->
                    assistantResponse += token

                    // Update assistant message in real-time
                    val currentMessages = _messages.value.toMutableList()
                    if (currentMessages.lastOrNull()?.isUser == false) {
                        currentMessages[currentMessages.lastIndex] =
                            ChatMessage(assistantResponse, isUser = false)
                    } else {
                        currentMessages.add(ChatMessage(assistantResponse, isUser = false))
                    }
                    _messages.value = currentMessages
                }
            } catch (e: Exception) {
                _messages.value += ChatMessage("Error: ${e.message}", isUser = false)
            }

            _isLoading.value = false
        }
    }

    fun refreshModels() {
        loadAvailableModels()
    }
}