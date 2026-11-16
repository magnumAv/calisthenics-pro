package com.calisthenicspro.ui.workout

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
import com.calisthenicspro.utils.PreferenceManager
import com.google.gson.Gson

class WorkoutPlanFragment : Fragment() {

    private val preferenceManager by lazy { PreferenceManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }

        val title = TextView(requireContext()).apply {
            text = "برنامه تمرینی"
            textSize = 24f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 16)
        }

        val routine = RoutinePlanner().buildRoutine(
            preferenceManager.userLevel,
            preferenceManager.trainingDays,
            preferenceManager.weeklyGoal
        )

        val daySelector = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 16)
        }

        routine.days.forEachIndexed { index, day ->
            val btn = Button(requireContext()).apply {
                text = day.dayName
                setBackgroundColor(if (index == 0) Color.parseColor("#FF5722") else Color.parseColor("#1E1E1E"))
                setTextColor(Color.WHITE)
                setPadding(18, 10, 18, 10)
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                ).apply {
                    marginEnd = 8
                }
                setOnClickListener {
                    val json = Gson().toJson(routine)
                    startActivity(Intent(requireContext(), WorkoutActivity::class.java).apply {
                        putExtra("routine_json", json)
                        putExtra("selected_day_index", index)
                    })
                }
            }
            daySelector.addView(btn)
        }

        val planText = TextView(requireContext()).apply {
            text = "${routine.planName}\n\n${routine.days.joinToString(separator = "\n\n") { day ->
                "• ${day.dayName}: ${day.title} (${day.durationMinutes} دقیقه)\n${day.exercises.joinToString(" | ") { it.name }}"
            }}"
            textSize = 15f
            setTextColor(0xFFDDDDDD.toInt())
            setPadding(0, 0, 0, 20)
        }

        val button = Button(requireContext()).apply {
            text = "شروع جلسه فعال"
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

        root.addView(title)
        root.addView(daySelector)
        root.addView(planText)
        root.addView(button)
        return root
    }
}
