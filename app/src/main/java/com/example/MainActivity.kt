package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainSaaSContainer()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSaaSContainer() {
    // Current Active Navigation tab index:
    // 0: Admin, 1: Teacher, 2: Student, 3: Sales Booking, 4: Dev Code (Architect)
    var currentTab by remember { mutableStateOf(2) } // default open with Student tab for easy Call-answering flow

    // Collect reactive state flows from SaaSRepository
    val teachers by SaaSRepository.teachers.collectAsStateWithLifecycle()
    val students by SaaSRepository.students.collectAsStateWithLifecycle()
    val classes by SaaSRepository.classes.collectAsStateWithLifecycle()
    val payments by SaaSRepository.payments.collectAsStateWithLifecycle()
    val bookings by SaaSRepository.bookings.collectAsStateWithLifecycle()
    val activeRingingCall by SaaSRepository.activeRingingCall.collectAsStateWithLifecycle()
    val liveActiveSession by SaaSRepository.liveActiveSession.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Only render bottom bar if we are NOT inside a fullscreen incoming call or active Live Zoom class
            if (activeRingingCall == null && liveActiveSession == null) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    NavigationBarItem(
                        selected = currentTab == 0,
                        onClick = { currentTab = 0 },
                        icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
                        label = { Text("Admin", fontWeight = FontWeight.Bold) }
                    )
                    NavigationBarItem(
                        selected = currentTab == 1,
                        onClick = { currentTab = 1 },
                        icon = { Icon(Icons.Default.School, contentDescription = "Teacher") },
                        label = { Text("Teacher", fontWeight = FontWeight.Bold) }
                    )
                    NavigationBarItem(
                        selected = currentTab == 2,
                        onClick = { currentTab = 2 },
                        icon = { Icon(Icons.Default.Face, contentDescription = "Student") },
                        label = { Text("Student", fontWeight = FontWeight.Bold) }
                    )
                    NavigationBarItem(
                        selected = currentTab == 3,
                        onClick = { currentTab = 3 },
                        icon = { Icon(Icons.Default.LocalOffer, contentDescription = "Marketing") },
                        label = { Text("Sales", fontWeight = FontWeight.Bold) }
                    )
                    NavigationBarItem(
                        selected = currentTab == 4,
                        onClick = { currentTab = 4 },
                        icon = { Icon(Icons.Default.Code, contentDescription = "Specs") },
                        label = { Text("Dev Console", fontWeight = FontWeight.Bold) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // CORE NAVIGATION SWITCH CHANGER
            when {
                // 1. WhatsApp Fullscreen Incoming Calling Overlay takes precedence!
                activeRingingCall != null -> {
                    IncomingCallOverlay(
                        activeCall = activeRingingCall!!,
                        onAccept = { SaaSRepository.acceptCall() },
                        onDecline = { SaaSRepository.rejectCall() }
                    )
                }
                
                // 2. Teleport into live browser Zoom Video SDK classroom if active
                liveActiveSession != null -> {
                    ZoomSDKView(
                        activeSession = liveActiveSession!!,
                        onCompleteLesson = { commentsMakharij, commentsTajweed, commentsRevision ->
                            SaaSRepository.finishClass(
                                classId = liveActiveSession!!.id,
                                commentsMakharij = commentsMakharij,
                                commentsTajweed = commentsTajweed,
                                commentsRevision = commentsRevision
                            )
                        },
                        onLeave = { SaaSRepository.endLiveSession() }
                    )
                }

                // 3. Tab views
                else -> {
                    when (currentTab) {
                        0 -> AdminPanel(
                            teachers = teachers,
                            students = students,
                            classes = classes,
                            payments = payments,
                            onAddTeacher = { name, email, cert, rate ->
                                SaaSRepository.addTeacher(name, email, cert, rate)
                            },
                            onLogTeacherMiss = { id ->
                                SaaSRepository.logTeacherMiss(id)
                            },
                            onDeleteTeacher = { id ->
                                SaaSRepository.deleteTeacher(id)
                            }
                        )
                        1 -> TeacherPanel(
                            classes = classes,
                            onTriggerStart = { classId -> SaaSRepository.startClass(classId) },
                            onManualCheckin = { classId -> SaaSRepository.startClass(classId) } // can also start immediately
                        )
                        2 -> StudentPanel(
                            students = students,
                            classes = classes,
                            onJoinSession = { classSlot -> SaaSRepository.acceptCall() },
                            onMakeStripePayment = { studentId, amount ->
                                SaaSRepository.collectFee(studentId, amount, "Stripe Processing API")
                            }
                        )
                        3 -> SalesPanel(
                            bookings = bookings,
                            onBookTrial = { name, email, phone, preferredTime ->
                                SaaSRepository.bookTrial(name, email, phone, preferredTime)
                            }
                        )
                        4 -> ArchitectConsoleView()
                    }
                }
            }
        }
    }
}
