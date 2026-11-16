package com.calisthenicspro.ui.analytics

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.calisthenicspro.utils.PreferenceManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ProgressFragment : Fragment() {

    private val preferenceManager by lazy { PreferenceManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
            setBackgroundColor(Color.parseColor("#121212"))
        }

        val title = TextView(requireContext()).apply {
            text = "پیشرفت"
            textSize = 24f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 16)
        }

        val statRow = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            weightSum = 3f
            setPadding(0, 0, 0, 16)
        }

        val stats = listOf(
            "رشد\n${preferenceManager.userProgressScore}%" to 0xFF2A2A2A.toInt(),
            "جلسات\n${preferenceManager.totalWorkoutsCompleted}" to 0xFF1E1E1E.toInt(),
            "تداوم\n${preferenceManager.workoutStreak} روز" to 0xFF2B2B2B.toInt()
        )

        stats.forEach { (label, color) ->
            val card = TextView(requireContext()).apply {
                text = label
                setTextColor(Color.WHITE)
                textSize = 15f
                gravity = Gravity.CENTER
                setBackgroundColor(color)
                setPadding(18, 20, 18, 20)
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply {
                    marginEnd = 8
                }
            }
            statRow.addView(card)
        }

        val chart = LineChart(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                360
            )
        }

        val history = preferenceManager.workoutHistory
        val chartValues = history.takeLast(6).mapIndexed { index, entry ->
            Entry(index.toFloat() + 1f, entry.score.toFloat())
        }

        val summary = TextView(requireContext()).apply {
            text = "سطح فعلی: ${preferenceManager.userLevel}\nروزهای تمرین: ${preferenceManager.trainingDays}\nآخرین جلسه: ${preferenceManager.lastWorkoutMinutes} دقیقه\nآخرین روز: ${preferenceManager.lastWorkoutDay}\nتعداد سوابق: ${history.size}"
            textSize = 15f
            setTextColor(0xFFCCCCCC.toInt())
            setPadding(0, 16, 0, 12)
        }

        val historyTitle = TextView(requireContext()).apply {
            text = "سوابق تمرین"
            textSize = 18f
            setTextColor(Color.WHITE)
            setPadding(0, 8, 0, 8)
        }

        val historyList = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
        }

        if (history.isEmpty()) {
            val empty = TextView(requireContext()).apply {
                text = "هنوز جلسات تمرینی ثبت نشده است. اولین تمرین خود را شروع کنید."
                setTextColor(0xFFBDBDBD.toInt())
                textSize = 14f
            }
            historyList.addView(empty)
        } else {
            history.forEach { item ->
                val row = TextView(requireContext()).apply {
                    text = "${item.date} · ${item.dayName} · ${item.minutes} دقیقه · امتیاز ${item.score}"
                    setTextColor(0xFFDDDDDD.toInt())
                    textSize = 14f
                    setPadding(0, 6, 0, 6)
                }
                historyList.addView(row)
            }
        }

        root.addView(title)
        root.addView(statRow)
        root.addView(chart)
        root.addView(summary)
        root.addView(historyTitle)
        root.addView(historyList)

        val dataSet = LineDataSet(chartValues.ifEmpty {
            listOf(Entry(1f, 35f), Entry(2f, 42f), Entry(3f, 50f), Entry(4f, 64f))
        }, "رشد تمرینی")
        dataSet.color = Color.parseColor("#00E676")
        dataSet.valueTextColor = Color.WHITE
        dataSet.circleRadius = 4f
        dataSet.setCircleColor(Color.WHITE)

        chart.data = LineData(dataSet)
        chart.description.text = "روند رشد"
        chart.animateX(800)
        chart.invalidate()
        return root
    }
}
