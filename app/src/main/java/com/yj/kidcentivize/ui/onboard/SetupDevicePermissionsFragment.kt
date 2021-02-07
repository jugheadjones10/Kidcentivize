package com.yj.kidcentivize.ui.onboard

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yj.kidcentivize.R
import com.yj.kidcentivize.databinding.FragmentSetupDevicePermissionsBinding
import com.yj.kidcentivize.receivers.AdminReceiver


class SetupDevicePermissionsFragment : Fragment() {

    private lateinit var binding: FragmentSetupDevicePermissionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetupDevicePermissionsBinding.inflate(inflater, container, false)
        val navController = findNavController()


        binding.handlers = object: SetupDevicePermissionsHandlers {

            override fun openUsageStatsSettings() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // According to user reports, some devices open the wrong screen
                    // with the Settings.ACTION_USAGE_ACCESS_SETTINGS
                    // but using an activity launcher to open this intent works for them.
                    // This intent works at regular android too, so try this first
                    // and use the "correct" one as fallback.

                    try {
                        startActivity(
                            Intent()
                                .setClassName("com.android.settings", "com.android.settings.Settings\$UsageAccessSettingsActivity")
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                    } catch (ex: Exception) {
                        try {
                            startActivity(
                                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                        } catch (ex: Exception) {
                            //Show dialog for user. Open time limit implementation is too weird.
                        }
                    }
                }
            }

            override fun manageDeviceAdmin() {
                try {
                    startActivity(
                        Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                            .putExtra(
                                DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                                ComponentName(context!!, AdminReceiver::class.java)
                            )
                    )
                } catch(ex: Exception){
                    Log.d("hey", ex.stackTrace.toString())
                }
            }

            override fun openNotificationAccessSettings() {
                try {
                    startActivity(
                        Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                } catch (ex: Exception) {
                    Toast.makeText(context, R.string.error_general, Toast.LENGTH_SHORT).show()
                }
            }

            override fun openDrawOverOtherAppsScreen() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        startActivity(
                            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context!!.packageName))
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                    } catch (ex: Exception) {
                        Toast.makeText(context, R.string.error_general, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun openAccessibilitySettings() {
                try {
                    startActivity(
                        Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                } catch (ex: Exception) {
                    Toast.makeText(context, R.string.error_general, Toast.LENGTH_SHORT).show()
                }
            }

            override fun gotoNextStep() {
                navController.navigate(R.id.action_setupDevicePermissionsFragment_to_navigation_home)
            }

        }

        refreshStatus()


        return binding.root
    }

    private fun refreshStatus() {
        val devicePermissionsChecker = DevicePermissionsChecker(requireContext())
        binding.protectionLevel = devicePermissionsChecker.getCurrentProtectionLevel()
        binding.usageStatsAccess = devicePermissionsChecker.getUsageStatsPermissionStatus()
        binding.overlayPermission = devicePermissionsChecker.getOverlayPermissionStatus()
    }

    override fun onResume() {
        super.onResume()

        refreshStatus()
    }

}

interface SetupDevicePermissionsHandlers {
    fun manageDeviceAdmin()
    fun openUsageStatsSettings()
    fun openNotificationAccessSettings()
    fun openDrawOverOtherAppsScreen()
    fun openAccessibilitySettings()
    fun gotoNextStep()
}
