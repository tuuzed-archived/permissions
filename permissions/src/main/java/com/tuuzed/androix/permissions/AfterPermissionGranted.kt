package com.tuuzed.androix.permissions

/** 用于标注成功获取权限后执行的方法,该方法必须是无参的 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class AfterPermissionGranted(
    /** 请求码 */
    @androidx.annotation.IntRange(from = 0)
    val requestCode: Int
)