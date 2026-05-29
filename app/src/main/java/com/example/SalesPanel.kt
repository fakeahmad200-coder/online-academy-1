package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesPanel(
    bookings: List<DirectTrialBooking>,
    onBookTrial: (name: String, email: String, phone: String, slots: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var trialName by remember { mutableStateOf("") }
    var trialEmail by remember { mutableStateOf("") }
    var trialPhone by remember { mutableStateOf("") }
    var trialSlot by remember { mutableStateOf("01:00 PM") }
    
    var bookSuccessMsg by remember { mutableStateOf<String?>(null) }
    var refCopiedMsg by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Upper Intro
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Public, contentDescription = "Funnel", tint = EmeraldPrimary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Organic Marketing & Autopilot Sales", fontWeight = FontWeight.Bold)
                }
                Text(
                    text = "No human intervention required. Free trial accounts are created and paired instantly by the PWA's matchmaker.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // PUBLIC FREE TRIAL BOOKING FLYER
        Text("Public Trial Booking Funnel", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Request your 30-Minute Trial Session",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = EmeraldPrimary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = trialName,
                    onValueChange = { trialName = it },
                    label = { Text("Parent / Student Name") },
                    textStyle = TextStyle(fontSize = 13.sp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = trialEmail,
                    onValueChange = { trialEmail = it },
                    label = { Text("Email Address") },
                    textStyle = TextStyle(fontSize = 13.sp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = trialPhone,
                    onValueChange = { trialPhone = it },
                    label = { Text("WhatsApp Contact (with country code)") },
                    textStyle = TextStyle(fontSize = 13.sp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )

                // Selectable Time Slots
                Text("Select Preferred Time Slot:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val slots = listOf("01:00 PM", "03:30 PM", "06:00 PM")
                    slots.forEach { slot ->
                        Card(
                            onClick = { trialSlot = slot },
                            colors = CardDefaults.cardColors(
                                containerColor = if (trialSlot == slot) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp).height(32.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = slot,
                                    fontSize = 10.sp,
                                    color = if (trialSlot == slot) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (trialName.isNotBlank() && trialEmail.isNotBlank()) {
                            onBookTrial(trialName, trialEmail, trialPhone, trialSlot)
                            bookSuccessMsg = "Trial scheduled for $trialSlot! Accomodated Student Account provisioned & Teacher auto-assigned."
                            trialName = ""
                            trialEmail = ""
                            trialPhone = ""
                            focusManager.clearFocus()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Instantly Schedule & Provision", fontWeight = FontWeight.Bold)
                }

                bookSuccessMsg?.let { msg ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "✓ $msg",
                            color = SuccessGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // STUDENT AFFILIATE MARKETING MODULE
        Text("SaaS Affiliate & Referrals Dashboard", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CardGiftcard, contentDescription = "Reward", tint = GoldAccent, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Grow the Academy, Study for Free!", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Invite friends. When they sign up for a trial, you get rewarded. If they subscribe, you receive a 20% discount coupon applied automatically.",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Custom referral Link copy card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Your Personalized Affiliate Code:", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                            Text(
                                "https://tarteel.saas/?aff=AISHA_REF_50",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        }
                        IconButton(onClick = { refCopiedMsg = true }) {
                            Icon(Icons.Default.ContentCopy, "Copy", tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                if (refCopiedMsg) {
                    Text(
                        "✓ Referral link copied to your clipboard!",
                        color = SuccessGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                Spacer(modifier = Modifier.height(12.dp))

                // Referrals Track record Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ReferralStatBlock("3", "Leads Invited")
                    ReferralStatBlock("2", "Joined Demo")
                    ReferralStatBlock("20%", "Your Discount")
                }
            }
        }
    }
}

@Composable
fun ReferralStatBlock(value: String, title: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = EmeraldPrimary)
        Text(title, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
    }
}
