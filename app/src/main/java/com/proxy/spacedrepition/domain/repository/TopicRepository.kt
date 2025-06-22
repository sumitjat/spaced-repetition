package com.proxy.spacedrepition.domain.repository

import com.proxy.spacedrepition.domain.model.Topic
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface defining what the domain layer needs for topic management.
 * This is a CONTRACT - the domain layer doesn't care HOW these operations work,
 * just that they work reliably.
 *
 * Why interface? Because we can mock this for testing without touching the database.
 * Why Flow? Because UI needs reactive updates when data changes.
 */
interface TopicRepository {

    /**
     * Get all active topics as a reactive stream.
     * Why Flow? Because when user adds a topic in one screen,
     * other screens should automatically update.
     */
    fun getAllTopics(): Flow<List<Topic>>

    /**
     * Get topics filtered by category.
     * This supports the user's custom categorization we discussed.
     */
    fun getTopicsByCategory(category: String): Flow<List<Topic>>

    /**
     * Get topics that need review today.
     * Business logic: What constitutes "needs review" is determined by the domain layer,
     * but the repository provides the data filtering mechanism.
     */
    fun getTopicsForReview(currentTime: Long = System.currentTimeMillis()): Flow<List<Topic>>

    /**
     * Get all unique categories that users have created.
     * This powers the autocomplete functionality we planned.
     */
    fun getAllCategories(): Flow<List<String>>

    /**
     * Get topics by their urgency level for interview preparation.
     * Supports the smart scheduling we want to build.
     */
    fun getTopicsByUrgency(daysUntilInterview: Int): Flow<List<Topic>>

    /**
     * Search topics by name or content.
     * Essential for users with large topic collections.
     */
    fun searchTopics(query: String): Flow<List<Topic>>

    /**
     * Get a single topic by ID.
     * Returns null if not found - explicit nullable handling.
     */
    suspend fun getTopicById(id: String): Topic?

    /**
     * Add a new topic.
     * Returns the created topic with any generated fields (like timestamps).
     */
    suspend fun addTopic(topic: Topic): Topic

    /**
     * Update an existing topic.
     * Returns updated topic or null if topic doesn't exist.
     */
    suspend fun updateTopic(topic: Topic): Topic?

    /**
     * Soft delete a topic (mark as inactive).
     * We don't hard delete because review history might reference this topic.
     */
    suspend fun deleteTopic(id: String): Boolean

    /**
     * Get topic statistics for analytics.
     * This supports the dashboard we want to build.
     */
    suspend fun getTopicStats(): TopicStats

    /**
     * Batch operations for performance.
     * When importing large datasets, single inserts are too slow.
     */
    suspend fun addTopics(topics: List<Topic>): List<Topic>
    suspend fun updateTopics(topics: List<Topic>): List<Topic>
}


/**
 * Data class for topic analytics.
 * Encapsulates complex calculations that would be expensive to compute in UI.
 */
data class TopicStats(
    val totalTopics: Int,
    val topicsByCategory: Map<String, Int>,
    val topicsByDifficulty: Map<String, Int>,
    val averageTopicsPerCategory: Double,
    val mostActiveCategory: String?,
    val newestTopic: Topic?,
    val oldestTopic: Topic?
)
