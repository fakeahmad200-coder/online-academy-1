package com.example

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.CastForEducation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun IncomingCallOverlay(
    activeCall: QuranClass,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // 1. Trigger rhythmic phone ringing sounds on emulator
    LaunchedEffect(activeCall) {
        coroutineScope.launch {
            try {
                // Initialize low latency DTMF/CDMA tone generator
                val toneGen = ToneGenerator(AudioManager.STREAM_RING, 100)
                while (true) {
                    // Double high frequency ring (standard phone call ring cadence)
                    toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 400)
                    delay(500)
                    toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 400)
                    delay(3000) // wait before next ring group
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 2. Continuous pulsing animations representing "Incoming Ringing Wave"
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_ringing")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F2C24), // deep cosmic emerald
                        Color(0xFF050D0B)  // obsidian pitch
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 48.dp, horizontal = 24.dp)
        ) {
            // Screen Header Indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0x3300C853))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Surface(
                    color = Color(0xFF00E676),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(8.dp)
                        .scale(pulseScale)
                ) {}
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "QURAN ACADEMY INCOMING SESSION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB9F6CA),
                    letterSpacing = 1.5.sp
                )
            }

            // Core Profile Center
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Pulsing Green ring surrounding Islamic Icon identifier
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(160.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(Color(0x1100C853))
                            .border(2.dp, Color(0x3300C853), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0F1E19))
                            .border(3.dp, Color(0xFF0D5E35), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CastForEducation,
                            contentDescription = "Quran Academy logo",
                            tint = Color(0xFFD4AF37),
                            modifier = Modifier.size(56.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // The Urdu Notification Title and details requested explicitly by the user
                Text(
                    text = "آپ کی قرآن کلاس شروع ہو رہی ہے",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Your Quran class is starting!",
                    fontSize = 15.sp,
                    color = Color(0xFF90A4AE),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Instructor: ${activeCall.teacherName}",
                    fontSize = 18.sp,
                    color = Color(0xFFFFD700), // gold
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Topic: ${activeCall.surahTarget}",
                    fontSize = 13.sp,
                    color = Color.White.copy(0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            // Interactive calling buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // DECLINE BUTTON (Red)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = onDecline,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        shape = CircleShape,
                        modifier = Modifier.size(72.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CallEnd,
                            contentDescription = "Decline Call",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Decline",
                        color = Color.White.copy(0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // ACCEPT BUTTON (Green - animated glow)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = onAccept,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
                        shape = CircleShape,
                        modifier = Modifier
                            .size(72.dp)
                            .scale(pulseScale),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Accept Call",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Accept",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}
