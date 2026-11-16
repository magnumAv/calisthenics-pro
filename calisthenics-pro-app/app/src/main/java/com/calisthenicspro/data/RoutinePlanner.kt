package com.calisthenicspro.data

import kotlin.math.max

data class WorkoutExercise(
    val name: String,
    val sets: Int,
    val reps: Int,
    val restSeconds: Int,
    val notes: String = ""
)

data class WorkoutDay(
    val dayName: String,
    val title: String,
    val focus: String,
    val durationMinutes: Int,
    val exercises: List<WorkoutExercise>
)

data class WorkoutRoutine(
    val planName: String,
    val level: String,
    val weeklyGoal: String,
    val totalDays: Int,
    val days: List<WorkoutDay>
)

class RoutinePlanner {
    fun buildRoutine(level: String, daysPerWeek: Int, goal: String): WorkoutRoutine {
        val resolvedLevel = when (level.lowercase()) {
            "beginner" -> "Beginner"
            "intermediate" -> "Intermediate"
            "advanced" -> "Advanced"
            else -> "Beginner"
        }

        val dayCount = max(2, daysPerWeek)

        val baseDays = when (resolvedLevel) {
            "Beginner" -> listOf(
                WorkoutDay("Mon", "Push + Core Foundation", "Upper body and brace", 45, listOf(
                    WorkoutExercise("Push-up", 3, 8, 45, "Straight line from head to heels"),
                    WorkoutExercise("Incline Push-up", 3, 10, 45, "If regular push-up is too hard"),
                    WorkoutExercise("Bodyweight Squat", 3, 12, 45, "Full ROM"),
                    WorkoutExercise("Plank", 3, 25, 30, "Ribs down")
                )),
                WorkoutDay("Wed", "Leg + Balance Base", "Lower body and hips", 50, listOf(
                    WorkoutExercise("Reverse Lunge", 3, 10, 45, "Control the descent"),
                    WorkoutExercise("Split Squat", 3, 8, 45, "Stay tall"),
                    WorkoutExercise("Glute Bridge", 3, 15, 45, "Squeeze strongly"),
                    WorkoutExercise("Dead Bug", 3, 10, 30, "Slow and controlled")
                )),
                WorkoutDay("Fri", "Pull + Posture", "Back and shoulders", 50, listOf(
                    WorkoutExercise("Australian Pull-up", 3, 6, 60, "Use bar or sturdy beam"),
                    WorkoutExercise("Inverted Row", 3, 8, 60, "Pull shoulder blades back"),
                    WorkoutExercise("Bear Crawl Hold", 3, 20, 30, "Stay hollow"),
                    WorkoutExercise("Hollow Hold", 3, 20, 30, "Neutral spine")
                ))
            )
            "Intermediate" -> listOf(
                WorkoutDay("Mon", "Upper Body Power", "Push + pull strength", 60, listOf(
                    WorkoutExercise("Pull-up", 4, 6, 60, "Full range of motion"),
                    WorkoutExercise("Dip", 4, 8, 60, "Chest up and controlled"),
                    WorkoutExercise("Push-up", 4, 12, 45, "Tempo 3-1-1"),
                    WorkoutExercise("Hanging Knee Raise", 3, 10, 45, "Strong core")
                )),
                WorkoutDay("Tue", "Lower Body Engine", "Leg strength + hypertrophy", 60, listOf(
                    WorkoutExercise("Squat", 4, 15, 45, "Deep squat"),
                    WorkoutExercise("Romanian Deadlift", 3, 12, 45, "Hip hinge strong"),
                    WorkoutExercise("Lunge", 3, 12, 45, "Alternate legs"),
                    WorkoutExercise("Calf Raise", 4, 20, 30, "Full stretch")
                )),
                WorkoutDay("Thu", "Skill & Conditioning", "Power + endurance", 60, listOf(
                    WorkoutExercise("Pike Push-up", 3, 10, 45, "Elbows close"),
                    WorkoutExercise("Tuck Planche Hold", 3, 15, 45, "Shoulders active"),
                    WorkoutExercise("Mountain Climber", 3, 20, 30, "Drive knees hard"),
                    WorkoutExercise("Front Lever Progression", 3, 15, 45, "Keep scapula active")
                )),
                WorkoutDay("Sat", "Full Circuit", "Conditioning + muscular endurance", 60, listOf(
                    WorkoutExercise("Burpee", 4, 10, 30, "Quality before speed"),
                    WorkoutExercise("Jump Squat", 4, 12, 30, "Explosive"),
                    WorkoutExercise("Chair Dip", 3, 12, 45, "Control the negative"),
                    WorkoutExercise("Plank Shoulder Tap", 3, 20, 30, "Stay stable")
                ))
            )
            else -> listOf(
                WorkoutDay("Mon", "Max Strength Pull/Push", "Upper body power", 75, listOf(
                    WorkoutExercise("Weighted Pull-up", 4, 6, 75, "Add load every few weeks"),
                    WorkoutExercise("Ring Dip", 4, 8, 60, "Full stretch, active shoulders"),
                    WorkoutExercise("Planche Push-up", 4, 5, 75, "Strict form"),
                    WorkoutExercise("Dragon Flag", 3, 6, 60, "Keep lower back neutral")
                )),
                WorkoutDay("Wed", "Leg Power", "Explosive lower-body work", 75, listOf(
                    WorkoutExercise("Bulgarian Split Squat", 4, 10, 60, "Stay upright"),
                    WorkoutExercise("Single-leg Squat", 4, 8, 60, "Long range"),
                    WorkoutExercise("Hamstring Walkout", 3, 12, 45, "Posterior chain"),
                    WorkoutExercise("Calf Raise", 5, 25, 30, "Slow lowering")
                )),
                WorkoutDay("Fri", "Skill & Gymnastics", "Advanced holds and lines", 75, listOf(
                    WorkoutExercise("Front Lever Hold", 4, 15, 60, "Keep tension through body"),
                    WorkoutExercise("Back Lever Progression", 3, 10, 60, "Ribs down"),
                    WorkoutExercise("Human Flag Progression", 3, 10, 60, "Side-body active"),
                    WorkoutExercise("Maltese Hold", 3, 15, 60, "Shoulder strength")
                )),
                WorkoutDay("Sun", "Conditioning Circuit", "Energy system and work capacity", 60, listOf(
                    WorkoutExercise("Burpee", 5, 12, 30, "Strong rhythm"),
                    WorkoutExercise("Jump Rope", 5, 50, 30, "Keep tension"),
                    WorkoutExercise("Handstand Push-up", 3, 6, 60, "Wall support"),
                    WorkoutExercise("Hollow Body Rock", 4, 15, 30, "Strong midline")
                ))
            )
        }

        val selectedDays = when {
            dayCount <= 2 -> baseDays.take(2)
            dayCount == 3 -> baseDays.take(3)
            dayCount == 4 -> baseDays.take(4)
            else -> baseDays.take(5.coerceAtMost(baseDays.size))
        }

        return WorkoutRoutine(
            planName = "${resolvedLevel} ${goal} Routine",
            level = resolvedLevel,
            weeklyGoal = goal,
            totalDays = selectedDays.size,
            days = selectedDays.ifEmpty { baseDays }
        )
    }
}
