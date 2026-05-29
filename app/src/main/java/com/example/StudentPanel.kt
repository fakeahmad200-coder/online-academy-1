package com.example

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
fun StudentPanel(
    students: List<StudentProfile>,
    classes: List<QuranClass>,
    onJoinSession: (quranClass: QuranClass) -> Unit,
    onMakeStripePayment: (studentId: String, amount: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeStudent = students.find { it.userId == "u-s2" } ?: students.firstOrNull() ?: StudentProfile(
        id = "s-dummy", userId = "u-dummy", name = "Aisha Khan", email = "aisha@gmail.com", parentName = "Kamran Khan", parentContact = "555", assignedTeacherId = "t1", assignedTeacherName = "Qari Ahmed"
    )

    val activeLiveClassSlot = classes.find { it.studentId == activeStudent.id && it.status == ClassStatus.ACTIVE }
    val scheduledClassSlot = classes.find { it.studentId == activeStudent.id && it.status == ClassStatus.SCHEDULED }

    var showStripeModal by remember { mutableStateOf(false) }

    // Pulsing animation for the active "Ustadh Online" join button
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_join")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btn_pulse"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Welcoming header card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(GoldAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("AK", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Assalamu Alaykum, student portal", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(activeStudent.name, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        Text("Assigned Ustadh: ${activeStudent.assignedTeacherName}", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = EmeraldPrimary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // DYNAMIC JOIN CLASS PORTAL TRIGGER
        Text("Your Virtual Live Quran Studio Room", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        if (activeLiveClassSlot != null) {
            // Ustadh has started session: Trigger Active Green Pulsing button!
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(modifier = Modifier.size(8.dp), color = Color.Green, shape = CircleShape) {}
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "USTADH IS ONLINE (CALLING YOU)",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Your daily Live Sura Recitation is ready!",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Topic: ${activeLiveClassSlot.surahTarget}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onJoinSession(activeLiveClassSlot) },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .scale(pulseScale)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Videocam, contentDescription = "Join Live Room", tint = Color.White)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("🟢 JOIN LIVE ROOM NOW", fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                    }
                }
            }
        } else if (scheduledClassSlot != null) {
            // Class is scheduled but teacher has not started yet. Button remains inactive
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SLOT SCHEDULED: ${scheduledClassSlot.scheduledTime}",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Ustadh has not initiated the session yet.",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "You will hear an incoming ring alert or can join here when Sheikh Ahmed Al-Masry clicks 'Start'.",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {},
                        enabled = false,
                        colors = ButtonDefaults.buttonColors(disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Waiting for Ustadh...", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Text(
                    text = "No classes scheduled for you today. Take some rest or practice via recording archives!",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ACADEMIC ACHIEVEMENT TRACKS
        Text("Your Memorization (Hifz) Milestones", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Overall Surah Progress Card", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = EmeraldPrimary)
                Spacer(modifier = Modifier.height(12.dp))

                HifzProgressBarRow("Makharij (Pronunciation Accuracy)", "Excellent (94%)", 0.94f)
                HifzProgressBarRow("Tajweed Code Comprehension", "Good (80%)", 0.80f)
                HifzProgressBarRow("Revision & Homework Tracker", "Intermediate (72%)", 0.72f)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // BILLING AND PAYMENT PORTAL (STRIPE GATEWAY)
        Text("Tuition Invoice Registry & Gateways", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Invoice Cycle: monthly recurring subscription", fontSize = 11.sp, color = Color.Gray)
                        Text(
                            text = "Due Balance: $${String.format("%.2f", activeStudent.feeBalance)}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = if (activeStudent.feeBalance > 0) AlertRed else SuccessGreen
                        )
                    }

                    if (activeStudent.feeBalance > 0) {
                        Button(
                            onClick = { showStripeModal = true },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("Pay via Stripe", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    } else {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.CheckCircle, "Paid", tint = SuccessGreen)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ARCHIVED RECORDED VIDEO LESSONS (VOD)
        Text("Your Archived Lessons Library (VOD)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        CachedLectureItem("Lecture #34: In-depth articulation rules of letter Qaf & Kha", "Length: 45:12 min | Recorded: 2 days ago")
        CachedLectureItem("Lecture #33: Tajweed Qalqala and phonetic echoing techniques", "Length: 38:00 min | Recorded: 5 days ago")
    }

    // Modal credit card processing sheet simulation
    if (showStripeModal) {
        StripeCheckoutDialog(
            activeStudent.feeBalance,
            onDismiss = { showStripeModal = false },
            onConfirmPay = { amount ->
                onMakeStripePayment(activeStudent.id, amount)
                showStripeModal = false
            }
        )
    }
}

@Composable
fun HifzProgressBarRow(label: String, grade: String, progress: Float) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(grade, fontSize = 11.sp, color = GoldAccent, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = progress,
            color = EmeraldPrimary,
            trackColor = Color.LightGray.copy(0.3f),
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun CachedLectureItem(title: String, meta: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {},
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFE8F5E9)),
                modifier = Modifier.size(36.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play back", tint = EmeraldPrimary)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(meta, fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun StripeCheckoutDialog(
    balance: Double,
    onDismiss: () -> Unit,
    onConfirmPay: (Double) -> Unit
) {
    var cardNo by remember { mutableStateOf("4242 •••• •••• 4242") }
    var cvv by remember { mutableStateOf("123") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Secured Stripe Payment Gateway", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = EmeraldPrimary) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "You are paying outstanding tuition fee balance of: $${String.format("%.2f", balance)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = cardNo,
                    onValueChange = { cardNo = it },
                    label = { Text("Credit Card Number") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = "12/28",
                        onValueChange = {},
                        label = { Text("Expiry (MM/YY)") },
                        modifier = Modifier.weight(1f).padding(end = 6.dp)
                    )
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { cvv = it },
                        label = { Text("CVV Code") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = "SECURE", tint = SuccessGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("SSL Secure encrypted transactions hosted by Stripe API.", fontSize = 10.sp, color = Color.Gray)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirmPay(balance) },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text("Confirm Pay", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
