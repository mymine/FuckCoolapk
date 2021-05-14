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

package com.fuckcoolapk.utils

import android.app.Activity
import android.content.Context
import com.fuckcoolapk.utils.ktx.callMethod
import com.fuckcoolapk.utils.ktx.callStaticMethod

object CoolContext {
    var isXpatch = false
    lateinit var context: Context
    lateinit var classLoader: ClassLoader
    lateinit var activity: Activity
    val loginSession by lazy { "com.coolapk.market.manager.DataManager".callStaticMethod("getInstance")?.callMethod("getLoginSession")!! }
    val appTheme by lazy { "com.coolapk.market.AppHolder".callStaticMethod("getAppTheme")!! }
}