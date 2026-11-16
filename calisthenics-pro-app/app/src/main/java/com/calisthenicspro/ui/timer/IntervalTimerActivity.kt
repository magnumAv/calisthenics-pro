package com.calisthenicspro.ui.timer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.calisthenicspro.R
import com.calisthenicspro.databinding.ActivityIntervalTimerBinding

class IntervalTimerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntervalTimerBinding
    private var timer: CountDownTimer? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntervalTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaPlayer = MediaPlayer.create(this, R.raw.finished)

        binding.btnStartTimer.setOnClickListener {
            val workTime = binding.etWorkTime.text.toString().toLongOrNull() ?: 30
            startTimer(workTime * 1000)
        }
    }

    private fun startTimer(millis: Long) {
        timer?.cancel()
        timer = object : CountDownTimer(millis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimerDisplay.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                binding.tvTimerDisplay.text = "0"
                mediaPlayer?.start()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        mediaPlayer?.release()
    }
}
