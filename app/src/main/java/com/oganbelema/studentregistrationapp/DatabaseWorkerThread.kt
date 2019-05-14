package com.oganbelema.studentregistrationapp

import android.os.Handler
import android.os.HandlerThread

class DatabaseWorkerThread(threadName: String): HandlerThread(threadName) {

    private lateinit var workHandler: Handler

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        workHandler = Handler(looper)
    }

    fun postTask(task: Runnable){
        workHandler.post(task)
    }
}