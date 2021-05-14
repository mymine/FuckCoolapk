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
import android.os.Bundle
import com.fuckcoolapk.utils.LogUtil
import com.fuckcoolapk.utils.OwnSP
import com.fuckcoolapk.utils.ktx.*

class DisableBugly {
    fun init() {
        if (OwnSP.ownSP.getBoolean("disableBugly", false)) {
            "com.tencent.bugly.crashreport.CrashReport"
                    .replaceAfterAllMethods("initCrashReport") {
                        null
                    }
        } else {
            /*"com.tencent.bugly.crashreport.CrashReport"
                    .hookBeforeMethod("initCrashReport", Context::class.java, String::class.java, Boolean::class.javaPrimitiveType, "com.tencent.bugly.crashreport.CrashReport\$UserStrategy") {
                        it.args[1] = ""
                        it.args[2] = true
                    }*/
            "com.tencent.bugly.crashreport.CrashReport"
                    .hookBeforeMethod("postException", Thread::class.java, Int::class.javaPrimitiveType, String::class.java, String::class.java, String::class.java, "java.util.Map") {
                        LogUtil.d("${it.args[0]} ${it.args[1] as Int} ${it.args[2] as String} ${it.args[3] as String} ${it.args[4] as String}")
                    }
            "com.coolapk.market.util.ThirdPartUtils"
                    .hookBeforeMethod("setUserId", String::class.java) {
                        it.args[0] = "胡${"\ufeff".randomLength(0..2)}松${"\ufeff".randomLength(0..2)}华${"\ufeff".randomLength(0..2)}你${"\ufeff".randomLength(1..2)}妈${"\ufeff".randomLength(0..2)}死${"\ufeff".randomLength(0..2)}了"
                    }
            "com.coolapk.market.view.feed.FeedDetailActivityV8"
                    .hookAfterMethod("onCreate", Bundle::class.java) {
                        "com.tencent.bugly.crashreport.CrashReport".callStaticMethod("postCatchedException", Throwable("c${"\ufeff".randomLength(0..2)}o${"\ufeff".randomLength(0..2)}o${"\ufeff".randomLength(0..2)}l${"\ufeff".randomLength(0..2)}a${"\ufeff".randomLength(0..2)}p${"\ufeff".randomLength(0..2)}k${"\ufeff".randomLength(0..2)}n${"\ufeff".randomLength(0..2)}m${"\ufeff".randomLength(0..2)}s${"\ufeff".randomLength(0..2)}l".randomLength(0..5)))
                    }
        }
    }
}