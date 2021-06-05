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

package com.bytedance.sdk.openadsdk.a

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.widget.LinearLayout
import android.widget.ScrollView
import com.fuckcoolapk.BuildConfig
import com.fuckcoolapk.PACKAGE_NAME
import com.fuckcoolapk.module.*
import com.fuckcoolapk.utils.*
import com.fuckcoolapk.utils.ktx.MethodHookParam
import com.fuckcoolapk.utils.ktx.callMethod
import com.fuckcoolapk.utils.ktx.hookAfterAllMethods
import com.fuckcoolapk.utils.ktx.hookAfterMethod
import com.fuckcoolapk.view.FuckTextView
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.sfysoft.android.xposed.shelling.XposedShelling
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import io.noties.markwon.Markwon
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class d : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == PACKAGE_NAME) {
            try {
                System.loadLibrary("fc")
            } catch (e: Throwable) {
                LogUtil.e(e)
            }
            try {
                if (XposedHelpers.findClassIfExists("com.wrapper.proxyapplication.WrapperProxyApplication", lpparam.classLoader) != null) {
                    "com.wrapper.proxyapplication.WrapperProxyApplication"
                            .hookAfterMethod("attachBaseContext", Context::class.java, classLoader = lpparam.classLoader) {
                                //获取 context
                                CoolContext.context = it.args[0] as Context
                                //获取 classloader
                                CoolContext.classLoader = CoolContext.context.classLoader
                                init(lpparam, it)
                            }
                } else if (XposedHelpers.findClassIfExists("com.wind.xpatch.proxy.XpatchProxyApplication", lpparam.classLoader) != null) {
                    "com.wind.xpatch.proxy.XpatchProxyApplication"
                            .hookAfterMethod("attachBaseContext", Context::class.java, classLoader = lpparam.classLoader) {
                                //获取 context
                                CoolContext.context = it.args[0] as Context
                                //获取 classloader
                                CoolContext.classLoader = CoolContext.context.classLoader
                                CoolContext.isXpatch = true
                                init(lpparam, it)
                            }
                } else {
                    XposedHelpers.findAndHookMethod(Application::class.java, "attach", Context::class.java, object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam) {
                            super.afterHookedMethod(param)
                            //获取 context
                            CoolContext.context = param.args[0] as Context
                            //获取 classloader
                            CoolContext.classLoader = CoolContext.context.classLoader
                            init(lpparam, param)
                        }
                    })
                }
            } catch (e: Throwable) {
                LogUtil.e(e)
            }
        }
    }

    private fun init(lpparam: XC_LoadPackage.LoadPackageParam, param: MethodHookParam) {
        //检查太极
        //FileUtil.getParamAvailability(param, Binder.getCallingPid())
        LogUtil.d(CoolContext.context.packageName)
        //获取 activity
        XposedHelpers.findClass("android.app.Instrumentation", CoolContext.classLoader)
                .hookAfterAllMethods("newActivity") { activityParam ->
                    CoolContext.activity = activityParam.result as Activity
                    LogUtil.d("Current activity: ${CoolContext.activity.javaClass}")
                }
        try {
            XposedHelpers.findClass("com.coolapk.market.view.main.MainActivity", CoolContext.classLoader)
                    .hookAfterMethod("onCreate", Bundle::class.java) {
                        val okHttpClient = OkHttpClient.Builder().build()
                        //appcenter
                        AppCenter.start(CoolContext.activity.application, "44ab5622-fbcb-4fcd-9eff-04dab0061d30", Analytics::class.java, Crashes::class.java)
                        if (CoolContext.loginSession.callMethod("isLogin") as Boolean) {
                            Analytics.trackEvent("user ${CoolContext.loginSession.callMethod("getUserName") as String}", HashMap<String, String>().apply {
                                put("userName", CoolContext.loginSession.callMethod("getUserName") as String)
                                put("UID", CoolContext.loginSession.callMethod("getUid") as String)
                                put("isAdmin", (CoolContext.loginSession.callMethod("isAdmin") as Boolean).toString())
                            })
                        }
                        //首次使用
                        if (OwnSP.ownSP.getBoolean("isFirstUse", true)) {
                            val normalDialog = AlertDialog.Builder(CoolContext.activity)
                            normalDialog.setTitle("欢迎")
                            normalDialog.setMessage("你来了？\n这是一份送给316和423的礼物。其功能是默认关闭的，如需使用，请转到设置页打开。")
                            normalDialog.setOnDismissListener { OwnSP.set("isFirstUse", false) }
                            normalDialog.show()
                        }
                        //获取配置
                        //if (true) {//debug
                        if ((System.currentTimeMillis() - OwnSP.ownSP.getLong("lastGetConfigTime", 0)) >= 86400000) {
                            //检查更新
                            if (OwnSP.ownSP.getBoolean("checkUpdate", true)) {
                                okHttpClient.newCall(Request.Builder()
                                        .url("https://api.github.com/repos/ejiaogl/FuckCoolapk/releases/latest")
                                        .get()
                                        .build())
                                        .enqueue(object : Callback {
                                            override fun onFailure(call: Call, e: IOException) {
                                                LogUtil.e(e)
                                            }

                                            override fun onResponse(call: Call, response: Response) {
                                                try {
                                                    val jsonObject = JSONObject(response.body!!.string())
                                                    //if (true) {//debug
                                                    if ((jsonObject.getString("tag_name").toInt() > BuildConfig.VERSION_CODE) and (!jsonObject.getBoolean("prerelease"))) {
                                                        Looper.prepare()
                                                        val dialogBuilder = AlertDialog.Builder(CoolContext.activity)
                                                        val markwon = Markwon.builder(CoolContext.context).build()
                                                        dialogBuilder.setTitle("FC 有新版本可用")
                                                        dialogBuilder.setView(ScrollView(CoolContext.context).apply {
                                                            overScrollMode = 2
                                                            addView(LinearLayout(CoolContext.context).apply {
                                                                orientation = LinearLayout.VERTICAL
                                                                setPadding(dp2px(CoolContext.context, 20f), dp2px(CoolContext.context, 10f), dp2px(CoolContext.context, 20f), dp2px(CoolContext.context, 5f))
                                                                val message = FuckTextView.Builder {}.build()
                                                                markwon.setMarkdown(message, "${BuildConfig.VERSION_NAME} -> ${jsonObject.getString("name")}\n\n${jsonObject.getString("body")}")
                                                                addView(message)
                                                            })
                                                        })
                                                        dialogBuilder.setPositiveButton("查看") { dialogInterface: DialogInterface, i: Int -> CoolContext.activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ejiaogl/FuckCoolapk/releases"))) }
                                                        dialogBuilder.show().getButton(Dialog.BUTTON_POSITIVE).setTextColor(Color.parseColor(getColorFixWithHashtag(::getColorAccent)))
                                                        Looper.loop()
                                                    }
                                                } catch (e: Throwable) {
                                                    LogUtil.e(e)
                                                }
                                            }
                                        })
                            }
                            okHttpClient.newCall(Request.Builder()
                                    .url("https://cdn.jsdelivr.net/gh/ejiaogl/FuckCoolapk@master/config.json")
                                    .get()
                                    .build())
                                    .enqueue(object : Callback {
                                        override fun onFailure(call: Call, e: IOException) {
                                            LogUtil.e(e)
                                        }

                                        override fun onResponse(call: Call, response: Response) {
                                            try {
                                                val jsonObject = JSONObject(response.body!!.string())
                                                OwnSP.set("lastGetConfigTime", System.currentTimeMillis())
                                                OwnSP.set("configPhotoIndexStart", jsonObject.getJSONArray("photoIndex")[0])
                                                OwnSP.set("configPhotoIndexEnd", jsonObject.getJSONArray("photoIndex")[1])
                                                OwnSP.set("configBannerCard", jsonObject.getJSONArray("bannerCard").toString())
                                            } catch (e: Throwable) {
                                                LogUtil.e(e)
                                            }
                                        }
                                    })
                        }
                    }
        } catch (e: Throwable) {
            LogUtil.e(e)
        }
        //关闭反 xposed
        DisableAntiXposed().init()
        //隐藏模块
        HideModule().init()
        //hook 设置
        HookSettings().init()
        if (OwnSP.ownSP.getBoolean("agreeEULA", false)) {
            //脱壳
            XposedShelling.runShelling(lpparam)
            //去除启动广告
            RemoveStartupAds().init()
            //去除信息流广告
            RemoveFeedAds().init()
            //去除帖子下方广告
            RemoveBannerAds().init()
            //去除底部多余按钮
            RemoveBottomNavigation().init()
            //去除搜索栏热词
            RemoveSearchBoxHotWord().init()
            //搜索界面精简
            RemoveSearchActivityItem().init()
            //允许在应用列表内卸载酷安
            AllowUninstallCoolapk().init()
            //插入头条 banner
            InsertHeadlineCard().init()
            //显示更详细的 App 信息
            ShowAppDetail().init()
            //关闭友盟
            DisableUmeng().init()
            //关闭 Bugly
            DisableBugly().init()
            //关闭更新提醒
            DisableUpdateDialog().init()
            //开启管理员模式
            EnableAdminMode().init()
            //检测动态折叠
            CheckFeedStatus().init()
            //更改酷安模式
            ModifyAppMode().init()
            //更改「发现」按钮点击事件为打开发布列表
            ModifyGoodsButton().init()
            //添加「复制图片链接」
            //AddCopyImageURLItem().init()
            //去除动态审核的水印
            RemoveAuditWatermark().init()
            //发布列表自定义移除
            RemoveEntranceItem().init()
            //临时去除图片水印
            ModifyPictureWatermark().init()
            //开启频道自由编辑
            EnableChannelEdit().init()
            //对私信开启反和谐
            AntiMessageCensorship().init()
            //关闭链接追踪
            DisableURLTracking().init()
            //开启动态 Markdown
            EnableMarkdown().init()
            //点击开发者平台直接跳转网页
            ModifyDeveloperCenter().init()
            //显示酷安 Debug Log
            ShowCoolapkDebugLog().init()
        }
    }
}
