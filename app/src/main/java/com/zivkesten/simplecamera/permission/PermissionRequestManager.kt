package com.lemonadeinc.lemonade.managers.permission

import android.Manifest
import android.app.Activity
import androidx.core.app.ActivityCompat
import com.zivkesten.simplecamera.permission.PermissionState
import com.lemonadeinc.analytics.Analytics
import com.lemonadeinc.analytics.Analytics.reportPermissionsRequest
import com.lemonadeinc.common.prefs.Preferences
import com.lemonadeinc.common.utils.android.SDKSupports
import com.lemonadeinc.lemonade.utils.PermissionUtil
import com.lemonadeinc.lemonade.utils.android.intents.IntentLauncher

object PermissionRequestManager {

    const val REQUEST_PERMISSIONS = 1

    const val BATTERY_OPTIMIZATION = "battery_optimization"

    const val PREF_REQUESTED_SUFFIX = "_requested"

    @JvmStatic
    fun requestNeededPermissions(context: Activity, permissions: Array<String>): Boolean {
        if (PermissionUtil.hasNeededPermissions(context, permissions)) {
            // No need to request, we already have all permissions needed
            return false
        }
        requestPermissions(context, permissions, REQUEST_PERMISSIONS)
        return true
    }

    fun requestPermission(activity: Activity, permission: String, requestCode: Int) {
        val permissions =
            if (permission == Manifest.permission.ACCESS_BACKGROUND_LOCATION && SDKSupports.Q && !SDKSupports.R) {
                arrayOf(permission, Manifest.permission.ACCESS_FINE_LOCATION)
            } else if (permission == Manifest.permission.ACCESS_FINE_LOCATION && SDKSupports.S) {
                arrayOf(permission, Manifest.permission.ACCESS_COARSE_LOCATION)
            } else {
                arrayOf(permission)
            }
        if (permission == Manifest.permission.POST_NOTIFICATIONS) {
            Analytics.reportPushPermissionDialogShown()
        }
        requestPermissions(activity, permissions, requestCode)
    }

    private fun requestPermissions(
        activity: Activity,
        permissions: Array<String>,
        requestCode: Int
    ) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
        val requested = persistPermissionRequest(*permissions)
        reportPermissionsRequest(requested.toString())
    }

    fun requestPermissionSettings(activity: Activity, permission: String, requestCode: Int) {
        if (canRequestBackgroundLocation(activity, permission)) {
            requestPermission(activity, permission, requestCode)
        } else {
            IntentLauncher.goToAppDetails(activity, requestCode)
        }
    }

    fun requestIgnoreBatteryOptimization(activity: Activity, requestCode: Int): Boolean {
        if (!PermissionUtil.isIgnoringBatteryOptimizations(activity)) {
            IntentLauncher.goToBatteryOptimization(activity, requestCode)
            return true
        }
        return false
    }

    private fun canRequestBackgroundLocation(activity: Activity, permission: String): Boolean =
        SDKSupports.R && permission == Manifest.permission.ACCESS_BACKGROUND_LOCATION &&
            PermissionUtil.getPermissionState(activity, permission) != PermissionState.DENIED &&
            PermissionUtil.getPermissionState(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PermissionState.ALLOWED

    fun persistPermissionRequest(vararg permissions: String): List<String> {
        val pref = Preferences.getInstance()
        val requested: MutableList<String> = ArrayList(permissions.size)
        for (permission in permissions) {
            requested.add(permission)
            pref.putBoolean("$permission$PREF_REQUESTED_SUFFIX", true)
        }
        return requested
    }
}
