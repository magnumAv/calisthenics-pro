package com.calisthenicspro.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.calisthenicspro.data.RoutinePlanner
import com.calisthenicspro.ui.workout.WorkoutActivity
import com.calisthenicspro.utils.PreferenceManager
import com.google.gson.Gson

class HomeFragment : Fragment() {

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

        val routine = RoutinePlanner().buildRoutine(
            preferenceManager.userLevel,
            preferenceManager.trainingDays,
            preferenceManager.weeklyGoal
        )

        val title = TextView(requireContext()).apply {
            text = "Calisthenics Pro"
            textSize = 28f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 16)
        }

        val statPanel = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            weightSum = 3f
            setPadding(0, 0, 0, 16)
        }

        listOf(
            "سطح\n${preferenceManager.userLevel}" to 0xFF2A2A2A.toInt(),
            "روزها\n${preferenceManager.trainingDays}" to 0xFF1E1E1E.toInt(),
            "رشد\n${preferenceManager.userProgressScore}%" to 0xFF2B2B2B.toInt()
        ).forEach { item ->
            val card = TextView(requireContext()).apply {
                text = item.first
                setTextColor(Color.WHITE)
                textSize = 16f
                gravity = Gravity.CENTER
                setBackgroundColor(item.second)
                setPadding(20, 24, 20, 24)
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply {
                    marginEnd = 8
                }
            }
            statPanel.addView(card)
        }

        val summaryText = TextView(requireContext()).apply {
            text = "هدف هفته: ${preferenceManager.weeklyGoal}\nآخرین جلسه: ${preferenceManager.lastWorkoutMinutes} دقیقه\nرکورد تداوم: ${preferenceManager.workoutStreak} روز"
            textSize = 15f
            setTextColor(0xFFCCCCCC.toInt())
            setPadding(0, 0, 0, 20)
        }

        val dayBadge = TextView(requireContext()).apply {
            text = "روز فعلی: ${routine.days.firstOrNull()?.dayName ?: "Mon"} · ${routine.days.firstOrNull()?.title ?: "Strength Base"}"
            textSize = 16f
            setTextColor(0xFF00E676.toInt())
            setPadding(0, 0, 0, 16)
        }

        val button = Button(requireContext()).apply {
            text = "شروع تمرین امروز"
            setBackgroundColor(Color.parseColor("#FF5722"))
            setTextColor(Color.WHITE)
            setOnClickListener {
                val json = Gson().toJson(routine)
                startActivity(Intent(requireContext(), WorkoutActivity::class.java).apply {
                    putExtra("routine_json", json)
                    putExtra("selected_day_index", 0)
                })
            }
        }

        val detail = TextView(requireContext()).apply {
            text = "روتین پیشنهادی شامل گرم‌کردن، تمرینات اصلی، استراحت کنترل‌شده و تداوم روزانه است. در سطوح بالاتر، تمرین‌ها سنگین‌تر و تمرکز روی تکنیک و قدرت بیشتری دارد."
            textSize = 15f
            setTextColor(0xFFBDBDBD.toInt())
            setPadding(0, 16, 0, 0)
        }

        root.addView(title)
        root.addView(statPanel)
        root.addView(summaryText)
        root.addView(dayBadge)
        root.addView(button)
        root.addView(detail)
        return root
    }
}
