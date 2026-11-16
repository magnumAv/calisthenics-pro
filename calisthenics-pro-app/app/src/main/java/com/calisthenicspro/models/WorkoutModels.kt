package com.calisthenicspro.models

import androidx.room.Entity
import androidx.room.PrimaryKey

// تعریف تمرینات موجود
@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey val id: String, // مثلا exercise1
    val title: String,
    val videoId: String, // نام فایل ویدیویی در res/raw
    val type: String, // timed یا reps
    val defaultDurationSeconds: Int, // برای تمرینات زمانی
    val defaultReps: Int
)

// تعریف برنامه‌های تمرینی (برای روزهای مختلف)
@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dayName: String, // مثلا "Monday", "PowerDay"
    val name: String, // نام برنامه
    val description: String
)

// جدول واسط برای اتصال تمرینات به روتین‌ها با ترتیب مشخص
@Entity(tableName = "routine_exercises")
data class RoutineExercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val routineId: Int,
    val exerciseId: String,
    val order: Int,
    val sets: Int,
    val repsOrTime: String // مثلا "10 reps" یا "60s"
)
