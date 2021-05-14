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
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import com.fuckcoolapk.*
import com.fuckcoolapk.utils.*
import com.fuckcoolapk.utils.ktx.callMethod
import com.fuckcoolapk.utils.ktx.callStaticMethod
import com.fuckcoolapk.utils.ktx.hookBeforeMethod
import com.fuckcoolapk.utils.ktx.randomLength
import com.fuckcoolapk.view.AdjustImageView
import com.fuckcoolapk.view.FuckEditText
import com.fuckcoolapk.view.FuckSwitch
import com.fuckcoolapk.view.FuckTextView
import io.noties.markwon.Markwon
import java.io.File
import kotlin.system.exitProcess

class HookSettings {
    lateinit var settingActivity: Activity
    lateinit var dataList: java.util.List<Any>
    private var isOpen = false

    fun init() {
        "com.coolapk.market.view.settings.VXSettingsFragment"
                .hookBeforeMethod("initData") {
                    settingActivity = CoolContext.activity
                    val fuckCoolapkHolderItem = "com.coolapk.market.model.HolderItem"
                            .callStaticMethod("newBuilder")
                            ?.callMethod("entityType", "holder_item")
                            ?.callMethod("string", if (OwnSP.ownSP.getBoolean("agreeEULA", false)) "Fuck Coolapk" else "Fuck Coolapk（未激活）")
                            ?.callMethod("intValue", 233)
                            ?.callMethod("build")
                    val lineHolderItem = "com.coolapk.market.model.HolderItem"
                            .callStaticMethod("newBuilder")
                            ?.callMethod("entityType", "holder_item")
                            ?.callMethod("intValue", 14)
                            ?.callMethod("build")
                    dataList = (it.thisObject.callMethod("getDataList") as java.util.List<Any>).apply {
                        add(fuckCoolapkHolderItem)
                        add(lineHolderItem)
                    }
                }
        "com.coolapk.market.view.settings.VXSettingsFragment\$onCreateViewHolder$1"
                .hookBeforeMethod("onItemClick", "androidx.recyclerview.widget.RecyclerView\$ViewHolder", View::class.java) {
                    val intValue = dataList.get(it.args[0].callMethod("getAdapterPosition") as Int).callMethod("getIntValue")
                    if ((intValue == 233) and !isOpen) {
                        if (OwnSP.ownSP.getBoolean("agreeEULA", false)) {
                            showSettingsDialog()
                        } else {
                            GetUtil().sendGet("https://cdn.jsdelivr.net/gh/ejiaogl/FuckCoolapk@master/EULA.md") { result: String -> showEulaDialog(result) }
                        }
                    }
                }
    }

    private fun showSettingsDialog() {
        val dialogBuilder = AlertDialog.Builder(settingActivity)
        dialogBuilder.setView(ScrollView(settingActivity).apply {
            overScrollMode = 2
            addView(LinearLayout(settingActivity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp2px(CoolContext.context, 20f), dp2px(CoolContext.context, 10f), dp2px(CoolContext.context, 20f), dp2px(CoolContext.context, 5f))
                addView(FuckTextView.FastBuilder(mText = "F${"■".randomLength(2..5)} C${"■".randomLength(4..8)}", mSize = FuckTextView.titleSize).build())
                addView(FuckTextView.FastBuilder(mText = "${BuildConfig.VERSION_NAME} ${BuildConfig.VERSION_CODE} ${BuildConfig.BUILD_TYPE} ${if (CoolContext.isXpatch) " for xpatch" else ""}\n目标版本: $MODULE_TARGET_VERSION_NAME ($MODULE_TARGET_VERSION_CODE)").build())
                //addView(FuckTextView.FastBuilder(mText = "功能", mColor = getColorFixWithHashtag(::getColorAccent), mSize = FuckTextView.title2Size).build())
                addView(FuckTextView.FastBuilder(mText = "精简", mColor = getColorFixWithHashtag(::getColorAccent), mSize = FuckTextView.title2Size).build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "去除启动广告", mKey = "removeStartupAds").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "去除信息流广告（Alpha）", mKey = "removeFeedAds").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "去除帖子下方广告（Alpha）", mKey = "removeBannerAds").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "去除动态审核水印", mKey = "removeAuditWatermark").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "去除首页搜索栏热词", mKey = "removeSearchBoxHotWord").build())
                if (!CoolContext.isXpatch) addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "关闭更新提醒", mKey = "disableUpdateDialog").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "关闭 Umeng", mKey = "disableUmeng").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "关闭 Bugly", mKey = "disableBugly").build())
                addView(FuckTextView.FastBuilder(mText = "去除首页底部按钮") { showRemoveBottomNavigationDialog() }.build())
                addView(FuckTextView.FastBuilder(mText = "去除发布列表元素") { showRemoveEntranceItemDialog() }.build())
                addView(FuckTextView.FastBuilder(mText = "去除搜索界面元素") { showRemoveSearchActivityItemDialog() }.build())
                addView(FuckTextView.FastBuilder(mText = "加强", mColor = getColorFixWithHashtag(::getColorAccent), mSize = FuckTextView.title2Size).build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "允许在应用列表内卸载酷安", mKey = "allowUninstallCoolapk").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "在应用详情页显示更多信息", mKey = "showAppDetail").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "检测动态状态", mKey = "checkFeedStatus").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "开启频道自由编辑", mKey = "enableChannelEdit").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "对动态开启 Markdown（Alpha）", mKey = "enableMarkdown").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "对私信开启反和谐", mToastText = "通过自动替换相似字来达到反和谐的效果，不能保证一定有效。\n请勿滥用，请勿用于除私信外的其他地方，否则后果自负。", mKey = "antiMessageCensorship").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "加强私信反和谐效果", mToastText = "需同时开启「对私信开启反和谐」", mKey = "enhanceAntiMessageCensorship").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "切换酷安模式（正常版/社区版）", mKey = "modifyAppMode").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "更改「发现」按钮点击事件为打开发布列表", mKey = "modifyGoodsButton").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "管理员模式", mToastText = "仅供娱乐，不会有实际效果。\n慎重开启，开启后很有可能导致你号没了！！！", mKey = "adminMode").build())
                //addView(FuckSwitch.FastBuilder(mText = "关闭链接追踪",mKey = "disableURLTracking").build())
                addView(FuckTextView.FastBuilder(mText = "自定义水印") { showWaterMarkDialog() }.build())
                addView(FuckTextView.FastBuilder(mText = "其他", mColor = getColorFixWithHashtag(::getColorAccent), mSize = FuckTextView.title2Size).build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "检查模块更新", mDefaultState = true, mKey = "checkUpdate").build())
                addView(FuckTextView.FastBuilder(mText = "调试", mColor = getColorFixWithHashtag(::getColorAccent), mSize = FuckTextView.title2Size).build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "对 酷安 进行脱壳", mToastText = "不适用于较新的 Android 版本。\n重启应用后开始脱壳，文件存放在 /data/data/com.coolapk.market/fuck_coolapk_shell。", mKey = "shouldShelling").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "输出酷安 Debug Log", mKey = "showCoolapkDebugLog").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "输出调试 Toast", mKey = "showLogToast").build())
                addView(FuckTextView.Builder {
                    text = "生成随机 Token"
                    setOnClickListener {
                        (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText("token", getAS()))
                        LogUtil.toast("已复制到剪贴板（Call Native Method)")
                    }
                    setOnLongClickListener {
                        (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText("token", getAS2()))
                        LogUtil.toast("已复制到剪贴板")
                        true
                    }
                }.build())
                if (BuildConfig.DEBUG) {
                    addView(FuckTextView.FastBuilder(mText = "Do Test Hook") { TestHook().init() }.build())
                    addView(FuckTextView.FastBuilder(mText = "初始化状态") { File("/data/data/$PACKAGE_NAME/shared_prefs/${SP_NAME}.xml").delete();exitProcess(0) }.build())
                }
                addView(FuckTextView.FastBuilder(mText = "信息", mColor = getColorFixWithHashtag(::getColorAccent), mSize = FuckTextView.title2Size).build())
                addView(FuckTextView.FastBuilder(mText = "背景故事", mUrl = "https://github.com/ejiaogl/FuckCoolapk/wiki/Background-information").build())
                addView(FuckTextView.FastBuilder(mText = "Telegram", mUrl = "https://t.me/fuck_coolapk").build())
                addView(FuckTextView.FastBuilder(mText = "GitHub", mUrl = "https://github.com/ejiaogl/FuckCoolapk").build())
                addView(FuckTextView.FastBuilder(mText = "FAQ", mUrl = "https://github.com/ejiaogl/FuckCoolapk/wiki/FAQ").build())
            })
        })
        dialogBuilder.setPositiveButton("重启应用") { dialogInterface: DialogInterface, i: Int -> exitProcess(0) }
        dialogBuilder.show().apply {
            getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor(getColorFixWithHashtag(::getColorAccent)))
        }
    }

    private fun showRemoveBottomNavigationDialog() {
        val dialogBuilder = AlertDialog.Builder(settingActivity)
        dialogBuilder.setView(ScrollView(settingActivity).apply {
            overScrollMode = 2
            addView(LinearLayout(settingActivity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp2px(CoolContext.context, 20f), dp2px(CoolContext.context, 10f), dp2px(CoolContext.context, 20f), dp2px(CoolContext.context, 10f))
                addView(FuckTextView.FastBuilder(mText = "去除首页底部按钮", mSize = FuckTextView.titleSize).build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "首页", mToastText = "强烈建议去 设置->界面显示->首页选项 更改默认启动页为「应用游戏」", mKey = "removeBottomNavigationHome").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "数码", mKey = "removeBottomNavigationMobileBar").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "发现", mKey = "removeBottomNavigationDiscovery").build())
                if (getAppMode() != "community") addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "应用游戏", mKey = "removeBottomNavigationAppAndGame").build())
                //addView(FuckSwitch.FastBuilder(mText = "我的", mToastText = "这会导致你无法进入模块设置", mKey = "removeBottomNavigationCenter").build())
            })
        })
        dialogBuilder.show()
    }

    private fun showRemoveEntranceItemDialog() {
        val dialogBuilder = AlertDialog.Builder(settingActivity)
        dialogBuilder.setView(ScrollView(settingActivity).apply {
            overScrollMode = 2
            addView(LinearLayout(settingActivity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp2px(CoolContext.context, 20f), dp2px(CoolContext.context, 10f), dp2px(CoolContext.context, 20f), dp2px(CoolContext.context, 10f))
                addView(FuckTextView.FastBuilder(mText = "去除发布列表元素", mSize = FuckTextView.titleSize).build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "酷图", mKey = RemoveEntranceItem.itemMap["酷图"]!!).build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "提问", mKey = RemoveEntranceItem.itemMap["提问"]!!).build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "二手", mKey = RemoveEntranceItem.itemMap["二手"]!!).build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "扫一扫", mKey = RemoveEntranceItem.itemMap["扫一扫"]!!).build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "投票", mKey = RemoveEntranceItem.itemMap["投票"]!!, mToastText = "仅管理员模式有此元素").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "话题", mKey = RemoveEntranceItem.itemMap["话题"]!!).build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "好物", mKey = RemoveEntranceItem.itemMap["好物"]!!).build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "好物单", mKey = RemoveEntranceItem.itemMap["好物单"]!!).build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "应用集", mKey = RemoveEntranceItem.itemMap["应用集"]!!).build())

            })
        })
        dialogBuilder.show()
    }

    private fun showRemoveSearchActivityItemDialog() {
        val dialogBuilder = AlertDialog.Builder(settingActivity)
        dialogBuilder.setView(ScrollView(settingActivity).apply {
            overScrollMode = 2
            addView(LinearLayout(settingActivity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp2px(CoolContext.context, 20f), dp2px(CoolContext.context, 10f), dp2px(CoolContext.context, 20f), dp2px(CoolContext.context, 10f))
                addView(FuckTextView.FastBuilder(mText = "搜索界面精简", mSize = FuckTextView.titleSize).build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "热门搜索", mKey = "removeSearchActivityItemHotSearch").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "热榜", mKey = "removeSearchActivityItemHotSearchListCard").build())
            })
        })
        dialogBuilder.show()
    }

    private fun showWaterMarkDialog() {
        val waterMarkImageView = AdjustImageView.Builder {
            refreshWaterMarkImageView(this)
        }.build()
        val dialogBuilder = AlertDialog.Builder(settingActivity)
        dialogBuilder.setView(ScrollView(settingActivity).apply {
            overScrollMode = 2
            addView(LinearLayout(settingActivity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp2px(CoolContext.context, 20f), dp2px(CoolContext.context, 10f), dp2px(CoolContext.context, 20f), dp2px(CoolContext.context, 5f))
                addView(FuckTextView.FastBuilder(mText = "自定义水印", mSize = FuckTextView.titleSize).build())
                addView(FuckTextView.FastBuilder(mText = "切换后重启应用生效，请务必确保能生成预览再投入使用。").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "开启自定义水印", mKey = "enableCustomWatermark").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "全覆盖水印", mDefaultState = true, mKey = "enableTileWatermark").build())
                addView(FuckSwitch.FastBuilder(mContext = settingActivity, mText = "图片水印", mKey = "enablePictureWatermark").build())
                addView(FuckEditText.FastBuilder(mContext = settingActivity, mHint = "水印文字（留空即为用户名）", mKey = "waterMarkText").build())
                addView(FuckEditText.FastBuilder(mContext = settingActivity, mHint = "图片绝对位置", mKey = "waterMarkPicturePath").build())
                addView(FuckEditText.FastBuilder(mContext = settingActivity, mHint = "相对图片横坐标（0-1）", mKey = "waterMarkPositionX").build())
                addView(FuckEditText.FastBuilder(mContext = settingActivity, mHint = "相对图片纵坐标（0-1）", mKey = "waterMarkPositionY").build())
                addView(FuckEditText.FastBuilder(mContext = settingActivity, mHint = "选转角度（0-360）", mKey = "waterMarkRotation").build())
                addView(FuckEditText.FastBuilder(mContext = settingActivity, mHint = "透明度（0-255）", mKey = "waterMarkAlpha").build())
                addView(FuckEditText.FastBuilder(mContext = settingActivity, mHint = "文字颜色（#xxxxxx）", mKey = "waterMarkTextColor").build())
                addView(FuckEditText.FastBuilder(mContext = settingActivity, mHint = "文字大小（dp）", mKey = "waterMarkTextSize").build())
                addView(FuckEditText.FastBuilder(mContext = settingActivity, mHint = "图片相对大小（0-1）", mKey = "waterMarkPictureSize").build())
                addView(waterMarkImageView)
            })
        })
        dialogBuilder.setPositiveButton("刷新预览", null)
        dialogBuilder.show().apply {
            getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor(getColorFixWithHashtag(::getColorAccent)))
            getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener { refreshWaterMarkImageView(waterMarkImageView) }
        }
    }

    private fun showEulaDialog(eula: String) {
        val markwon = Markwon.builder(settingActivity).build()
        var time = if (BuildConfig.DEBUG) 0 else 30
        val dialogBuilder = AlertDialog.Builder(settingActivity)
        dialogBuilder.setView(ScrollView(settingActivity).apply {
            overScrollMode = 2
            addView(LinearLayout(settingActivity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp2px(CoolContext.context, 20f), dp2px(CoolContext.context, 10f), dp2px(CoolContext.context, 20f), dp2px(CoolContext.context, 5f))
                val message = FuckTextView.Builder {}.build()
                markwon.setMarkdown(message, eula)
                addView(message)
            })
        })
        dialogBuilder.setNegativeButton("不同意") { dialogInterface: DialogInterface, i: Int ->
            LogUtil.toast("请转到 Xposed 管理器关闭此模块")
            Thread {
                Thread.sleep(1500)
                exitProcess(0)
            }.start()
        }
        dialogBuilder.setPositiveButton("我已阅读并同意本协议") { dialogInterface: DialogInterface, i: Int ->
            OwnSP.set("agreeEULA", true)
            LogUtil.toast("重新启动 酷安 后生效")
            Thread {
                Thread.sleep(1500)
                exitProcess(0)
            }.start()
        }
        dialogBuilder.setCancelable(false)
        val dialog = dialogBuilder.show()
        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        negativeButton.setTextColor(Color.parseColor(getColorFixWithHashtag(::getColorAccent)))
        positiveButton.setTextColor(Color.parseColor(getColorFixWithHashtag(::getColorAccent)))
        positiveButton.isClickable = false
        Thread {
            do {
                positiveButton.post { positiveButton.text = "我已阅读并同意本协议 (${time}s)" }
                Thread.sleep(1000)
            } while (--time > 0)
            positiveButton.post {
                positiveButton.text = "我已阅读并同意本协议"
                positiveButton.isClickable = true
            }
        }.start()
    }

    private fun refreshWaterMarkImageView(waterMarkImageView: AdjustImageView) = waterMarkImageView
            .setUrl("https://cdn.jsdelivr.net/gh/t0HiiBwn/CoolapkPhoto@main/${(OwnSP.ownSP.getInt("configPhotoIndexStart", 0)..OwnSP.ownSP.getInt("configPhotoIndexEnd", 24)).random()}.jpg") {
                try {
                    doWaterMark(it).setToImageView(it)
                } catch (e: Throwable) {
                    LogUtil.toast("生成水印时出现错误")
                    LogUtil.e(e)
                }
            }
}
