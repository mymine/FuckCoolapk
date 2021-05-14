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

import android.text.Html
import android.widget.TextView
import com.fuckcoolapk.utils.OwnSP
import com.fuckcoolapk.utils.ktx.hookAfterMethod
import com.fuckcoolapk.utils.ktx.hookBeforeMethod
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

class EnableMarkdown {
    fun init() {
        if (OwnSP.ownSP.getBoolean("enableMarkdown", false)) {
            val extensions = listOf(StrikethroughExtension.create())
            val parser = Parser.builder().extensions(extensions).build()
            val renderer = HtmlRenderer.builder().extensions(extensions).build()
            "com.coolapk.market.binding.TextViewBindingAdapters"
                    .hookAfterMethod("setTextViewLinkable", TextView::class.java, String::class.java, "java.lang.Integer", String::class.java, Boolean::class.javaObjectType, Html.ImageGetter::class.java, String::class.java) {}
            "com.coolapk.market.util.LinkTextUtils"
                    .hookBeforeMethod("convert", String::class.java, Int::class.javaPrimitiveType, Html.ImageGetter::class.java) {
                        val string = it.args[0] as String
                        it.args[0] = renderer.render(parser.parse(string))
                        //LogUtil.d(it.args[0] as String)
                    }
            "android.text.Html"
                    .hookBeforeMethod("fromHtml", String::class.java, Html.ImageGetter::class.java, Html.TagHandler::class.java) {}
        }
    }
}