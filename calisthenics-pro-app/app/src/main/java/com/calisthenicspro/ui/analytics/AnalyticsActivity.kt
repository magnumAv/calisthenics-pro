package com.calisthenicspro.ui.analytics

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.calisthenicspro.databinding.ActivityAnalyticsBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class AnalyticsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalyticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupChart()
    }

    private fun setupChart() {
        // داده‌های نمونه برای نمایش کالری مصرفی در هفته
        val entries = ArrayList<Entry>()
        entries.add(Entry(1f, 450f))
        entries.add(Entry(2f, 520f))
        entries.add(Entry(3f, 480f))
        entries.add(Entry(4f, 600f))
        entries.add(Entry(5f, 550f))
        entries.add(Entry(6f, 700f))
        entries.add(Entry(7f, 650f))

        val dataSet = LineDataSet(entries, "کالری سوزانده شده (هفتگی)")
        dataSet.color = Color.GREEN
        dataSet.valueTextColor = Color.WHITE
        dataSet.lineWidth = 3f
        dataSet.circleRadius = 5f
        dataSet.setCircleColor(Color.WHITE)

        val lineData = LineData(dataSet)
        binding.chartProgress.data = lineData
        binding.chartProgress.animateX(1000)
        binding.chartProgress.description.text = "میزان فعالیت هفتگی شما"
        binding.chartProgress.invalidate()
    }
}
