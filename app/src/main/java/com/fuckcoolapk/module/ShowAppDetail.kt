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

import android.widget.LinearLayout
import android.widget.TextView
import com.fuckcoolapk.utils.OwnSP
import com.fuckcoolapk.utils.ktx.callMethod
import com.fuckcoolapk.utils.ktx.getObjectField
import com.fuckcoolapk.utils.ktx.hookAfterMethod
import com.fuckcoolapk.utils.ktx.hookBeforeMethod
import de.robv.android.xposed.XC_MethodHook
import org.json.JSONObject

class ShowAppDetail {
    fun init() {
        if (OwnSP.ownSP.getBoolean("showAppDetail", false)) {
            var responseBody = ""
            var okHttpHook: XC_MethodHook.Unhook? = null
            "com.coolapk.market.manager.DataManager$1"
                    .hookBeforeMethod("call", "okhttp3.ResponseBody") {
                        okHttpHook = "okhttp3.ResponseBody"
                                .hookAfterMethod("string") {
                                    responseBody = it.result as String
                                }
                    }
            "com.coolapk.market.manager.DataManager$1"
                    .hookAfterMethod("call", "okhttp3.ResponseBody") {
                        okHttpHook!!.unhook()
                    }

            "com.coolapk.market.view.node.app.AppNodeViewPart"
                    .hookAfterMethod("onBindToContent", "com.coolapk.market.model.ServiceApp") {
                        val data = JSONObject(responseBody).getJSONObject("data")
                        val appHeaderView = (it.thisObject.callMethod("getBinding")!!.getObjectField("headerContainer") as LinearLayout).getChildAt(1) as LinearLayout
                        (appHeaderView.getChildAt(1) as TextView).apply {
                            text = "$text\n${data.optString("pubStatusText")} ${if (data.optInt("isDownloadFromYybByPy") == 1) "从应用宝服务器下载" else ""}"
                        }
                    }
        }
    }
}