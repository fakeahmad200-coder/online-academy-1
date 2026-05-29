package com.example

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoomSDKView(
    activeSession: QuranClass,
    onCompleteLesson: (commentsMakharij: String, commentsTajweed: String, commentsRevision: String) -> Unit,
    onLeave: () -> Unit
) {
    var makharijComment by remember { mutableStateOf("Excellent pronunciation") }
    var tajweedComment by remember { mutableStateOf("Proper Ikhfa rule observance") }
    var revisionComment by remember { mutableStateOf("Weak fluency, needs 3x revision") }
    
    // Board drawing path coordination
    val drawnPoints = remember { mutableStateListOf<Offset>() }
    var isMuted by remember { mutableStateOf(false) }
    var isVideoOff by remember { mutableStateOf(false) }

    // Audio Waveform pulse simulation for live microphone indicators
    val infiniteTransition = rememberInfiniteTransition(label = "audio_wave")
    val waveHeight1 by infiniteTransition.animateFloat(
        initialValue = 10f, targetValue = 45f,
        animationSpec = infiniteRepeatable(tween(400, easing = LinearOutSlowInEasing), RepeatMode.Reverse),
        label = "w1"
    )
    val waveHeight2 by infiniteTransition.animateFloat(
        initialValue = 5f, targetValue = 35f,
        animationSpec = infiniteRepeatable(tween(300, easing = FastOutLinearInEasing), RepeatMode.Reverse),
        label = "w2"
    )
    val waveHeight3 by infiniteTransition.animateFloat(
        initialValue = 12f, targetValue = 50f,
        animationSpec = infiniteRepeatable(tween(500, easing = LinearEasing), RepeatMode.Reverse),
        label = "w3"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1513)) // dark cosmic jade
    ) {
        // Class Status Header Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF141E1A))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CastForEducation,
                        contentDescription = "Studio",
                        tint = GoldAccent,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "LIVE QURAN CLASSROOM (WEST-2)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = GoldAccent,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "Student: ${activeSession.studentName} | Teacher: ${activeSession.teacherName}",
                            fontSize = 13.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                // Live Stream tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFD32F2F))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(modifier = Modifier.size(6.dp), shape = CircleShape, color = Color.White) {}
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("LIVE SDK FEED", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // LEFT COLUMN (Main Quran Whiteboard Viewer)
            Column(
                modifier = Modifier
                    .weight(1.8f)
                    .fillMaxHeight()
                    .padding(12.dp)
            ) {
                // Quran text card with Tajweed markings
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F7F2)), // antique parchment
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📖 Interactive Board: ${activeSession.surahTarget}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F5E35)
                            )
                            Button(
                                onClick = { drawnPoints.clear() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0x334CAF50)),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Clear Pointer", tint = Color(0xFF1B5E20), modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Clear Highlights", color = Color(0xFF1B5E20), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Arabic Script Center
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        drawnPoints.add(change.position)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Real Arabic Script
                                Text(
                                    text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                    fontSize = 26.sp,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E3D37),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )

                                Text(
                                    text = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
                                    fontSize = 32.sp,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F5E35), // color coded Tajweed
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                Text(
                                    text = "الرَّحْمَٰنِ الرَّحِيمِ مَالِكِ يَوْمِ الدِّينِ",
                                    fontSize = 30.sp,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A237E), // color coded Tajweed
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Drag your finger to highlight specific terms with Qari pointer",
                                    color = Color.DarkGray.copy(0.6f),
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center
                                )
                            }

                            // Interactive canvas pointer drawings (drawnPoints)
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawnPoints.forEach { point ->
                                    drawCircle(
                                        color = Color(0x66FFEB3B), // translucent high-contrast highlighter
                                        radius = 18f,
                                        center = point
                                    )
                                }
                            }
                        }

                        // Color rules legend
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF1EDE4), RoundedCornerShape(6.dp))
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            TajweedLegendItem(color = Color(0xFF0F5E35), label = "Qalqala")
                            TajweedLegendItem(color = Color(0xFF1A237E), label = "Idgham")
                            TajweedLegendItem(color = Color(0xFFB71C1C), label = "Ghunnah")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom Zoom Controllers (Hardware Toggle simulation bar)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF141E1A))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { isMuted = !isMuted }) {
                            Icon(
                                imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                                contentDescription = "Mic",
                                tint = if (isMuted) Color.Red else EmeraldPrimary
                            )
                        }

                        IconButton(onClick = { isVideoOff = !isVideoOff }) {
                            Icon(
                                imageVector = if (isVideoOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                                contentDescription = "Video",
                                tint = if (isVideoOff) Color.Red else EmeraldPrimary
                            )
                        }

                        IconButton(onClick = { /* Simulated Screenshare */ }) {
                            Icon(
                                imageVector = Icons.Default.ScreenShare,
                                contentDescription = "Share board",
                                tint = Color.LightGray
                            )
                        }

                        // Danger exit button
                        Button(
                            onClick = onLeave,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A1D1D)),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ExitToApp, contentDescription = "Exit", tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Disconnect", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // RIGHT COLUMN (Participant Feeds + Teacher grading panel scroll)
            Column(
                modifier = Modifier
                    .weight(1.1f)
                    .fillMaxHeight()
                    .padding(top = 12.dp, end = 12.dp, bottom = 12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Teacher's camera view feed card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF22302A))
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Qari Ahmed (Ustadh)", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            
                            // Audio Waveform animation running
                            Row(
                                modifier = Modifier.height(30.dp),
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                (1..6).forEach { index ->
                                    val h = when (index % 3) {
                                        0 -> waveHeight1
                                        1 -> waveHeight2
                                        else -> waveHeight3
                                    }
                                    Box(
                                        modifier = Modifier
                                            .padding(end = 3.dp)
                                            .width(4.dp)
                                            .height(h.dp)
                                            .background(EmeraldPrimary, CircleShape)
                                    )
                                }
                            }
                        }
                        
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp)
                                .size(8.dp)
                                .background(Color.Green, CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Student's camera view feed card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF152A20))
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(activeSession.studentName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            
                            Text("🔴 Listening...", color = Color.White.copy(0.6f), fontSize = 10.sp, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Teacher Evaluation Panel: Ready-made Checklist instead of empty box! (CRITICAL SaaS constraint)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2F27)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "Qari Grading Sheet",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldAccent,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // MAKHARIJ SELECTION
                        Text("Pronunciation (Makharij):", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            EvaluatorChip(selected = makharijComment == "Excellent pronunciation", text = "Excellent", onClick = { makharijComment = "Excellent pronunciation" })
                            Spacer(modifier = Modifier.width(4.dp))
                            EvaluatorChip(selected = makharijComment == "Weak Makharij on dual letters", text = "Weak on letter", onClick = { makharijComment = "Weak Makharij on dual letters" })
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // TAJWEED SELECTION
                        Text("Fluency & Tajweed Rules:", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            EvaluatorChip(selected = tajweedComment == "Proper Ikhfa rule observance", text = "Good Ikhfa", onClick = { tajweedComment = "Proper Ikhfa rule observance" })
                            Spacer(modifier = Modifier.width(4.dp))
                            EvaluatorChip(selected = tajweedComment == "Requires practice on Qalqalah", text = "Fix Qalqalah", onClick = { tajweedComment = "Requires practice on Qalqalah" })
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // REVISION CHECKLIST
                        Text("History & Revision Track:", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            EvaluatorChip(selected = revisionComment == "Flawless homework verification", text = "Flawless", onClick = { revisionComment = "Flawless homework verification" })
                            Spacer(modifier = Modifier.width(4.dp))
                            EvaluatorChip(selected = revisionComment == "Weak fluency, needs 3x revision", text = "Needs 3x redo", onClick = { revisionComment = "Weak fluency, needs 3x revision" })
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Large action button submitting grade & closing Zoom SDK workspace!
                        Button(
                            onClick = {
                                onCompleteLesson(makharijComment, tajweedComment, revisionComment)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Complete Lesson ✓", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TajweedLegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluatorChip(selected: Boolean, text: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) EmeraldPrimary else Color(0xFF14241C),
        ),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.height(28.dp)
    ) {
        Box(modifier = Modifier.fillMaxHeight().padding(horizontal = 8.dp), contentAlignment = Alignment.Center) {
            Text(text = text, fontSize = 10.sp, color = if (selected) Color.White else Color.LightGray, fontWeight = FontWeight.Bold)
        }
    }
}
