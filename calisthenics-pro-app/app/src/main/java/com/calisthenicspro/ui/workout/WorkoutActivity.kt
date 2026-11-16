package com.calisthenicspro.ui.workout

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.calisthenicspro.databinding.ActivityWorkoutBinding

class WorkoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutBinding
    private val viewModel: WorkoutViewModel by viewModels()
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPlayer()
        observeViewModel()
    }

    private fun setupPlayer() {
        player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player
    }

    private fun observeViewModel() {
        viewModel.currentExercise.observe(this) { exercise ->
            // پیدا کردن ویدیو بر اساس videoId از پوشه res/raw
            val rawId = resources.getIdentifier(exercise.exerciseId, "raw", packageName)
            if (rawId != 0) {
                val uri = Uri.parse("android.resource://$packageName/$rawId")
                val mediaItem = MediaItem.fromUri(uri)
                player?.setMediaItem(mediaItem)
                player?.prepare()
                player?.play()
            }
        }

        viewModel.isResting.observe(this) { isResting ->
            binding.restOverlay.visibility = if (isResting) View.VISIBLE else View.GONE
        }

        viewModel.timer.observe(this) { seconds ->
            binding.tvTimer.text = "$seconds ثانیه"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }
}
