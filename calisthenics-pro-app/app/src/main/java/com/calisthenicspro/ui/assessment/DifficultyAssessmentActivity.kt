package com.calisthenicspro.ui.assessment

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.calisthenicspro.databinding.ActivityAssessmentBinding
import com.calisthenicspro.main.MainActivity
import com.calisthenicspro.utils.PreferenceManager

class DifficultyAssessmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssessmentBinding
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssessmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)

        val levels = arrayOf(
            "مبتدی (شروع آسان و تکنیک‌محور)",
            "متوسط (سطح پایه calisthenics)",
            "پیشرفته (حرکات تخصصی و قدرت بالا)"
        )
        val dayOptions = arrayOf("3 روز در هفته", "4 روز در هفته", "5 روز در هفته")
        val goalOptions = arrayOf("General Fitness", "Strength", "Muscle Gain", "Skill Work")

        binding.spinnerLevel.adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, levels)
        binding.spinnerDays.adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dayOptions)
        binding.spinnerGoal.adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, goalOptions)

        binding.btnSaveAssessment.setOnClickListener {
            val levelName = when (binding.spinnerLevel.selectedItemPosition) {
                0 -> "Beginner"
                1 -> "Intermediate"
                else -> "Advanced"
            }

            val selectedDays = when (binding.spinnerDays.selectedItemPosition) {
                0 -> 3
                1 -> 4
                else -> 5
            }

            val selectedGoal = goalOptions[binding.spinnerGoal.selectedItemPosition]

            preferenceManager.userLevel = levelName
            preferenceManager.trainingDays = selectedDays
            preferenceManager.weeklyGoal = selectedGoal
            preferenceManager.hasCompletedAssessment = true

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
