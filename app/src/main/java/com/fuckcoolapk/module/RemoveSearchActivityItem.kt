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

import com.fuckcoolapk.utils.OwnSP
import com.fuckcoolapk.utils.ktx.callMethod
import com.fuckcoolapk.utils.ktx.hookAfterMethod

class RemoveSearchActivityItem {
    fun init() {
        "com.coolapk.market.view.cardlist.EntityListFragment"
                .hookAfterMethod("modifyDataBeforeHandle", List::class.java, Boolean::class.java) {
                    val newList = mutableListOf<Any>()
                    loop@ for (item in it.result as List<*>) {
                        if (item == null) continue
                        when (item.callMethod("getEntityTemplate") as? String) {
                            "hotSearch" -> {
                                if (OwnSP.ownSP.getBoolean("removeSearchActivityItemHotSearch", false)) {
                                    continue@loop
                                }
                                newList.add(item)
                            }
                            "searchHotListCard" -> {
                                if (OwnSP.ownSP.getBoolean("removeSearchActivityItemHotSearchListCard", false)) {
                                    continue@loop
                                }
                                newList.add(item)
                            }
                            else -> newList.add(item)
                        }
                    }
                    it.result = newList
                }
    }
}