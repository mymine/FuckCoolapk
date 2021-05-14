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
import com.fuckcoolapk.utils.OwnSP
import com.fuckcoolapk.utils.getAppMode
import com.fuckcoolapk.utils.ktx.hookAfterMethod
import com.fuckcoolapk.utils.ktx.hookBeforeMethod
import java.util.*

//com.coolapk.market.view.main.NavigationHelper

class RemoveBottomNavigation {
    private val removeList by lazy {
        if (getAppMode() == "community") {
            listOf(OwnSP.ownSP.getBoolean("removeBottomNavigationHome", false),
                    OwnSP.ownSP.getBoolean("removeBottomNavigationMobileBar", false),
                    OwnSP.ownSP.getBoolean("removeBottomNavigationDiscovery", false),
                    OwnSP.ownSP.getBoolean("removeBottomNavigationCenter", false))
        } else {
            listOf(OwnSP.ownSP.getBoolean("removeBottomNavigationHome", false),
                    OwnSP.ownSP.getBoolean("removeBottomNavigationMobileBar", false),
                    OwnSP.ownSP.getBoolean("removeBottomNavigationDiscovery", false),
                    OwnSP.ownSP.getBoolean("removeBottomNavigationAppAndGame", false),
                    OwnSP.ownSP.getBoolean("removeBottomNavigationCenter", false))
        }
    }

    fun init() {
        "com.coolapk.market.view.main.NavigationHelper".hookAfterMethod("getAHBottomNavigationItems", Context::class.java) {
            val list = it.result as ArrayList<*>
            it.result = ArrayList<Any?>().apply {
                list.forEachIndexed { index, item ->
                    if (!removeList[index]) add(item)
                }
            }
        }
        "com.coolapk.market.view.main.MainFragment"
                .hookBeforeMethod("onTabSelected", Int::class.javaPrimitiveType, Boolean::class.javaPrimitiveType) {
                    val tabInt = (it.args[0] as Int)
                    var newTabInt = -1
                    for (i in -1 until tabInt) {
                        newTabInt++
                        while (removeList[newTabInt]) {
                            newTabInt++
                        }
                    }
                    it.args[0] = newTabInt
                }
    }
}
