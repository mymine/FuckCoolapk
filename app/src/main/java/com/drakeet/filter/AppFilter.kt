package com.drakeet.filter

import android.os.Build
import android.util.ArraySet
import androidx.annotation.RequiresApi
import com.fuckcoolapk.BuildConfig
import com.fuckcoolapk.utils.LogUtil
import com.fuckcoolapk.utils.ktx.hookAfterMethod

/**
 * @author Drakeet Xu
 */
class AppFilter {

    private val callerWhiteList = arrayOf(
            "com.android.systemui",
            "com.miui.home",
            "android.uid.system",
    )

    // Apps in this list will be filtered out
    private val filteredList = arrayOf(
            BuildConfig.APPLICATION_ID,
    )

    fun init(classLoader: ClassLoader) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            "com.android.server.pm.AppsFilter"
                    .hookAfterMethod("shouldFilterApplication", Int::class.javaPrimitiveType, "com.android.server.pm.SettingBase", "com.android.server.pm.PackageSetting", Int::class.javaPrimitiveType, classLoader = classLoader) {
                        if (it.result == true) return@hookAfterMethod
                        // toString will be faster
                        // val packageName = on(param.args[2]).field("name").get<String>()
                        val targetPkgSetting = it.args[2]
                        val callerSettingBase = it.args[1]

                        val targetPackageName = targetPkgSetting.packageName
                        val callerPackageName = callerSettingBase?.packageName ?: "N/A"

                        if (targetPackageName in filteredList && callerPackageName !in callerWhiteList) {
                            if (!notedSet.contains(callerPackageName)) {
                                notedSet.add(callerPackageName)
                                LogUtil.d("AppFilter Filtered: $callerPackageName → $targetPackageName")
                            }
                            it.result = true
                        }
                    }
        }
    }

    private val Any.packageName get() = toString().substringAfterLast(" ").substringBefore("/")

    companion object {
        @RequiresApi(Build.VERSION_CODES.M)
        private val notedSet = ArraySet<String>()
    }
}