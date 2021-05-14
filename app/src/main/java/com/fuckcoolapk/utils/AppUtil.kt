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

import android.content.Context
import android.content.res.Configuration
import com.fuckcoolapk.utils.ktx.callMethod
import com.fuckcoolapk.utils.ktx.callStaticMethod
import java.util.*
import kotlin.math.absoluteValue


fun dp2px(context: Context, dpValue: Float): Int = (dpValue * context.resources.displayMetrics.density + 0.5f).toInt()

fun sp2px(context: Context, spValue: Float): Int = (spValue * context.resources.displayMetrics.scaledDensity + 0.5f).toInt()

fun getAS(uuid: String = UUID.randomUUID().toString()) = "com.coolapk.market.util.AuthUtils".callStaticMethod("getAS", CoolContext.context, UUID.randomUUID().toString()) as String

fun getAppMode() = "com.coolapk.market.AppHolder".callStaticMethod("getAppMetadata")?.callMethod("getAppMode")!! as String

fun isNightMode(context: Context): Boolean = (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
inline fun getColorFix(block: () -> String): String {
    var string = block()
    while (string.length < 6) {
        string = "0$string"
    }
    return string
}

inline fun getColorFixWithHashtag(block: () -> String): String = "#${getColorFix(block)}"

fun getColorPrimary(): String = ("com.coolapk.market.util.ColorUtils".callStaticMethod("adjustAlpha", CoolContext.appTheme.callMethod("getColorPrimary"), 0f) as Int).absoluteValue.toString(16)

fun getColorAccent(): String = ("com.coolapk.market.util.ColorUtils".callStaticMethod("adjustAlpha", CoolContext.appTheme.callMethod("getColorAccent"), 0f) as Int).absoluteValue.toString(16)

fun getTextColor(): String = if (CoolContext.appTheme.callMethod("isDayTheme") as Boolean) {
    if (CoolContext.appTheme.callMethod("isLightColorTheme") as Boolean) {
        "000000"
    } else {
        "ffffff"
    }
} else {
    "ffffff"
}

fun reverseColor(mString: String): String = "${(255 - mString.substring(0, 2).toInt(16)).toString(16)}${(255 - mString.substring(2, 4).toInt(16)).toString(16)}${(255 - mString.substring(4, 6).toInt(16)).toString(16)}"