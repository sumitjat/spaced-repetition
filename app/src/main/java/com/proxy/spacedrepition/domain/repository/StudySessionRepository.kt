package com.proxy.spacedrepition.domain.repository

import com.proxy.spacedrepition.domain.model.StudySession
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing study sessions. Tracks user behavior patterns and enables advanced
 * analytics.
 */
interface StudySessionRepository {

    /** Get all study sessions, most recent first. */
    fun getAllSessions(): Flow<List<StudySession>>

    /** Get sessions within a date range. */
    fun getSessionsInRange(startDate: Long, endDate: Long): Flow<List<StudySession>>

    /** Get the current active session (if any). Only one session should be active at a time. */
    suspend fun getActiveSession(): StudySession?

    /** Start a new study session. */
    suspend fun startSession(): StudySession

    /** End the current session with final statistics. */
    suspend fun endSession(
        sessionId: String,
        topicsReviewed: List<String>,
        totalCorrect: Int,
        totalReviewed: Int,
        averageConfidence: Double,
    ): StudySession?

    /** Update session in progress. For real-time tracking during study sessions. */
    suspend fun updateSessionProgress(
        sessionId: String,
        topicsReviewed: List<String>,
        totalCorrect: Int,
        totalReviewed: Int,
    ): StudySession?

    /** Get session analytics for performance tracking. */
    suspend fun getSessionAnalytics(days: Int = 30): SessionAnalytics
}

data class SessionAnalytics(
    val totalSessions: Int,
    val averageSessionDuration: Int, // minutes
    val totalStudyTime: Int, // minutes
    val averageTopicsPerSession: Double,
    val averageSuccessRate: Double,
    val studyStreak: Int, // consecutive days with sessions
    val preferredStudyTimes: List<Int>, // hours of day
    val productivityTrends: List<ProductivityDataPoint>,
)

data class ProductivityDataPoint(
    val date: Long,
    val sessionsCount: Int,
    val totalDuration: Int,
    val averageSuccessRate: Double,
)
