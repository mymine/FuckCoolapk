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
import com.fuckcoolapk.utils.ktx.hookBeforeMethod
import de.robv.android.xposed.XposedHelpers

class RemoveFeedAds {
    private val onAdLoadListener = "com.coolapk.market.view.ad.OnAdLoadListener"

    private val titleRemoveList = listOf(
            "猜你喜欢", // Title过滤
            "酷友在搜的优惠券", // Title过滤
            "什么值得买", // Title过滤
            "优选配件", // Title过滤
            "好物试用", "酷友分享", "的好物"
    )
    private val extraRemoveList = listOf(
            "_AD",
            "_GOODS",
            "酷安小卖部",
            "欢迎分享使用体验"
    )

    fun init() {
        if (OwnSP.ownSP.getBoolean("removeFeedAds", false)) {
            "com.coolapk.market.view.ad.toutiao.TTFeedSelfDrawAd"
                    .hookBeforeMethod("load", onAdLoadListener) {
                        XposedHelpers.setIntField(it.thisObject, "state", 1)
                    }
            "com.coolapk.market.view.ad.tencent.GDTFeedSelfDrawAD"
                    .hookBeforeMethod("load", onAdLoadListener) {
                        XposedHelpers.setIntField(it.thisObject, "state", 1)
                    }
            "com.coolapk.market.view.ad.toutiao.TTFeedAd"
                    .hookBeforeMethod("load", onAdLoadListener) {
                        XposedHelpers.setIntField(it.thisObject, "state", 1)
                    }
            "com.coolapk.market.view.ad.tencent.GDTFeedAd"
                    .hookBeforeMethod("load", onAdLoadListener) {
                        XposedHelpers.setIntField(it.thisObject, "state", 1)
                    }
            "com.coolapk.market.view.ad.tencent.GDTFeedAd2"
                    .hookBeforeMethod("load", onAdLoadListener) {
                        XposedHelpers.setIntField(it.thisObject, "state", 1)
                    }
            "com.coolapk.market.view.cardlist.EntityRemoveHelper".hookAfterMethod("modifyData", List::class.java, Boolean::class.java) {
                val newList = mutableListOf<Any>()
                loop@ for (item in it.result as List<*>) {
                    if (item == null) continue
                    val entityType = item.callMethod("getEntityType") as? String
                    val title = item.callMethod("getTitle") as? String
                    val extraData = item.callMethod("getExtraData")?.callMethod("toString") as? String
                            ?: item.callMethod("getExtraData") as? String
                    val url = item.callMethod("getUrl") as? String
                    when {
                        entityType?.contains("_goods", ignoreCase = true) ?: false -> continue@loop
                        titleRemoveList.any { items ->
                            title?.contains(items) ?: false
                        } -> continue@loop
                        extraRemoveList.any { items ->
                            extraData?.contains(items, ignoreCase = true) ?: false
                        } -> continue@loop
                        url?.contains("pearGoods", ignoreCase = true) ?: false -> continue@loop
                        else -> newList.add(item)
                    }
                }
                it.result = newList
            }
        }
    }
}
