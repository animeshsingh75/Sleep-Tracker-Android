package com.example.android.trackmysleepquality.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.*

class SleepQualityViewModel(
        private val sleepNightKey: Long = 0L,
        val database: SleepDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope= CoroutineScope(Dispatchers.Main+viewModelJob)
    private val _navigateToSleepTracker= MutableLiveData<Boolean>()
    val navigateToSleepQuality:LiveData<Boolean>
        get() = _navigateToSleepTracker
    fun doneNavigate(){
        _navigateToSleepTracker.value=false
    }
    fun onSetSleepQuality(quality:Int){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val tonight=database.get(sleepNightKey)?:return@withContext
                tonight.sleepQuality=quality
                database.update(tonight)
            }
            _navigateToSleepTracker.value=true
        }
    }
    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }
}