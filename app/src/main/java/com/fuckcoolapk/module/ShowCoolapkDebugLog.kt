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

import com.fuckcoolapk.utils.LogUtil
import com.fuckcoolapk.utils.OwnSP
import com.fuckcoolapk.utils.ktx.findClass
import com.fuckcoolapk.utils.ktx.hookBeforeMethod
import java.util.*

const val fromCoolapkTag = "FROMCOOLAPK"

class ShowCoolapkDebugLog {
    fun init() {
        if (OwnSP.ownSP.getBoolean("showCoolapkDebugLog", false)) {
            val logUtils = "com.coolapk.market.util.LogUtils".findClass()
            logUtils.hookBeforeMethod("v", String::class.java, Array<Object>::class.java) {
                LogUtil.v(tag = fromCoolapkTag, obj = String.format(it.args[0] as String, *(it.args[1] as Array<*>)))
            }
            logUtils.hookBeforeMethod("d", String::class.java, Array<Object>::class.java) {
                LogUtil.d(tag = fromCoolapkTag, obj = String.format(it.args[0] as String, *(it.args[1] as Array<*>)))
            }
            logUtils.hookBeforeMethod("i", String::class.java, Array<Object>::class.java) {
                LogUtil.i(tag = fromCoolapkTag, obj = String.format(it.args[0] as String, *(it.args[1] as Array<*>)))
            }
            logUtils.hookBeforeMethod("w", String::class.java, Array<Object>::class.java) {
                LogUtil.w(tag = fromCoolapkTag, obj = String.format(it.args[0] as String, *(it.args[1] as Array<*>)))
            }
            logUtils.hookBeforeMethod("e", String::class.java, Array<Object>::class.java) {
                LogUtil.e(tag = fromCoolapkTag, obj = String.format(it.args[0] as String, *(it.args[1] as Array<*>)))
            }
        }
    }
}