package com.proxy.spacedrepition.domain.model

import java.util.UUID

/**
 * Domain model representing a spaced repetition review session.
 * Contains all the business logic for spaced repetition calculations.
 */
data class Review(
    val id: String = UUID.randomUUID().toString(),
    val topicId: String,
    val reviewedAt: Long,
    val confidenceLevel: ConfidenceLevel,
    val previousInterval: Int, // days since last review
    val nextReviewDate: Long,
    val easeFactor: Double = 2.5, // SM-2 algorithm ease factor
    val reviewCount: Int = 1
) {
    init {
        require(topicId.isNotBlank()) { "Topic ID cannot be blank" }
        require(easeFactor >= 1.3) { "Ease factor must be at least 1.3" }
        require(reviewCount >= 1) { "Review count must be positive" }
    }

    /**
     * Business logic: Check if this review is overdue
     */
    fun isOverdue(currentTime: Long = System.currentTimeMillis()): Boolean {
        return currentTime > nextReviewDate
    }

    /**
     * Business logic: Calculate how many days overdue this review is
     */
    fun daysOverdue(currentTime: Long = System.currentTimeMillis()): Int {
        if (!isOverdue(currentTime)) return 0
        return ((currentTime - nextReviewDate) / (24 * 60 * 60 * 1000)).toInt()
    }

    /**
     * Business logic: Determine urgency level for scheduling
     */
    fun urgencyLevel(): UrgencyLevel {
        val daysOverdue = daysOverdue()
        return when {
            daysOverdue >= 7 -> UrgencyLevel.CRITICAL
            daysOverdue >= 3 -> UrgencyLevel.HIGH
            daysOverdue >= 1 -> UrgencyLevel.MEDIUM
            else -> UrgencyLevel.LOW
        }
    }
}
