package com.proxy.spacedrepition.domain.model

/**
 * Domain model representing the result of a spaced repetition calculation.
 * This encapsulates all the data needed to schedule the next review.
 */
data class SpacedRepetitionResult(
    val nextReviewDate: Long,
    val intervalDays: Int,
    val newEaseFactor: Double,
    val confidenceLevel: ConfidenceLevel,
    val reviewCount: Int,
    val wasCorrect: Boolean
) {
    /**
     * Business logic: Determine if the user is mastering this topic
     */
    fun indicatesMastery(): Boolean {
        return reviewCount >= 4 &&
                intervalDays >= 30 &&
                confidenceLevel in listOf(ConfidenceLevel.EASY, ConfidenceLevel.GOOD)
    }

    /**
     * Business logic: Determine if the topic needs more attention
     */
    fun needsMoreAttention(): Boolean {
        return confidenceLevel == ConfidenceLevel.FORGOT ||
                (reviewCount >= 3 && confidenceLevel == ConfidenceLevel.HARD)
    }
}
