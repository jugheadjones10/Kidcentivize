package com.yj.kidcentivize.receivers

import android.content.Context
import android.util.Log
import com.yj.kidcentivize.db.Time
import com.yj.kidcentivize.db.TimeDao
import com.yj.kidcentivize.db.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AppLogic(
    private val context: Context,
    private val timeDao: TimeDao,
    private val userDao: UserDao
){

    companion object {
        private var instance: AppLogic? = null
        fun with(context: Context, timeDao: TimeDao, userDao: UserDao): AppLogic {
            if (instance == null) {
                instance = AppLogic(context, timeDao, userDao)
            }
            return instance!!
        }
    }

    fun startLoop(){
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("hey", "Loop started enabled")
            backgroundServiceLoop()
        }
    }

    private suspend fun backgroundServiceLoop(){
        while(true){
            timeDao.updateTime(
                Time(
                  "Now",
                    LocalDateTime.now()
                )
            )

            val timeRemaining = userDao.getTimeRemaining()
            if(timeRemaining == 0){

            }
        }
    }


}
