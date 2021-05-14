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

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.fuckcoolapk.utils.OwnSP
import com.fuckcoolapk.utils.ktx.callMethod
import com.fuckcoolapk.utils.ktx.hookAfterMethod
import com.fuckcoolapk.utils.ktx.replaceMethod
import com.fuckcoolapk.utils.ktx.setReturnConstant

class RemoveStartupAds {
    fun init() {
        if (OwnSP.ownSP.getBoolean("removeStartupAds", false)) {
            /*"com.coolapk.market.manager.MainInitHelper".setReturnConstant("isDataLoaded",result = true)
            "com.coolapk.market.view.splash.SplashActivity"
                    .hookBeforeMethod("getLaunchMode"){
                        //(it.thisObject as Activity).callMethod("finishSplashInternal",false)
                        (it.thisObject as Activity).setResult(-1)
                        (it.thisObject as Activity).finish()
                        (it.thisObject as Activity).overridePendingTransition(0,0)
                        it.result = "HOT_BOOT"
                    }
            "com.coolapk.market.view.splash.BaseFullScreenAdActivity"
                    .replaceMethod("doStart"){}*/
            "com.coolapk.market.view.splash.FullScreenAdUtils".setReturnConstant("shouldShowAd", Context::class.java, result = false)
            "com.coolapk.market.manager.ActionManager"
                    .replaceMethod("startSplashActivity", Activity::class.java, String::class.java, Int::class.javaPrimitiveType) {}
            "com.coolapk.market.view.main.MainActivity"
                    .hookAfterMethod("onCreate", Bundle::class.java) {
                        (it.thisObject as Activity).callMethod("onActivityResult", 1, Activity.RESULT_OK, null)
                    }
            "com.coolapk.market.manager.MainInitHelper".setReturnConstant("isDataLoaded", result = true)
        }
    }
}