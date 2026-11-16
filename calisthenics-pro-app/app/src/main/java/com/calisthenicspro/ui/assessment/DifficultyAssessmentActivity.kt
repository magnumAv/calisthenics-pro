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

        val levels = arrayOf("مبتدی (نیاز به حرکات اصلاحی و آسان)", "متوسط (توانایی اجرای حرکات پایه مثل بارفیکس و شنا)", "پیشرفته (تسلط بر حرکات تعادلی، هندستاند و ماسل‌آپ)")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, levels)
        binding.spinnerLevel.adapter = adapter

        binding.btnSaveAssessment.setOnClickListener {
            val selectedPosition = binding.spinnerLevel.selectedItemPosition
            val levelName = when(selectedPosition) {
                0 -> "Beginner"
                1 -> "Intermediate"
                else -> "Advanced"
            }

            // ذخیره سطح کاربر
            preferenceManager.userId = levelName

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
