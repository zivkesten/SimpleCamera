package com.lemonadeinc.lemonade.managers.permission

import android.Manifest
import com.lemonadeinc.common.utils.android.SDKSupports

object PermissionProvider {

    @JvmField
    val PERMISSIONS_PICTURES = cameraPermissions()

    @JvmField
    val PERMISSIONS_VIDEO = videoPermissions()

    val PERMISSIONS_LOCATION = locationPermissions()

    private fun cameraPermissions(): Array<String> {
        val permissions: MutableList<String> =
            arrayListOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
        if (!SDKSupports.Q) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (SDKSupports.S) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        return permissions.toTypedArray()
    }

    private fun videoPermissions(): Array<String> {
        val permissions: MutableList<String> = arrayListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (SDKSupports.S) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        return permissions.toTypedArray()
    }

    private fun locationPermissions(): Array<String> {
        val permissions: MutableList<String> = arrayListOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (SDKSupports.S) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        return permissions.toTypedArray()
    }
}
