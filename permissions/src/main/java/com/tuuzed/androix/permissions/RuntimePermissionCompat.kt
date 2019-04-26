package com.tuuzed.androix.permissions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.IntRange
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.lang.reflect.InvocationTargetException


/** 运行时权限工具 */
object RuntimePermissionCompat {

    private const val TAG = "RuntimePermissionCompat"

    @JvmStatic
    fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {  // Android版本小于Android6.0
            return true
        } else {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }
    }

    @JvmStatic
    fun requestPermissions(activity: Activity, permissions: Array<String>, @IntRange(from = 0) requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }

    @JvmStatic
    fun requestPermissions(fragment: Fragment, permissions: Array<String>, @IntRange(from = 0) requestCode: Int) {
        fragment.requestPermissions(permissions, requestCode)
    }

    @JvmStatic
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        handler: Any
    ) {
        var result = true
        for (i in grantResults.indices) {
            val grantResult = grantResults[i]
            result = grantResult == PackageManager.PERMISSION_GRANTED
            if (!result) {
                Log.w(TAG, "onRequestPermissionsResult: 获取不到" + permissions[i] + "权限")
                break
            }
        }
        if (result) {
            val methods = handler.javaClass.declaredMethods ?: return
            for (method in methods) {
                val afterPermissionGranted = method.getAnnotation(AfterPermissionGranted::class.java)
                afterPermissionGranted?.let {
                    if (it.requestCode == requestCode) {
                        if (!method.isAccessible) {
                            method.isAccessible = true
                        }
                        try {
                            method.invoke(handler)
                        } catch (e: IllegalAccessException) {
                            Log.w(TAG, "onRequestPermissionsResult: ", e)
                        } catch (e: InvocationTargetException) {
                            Log.w(TAG, "onRequestPermissionsResult: ", e)
                        }
                    }
                }
            }
        }
    }

}


