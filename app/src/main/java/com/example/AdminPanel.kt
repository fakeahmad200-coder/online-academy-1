package com.example

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AlertRed
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanel(
    teachers: List<TeacherProfile>,
    students: List<StudentProfile>,
    classes: List<QuranClass>,
    payments: List<Payment>,
    onAddTeacher: (name: String, email: String, certification: String, hourlyRate: Double) -> Unit,
    onLogTeacherMiss: (teacherId: String) -> Unit,
    onDeleteTeacher: (teacherId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddTeacherDialog by remember { mutableStateOf(false) }
    var adminSubSection by remember { mutableStateOf(0) } // 0: Dashboard, 1: Qaris (CRUD), 2: Students List, 3: Budget/Finances

    val profitLoss = SaaSRepository.getProfitLossStats()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Upper Sub-Navigation Tabs inside Admin Portal
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AdminSubNavTab(selected = adminSubSection == 0, text = "Overview", icon = Icons.Default.Dashboard) { adminSubSection = 0 }
            AdminSubNavTab(selected = adminSubSection == 1, text = "Qari CRUD", icon = Icons.Default.Person2) { adminSubSection = 1 }
            AdminSubNavTab(selected = adminSubSection == 2, text = "Students", icon = Icons.Default.People) { adminSubSection = 2 }
            AdminSubNavTab(selected = adminSubSection == 3, text = "P&L Ledger", icon = Icons.Default.MonetizationOn) { adminSubSection = 3 }
        }

        when (adminSubSection) {
            0 -> AdminDashboardOverview(teachers, students, classes, profitLoss)
            1 -> AdminTeacherCrudSection(teachers, onLogTeacherMiss, onDeleteTeacher) { showAddTeacherDialog = true }
            2 -> AdminStudentsSection(students)
            3 -> AdminFinancesSection(payments, profitLoss)
        }
    }

    // Modal dialog to satisfy CRUD Creation requests
    if (showAddTeacherDialog) {
        AddTeacherDialog(
            onDismiss = { showAddTeacherDialog = false },
            onConfirm = { name, email, cert, rate ->
                onAddTeacher(name, email, cert, rate)
                showAddTeacherDialog = false
            }
        )
    }
}

@Composable
fun AdminSubNavTab(selected: Boolean, text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) EmeraldPrimary else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = if (selected) Color.White else MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) Color.White else MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun AdminDashboardOverview(
    teachers: List<TeacherProfile>,
    students: List<StudentProfile>,
    classes: List<QuranClass>,
    stats: ProfitLossStats
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Autopilot Business KPI Dashboard", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        // Grid cards
        Row(modifier = Modifier.fillMaxWidth()) {
            KpiCard(
                modifier = Modifier.weight(1f),
                title = "Monthly Profit (SaaS)",
                value = "$${String.format("%.2f", stats.netProfit)}",
                desc = "Net margin on runway",
                icon = Icons.Default.TrendingUp,
                color = SuccessGreen
            )
            Spacer(modifier = Modifier.width(10.dp))
            KpiCard(
                modifier = Modifier.weight(1f),
                title = "Active Academicians",
                value = "${students.size}",
                desc = "Subscribed students",
                icon = Icons.Default.School,
                color = EmeraldPrimary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            KpiCard(
                modifier = Modifier.weight(1f),
                title = "Instructors Active",
                value = "${teachers.size}",
                desc = "Certified certified Qaris",
                icon = Icons.Default.Groups,
                color = GoldAccent
            )
            Spacer(modifier = Modifier.width(10.dp))
            KpiCard(
                modifier = Modifier.weight(1f),
                title = "Classes Scheduled",
                value = "${classes.size}",
                desc = "Daily live slots",
                icon = Icons.Default.VideoCall,
                color = Color.Blue
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Autopilot Virtual Zoom Classroom Registry
        Text("Virtual Zoom SDK Chambers status", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(10.dp))

        classes.forEach { classSession ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Room ID: ${classSession.topicId}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Topics: ${classSession.surahTarget}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Instructor: ${classSession.teacherName} ➔ Student: ${classSession.studentName}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = EmeraldPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                when (classSession.status) {
                                    ClassStatus.ACTIVE -> Color(0x334CAF50)
                                    ClassStatus.COMPLETED -> Color(0x1100C853)
                                    ClassStatus.MISSED -> Color(0x22F44336)
                                    else -> Color(0x229E9E9E)
                                }
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = classSession.status.name,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = when (classSession.status) {
                                ClassStatus.ACTIVE -> SuccessGreen
                                ClassStatus.COMPLETED -> EmeraldPrimary
                                ClassStatus.MISSED -> AlertRed
                                else -> Color.Gray
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KpiCard(modifier: Modifier = Modifier, title: String, value: String, desc: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(desc, fontSize = 10.sp, color = Color.Gray)
        }
    }
}

@Composable
fun AdminTeacherCrudSection(
    teachers: List<TeacherProfile>,
    onLogTeacherMiss: (teacherId: String) -> Unit,
    onDeleteTeacher: (teacherId: String) -> Unit,
    onTriggerAdd: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Qari Profiles Management (CRUD)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Button(
                onClick = onTriggerAdd,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Teacher", modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Teacher", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(teachers) { teacher ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(teacher.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(teacher.email, fontSize = 11.sp, color = Color.Gray)
                            }
                            // Delete button
                            IconButton(onClick = { onDeleteTeacher(teacher.id) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Profile", tint = AlertRed, modifier = Modifier.size(16.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Certifications: ${teacher.certification}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Rate: $${teacher.hourlyRate}/hr | Unpaid: $${teacher.salaryBalance}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )

                            // Rating & Class demerit actions
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Rating: ${String.format("%.0f", teacher.attendanceRating * 100)}%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (teacher.attendanceRating < 0.95f) AlertRed else SuccessGreen
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { onLogTeacherMiss(teacher.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBE9E7)),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(26.dp),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text("Log Delay/Miss", color = Color(0xFFD84315), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminStudentsSection(students: List<StudentProfile>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text("Subscribed Student Profiles Desk", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(students) { student ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(student.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(student.email, fontSize = 11.sp, color = Color.Gray)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (student.feeBalance > 0) Color(0xFFFFF3E0) else Color(0xFFE8F5E9))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (student.feeBalance > 0) "Arrears: $${student.feeBalance}" else "Paid in Full ✓",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (student.feeBalance > 0) Color(0xFFE65100) else Color(0xFF2E7D32)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Guardian: ${student.parentName}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("Contact: ${student.parentContact}", fontSize = 11.sp, color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Instructor: ${student.assignedTeacherName}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                            Text("Current: Surah ${student.currentSurah}", fontSize = 11.sp, color = GoldAccent, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminFinancesSection(payments: List<Payment>, stats: ProfitLossStats) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Autopilot Business Ledger & P&L Charts", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        // Balance calculations card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("SaaS Runway Accounts Breakdown", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.height(12.dp))

                FinancialLedgerRow("Gross Subscriptions Revenue", "+$${String.format("%.2f", stats.revenue)}", SuccessGreen)
                FinancialLedgerRow("Teacher Freelancer Payroll Balance", "-$${String.format("%.2f", stats.payrollExpense)}", AlertRed)
                FinancialLedgerRow("Automated Zoom Licensing & Push Server", "-$${String.format("%.2f", stats.licensingExpense)}", Color.DarkGray)

                Divider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Operating Margin Profit", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                    Text("$${String.format("%.2f", stats.netProfit)}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = SuccessGreen)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Professional P&L graphical bar chart rendered inside Android canvas!
        Text("Visual Profit/Loss Breakdown", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Maximum bar range set dynamically
                    val maxValue = stats.revenue.coerceAtLeast(100.0).toFloat()
                    val widthScale = size.width / 4f
                    val chartHeight = size.height - 30f

                    // 1. Gross Revenue Bar (Green)
                    val rHeight = (stats.revenue.toFloat() / maxValue) * chartHeight
                    drawRoundRect(
                        color = Color(0xFF388E3C),
                        topLeft = Offset(widthScale * 0.4f, chartHeight - rHeight),
                        size = Size(widthScale * 0.7f, rHeight),
                        cornerRadius = CornerRadius(8f, 8f)
                    )

                    // 2. Freelancer Expense Bar (Orange)
                    val expHeight = (stats.payrollExpense.toFloat() / maxValue) * chartHeight
                    drawRoundRect(
                        color = Color(0xFFD84315),
                        topLeft = Offset(widthScale * 1.5f, chartHeight - expHeight),
                        size = Size(widthScale * 0.7f, expHeight),
                        cornerRadius = CornerRadius(8f, 8f)
                    )

                    // 3. Platform Margins (Green-Yellow)
                    val profHeight = (stats.netProfit.toFloat() / maxValue) * chartHeight
                    drawRoundRect(
                        color = Color(0xFF0F9B58),
                        topLeft = Offset(widthScale * 2.6f, chartHeight - profHeight),
                        size = Size(widthScale * 0.7f, profHeight),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                }

                // Bar Labels Overlay
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text("Revenue", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text("Freelancers", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text("Net Margin", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun AddTeacherDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, email: String, certification: String, hourlyRate: Double) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var certification by remember { mutableStateOf("Hafiz Codec, Ijazah in Tajweed") }
    var rateStr by remember { mutableStateOf("15.00") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Profile Creator: Add New Qari", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Sheikh / Qari Name") },
                    textStyle = TextStyle(fontSize = 13.sp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Corporate Email Address") },
                    textStyle = TextStyle(fontSize = 13.sp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = certification,
                    onValueChange = { certification = it },
                    label = { Text("Certifications / Ijazah") },
                    textStyle = TextStyle(fontSize = 13.sp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = rateStr,
                    onValueChange = { rateStr = it },
                    label = { Text("Hourly rate charge ($)") },
                    textStyle = TextStyle(fontSize = 13.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val rate = rateStr.toDoubleOrNull() ?: 15.0
                    if (name.isNotBlank() && email.isNotBlank()) {
                        onConfirm(name, email, certification, rate)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text("Create Account", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun FinancialLedgerRow(label: String, value: String, tint: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = tint)
    }
}
