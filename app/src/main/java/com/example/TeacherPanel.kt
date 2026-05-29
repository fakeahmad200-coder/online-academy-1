package com.example

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AlertRed
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherPanel(
    classes: List<QuranClass>,
    onTriggerStart: (classId: String) -> Unit,
    onManualCheckin: (classId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedClassForReport by remember { mutableStateOf<QuranClass?>(null) }
    var evaluationMakharij by remember { mutableStateOf("Good") }
    var evaluationTajweed by remember { mutableStateOf("Needs Practice") }
    var evaluationRevision by remember { mutableStateOf("Average") }
    
    // Pulse animation for the major ringing button
    val infiniteTransition = rememberInfiniteTransition(label = "ring_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_pulse"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Upper Greeting Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(EmeraldPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("QA", fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Assalamu Alaikum,", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Sheikh Ahmed Al-Masry", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    Text("Instructor Ijazah Rank: Hafiz-ul-Quran", fontSize = 11.sp, color = GoldAccent, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Large physical call button triggering the Student's PWA sound ringtone alert in-app
        val currentSlot = classes.find { it.status == ClassStatus.SCHEDULED }
        
        if (currentSlot != null) {
            Text(
                text = "Next Upcoming Scheduled Session",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "STUDENT ONLINE READY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SuccessGreen,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Recitation Slot with ${currentSlot.studentName}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Target: ${currentSlot.surahTarget} (Time: ${currentSlot.scheduledTime})",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // THE "WHATSAPP-RINGING" CALL TRIGGER BUTTON
                    Button(
                        onClick = { onTriggerStart(currentSlot.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .scale(pulseScale)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Call, contentDescription = "Ring Student", tint = Color.White)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "START CLASS & RING STUDENT ➔",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "*This immediately locks the student on a deep-linked Urdu phone alert ring tone on their portal.",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Done", tint = SuccessGreen, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Alhamdulillah! All scheduled sessions for today are completed.",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Daily Classes & Evaluation Schedule list
        Text("Your Daily Classes Schedule", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(10.dp))

        classes.forEach { session ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clickable { selectedClassForReport = session },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(session.studentName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(
                                            when (session.status) {
                                                ClassStatus.ACTIVE -> Color.Green
                                                ClassStatus.COMPLETED -> EmeraldPrimary
                                                else -> Color.Gray
                                            }
                                        )
                                        .size(6.dp)
                                )
                            }
                            Text("Scheduled Slot: ${session.scheduledTime}", fontSize = 11.sp, color = Color.Gray)
                        }

                        // Start/Grade CTA
                        if (session.status == ClassStatus.SCHEDULED) {
                            Button(
                                onClick = { onManualCheckin(session.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                border = BorderStroke(1.dp, EmeraldPrimary),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text("Mark Present", color = EmeraldPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Text(
                                text = "✓ Checkin Logged",
                                color = SuccessGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Curriculum Target: ${session.surahTarget}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (session.status == ClassStatus.COMPLETED) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
                                .padding(8.dp)
                        ) {
                            Column {
                                Text("Student Performance Report Card:", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = EmeraldPrimary)
                                Text("• Makharij: ${session.commentsMakharij}", fontSize = 10.sp)
                                Text("• Tajweed Code: ${session.commentsTajweed}", fontSize = 10.sp)
                                Text("• Memorization: ${session.commentsRevision}", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
