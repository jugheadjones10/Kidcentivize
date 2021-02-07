package com.yj.kidcentivize.ui.onboard

import android.app.AppOpsManager
import android.app.Application
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import com.yj.kidcentivize.BuildConfig
import com.yj.kidcentivize.receivers.AdminReceiver

enum class RuntimePermissionStatus {
    NotRequired, Granted, NotGranted
}

enum class ProtectionLevel {
    None, SimpleDeviceAdmin, PasswordDeviceAdmin, DeviceOwner
}

class DevicePermissionsChecker(context: Context){
    private val applicationContext = context.applicationContext

    private val policyManager = applicationContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    private val appOpsManager = applicationContext.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    private val packageManager = applicationContext.packageManager

    fun getUsageStatsPermissionStatus(): RuntimePermissionStatus {
        val appOpsStatus = appOpsManager.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), applicationContext.packageName)
        val packageManagerStatus = packageManager.checkPermission("android.permission.PACKAGE_USAGE_STATS", BuildConfig.APPLICATION_ID)

        val allowedUsingSystemSettings = appOpsStatus == AppOpsManager.MODE_ALLOWED
        val allowedUsingAdb = appOpsStatus == AppOpsManager.MODE_DEFAULT && packageManagerStatus == PackageManager.PERMISSION_GRANTED

        if(allowedUsingSystemSettings || allowedUsingAdb) {
            return RuntimePermissionStatus.Granted
        } else {
            return RuntimePermissionStatus.NotGranted
        }
    }

    fun getOverlayPermissionStatus() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        if (Settings.canDrawOverlays(applicationContext as Application))
            RuntimePermissionStatus.Granted
        else
            RuntimePermissionStatus.NotGranted
    else
        RuntimePermissionStatus.NotRequired


    fun getCurrentProtectionLevel(): ProtectionLevel {
        val component = ComponentName(applicationContext, AdminReceiver::class.java)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (policyManager.isDeviceOwnerApp(applicationContext.packageName)) {
                ProtectionLevel.DeviceOwner
            } else if (policyManager.isAdminActive(component)) {
                ProtectionLevel.SimpleDeviceAdmin
            } else {
                ProtectionLevel.None
            }
        } else /* if below Lollipop */ {
            if (policyManager.isAdminActive(component)) {
                ProtectionLevel.PasswordDeviceAdmin
            } else {
                ProtectionLevel.None
            }
        }
    }



}