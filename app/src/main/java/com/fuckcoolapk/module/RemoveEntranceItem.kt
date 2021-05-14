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
import com.fuckcoolapk.utils.ktx.getObjectField
import com.fuckcoolapk.utils.ktx.hookAfterMethod
import java.util.*

class RemoveEntranceItem {

    companion object {
        val itemMap = mapOf(
                "酷图" to "removeEntranceItemCoolPic",
                "提问" to "removeEntranceItemQuestion",
                "二手" to "removeEntranceItemSecondHand",
                "扫一扫" to "removeEntranceItemScan",
                "投票" to "removeEntranceItemVote",
                "话题" to "removeEntranceItemTopic",
                "好物" to "removeEntranceItemGoods",
                "好物单" to "removeEntranceItemGoodsList",
                "应用集" to "removeEntranceItemAppSet"
        )
    }

    private fun shouldHide(name: String) = OwnSP.ownSP.getBoolean(itemMap[name], false)

    fun init() {
        "com.coolapk.market.view.feedv8.FeedEntranceV8Activity\$onCreate$3"
                .hookAfterMethod("invoke") {
                    val itemList = it.result as ArrayList<*>
                    val newList = mutableListOf<Any>()
                    for (item in itemList) {
                        if (shouldHide(item.getObjectField("title") as String)) {
                            continue
                        }
                        newList.add(item)
                    }
                    it.result = newList
                }
    }
}
