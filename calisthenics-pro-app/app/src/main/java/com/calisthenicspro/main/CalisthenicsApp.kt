package com.calisthenicspro.main

import android.app.Application
import com.calisthenicspro.database.AppDatabase
import com.calisthenicspro.utils.Logger

class CalisthenicsApp : Application() {

    companion object {
        private const val TAG = "CalisthenicsApp"
        lateinit var instance: CalisthenicsApp
            private set
    }

    lateinit var appDatabase: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        Logger.i(TAG, "Calisthenics Pro App Initialized")
        appDatabase = AppDatabase.getInstance(this)
    }
}
