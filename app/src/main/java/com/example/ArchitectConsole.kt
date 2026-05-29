package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchitectConsoleView(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Monorepo Layout", "Database Schema", "Zoom Video SDK", "Push SW (sw.js)", "React Components")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Terminal,
                    contentDescription = "Console",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "SaaS System Architect Console",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Production Web Files, Node.js JWTs, and Prisma Schemas",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Horizontal scrolling tabs for different documents
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 12.dp,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, fontWeight = FontWeight.SemiBold) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            when (selectedTab) {
                0 -> MonorepoLayoutDoc()
                1 -> DatabaseSchemaDoc()
                2 -> ZoomSdkDoc()
                3 -> PushServiceWorkerDoc()
                4 -> ReactComponentsDoc()
            }
        }
    }
}

@Composable
fun CodeContainer(codeText: String) {
    val scrollStateX = rememberScrollState()
    val scrollStateY = rememberScrollState()
    var isCopied by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1412)),
        shape = RoundedCornerShape(8.dp),
        border = AssistChipDefaults.assistChipBorder(true, borderColor = Color(0xFF2E3D37))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Tiny code header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF141A17))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PRODUCTION FILE SOURCE (Vercel + Railway Ready)",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = Color(0xFF90A4AE),
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = { isCopied = true },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = if (isCopied) EmeraldPrimary else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            if (isCopied) {
                Surface(
                    color = EmeraldPrimary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "✓ Copied to clipboard for deployment",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .verticalScroll(scrollStateY)
                    .horizontalScroll(scrollStateX)
            ) {
                Text(
                    text = codeText,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = Color(0xFFA7FFEB),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun MonorepoLayoutDoc() {
    val docText = """# ==========================================
# PROPOSED PROFESSIONAL MONOREPO LAYOUT
# ==========================================
quran-academy-saas/
├── package.json              # Workspace setup
├── .github/workflows/        # Automates Docker & deploy (Vercel, Railway)
├── prisma/
│   ├── schema.prisma         # Postgres Schemas (Prisma Models)
│   └── seed.js               # Mock seed values of Qaris and curriculum
├── server/                   # (Back-End) Python FastAPI/Node.js microservice
│   ├── package.json
│   ├── tsconfig.json
│   ├── .env
│   ├── src/
│   │   ├── index.ts          # Main entry root
│   │   ├── controllers/      # Handlers: classroom, stripe-webhook, affiliate
│   │   ├── middleware/       # JWT Auth verification, Roll checking
│   │   ├── routes/           # REST endpoints (/api/v1/auth, /api/v1/zoom)
│   │   └── services/         # JWT generation, Zoom Video SDK token and web push services
│   └── Dockerfile            # Multi-stage production container
├── client/                   # (Front-End) React.js Web Platform
│   ├── package.json
│   ├── vite.config.ts
│   ├── tailwind.config.js
│   ├── postcss.config.js
│   ├── public/
│   │   ├── sw.js             # Service Worker: WhatsApp ringing interceptor
│   │   ├── ringtone.mp3      # Urdu vocal ringing notice source ("آپ کی قرآن کلاس شروع ہو رہی ہے")
│   │   └── manifest.json     # PWA meta declarations
│   └── src/
│       ├── main.tsx
│       ├── index.css
│       ├── components/       # Reusable tables, dashboards, dialers
│       ├── pages/            # 4 panels: AdminSuper, TeacherPortal, studentDesk, LandingPages
│       ├── hooks/            # useZoomVideoSDK, useServiceWorkerAlerts
│       └── utils/            # JWT decoders, formatting helper assets
└── README.md                 # Complete platform boot deployment guides
"""
    CodeContainer(docText)
}

@Composable
fun DatabaseSchemaDoc() {
    val docText = """// ==========================================
// PRISMA POSTGRESQL MULTI-ENTITY SCHEMA
// ==========================================

datasource db {
  provider = "postgresql"
  url      = env("DATABASE_URL")
}

generator client {
  provider = "prisma-client-js"
}

enum Role {
  SUPER_ADMIN
  TEACHER
  STUDENT
  PARENT
}

enum ClassStatus {
  SCHEDULED
  ACTIVE
  COMPLETED
  MISSED
}

enum PaymentStatus {
  PAID
  PENDING
  OVERDUE
}

model User {
  id             String          @id @default(uuid())
  email          String          @unique
  passwordHash   String
  name           String
  role           Role            @default(STUDENT)
  createdAt      DateTime        @default(now())
  updatedAt      DateTime        @updatedAt
  
  // Relations
  teacherProfile TeacherProfile?
  studentProfile StudentProfile?
  parentProfile  ParentProfile?
  notifications  Notification[]
}

model TeacherProfile {
  id             String          @id @default(uuid())
  userId         String          @unique
  user           User            @relation(fields: [userId], references: [id], onDelete: Cascade)
  certification  String          // e.g. "Hafiz, Ijazah in Tajweed"
  hourlyRate     Decimal         @default(15.00)
  salaryBalance  Decimal         @default(0.00)
  rating         Float           @default(5.0)
  missedClasses  Int             @default(0)
  active         Boolean         @default(true)
  
  // Relations
  classes        Class[]
  students       StudentProfile[]
}

model StudentProfile {
  id             String          @id @default(uuid())
  userId         String          @unique
  user           User            @relation(fields: [userId], references: [id], onDelete: Cascade)
  parentName     String
  parentContact  String
  currentSurah   String          @default("Al-Baqarah")
  makharijGrade  String          @default("Average")
  tajweedGrade   String          @default("Needs Practice")
  revisionRate   Float           @default(0.0) // percentage
  feeBalance     Decimal         @default(75.00)
  affiliateCode  String          @unique
  referredByCode String?
  
  // Relations
  assignedTeacherId String
  teacher         TeacherProfile  @relation(fields: [assignedTeacherId], references: [id])
  classes        Class[]
  payments       Payment[]
}

model ParentProfile {
  id             String          @id @default(uuid())
  userId         String          @unique
  user           User            @relation(fields: [userId], references: [id], onDelete: Cascade)
  emergencyNo    String
}

model Class {
  id             String          @id @default(uuid())
  topicId        String          // Custom unique room name for Zoom Video SDK Session
  scheduledTime  DateTime
  status         ClassStatus     @default(SCHEDULED)
  surahTarget    String
  
  // Student Evaluation Metrics
  makharijMark   String?
  tajweedMark    String?
  revisionMark   String?
  performanceNote String?
  
  // Relations
  teacherId      String
  teacher        TeacherProfile  @relation(fields: [teacherId], references: [id], onDelete: Cascade)
  studentId      String
  student        StudentProfile  @relation(fields: [studentId], references: [id], onDelete: Cascade)
  
  createdAt      DateTime        @default(now())
  
  @@index([scheduledTime])
  @@index([status])
}

model Payment {
  id             String          @id @default(uuid())
  invoiceNo      String          @unique
  amount         Decimal
  dueDate        DateTime
  paidAt         DateTime?
  status         PaymentStatus   @default(PENDING)
  receiptPdf     String?
  gateway        String          @default("STRIPE") // strip or payoneer
  
  // Relations
  studentId      String
  student        StudentProfile  @relation(fields: [studentId], references: [id], onDelete: Cascade)
  
  createdAt      DateTime        @default(now())
}

model Notification {
  id             String          @id @default(uuid())
  title          String
  body           String
  isRead         Boolean         @default(false)
  actionLink     String?         // Deep link to class
  userId         String
  user           User            @relation(fields: [userId], references: [id], onDelete: Cascade)
  createdAt      DateTime        @default(now())
}
"""
    CodeContainer(docText)
}

@Composable
fun ZoomSdkDoc() {
    val docText = """// ==========================================
// ZOOM VIDEO SDK BACKEND & FRONTEND DRIVER
// ==========================================

// -- backend/src/services/zoomTokenService.ts -- (NodeJS Controller)
import jwt from 'jsonwebtoken';

interface ZoomTokenPayload {
  appKey: string;
  sdkKey: string;
  tpc: string;         // Room Title / Zoom SDK Topic Name
  roleType: number;   // 1 for Teacher/Host, 0 for Student/Viewer
  user_identity: string;
  session_key?: string;
}

export function generateZoomVideoSdkToken(
  topicName: string, 
  role: number, 
  username: string
): string {
  const sdkKey = process.env.ZOOM_SDK_KEY;
  const sdkSecret = process.env.ZOOM_SDK_SECRET;
  
  if (!sdkKey || !sdkSecret) {
    throw new Error('Zoom SDK Key or Secret environment variable missing');
  }

  const iat = Math.round(new Date().getTime() / 1000) - 30;
  const exp = iat + 60 * 60 * 2; // Token valid for 2 hours

  const payload = {
    appKey: sdkKey,
    sdkKey: sdkKey,
    tpc: topicName,
    roleType: role,
    user_identity: username,
    iat: iat,
    exp: exp
  };

  return jwt.sign(payload, sdkSecret, { algorithm: 'HS256' });
}


// -- client/src/hooks/useZoomVideoSDK.tsx -- (React Frontend Mount)
import React, { useEffect, useRef, useState } from 'react';
import ZoomVideo, { ConnectionState, VideoPlayer } from '@zoom/videosdk';

export const ZoomClassroom: React.FC<{
  token: string;
  topicName: string;
  username: string;
  isTeacher: boolean;
}> = ({ token, topicName, username, isTeacher }) => {
  const containerRef = useRef<HTMLDivElement>(null);
  const [client] = useState(() => ZoomVideo.createClient());
  const [joined, setJoined] = useState(false);

  useEffect(() => {
    const initZoom = async () => {
      try {
        await client.init('en-US', 'Global');
        
        // Join browser sandboxed session
        await client.join(topicName, token, username);
        setJoined(true);

        const mediaStream = client.getMediaStream();
        
        // Start camera feed instantly
        if (mediaStream.isSupportCamera()) {
          await mediaStream.startVideo();
          const videoDom = await mediaStream.attachVideo(client.getCurrentUserInfo().userId, 3);
          if (containerRef.current && videoDom) {
            containerRef.current.appendChild(videoDom as HTMLElement);
          }
        }
        
        // Start microphone and speaker feeds
        await mediaStream.startAudio();
        
      } catch (err) {
        console.error('Error starting Zoom Video SDK Session:', err);
      }
    };

    initZoom();

    return () => {
      const leaveSession = async () => {
        try {
          const mediaStream = client.getMediaStream();
          await mediaStream.stopVideo();
          await mediaStream.stopAudio();
          await client.leave();
        } catch (e) {
          console.error('Error exiting Zoom session:', e);
        }
      };
      leaveSession();
    };
  }, [token, topicName, username]);

  return (
    <div className="flex flex-col h-screen bg-slate-900 text-white">
      <div className="p-4 bg-slate-800 flex justify-between items-center border-b border-emerald-500">
        <h2 className="text-emerald-400 font-bold">LIVE Tajweed & Makharij Studio Room: {topicName}</h2>
        <span className="bg-emerald-600 px-3 py-1 rounded text-xs">
          {isTeacher ? 'Instructor Console' : 'Student Class Desk'}
        </span>
      </div>
      <div className="flex-1 flex justify-center items-center p-4">
        <div 
          ref={containerRef} 
          className="relative w-full max-w-4xl h-96 bg-black rounded-lg overflow-hidden border border-slate-700"
          id="zoom-video-container"
        >
          {!joined && (
            <div className="absolute inset-0 flex flex-col justify-center items-center bg-slate-950">
              <div className="loader border-4 border-emerald-500 border-t-transparent rounded-full w-12 h-12 animate-spin mb-4"></div>
              <p className="text-emerald-300 font-serif">Connecting with video servers, initializing Tajweed pointer...</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
"""
    CodeContainer(docText)
}

@Composable
fun PushServiceWorkerDoc() {
    val docText = """// ===============================================
// INCOMING CLASS CALL WEB PUSH SERVICE & ROUTER
// ===============================================

// -- client/public/sw.js -- (Service Worker logic)
self.addEventListener('push', function(event) {
  if (!event.data) return;
  
  const payload = event.data.json();
  const title = payload.title || 'آپ کی قرآن کلاس شروع ہو رہی ہے';
  
  const options = {
    body: payload.body || 'قاری صاحب آپ کا انتظار کر رہے ہیں۔ ابھی شمولیت اختیار کریں۔',
    icon: '/quran-icon.png',
    badge: '/badge-icon.png',
    vibrate: [200, 100, 200, 100, 200, 100, 500, 500, 500],
    sound: '/assets/ringtone.mp3', // Call ring tone!
    tag: 'live-class-alert',
    requireInteraction: true, // Persists until student clicks
    data: {
      url: payload.url || '/student/classroom'
    }
  };

  event.waitUntil(
    self.registration.showNotification(title, options)
  );
});

self.addEventListener('notificationclick', function(event) {
  event.notification.close();
  
  // Deep-link to student classroom tab instantly
  event.waitUntil(
    clients.matchAll({ type: 'window', includeUncontrolled: true }).then(function(clientList) {
      for (var i = 0; i < clientList.length; i++) {
        var client = clientList[i];
        if (client.url.includes('/student/classroom') && 'focus' in client) {
          return client.focus();
        }
      }
      if (clients.openWindow) {
        return clients.openWindow(event.notification.data.url);
      }
    })
  );
});


// -- server/src/routes/notify.ts -- (Backend triggers push router)
import express from 'express';
import webpush from 'web-push';

const router = express.Router();

// Config Web Push Keys
webpush.setVapidDetails(
  'mailto:operators@quranacademy.com',
  process.env.PUBLIC_VAPID_KEY!,
  process.env.PRIVATE_VAPID_KEY!
);

// Triggered when Teacher clicks "Start Class" button
router.post('/api/v1/classroom/trigger-call', async (req, res) => {
  const { studentUserId, classId, zoomTopic, teacherName } = req.body;
  
  try {
    // 1. Fetch Student active push subscription from Postgres DB
    const subscription = await prisma.pushSubscription.findUnique({
      where: { userId: studentUserId }
    });
    
    if (!subscription) {
      return res.status(404).json({ error: 'Student push credentials offline' });
    }
    
    // 2. Prepare payload triggering the custom sw "Ringing Voice"
    const pushPayload = JSON.stringify({
      title: 'آپ کی قرآن کلاس شروع ہو رہی ہے',
      body: 'قاری ' + teacherName + ' کلاس روم میں موجود ہیں۔ براہ کرم فوری شامل ہوں۔',
      url: 'https://quranacademy.saas/student/classroom?classId=' + classId + '&room=' + zoomTopic
    });
    
    // 3. Send real-time wake push
    await webpush.sendNotification(
      {
        endpoint: subscription.endpoint,
        keys: {
          p256dh: subscription.p256dh,
          auth: subscription.auth
        }
      },
      pushPayload
    );
    
    res.json({ success: true, message: 'Ringing notification successfully pushed to Student platform' });
  } catch (error: any) {
    console.error('Trigger push failure:', error);
    res.status(500).json({ error: 'Failed to deliver web push tone' });
  }
});

export default router;
"""
    CodeContainer(docText)
}

@Composable
fun ReactComponentsDoc() {
    val docText = """// ===============================================
// REACT CORE COMPONENT PAIRINGS FOR PANELS
// ===============================================

// -- client/src/components/TeacherStartButton.tsx -- (Teacher's Trigger Action)
import React, { useState } from 'react';
import axios from 'axios';

export const TeacherStartButton: React.FC<{ 
  classId: string; 
  studentUserId: string; 
  zoomTopic: string;
  teacherName: string;
  onSessionInit: () => void;
}> = ({ classId, studentUserId, zoomTopic, teacherName, onSessionInit }) => {
  const [calling, setCalling] = useState(false);

  const startAndAlertStudent = async () => {
    setCalling(true);
    try {
      // 1. Mark class status as active in Database
      await axios.put('/api/v1/classes/' + classId, { status: 'ACTIVE' });
      
      // 2. Fire custom WhatsApp Ringing trigger endpoint
      await axios.post('/api/v1/classroom/trigger-call', {
        studentUserId,
        classId,
        zoomTopic,
        teacherName
      });
      
      // 3. Teleport to Zoom Video SDK Live Room on client
      onSessionInit();
    } catch (err) {
      alert('Error triggering calling pipeline. Please retry: ' + err);
    } finally {
      setCalling(false);
    }
  };

  return (
    <button
      onClick={startAndAlertStudent}
      disabled={calling}
      className="btn bg-gradient-to-r from-emerald-600 to-green-500 hover:from-emerald-700 hover:to-green-600 text-white font-bold py-4 px-8 rounded-lg shadow-lg flex items-center gap-3 animate-pulse transition"
    >
      {calling ? (
        <span className="loader animate-spin border-2 border-white rounded-full w-5 h-5"></span>
      ) : (
        <span className="phone-icon text-xl">📞</span>
      )}
      {calling ? 'Ringing Student...' : 'Start Class & Ring Student'}
    </button>
  );
};


// -- client/src/components/StudentJoinButton.tsx -- (Student's Autolink)
import React, { useEffect, useState } from 'react';
import axios from 'axios';

export const StudentJoinButton: React.FC<{
  classId: string;
  roomTopic: string;
  onJoinClick: () => void;
}> = ({ classId, roomTopic, onJoinClick }) => {
  const [isActive, setIsActive] = useState(false);
  const [checking, setChecking] = useState(true);

  useEffect(() => {
    // Poll Database or subscribe to WebSocket channel for class activation
    const checkClassActive = async () => {
      try {
        const response = await axios.get('/api/v1/classes/' + classId);
        if (response.data.status === 'ACTIVE') {
          setIsActive(true);
        } else {
          setIsActive(false);
        }
      } catch (err) {
        console.warn('Sync class state error:', err);
      } finally {
        setChecking(false);
      }
    };

    checkClassActive();
    const interval = setInterval(checkClassActive, 4000); // sync every 4s
    return () => clearInterval(interval);
  }, [classId]);

  if (checking) return <p className="text-zinc-400 font-semibold text-center">Checking class schedule...</p>;

  return (
    <div className="p-6 bg-slate-800 rounded-xl border border-slate-700 shadow-xl flex flex-col items-center">
      <h3 className="text-xl font-bold font-serif mb-2 text-gold">Live Daily Quran Class</h3>
      <p className="text-xs text-zinc-400 mb-6 text-center leading-normal">
        Your class is scheduled with your Ustadh. Once your Ustadh starts the lesson, you will hear a ringtone notification or can join directly.
      </p>

      {isActive ? (
        <button
          onClick={onJoinClick}
          className="w-full bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-600 hover:to-teal-600 text-slate-900 font-extrabold py-5 px-10 rounded-lg shadow-emerald-500/20 shadow-2xl transition transform hover:scale-105 active:scale-95 text-center flex items-center justify-center gap-3"
        >
          <span className="live-ripple animate-ping absolute inline-flex h-3 w-3 rounded-full bg-red-400 opacity-75"></span>
          <span>🟢 JOIN CLASS NOW (USTADH ONLINE)</span>
        </button>
      ) : (
        <button
          disabled
          className="w-full bg-slate-700 text-slate-500 font-bold py-5 px-10 rounded-lg cursor-not-allowed text-center"
        >
          Waiting for Ustadh...
        </button>
      )}
    </div>
  );
};
"""
    CodeContainer(docText)
}
