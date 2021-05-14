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

import android.view.View
import com.fuckcoolapk.utils.OwnSP
import com.fuckcoolapk.utils.ktx.getObjectField
import com.fuckcoolapk.utils.ktx.hookAfterMethod

class RemoveAuditWatermark {
    fun init() {
        if (OwnSP.ownSP.getBoolean("removeAuditWatermark", false)) {
            "com.coolapk.market.binding.ViewBindingAdapters"
                    .hookAfterMethod("updateFeed", "com.coolapk.market.widget.ForegroundTextView", "com.coolapk.market.model.Feed") {
                        if (it.args[0] != null) {
                            val foregroundTextView = (it.args[0] as View)
                            foregroundTextView.visibility = View.GONE
                        }
                    }
            "com.coolapk.market.view.feed.FeedDetailActivityV8"
                    .hookAfterMethod("installForegroundTextView", "com.coolapk.market.model.Feed") {
                        val foregroundTextView = it.thisObject.getObjectField("foregroundTextView") as? View
                                ?: return@hookAfterMethod
                        foregroundTextView.visibility = View.GONE
                    }
        }
    }
}