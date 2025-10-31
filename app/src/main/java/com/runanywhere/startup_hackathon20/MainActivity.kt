// In app/src/main/java/com/runanywhere/startup_hackathon20/MainActivity.kt

package com.runanywhere.startup_hackathon20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.runanywhere.startup_hackathon20.ui.theme.RunAnywhereAITheme

// --- TEMPORARY MAIN ACTIVITY FOR UI TESTING ---

class MainActivity : ComponentActivity() {

    private val chatViewModel: ChatViewModel by viewModels()
    // --- All other logic is temporarily removed ---
    // private lateinit var speechRecognizer: SpeechRecognizer
    // private lateinit var speechRecognizerIntent: Intent
    // private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- All logic from onCreate is temporarily removed ---

        setContent {
            // This should fix all the "Overload resolution" errors
            RunAnywhereAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // We are skipping navigation and showing ChatScreen directly
                    ChatScreen(chatViewModel = chatViewModel)
                }
            }
        }
    }

    // --- All other functions are temporarily removed to fix build errors ---
    // override fun onDestroy() { ... }
    // private fun navigateToLifebuoy(...) { ... }
    // private fun checkAndRequestPermissions() { ... }
    // override fun onRequestPermissionsResult(...) { ... }
    // private fun startAegisListening() { ... }
    // private fun processAegisText(...) { ... }
    // private fun sendSmsAlert(...) { ... }
    // private fun triggerAICall(...) { ... }
}

// --- All Composable screens are temporarily removed except for ChatScreen ---
// @Composable
// fun MainScreen(...) { ... }

// @Composable
// fun LifebuoyScreen(...) { ... }

// @Preview(showBackground = true)
// @Composable
// fun DefaultPreview() { ... }