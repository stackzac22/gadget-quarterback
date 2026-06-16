package com.stackzac22.quarterback.csiradar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import java.util.concurrent.TimeUnit

private const val MEAN_HISTORY_SIZE = 120
private const val ALERT_LOG_SIZE = 20

private fun motionColor(motion: String): Color = when (motion) {
    "still" -> Color(0xFF3FB950)
    "motion" -> Color(0xFFD29922)
    "gesture" -> Color(0xFFF85149)
    "alert" -> Color(0xFFF85149)
    else -> Color(0xFF8B949E)
}

private fun motionLabel(motion: String): String = when (motion) {
    "still" -> "■ STILL"
    "motion" -> "▲ MOTION"
    "gesture" -> "★ GESTURE"
    "alert" -> "⚠ ALERT"
    else -> "■ INIT"
}

@Composable
fun CsiRadarScreen(onBack: () -> Unit) {
    var host by remember { mutableStateOf("192.168.12.235:5000") }
    var connectTrigger by remember { mutableStateOf(0) }
    var status by remember { mutableStateOf("disconnected") }
    var mags by remember { mutableStateOf(listOf<Float>()) }
    var meanHistory by remember { mutableStateOf(List(MEAN_HISTORY_SIZE) { 0f }) }
    var motion by remember { mutableStateOf("init") }
    var deviceId by remember { mutableStateOf("-") }
    var rssi by remember { mutableStateOf(0) }
    var alerts by remember { mutableStateOf(listOf<String>()) }

    DisposableEffect(host, connectTrigger) {
        val client = OkHttpClient.Builder()
            .pingInterval(10, TimeUnit.SECONDS)
            .build()
        status = "connecting"
        val request = Request.Builder().url("ws://$host/ws").build()
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                status = "connected"
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val json = try { JSONObject(text) } catch (e: Exception) { return }
                if (json.has("ping")) return

                if (json.has("alert")) {
                    val alert = json.optJSONObject("alert")
                    val trigger = alert?.optString("trigger")?.takeIf { it.isNotEmpty() }
                        ?: alert?.optString("type")?.takeIf { it.isNotEmpty() } ?: "?"
                    val source = alert?.optString("id")?.takeIf { it.isNotEmpty() }
                        ?: alert?.optString("bssid")?.takeIf { it.isNotEmpty() } ?: "?"
                    alerts = (listOf("$trigger from $source") + alerts).take(ALERT_LOG_SIZE)
                }

                json.optJSONArray("mags")?.let { arr ->
                    mags = List(arr.length()) { i -> arr.optDouble(i, 0.0).toFloat() }
                }
                motion = json.optString("motion", motion)
                deviceId = json.optString("id", deviceId)
                rssi = json.optInt("rssi", rssi)
                if (json.has("mean")) {
                    val mean = json.optDouble("mean", 0.0).toFloat()
                    meanHistory = (meanHistory.drop(1) + mean)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                status = "error: ${t.message}"
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                status = "disconnected"
            }
        }
        val webSocket = client.newWebSocket(request, listener)

        onDispose {
            webSocket.close(1000, null)
            client.dispatcher.executorService.shutdown()
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                TextButton(onClick = onBack) { Text("← Back") }
                Text("CSI Radar", style = MaterialTheme.typography.titleLarge)
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = host,
                    onValueChange = { host = it },
                    label = { Text("host:port") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )
                Button(onClick = { connectTrigger++ }) { Text("Connect") }
            }

            Text("status: $status", style = MaterialTheme.typography.bodySmall)
            Text(
                text = motionLabel(motion),
                color = motionColor(motion),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 4.dp),
            )
            Text(
                "id:$deviceId  rssi:${rssi}dBm  frames:${meanHistory.size}",
                style = MaterialTheme.typography.bodySmall,
            )

            Text("Subcarrier Magnitude", modifier = Modifier.padding(top = 12.dp, bottom = 4.dp))
            SpectrumChart(mags, modifier = Modifier.fillMaxWidth().height(140.dp))

            Text("Mean Magnitude (history)", modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
            MeanHistoryChart(meanHistory, modifier = Modifier.fillMaxWidth().height(140.dp))

            Text("Alerts", modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
            LazyColumn(contentPadding = PaddingValues(vertical = 4.dp)) {
                items(alerts) { line ->
                    Text(line, color = Color(0xFFF85149), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun SpectrumChart(mags: List<Float>, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (mags.isEmpty()) return@Canvas
            val maxVal = 50f
            val barWidth = size.width / mags.size
            mags.forEachIndexed { i, v ->
                val h = (v.coerceIn(0f, maxVal) / maxVal) * size.height
                drawRect(
                    color = Color(0xFF1F6FEB),
                    topLeft = androidx.compose.ui.geometry.Offset(i * barWidth, size.height - h),
                    size = androidx.compose.ui.geometry.Size(barWidth * 0.9f, h),
                )
            }
        }
    }
}

@Composable
private fun MeanHistoryChart(history: List<Float>, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (history.size < 2) return@Canvas
            val maxVal = 40f
            val stepX = size.width / (history.size - 1)
            val path = androidx.compose.ui.graphics.Path()
            history.forEachIndexed { i, v ->
                val x = i * stepX
                val y = size.height - (v.coerceIn(0f, maxVal) / maxVal) * size.height
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(
                path = path,
                color = Color(0xFF58A6FF),
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = 3f,
                    cap = StrokeCap.Round,
                ),
            )
        }
    }
}
