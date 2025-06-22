package com.proxy.spacedrepition.domain.repository

import com.proxy.spacedrepition.domain.model.Review
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing review history and spaced repetition data. This is where the
 * magic of spaced repetition gets its data foundation.
 */
interface ReviewRepository {

    /**
     * Get all reviews for a specific topic, ordered by review date. This powers the progress
     * tracking for individual topics.
     */
    fun getReviewsForTopic(topicId: String): Flow<List<Review>>

    /**
     * Get the most recent review for a topic. Critical for calculating when the next review should
     * happen.
     */
    suspend fun getLatestReviewForTopic(topicId: String): Review?

    /**
     * Get all reviews that are due for review. This drives the "what should I study today?"
     * functionality.
     */
    fun getOverdueReviews(currentTime: Long = System.currentTimeMillis()): Flow<List<Review>>

    /**
     * Get reviews due within a specific time window. Supports planning study sessions in advance.
     */
    fun getReviewsDueWithin(
        hours: Int,
        currentTime: Long = System.currentTimeMillis(),
    ): Flow<List<Review>>

    /**
     * Get review history within a date range. Powers the analytics dashboard with time-based
     * filtering.
     */
    fun getReviewHistory(startDate: Long, endDate: Long): Flow<List<Review>>

    /**
     * Add a new review and update the spaced repetition schedule. This is the core operation that
     * happens after each study session.
     */
    suspend fun addReview(review: Review): Review

    /**
     * Batch add reviews for performance during study sessions. When reviewing multiple topics,
     * individual inserts are inefficient.
     */
    suspend fun addReviews(reviews: List<Review>): List<Review>

    /**
     * Update a review (for corrections or modifications). Allows users to fix mistakes in their
     * confidence ratings.
     */
    suspend fun updateReview(review: Review): Review?

    /** Delete a review. For when users want to remove incorrect data points. */
    suspend fun deleteReview(reviewId: String): Boolean

    /** Get comprehensive review statistics. Powers the main analytics screen. */
    suspend fun getReviewStats(topicId: String? = null): ReviewStats

    /** Get retention rate over time. Shows learning progress trends. */
    suspend fun getRetentionRateHistory(days: Int = 30): List<RetentionDataPoint>

    /** Calculate study session statistics. For analyzing study patterns and effectiveness. */
    suspend fun getStudySessionStats(sessionId: String): StudySessionStats?
}

/**
 * Analytics data structures. These encapsulate complex calculations and provide clean interfaces
 * for UI.
 */
data class ReviewStats(
    val totalReviews: Int,
    val averageConfidence: Double,
    val retentionRate: Double,
    val streakDays: Int,
    val reviewsThisWeek: Int,
    val reviewsThisMonth: Int,
    val strongestCategories: List<String>,
    val weakestCategories: List<String>,
    val averageSessionDuration: Int, // minutes
    val bestStudyTime: Int, // hour of day (0-23)
)

data class RetentionDataPoint(
    val date: Long,
    val retentionRate: Double,
    val reviewCount: Int,
    val averageConfidence: Double,
)

data class StudySessionStats(
    val sessionId: String,
    val duration: Int, // minutes
    val topicsReviewed: Int,
    val successRate: Double,
    val averageConfidence: Double,
    val categoriesStudied: List<String>,
    val difficultyDistribution: Map<String, Int>,
)
