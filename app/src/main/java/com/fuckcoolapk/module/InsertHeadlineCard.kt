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

import com.fuckcoolapk.utils.CoolContext
import com.fuckcoolapk.utils.OwnSP
import com.fuckcoolapk.utils.ktx.callMethod
import com.fuckcoolapk.utils.ktx.callStaticMethod
import com.fuckcoolapk.utils.ktx.hookBeforeMethod
import org.json.JSONArray
import java.util.*

class InsertHeadlineCard {
    fun init() {
        "com.coolapk.market.viewholder.ImageListCardViewHolder"
                .hookBeforeMethod("bindTo", Object::class.java) { param ->
                    if ((CoolContext.activity.javaClass.name == "com.coolapk.market.view.splash.SplashActivity") or (CoolContext.activity.javaClass.name == "com.coolapk.market.view.main.MainActivity")) {
                        val entityCard = param.args[0]
                        param.args[0] = "com.coolapk.market.model.EntityCard"
                                .callStaticMethod("builder")
                                ?.callMethod("setEntityId", entityCard.callMethod("getEntityId"))
                                ?.callMethod("setEntityFixed", entityCard.callMethod("getEntityFixed"))
                                ?.callMethod("setEntityTemplate", entityCard.callMethod("getEntityTemplate"))
                                ?.callMethod("setEntityType", entityCard.callMethod("getEntityType"))
                                ?.callMethod("setExtraData", entityCard.callMethod("getExtraData"))
                                ?.callMethod("setId", entityCard.callMethod("getId"))
                                ?.callMethod("setLastUpdate", entityCard.callMethod("getLastUpdate"))
                                ?.callMethod("setEntities", (entityCard.callMethod("getEntities") as ArrayList<Any?>)
                                        .apply {
                                            if (OwnSP.ownSP.getBoolean("removeFeedAds", false))
                                                listIterator().run {
                                                    while (hasNext()) {
                                                        next()?.let {
                                                            if ((it.callMethod("getUrl") as String).contains("/apk/")) remove()
                                                            if ((it.callMethod("getTitle") as String).contains("好物")) remove()
                                                        }
                                                    }
                                                }
                                            val jsonArray = JSONArray(OwnSP.ownSP.getString("configBannerCard", "[]"))
                                            for (i in 0 until jsonArray.length()) {
                                                val jsonObject = jsonArray.getJSONObject(i)
                                                add(if (jsonObject.getBoolean("countDown")) size - jsonObject.getInt("position") else jsonObject.getInt("position"), "com.coolapk.market.model.EntityCard"
                                                        .callStaticMethod("builder")
                                                        ?.callMethod("setEntityType", jsonObject.getString("entityType"))
                                                        ?.callMethod("setTitle", jsonObject.getString("title"))
                                                        ?.callMethod("setUrl", jsonObject.getString("url"))
                                                        ?.callMethod("setPic", jsonObject.getString("pic"))
                                                        ?.callMethod("build"))
                                            }
                                        }
                                )
                                ?.callMethod("build")
                    }
                }
    }
}
