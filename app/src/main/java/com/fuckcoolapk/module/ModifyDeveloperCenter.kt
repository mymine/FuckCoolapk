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
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.fuckcoolapk.utils.*
import com.fuckcoolapk.utils.ktx.callMethod
import com.fuckcoolapk.utils.ktx.callStaticMethod
import com.fuckcoolapk.utils.ktx.hookAfterMethod
import com.fuckcoolapk.utils.ktx.replaceMethod
import de.robv.android.xposed.XposedHelpers


class ModifyDeveloperCenter {
    fun init() {
        "com.coolapk.market.manager.ActionManager"
                .replaceMethod("startDeveloperAppListActivity", Context::class.java, String::class.java, String::class.java) {
                    XposedHelpers.findClass("com.coolapk.market.manager.ActionManager", CoolContext.classLoader).callStaticMethod("startWebViewActivity", it.args[0] as Context, "https://developer.coolapk.com/do?c=apk&m=myList")
                }
        "com.coolapk.market.view.webview.WebViewActivity"
                .hookAfterMethod("onCreate", Bundle::class.java) {
                    LogUtil.d(getColorFix(::getColorPrimary))
                    LogUtil.d(getTextColor())
                    //隐藏toolbar
                    val url = (it.thisObject as Activity).intent.getBundleExtra("extra_bundle")?.getString("external_url")!!
                    if (url.contains("developer.coolapk.com")) (it.thisObject.callMethod("getToolbar") as ViewGroup).visibility = View.GONE
                }
        "com.coolapk.market.view.webview.WebViewFragment"
                .hookAfterMethod("onPageFinished", WebView::class.java, String::class.java) {
                    val url = it.args[1] as String
                    if (url.contains("developer.coolapk.com")) (it.args[0] as WebView).apply {
                        //去除提示、增加退出按钮
                        if (!url.contains("m=edit") and !url.contains("m=aptitudeList")) loadUrl("javascript:void(function(){document.getElementsByTagName(\"main\")[0].getElementsByTagName(\"div\")[0].style[\"display\"]=\"none\";let a=document.getElementsByTagName(\"dd\")[0];let b=a.getElementsByTagName(\"a\");b[b.length-1].setAttribute(\"class\",\"mdl-navigation__link mdl-navigation__link--full-bleed-divider\");let c=document.createElement(\"a\");c.setAttribute(\"class\",\"mdl-navigation__link\");c.setAttribute(\"href\",\"javascript:window.opener=null;window.open('','_self');window.close();\");c.innerHTML=\"关闭页面\";a.appendChild(c);})()")
                        //自适应主题色
                        loadUrl("javascript:void(function(){const a=\"${getColorFix(::getColorPrimary)}\";const b=\"${getTextColor()}\";document.getElementsByClassName(\"mdl-layout__tab-bar-button\")[0].style[\"backgroundColor\"]=\"#00000000\";document.getElementsByClassName(\"mdl-layout__tab-bar-button\")[1].style[\"backgroundColor\"]=\"#00000000\";document.getElementsByClassName(\"mdl-layout__header\")[0].style[\"backgroundColor\"]=\"#\"+a;document.getElementsByClassName(\"mdl-layout__tab-bar\")[0].style[\"backgroundColor\"]=\"#00000000\";document.getElementsByClassName(\"mdl-layout__header\")[0].style[\"color\"]=\"#\"+b;let c=document.getElementsByClassName(\"mdl-layout__tab-bar\")[0].getElementsByTagName(\"a\");document.getElementsByClassName(\"mdl-layout__drawer-button\")[0].style[\"color\"]=\"#\"+b;for(let i=0;i<c.length;i++){c[i].style[\"color\"]=\"#\"+b}})()")
                    }
                }
    }
}