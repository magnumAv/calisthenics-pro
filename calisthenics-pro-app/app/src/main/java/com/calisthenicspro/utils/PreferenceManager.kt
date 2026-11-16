package com.calisthenicspro.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class WorkoutHistoryEntry(
    val date: String,
    val dayName: String,
    val level: String,
    val minutes: Int,
    val score: Int
)

class PreferenceManager(context: Context) {
    private val prefs = context.getSharedPreferences("calisthenics_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    var userLevel: String
        get() = prefs.getString("user_level", "Beginner") ?: "Beginner"
        set(value) = prefs.edit().putString("user_level", value).apply()

    var trainingDays: Int
        get() = prefs.getInt("training_days", 3)
        set(value) = prefs.edit().putInt("training_days", value).apply()

    var weeklyGoal: String
        get() = prefs.getString("weekly_goal", "General Fitness") ?: "General Fitness"
        set(value) = prefs.edit().putString("weekly_goal", value).apply()

    var hasCompletedAssessment: Boolean
        get() = prefs.getBoolean("assessment_done", false)
        set(value) = prefs.edit().putBoolean("assessment_done", value).apply()

    var lastWorkoutMinutes: Int
        get() = prefs.getInt("last_workout_minutes", 45)
        set(value) = prefs.edit().putInt("last_workout_minutes", value).apply()

    var userProgressScore: Int
        get() = prefs.getInt("user_progress_score", 62)
        set(value) = prefs.edit().putInt("user_progress_score", value).apply()

    var workoutStreak: Int
        get() = prefs.getInt("workout_streak", 5)
        set(value) = prefs.edit().putInt("workout_streak", value).apply()

    var lastWorkoutDay: String
        get() = prefs.getString("last_workout_day", "Mon") ?: "Mon"
        set(value) = prefs.edit().putString("last_workout_day", value).apply()

    var totalWorkoutsCompleted: Int
        get() = prefs.getInt("total_workouts_completed", 0)
        set(value) = prefs.edit().putInt("total_workouts_completed", value).apply()

    var workoutHistory: List<WorkoutHistoryEntry>
        get() {
            val raw = prefs.getString("workout_history", null) ?: return emptyList()
            return try {
                val type = object : TypeToken<List<WorkoutHistoryEntry>>() {}.type
                gson.fromJson<List<WorkoutHistoryEntry>>(raw, type) ?: emptyList()
            } catch (_: Exception) {
                emptyList()
            }
        }
        set(value) = prefs.edit().putString("workout_history", gson.toJson(value)).apply()

    fun addWorkoutHistory(entry: WorkoutHistoryEntry) {
        val updated = (workoutHistory + entry).takeLast(12)
        workoutHistory = updated
        lastWorkoutMinutes = entry.minutes
        lastWorkoutDay = entry.dayName
        totalWorkoutsCompleted = totalWorkoutsCompleted + 1
        userProgressScore = (userProgressScore + entry.score.coerceIn(0, 30)).coerceAtMost(100)
        workoutStreak = (workoutStreak + 1).coerceAtMost(30)
    }

    fun reset() {
        prefs.edit().clear().apply()
    }
}
