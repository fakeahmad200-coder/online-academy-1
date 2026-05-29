package com.example

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

// --- DATA SCHEMA REPRESENTATIONS (Direct translation of custom schema) ---

enum class UserRole { ADMIN, TEACHER, STUDENT }

data class User(
    val id: String = UUID.randomUUID().toString(),
    val email: String,
    val name: String,
    val role: UserRole,
    val dateCreated: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
)

data class TeacherProfile(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val name: String, // Denormalized for rendering ease
    val email: String,
    val certification: String, // e.g. "Ijazah in Tajweed", "Hafiz-ul-Quran"
    val hourlyRate: Double = 15.00,
    val salaryBalance: Double = 450.00,
    val activeStudentsCount: Int = 3,
    val attendanceRating: Float = 0.98f, // 98%
    val classMisses: Int = 0
)

data class StudentProfile(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val name: String,
    val email: String,
    val parentName: String,
    val parentContact: String,
    val assignedTeacherId: String,
    val assignedTeacherName: String,
    val currentSurah: String = "Al-Baqarah",
    val makharijGrade: String = "Excellent",
    val tajweedGrade: String = "Good",
    val revisionProgress: Float = 0.72f, // 72%
    val feeBalance: Double = 75.00,
    val affiliateCode: String = "QURAN_REFER_20",
    val referencedCount: Int = 2
)

enum class ClassStatus { SCHEDULED, ACTIVE, COMPLETED, MISSED }

data class QuranClass(
    val id: String = UUID.randomUUID().toString(),
    val topicId: String = "ZOOM_ROOM_" + (100000 + (10000..99999).random()), // Zoom Video SDK Topic/Session ID
    val teacherId: String,
    val teacherName: String,
    val studentId: String,
    val studentName: String,
    val scheduledTime: String, // e.g. "09:00 AM", "11:30 AM"
    var status: ClassStatus = ClassStatus.SCHEDULED,
    val surahTarget: String = "Al-Kahf (Verses 1-10)",
    var commentsMakharij: String = "Good",
    var commentsTajweed: String = "Focus on Qalqalah rules",
    var commentsRevision: String = "Requires a bit more revision of Al-Mulk"
)

data class Payment(
    val id: String = UUID.randomUUID().toString(),
    val studentId: String,
    val studentName: String,
    val amount: Double,
    val date: String,
    val status: String, // "Paid", "Pending", "Overdue"
    val receiptNo: String,
    val paymentMethod: String // "Stripe", "Payoneer", "Direct Transfer"
)

data class DirectTrialBooking(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val phone: String,
    val preferredTime: String,
    val createdDate: String = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
)

object SaaSRepository {

    // --- SEED SEEDS SEEDS ---
    private val initialTeachers = mutableListOf(
        TeacherProfile(
            userId = "u-t1",
            name = "Qari Ahmed Al-Masry",
            email = "ahmed.masry@quranacademy.com",
            certification = "Hafiz, Ijazah in Al-Shatibiyyah Qira'at",
            hourlyRate = 18.0,
            salaryBalance = 630.0,
            activeStudentsCount = 2,
            attendanceRating = 0.99f
        ),
        TeacherProfile(
            userId = "u-t2",
            name = "Ustadh Bilal Siddique",
            email = "bilal.siddique@quranacademy.com",
            certification = "Degree in Islamic Sharia, Certified Tajweed Instructor",
            hourlyRate = 15.0,
            salaryBalance = 420.0,
            activeStudentsCount = 1,
            attendanceRating = 0.96f
        ),
        TeacherProfile(
            userId = "u-t3",
            name = "Ustadha Fatima Zahra",
            email = "fatima.zahra@quranacademy.com",
            certification = "Masters in Quranic Studies, Al-Azhar Certified",
            hourlyRate = 20.0,
            salaryBalance = 800.0,
            activeStudentsCount = 1,
            attendanceRating = 1.00f
        )
    )

    private val initialStudents = mutableListOf(
        StudentProfile(
            userId = "u-s1",
            name = "Zayd Bin Harith",
            email = "zayd@gmail.com",
            parentName = "Ahmad Bin Harith",
            parentContact = "+92-300-1234567",
            assignedTeacherId = initialTeachers[0].id,
            assignedTeacherName = initialTeachers[0].name,
            currentSurah = "Al-Anbya",
            makharijGrade = "Excellent",
            tajweedGrade = "Good",
            revisionProgress = 0.85f,
            feeBalance = 0.0,
            affiliateCode = "ZAYD_REF_20",
            referencedCount = 1
        ),
        StudentProfile(
            userId = "u-s2",
            name = "Aisha Khan",
            email = "aisha@gmail.com",
            parentName = "Kamran Khan",
            parentContact = "+1-555-019-2834",
            assignedTeacherId = initialTeachers[0].id,
            assignedTeacherName = initialTeachers[0].name,
            currentSurah = "Yaseen",
            makharijGrade = "Good",
            tajweedGrade = "Needs Practice",
            revisionProgress = 0.60f,
            feeBalance = 75.0,
            affiliateCode = "AISHA_REF_50",
            referencedCount = 3
        ),
        StudentProfile(
            userId = "u-s3",
            name = "Omar Farooq",
            email = "omar@gmail.com",
            parentName = "Farooq Shah",
            parentContact = "+44-7700-900077",
            assignedTeacherId = initialTeachers[1].id,
            assignedTeacherName = initialTeachers[1].name,
            currentSurah = "Al-Mulk",
            makharijGrade = "Excellent",
            tajweedGrade = "Excellent",
            revisionProgress = 0.95f,
            feeBalance = 150.0,
            affiliateCode = "OMAR_REF_10",
            referencedCount = 0
        ),
        StudentProfile(
            userId = "u-s4",
            name = "Maryam Binte Ali",
            email = "maryam@gmail.com",
            parentName = "Ali Raza",
            parentContact = "+966-50-123-4567",
            assignedTeacherId = initialTeachers[2].id,
            assignedTeacherName = initialTeachers[2].name,
            currentSurah = "An-Naba",
            makharijGrade = "Good",
            tajweedGrade = "Good",
            revisionProgress = 0.70f,
            feeBalance = 0.0,
            affiliateCode = "MARYAM_REF_9",
            referencedCount = 0
        )
    )

    private val initialClasses = mutableListOf(
        QuranClass(
            teacherId = initialTeachers[0].id,
            teacherName = initialTeachers[0].name,
            studentId = initialStudents[0].id,
            studentName = initialStudents[0].name,
            scheduledTime = "09:00 AM",
            status = ClassStatus.SCHEDULED,
            surahTarget = "Surah Maryam (Verses 1-15)"
        ),
        QuranClass(
            teacherId = initialTeachers[0].id,
            teacherName = initialTeachers[0].name,
            studentId = initialStudents[1].id,
            studentName = initialStudents[1].name,
            scheduledTime = "11:30 AM",
            status = ClassStatus.SCHEDULED,
            surahTarget = "Surah Yaseen (Verses 12-30)"
        ),
        QuranClass(
            teacherId = initialTeachers[1].id,
            teacherName = initialTeachers[1].name,
            studentId = initialStudents[2].id,
            studentName = initialStudents[2].name,
            scheduledTime = "02:00 PM",
            status = ClassStatus.SCHEDULED,
            surahTarget = "Surah Al-Mulk (Full Revision)"
        ),
        QuranClass(
            teacherId = initialTeachers[2].id,
            teacherName = initialTeachers[2].name,
            studentId = initialStudents[3].id,
            studentName = initialStudents[3].name,
            scheduledTime = "04:30 PM",
            status = ClassStatus.SCHEDULED,
            surahTarget = "Surah An-Naba (Verses 1-40)"
        )
    )

    private val initialPayments = mutableListOf(
        Payment(
            studentId = initialStudents[0].id,
            studentName = initialStudents[0].name,
            amount = 75.0,
            date = "2026-05-15",
            status = "Paid",
            receiptNo = "REC-2026-0034",
            paymentMethod = "Stripe"
        ),
        Payment(
            studentId = initialStudents[1].id,
            studentName = initialStudents[1].name,
            amount = 75.0,
            date = "2026-05-01",
            status = "Pending",
            receiptNo = "REC-2026-0012",
            paymentMethod = "Stripe (Auto-Invoice)"
        ),
        Payment(
            studentId = initialStudents[2].id,
            studentName = initialStudents[2].name,
            amount = 150.0,
            date = "2026-05-10",
            status = "Overdue",
            receiptNo = "REC-1921-002",
            paymentMethod = "Payoneer"
        ),
        Payment(
            studentId = initialStudents[3].id,
            studentName = initialStudents[3].name,
            amount = 120.0,
            date = "2026-05-20",
            status = "Paid",
            receiptNo = "REC-2026-0045",
            paymentMethod = "Direct Credit Card"
        )
    )

    // --- FLOATING STATE ENGINE FOR SIMULATION ---
    private val _teachers = MutableStateFlow<List<TeacherProfile>>(initialTeachers)
    val teachers: StateFlow<List<TeacherProfile>> = _teachers.asStateFlow()

    private val _students = MutableStateFlow<List<StudentProfile>>(initialStudents)
    val students: StateFlow<List<StudentProfile>> = _students.asStateFlow()

    private val _classes = MutableStateFlow<List<QuranClass>>(initialClasses)
    val classes: StateFlow<List<QuranClass>> = _classes.asStateFlow()

    private val _payments = MutableStateFlow<List<Payment>>(initialPayments)
    val payments: StateFlow<List<Payment>> = _payments.asStateFlow()

    private val _bookings = MutableStateFlow<List<DirectTrialBooking>>(emptyList())
    val bookings: StateFlow<List<DirectTrialBooking>> = _bookings.asStateFlow()

    // Real-Time Ringing Call State for simulating the "WhatsApp-style Ringing Screen"
    private val _activeRingingCall = MutableStateFlow<QuranClass?>(null)
    val activeRingingCall: StateFlow<QuranClass?> = _activeRingingCall.asStateFlow()

    // Currently Active class workspace screen path
    private val _liveActiveSession = MutableStateFlow<QuranClass?>(null)
    val liveActiveSession: StateFlow<QuranClass?> = _liveActiveSession.asStateFlow()

    // --- CRM MUTATORS & CONTROLLERS ---

    // 1. Teacher CRUD
    fun addTeacher(name: String, email: String, certification: String, hourlyRate: Double) {
        val newTeacher = TeacherProfile(
            userId = "u-t" + UUID.randomUUID().toString().take(6),
            name = name,
            email = email,
            certification = certification,
            hourlyRate = hourlyRate,
            salaryBalance = 0.0,
            activeStudentsCount = 0,
            attendanceRating = 1.0f
        )
        _teachers.value = _teachers.value + newTeacher
    }

    fun updateTeacher(id: String, certification: String, hourlyRate: Double) {
        _teachers.value = _teachers.value.map {
            if (it.id == id) {
                it.copy(certification = certification, hourlyRate = hourlyRate)
            } else it
        }
    }

    fun deleteTeacher(id: String) {
        _teachers.value = _teachers.value.filterNot { it.id == id }
    }

    fun logTeacherMiss(id: String) {
        _teachers.value = _teachers.value.map {
            if (it.id == id) {
                val newRating = (it.attendanceRating - 0.05f).coerceAtLeast(0.5f)
                it.copy(classMisses = it.classMisses + 1, attendanceRating = newRating)
            } else it
        }
    }

    // 2. Student Management & Invoicing
    fun collectFee(studentId: String, amount: Double, method: String) {
        _students.value = _students.value.map {
            if (it.id == studentId) {
                it.copy(feeBalance = (it.feeBalance - amount).coerceAtLeast(0.0))
            } else it
        }
        val student = _students.value.find { it.id == studentId }
        val newPayment = Payment(
            studentId = studentId,
            studentName = student?.name ?: "Unknown",
            amount = amount,
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            status = "Paid",
            receiptNo = "REC-IN-" + (1000..9999).random(),
            paymentMethod = method
        )
        _payments.value = _payments.value + newPayment
    }

    // 3. Class scheduling and real-time triggers
    fun startClass(classId: String) {
        var startedClass: QuranClass? = null
        _classes.value = _classes.value.map { c ->
            if (c.id == classId) {
                val updated = c.copy(status = ClassStatus.ACTIVE)
                startedClass = updated
                updated
            } else c
        }
        // Fire the incoming ringing voice alert simulator!
        startedClass?.let {
            _activeRingingCall.value = it
        }
    }

    fun acceptCall() {
        val currentCall = _activeRingingCall.value
        _activeRingingCall.value = null // clear ringing overlay
        _liveActiveSession.value = currentCall // launch live zoom view
    }

    fun rejectCall() {
        _activeRingingCall.value = null
    }

    fun finishClass(
        classId: String, 
        commentsMakharij: String, 
        commentsTajweed: String, 
        commentsRevision: String
    ) {
        _classes.value = _classes.value.map { c ->
            if (c.id == classId) {
                c.status = ClassStatus.COMPLETED
                c.commentsMakharij = commentsMakharij
                c.commentsTajweed = commentsTajweed
                c.commentsRevision = commentsRevision
                c
            } else c
        }
        _liveActiveSession.value = null
    }

    fun endLiveSession() {
        _liveActiveSession.value = null
    }

    // 4. Sales & Booking funnel
    fun bookTrial(name: String, email: String, phone: String, timeSlot: String) {
        val newBooking = DirectTrialBooking(
            name = name,
            email = email,
            phone = phone,
            preferredTime = timeSlot
        )
        _bookings.value = _bookings.value + newBooking

        // SaaS Autopilot Engine: Automatically provisioning account, assigning random Qari:
        val rQari = _teachers.value.randomOrNull() ?: initialTeachers[0]
        val tempUserId = "trial-s-" + UUID.randomUUID().toString().take(6)
        
        // Add student automatically:
        val newTrialStudent = StudentProfile(
            userId = tempUserId,
            name = "$name (Free Trial)",
            email = email,
            parentName = "$name Parent",
            parentContact = phone,
            assignedTeacherId = rQari.id,
            assignedTeacherName = rQari.name,
            currentSurah = "Al-Fatihah",
            feeBalance = 0.0,
            affiliateCode = "${name.take(3).uppercase()}_TRIAL"
        )
        _students.value = _students.value + newTrialStudent

        // Schedule class automatically:
        val newTrialClass = QuranClass(
            teacherId = rQari.id,
            teacherName = rQari.name,
            studentId = newTrialStudent.id,
            studentName = newTrialStudent.name,
            scheduledTime = timeSlot,
            status = ClassStatus.SCHEDULED,
            surahTarget = "Introductory Tajweed & Fluency check"
        )
        _classes.value = _classes.value + newTrialClass
    }

    // 5. Financial P&L indicators
    fun getProfitLossStats(): ProfitLossStats {
        val totalRevenue = _payments.value.filter { it.status == "Paid" }.sumOf { it.amount }
        val teachersPayroll = _teachers.value.sumOf { it.salaryBalance }
        val platformLicensing = 120.00 // Zoom Video SDK usage, push servers, firebase
        val netProfit = totalRevenue - (teachersPayroll + platformLicensing)
        return ProfitLossStats(
            revenue = totalRevenue,
            payrollExpense = teachersPayroll,
            licensingExpense = platformLicensing,
            netProfit = netProfit
        )
    }
}

data class ProfitLossStats(
    val revenue: Double,
    val payrollExpense: Double,
    val licensingExpense: Double,
    val netProfit: Double
)
