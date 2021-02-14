package com.yj.kidcentivize.receivers

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.yj.kidcentivize.db.TimeDao
import com.yj.kidcentivize.db.UserDao
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminReceiver constructor(): DeviceAdminReceiver() {

    @Inject lateinit var timeDao: TimeDao
    @Inject lateinit var userDao: UserDao

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Log.d("hey", "Admin enabled")
        AppLogic.with(context, timeDao, userDao).startLoop()
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
    }

}