package com.proxy.spacedrepition.domain.model

enum class DifficultyLevel(val displayName: String, val baseScore: Int) {

    UNDEFINED("Undefined", 0),
    BEGINNER("Beginner", 10),
    INTERMEDIATE("Intermediate", 20),
    ADVANCED("Advanced", 30);

    companion object {
        fun fromString(value: String): DifficultyLevel {
            return DifficultyLevel.entries.find { it.name.equals(value, ignoreCase = true) }
                ?: BEGINNER
        }
    }
}

enum class ConfidenceLevel(val displayName: String, val multiplier: Double) {
    FORGOT("Forgot", 0.0), // Reset interval
    HARD("Hard", 0.6), // Reduce interval
    GOOD("Good", 1.0), // Standard interval
    EASY("Easy", 2.5); // Increase interval significantly

    companion object {
        fun fromString(value: String): ConfidenceLevel {
            return ConfidenceLevel.entries.find { it.name.equals(value, ignoreCase = true) } ?: GOOD
        }
    }
}

enum class UrgencyLevel(val displayName: String, val priority: Int) {
    LOW("Low", 1),
    MEDIUM("Medium", 2),
    HIGH("High", 3),
    CRITICAL("Critical", 4),
}

enum class SessionQuality(val displayName: String, val color: String) {
    EXCELLENT("Excellent", "#4CAF50"),
    GOOD("Good", "#8BC34A"),
    AVERAGE("Average", "#FF9800"),
    NEEDS_IMPROVEMENT("Needs Improvement", "#F44336"),
}
