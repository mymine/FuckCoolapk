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
import android.net.Uri
import com.fuckcoolapk.utils.ktx.callStaticMethod
import com.fuckcoolapk.utils.ktx.hookBeforeMethod
import com.fuckcoolapk.utils.ktx.replaceMethod
import java.net.URLDecoder

class DisableURLTracking {
    fun init() {
        //if (OwnSP.ownSP.getBoolean("disableURLTracking", false)) {
        "com.coolapk.market.manager.ActionManager"
                .hookBeforeMethod("startBrowserActivity", Context::class.java, String::class.java) {
                    val url = it.args[1] as String
                    it.args[1] = if ("https://www.coolapk.com/link" in url) URLDecoder.decode(url.substring(url.indexOf("https://www.coolapk.com/link") + 33), "utf-8") else url

                }
        "com.coolapk.market.util.UriActionUtils\$WebUriAction".replaceMethod("takeAction", Context::class.java, Uri::class.java, Array<Any>::class.java) {
            "com.coolapk.market.manager.ActionManager".callStaticMethod("startBrowserActivity", it.args[0], (it.args[1] as Uri).toString())
        }
        //}
    }
}
