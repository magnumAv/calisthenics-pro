package com.calisthenicspro.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.calisthenicspro.R
import com.calisthenicspro.databinding.ActivityMainBinding
import com.calisthenicspro.ui.assessment.DifficultyAssessmentActivity
import com.calisthenicspro.utils.PreferenceManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)

        if (!preferenceManager.hasCompletedAssessment) {
            val intent = Intent(this, DifficultyAssessmentActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_host) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }
}
