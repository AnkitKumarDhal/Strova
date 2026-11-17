package com.solqus.strova

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.solqus.strova.ui.StrovaApp
import com.solqus.strova.ui.StrovaViewModel
import com.solqus.strova.ui.theme.StrovaTheme

class MainActivity : ComponentActivity() {

    private val viewModel: StrovaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Only handle the launching intent once, not again after rotation.
        if (savedInstanceState == null) {
            handleShareIntent(intent)
        }

        setContent {
            StrovaTheme {
                StrovaApp(viewModel = viewModel)
            }
        }
    }

    // Called when Strova is already open and the user shares another link to it.
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleShareIntent(intent)
    }

    private fun handleShareIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            intent.getStringExtra(Intent.EXTRA_TEXT)?.let { viewModel.addSharedText(it) }
        }
    }
}