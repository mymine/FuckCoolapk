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

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.fuckcoolapk.PACKAGE_NAME
import com.fuckcoolapk.utils.CoolContext
import com.fuckcoolapk.utils.OwnSP
import com.fuckcoolapk.utils.ktx.*
import de.robv.android.xposed.XposedHelpers

class ModifyGoodsButton {

    private val titleMap = mapOf(
            "发现" to "发布",
            "發現" to "發佈",
            "発見" to "発表",
            "Discovery" to "New Post"
    )

    fun init() {
        if (OwnSP.ownSP.getBoolean("modifyGoodsButton", false)) {
            "com.coolapk.market.view.main.MainFragment".hookAfterMethod("setDiscoveryLongClick") {
                val mainFragment = it.thisObject
                val mainFragmentBinding = mainFragment.getObjectField("binding")
                val bottomNavigation = mainFragmentBinding?.getObjectField("bottomNavigation")
                val views = bottomNavigation?.getObjectFieldAs<ArrayList<View>>("views")
                views?.forEach { view ->
                    val textView = (view as ViewGroup).getChildAt(1) as TextView
                    if (textView.text in titleMap.keys) {
                        (view.getChildAt(0) as ImageView).apply {
                            setImageResource(CoolContext.context.resources.getIdentifier("ic_add_circle_outline_white_24dp", "drawable", PACKAGE_NAME))
                            setColorFilter(Color.parseColor("#FF747474"))
                        }
                        view.setOnClickListener {
                            CoolContext.context.startActivity(Intent(
                                    CoolContext.context,
                                    "com.coolapk.market.view.feedv8.FeedEntranceV8Activity".findClass()
                            ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
                        }
                        view.setOnLongClickListener {
                            CoolContext.context.startActivity(Intent(
                                    CoolContext.context,
                                    "com.coolapk.market.view.notification.NotificationActivity".findClass()
                            ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); putExtra("tab", 0) })
                            return@setOnLongClickListener false
                        }
                        textView.text = titleMap[textView.text]
                        return@forEach
                    }
                }
            }
            "com.coolapk.market.widget.SearchAppHeader\$initUI\$postButton\$1\$1".hookAfterConstructor(FrameLayout::class.java) {
                (it.args[0] as FrameLayout).visibility = View.GONE
            }
        }
    }
}
