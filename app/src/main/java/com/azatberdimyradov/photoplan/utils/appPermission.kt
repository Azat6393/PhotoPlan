package com.azatberdimyradov.photoplan.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.azatberdimyradov.photoplan.ui.activities.MainActivity

lateinit var APP_ACTIVITY: MainActivity

const val PERMISSION_REQUEST = 200
const val READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

fun checkPermission(permission: String): Boolean {
    return if (Build.VERSION.SDK_INT >= 23
        && ContextCompat.checkSelfPermission(
            APP_ACTIVITY,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(APP_ACTIVITY, arrayOf(permission), PERMISSION_REQUEST)
        false
    } else true
}