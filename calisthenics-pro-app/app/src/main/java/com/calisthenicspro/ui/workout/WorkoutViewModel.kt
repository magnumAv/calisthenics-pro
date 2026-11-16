package com.calisthenicspro.ui.workout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calisthenicspro.models.RoutineExercise
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WorkoutViewModel : ViewModel() {

    private val _currentExercise = MutableLiveData<RoutineExercise>()
    val currentExercise: LiveData<RoutineExercise> = _currentExercise

    private val _isResting = MutableLiveData<Boolean>(false)
    val isResting: LiveData<Boolean> = _isResting

    private val _timer = MutableLiveData<Int>()
    val timer: LiveData<Int> = _timer

    private var routineExercises: List<RoutineExercise> = emptyList()
    private var currentIndex = 0

    fun startRoutine(exercises: List<RoutineExercise>) {
        routineExercises = exercises
        currentIndex = 0
        playCurrentExercise()
    }

    private fun playCurrentExercise() {
        if (currentIndex < routineExercises.size) {
            _currentExercise.value = routineExercises[currentIndex]
            _isResting.value = false
            // در اینجا باید منطق پخش ویدیو فعال شود
        }
    }

    fun onExerciseFinished() {
        // شروع استراحت
        _isResting.value = true
        startTimer(60) // 60 ثانیه استراحت پیش‌فرض
    }

    private fun startTimer(seconds: Int) {
        viewModelScope.launch {
            for (i in seconds downTo 0) {
                _timer.value = i
                delay(1000)
            }
            // پایان استراحت، تمرین بعدی
            currentIndex++
            playCurrentExercise()
        }
    }
}
