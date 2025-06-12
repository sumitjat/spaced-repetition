package com.proxy.spacedrepition.domain.model

import java.util.UUID
import kotlin.random.Random

data class Topic(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val category: TopicCategory,
    val difficultyLevel: DifficultLevel,
    val notes: String = "",
    val tags: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
) {
    init {
        require(name.isNotBlank()) { "Topic name cannot be blank" }
        require(tags.all { it.isNotBlank() }) { "Tags cannot contain blank values" }
        require(difficultyLevel != DifficultLevel.UNDEFINED) { "Difficulty level must be defined" }
    }

    fun isUrgentForInterview(daysUntilInterview: Int): Boolean {
        return when (difficultyLevel) {
            DifficultLevel.EASY -> daysUntilInterview <= 30
            DifficultLevel.MEDIUM -> daysUntilInterview <= 15
            DifficultLevel.HARD -> daysUntilInterview <= 7
            DifficultLevel.VERY_HARD -> daysUntilInterview <= 3
            DifficultLevel.UNDEFINED -> false
        }
    }
}
