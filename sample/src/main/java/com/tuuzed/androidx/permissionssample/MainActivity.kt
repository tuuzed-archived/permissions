package com.tuuzed.androidx.permissionssample

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tuuzed.androidx.permissions.AfterPermissionGranted
import com.tuuzed.androidx.permissions.RuntimePermissionCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        requirePermission()
    }

    @AfterPermissionGranted(1)
    fun requirePermission() {
        val perms = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (RuntimePermissionCompat.hasPermissions(this, *perms)) {
            Log.d("MainActivity", "获取到权限")
        } else {
            RuntimePermissionCompat.requestPermissions(this, perms, 1)
        }
    }

}
