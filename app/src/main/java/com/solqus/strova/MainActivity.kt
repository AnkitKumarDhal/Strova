
package com.solqus.strova

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.solqus.strova.ui.StrovaApp
import com.solqus.strova.ui.theme.StrovaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StrovaTheme {
                StrovaApp()
            }
        }
    }
}
