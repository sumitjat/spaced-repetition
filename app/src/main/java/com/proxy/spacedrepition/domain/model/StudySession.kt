package com.proxy.spacedrepition.domain.model

import java.util.UUID

/**
 * Domain model representing a complete study session. Aggregates multiple topic reviews into a
 * single session.
 */
data class StudySession(
    val id: String = UUID.randomUUID().toString(),
    val startTime: Long,
    val endTime: Long? = null,
    val topicsReviewed: List<String> = emptyList(),
    val totalCorrect: Int = 0,
    val totalReviewed: Int = 0,
    val averageConfidence: Double = 0.0,
) {
    init {
        require(totalReviewed >= totalCorrect) { "Correct answers cannot exceed total reviewed" }
        require(averageConfidence in 0.0..4.0) { "Average confidence must be between 0 and 4" }
    }

    /** Business logic: Calculate session success rate */
    fun successRate(): Double {
        return if (totalReviewed == 0) 0.0 else totalCorrect.toDouble() / totalReviewed
    }

    /** Business logic: Determine session quality */
    fun sessionQuality(): SessionQuality {
        val rate = successRate()
        return when {
            rate >= 0.9 -> SessionQuality.EXCELLENT
            rate >= 0.75 -> SessionQuality.GOOD
            rate >= 0.5 -> SessionQuality.AVERAGE
            else -> SessionQuality.NEEDS_IMPROVEMENT
        }
    }

    /** Business logic: Calculate session duration in minutes */
    fun durationMinutes(): Int {
        return if (endTime == null) 0 else ((endTime - startTime) / (60 * 1000)).toInt()
    }

    /** Business logic: Check if session was completed */
    fun isCompleted(): Boolean = endTime != null
}
