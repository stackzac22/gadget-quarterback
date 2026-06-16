package com.stackzac22.quarterback

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stackzac22.quarterback.csiradar.CsiRadarScreen
import com.stackzac22.quarterback.ui.theme.QuarterbackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuarterbackTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var screen by remember { mutableStateOf("home") }
                    when (screen) {
                        "csi_radar" -> CsiRadarScreen(onBack = { screen = "home" })
                        else -> HudDashboard(onOpenCsiRadar = { screen = "csi_radar" })
                    }
                }
            }
        }
    }
}

@Composable
fun HudDashboard(onOpenCsiRadar: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Gadget Quarterback", style = MaterialTheme.typography.headlineMedium)
        Text(text = "ESP32 Ecosystem HUD", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onOpenCsiRadar) { Text("CSI Radar") }
    }
}

@Preview(showBackground = true)
@Composable
fun HudDashboardPreview() {
    QuarterbackTheme {
        HudDashboard()
    }
}
