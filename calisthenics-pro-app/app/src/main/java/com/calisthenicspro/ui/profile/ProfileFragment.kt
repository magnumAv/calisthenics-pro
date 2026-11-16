package com.calisthenicspro.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.calisthenicspro.ui.assessment.DifficultyAssessmentActivity
import com.calisthenicspro.utils.PreferenceManager

class ProfileFragment : Fragment() {

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
            text = "پروفایل"
            textSize = 24f
            setTextColor(0xFFFFFFFF.toInt())
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 16)
        }

        val info = TextView(requireContext()).apply {
            text = "سطح: ${preferenceManager.userLevel}\nروزهای تمرین: ${preferenceManager.trainingDays}\nهدف: ${preferenceManager.weeklyGoal}"
            textSize = 16f
            setTextColor(0xFFDDDDDD.toInt())
            setPadding(0, 0, 0, 24)
        }

        val retestButton = Button(requireContext()).apply {
            text = "تغییر سطح و روتین"
            setOnClickListener {
                startActivity(Intent(requireContext(), DifficultyAssessmentActivity::class.java))
                requireActivity().finish()
            }
        }

        root.addView(title)
        root.addView(info)
        root.addView(retestButton)
        return root
    }
}
