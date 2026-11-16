package com.calisthenicspro.ui.workout

import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.calisthenicspro.data.WorkoutRoutine
import com.calisthenicspro.databinding.ActivityWorkoutBinding
import com.calisthenicspro.utils.ExerciseVideoResolver
import com.calisthenicspro.utils.PreferenceManager
import com.calisthenicspro.utils.WorkoutHistoryEntry
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WorkoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutBinding
    private lateinit var routine: WorkoutRoutine
    private lateinit var preferenceManager: PreferenceManager
    private var activeDayIndex = 0
    private var activeExerciseIndex = 0
    private var countdownTimer: CountDownTimer? = null
    private var sessionMinutes = 0
    private var hasCompletedSession = false
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)
        player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player

        val rawRoutine = intent.getStringExtra("routine_json")
        val selectedDayIndex = intent.getIntExtra("selected_day_index", 0)

        routine = if (rawRoutine != null) {
            Gson().fromJson(rawRoutine, WorkoutRoutine::class.java)
        } else {
            WorkoutRoutine(
                planName = "Default Plan",
                level = "Beginner",
                weeklyGoal = "General Fitness",
                totalDays = 1,
                days = emptyList()
            )
        }

        if (routine.days.isNotEmpty()) {
            activeDayIndex = selectedDayIndex.coerceIn(0, routine.days.lastIndex)
        }

        binding.btnNext.setOnClickListener { nextExercise() }
        binding.btnPrevious.setOnClickListener { previousExercise() }

        if (routine.days.isNotEmpty()) {
            showCurrentExercise()
        } else {
            binding.tvDayTitle.text = "روز تمرینی"
            binding.tvExerciseName.text = "بدون تمرین"
            binding.tvExerciseMeta.text = "هیچ تمرینی برای نمایش وجود ندارد"
            binding.tvTimer.text = "00:00"
            binding.tvProgress.text = "0 / 0"
            binding.playerView.visibility = View.GONE
        }
    }

    private fun showCurrentExercise() {
        val day = routine.days[activeDayIndex]
        val exercise = day.exercises[activeExerciseIndex]

        binding.tvDayTitle.text = "${day.dayName} · ${day.title}"
        binding.tvExerciseName.text = exercise.name
        binding.tvExerciseMeta.text = "${exercise.sets} ست × ${exercise.reps} تکرار · ${exercise.restSeconds}s استراحت · ${exercise.notes}"
        binding.tvProgress.text = "${activeExerciseIndex + 1} / ${day.exercises.size}"

        playExerciseVideo(exercise.name)

        val cycleSeconds = maxOf(20, exercise.reps * 2 + exercise.restSeconds)
        sessionMinutes = routine.days.sumOf { it.durationMinutes }
        startCountdown(cycleSeconds)
    }

    private fun playExerciseVideo(exerciseName: String) {
        val uri = ExerciseVideoResolver.resolveUri(this, exerciseName)
        if (uri == null) {
            binding.playerView.visibility = View.GONE
            return
        }

        binding.playerView.visibility = View.VISIBLE
        val mediaItem = MediaItem.fromUri(uri)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.playWhenReady = true
    }

    private fun nextExercise() {
        if (routine.days.isEmpty()) return

        val currentDay = routine.days[activeDayIndex]
        val isLastExerciseInDay = activeExerciseIndex >= currentDay.exercises.size - 1
        val isLastDay = activeDayIndex >= routine.days.lastIndex

        if (!isLastExerciseInDay) {
            activeExerciseIndex += 1
            showCurrentExercise()
            return
        }

        if (!isLastDay) {
            activeDayIndex += 1
            activeExerciseIndex = 0
            showCurrentExercise()
            return
        }

        finalizeSession()
    }

    private fun previousExercise() {
        if (routine.days.isEmpty()) return

        if (activeExerciseIndex > 0) {
            activeExerciseIndex -= 1
            showCurrentExercise()
            return
        }

        if (activeDayIndex > 0) {
            activeDayIndex -= 1
            activeExerciseIndex = maxOf(0, routine.days[activeDayIndex].exercises.size - 1)
            showCurrentExercise()
        }
    }

    private fun finalizeSession() {
        if (hasCompletedSession) return
        hasCompletedSession = true

        val score = ((sessionMinutes / 10) + 20).coerceAtMost(100)
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val activeDayName = routine.days.getOrNull(activeDayIndex)?.dayName ?: "Mon"

        preferenceManager.addWorkoutHistory(
            WorkoutHistoryEntry(
                date = formattedDate,
                dayName = activeDayName,
                level = preferenceManager.userLevel,
                minutes = sessionMinutes,
                score = score
            )
        )

        binding.tvDayTitle.text = "جلسه کامل شد"
        binding.tvExerciseName.text = "Great work"
        binding.tvExerciseMeta.text = "مدت زمان: ${sessionMinutes} دقیقه · امتیاز: ${score}"
        binding.tvTimer.text = "00:00"
        binding.tvProgress.text = "Complete"
        countdownTimer?.cancel()
    }

    private fun startCountdown(seconds: Int) {
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer((seconds * 1000L), 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val totalSeconds = (millisUntilFinished / 1000).toInt()
                val minutes = totalSeconds / 60
                val secs = totalSeconds % 60
                binding.tvTimer.text = String.format("%02d:%02d", minutes, secs)
            }

            override fun onFinish() {
                binding.tvTimer.text = "00:00"
                nextExercise()
            }
        }.start()
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        countdownTimer?.cancel()
        player?.release()
    }
}
