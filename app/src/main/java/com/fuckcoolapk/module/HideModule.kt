/*
 * Fuck Coolapk - Best present for 316 and 423
 * Copyright (C) 2020-2021
 * https://github.com/ejiaogl/FuckCoolapk
 *
 * This software is non-free but opensource software: you can redistribute it
 * and/or modify it under the terms of the GNUGeneral Public License as
 * published by the Free Software Foundation; either version 3 of the License,
 * or any later version and our eula as published by ejiaogl.
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License and
 * eula along with this software.  If not, see
 * <https://www.gnu.org/licenses/>
 * <https://github.com/ejiaogl/FuckCoolapk/blob/master/LICENSE>.
 */

package com.fuckcoolapk.module

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.fuckcoolapk.BuildConfig
import com.fuckcoolapk.utils.LogUtil
import com.fuckcoolapk.utils.ktx.*
import java.util.*

class HideModule {
    fun init() {
        "com.coolapk.market.util.LocalApkUtils"
                .hookAfterMethod("getAppList", PackageManager::class.java, Boolean::class.javaPrimitiveType) {
                    val appList = it.result as ArrayList<Any>
                    val newAppList = ArrayList<Any>()
                    for (i in appList.indices) {
                        val app = appList[i]
                        if ((app.callMethod("getPackageName") as String) != BuildConfig.APPLICATION_ID) newAppList.add(app)
                    }
                    it.result = newAppList
                }
        "com.coolapk.market.receiver.PackageReceiverImpl"
                .replaceMethod("onPackageAdded", Context::class.java, Intent::class.java, String::class.java) {
                    if ((it.args[2] as String) == BuildConfig.APPLICATION_ID) {
                        LogUtil.d("onPackageAdded Filter ${it.args[2] as String}")
                    } else {
                        LogUtil.d("onPackageAdded Add ${it.args[2] as String}")
                        "com.coolapk.market.util.PendingAppsUtils".callStaticMethod("doAddAction", it.args[0], it.args[2])
                    }
                }
        "com.coolapk.market.manager.ActionManager"
                .hookBeforeMethod("packageAdded", Context::class.java, String::class.java) {
                    if ((it.args[1] as String) == BuildConfig.APPLICATION_ID) {
                        LogUtil.d("packageAdded Filter ${it.args[2] as String}")
                        it.result = null
                        return@hookBeforeMethod
                    } else {
                        LogUtil.d("packageAdded Add ${it.args[2] as String}")
                    }
                }
    }
}