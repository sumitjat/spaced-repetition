package com.proxy.spacedrepition.domain.model

import java.util.UUID

data class Topic(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val category: String,
    val difficultyLevel: DifficultyLevel,
    val notes: String = "",
    val tags: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
) {
    init {
        require(name.isNotBlank()) { "Topic name cannot be blank" }
        require(tags.all { it.isNotBlank() }) { "Tags cannot contain blank values" }
        require(difficultyLevel != DifficultyLevel.UNDEFINED) { "Difficulty level must be defined" }
        require(category.isNotBlank()) { "Category cannot be blank" }
    }

    /**
     * Business logic: Determine if this topic needs urgent review This logic belongs in the domain,
     * not in the UI or data layer
     */
    fun isUrgentForInterview(daysUntilInterview: Int): Boolean {
        return when (difficultyLevel) {
            DifficultyLevel.BEGINNER -> daysUntilInterview <= 3
            DifficultyLevel.INTERMEDIATE -> daysUntilInterview <= 7
            DifficultyLevel.ADVANCED -> daysUntilInterview <= 14
            else -> {
                true
            }
        }
    }

    /** Business logic: Calculate topic complexity score for scheduling */
    fun complexityScore(): Int {
        val baseScore = difficultyLevel.baseScore
        val tagBonus = tags.size * 2
        return baseScore + tagBonus
    }
}
