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

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.LinearLayout
import com.fuckcoolapk.utils.*
import com.fuckcoolapk.utils.ktx.*
import com.fuckcoolapk.view.FuckCheckBox
import com.watermark.androidwm_light.Watermark
import com.watermark.androidwm_light.WatermarkBuilder
import com.watermark.androidwm_light.bean.WatermarkImage
import com.watermark.androidwm_light.bean.WatermarkText
import de.robv.android.xposed.XposedHelpers
import java.io.File
import java.io.FileOutputStream


class ModifyPictureWatermark {
    fun init() {
        val instance = getHookField(CoolContext.classLoader.loadClass("com.coolapk.market.view.settings.settingsynch.SettingSynchronized"), "INSTANCE")
        //动态临时关闭图片水印
        "com.coolapk.market.view.feedv8.SubmitExtraViewPart"
                .hookAfterMethod("initWith", "com.coolapk.market.view.feedv8.SubmitFeedV8Activity") {
                    (it.thisObject.callMethod("getView") as LinearLayout).addView(LinearLayout(CoolContext.activity).apply {
                        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(CoolContext.context, 48f)).apply {
                            gravity = Gravity.CENTER_HORIZONTAL
                        }
                        orientation = LinearLayout.VERTICAL
                        addView(FuckCheckBox(CoolContext.activity).apply {
                            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                                gravity = Gravity.END
                                rightMargin = dp2px(CoolContext.context, 10f)
                            }
                            text = "临时关闭水印"
                            setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                                val pictureWatermarkPosition = CoolapkSP.coolapkSP.getString("picture_watermark_position", "0")
                                if (b) {
                                    if (pictureWatermarkPosition != "0") {
                                        OwnSP.set("pictureWatermarkPosition", pictureWatermarkPosition!!)
                                        instance.callMethod("uploadSetting", "picture_watermark_position", "0", 1)
                                    }
                                } else {
                                    if (pictureWatermarkPosition != "0") {
                                        instance.callMethod("uploadSetting", "picture_watermark_position", OwnSP.ownSP.getString("pictureWatermarkPosition", "0"), 1)
                                        OwnSP.remove("pictureWatermarkPosition")
                                    }
                                }
                            }
                        })
                    })
                }
        "com.coolapk.market.view.feedv8.BaseFeedContentHolder\$startSubmitFeed$2"
                .hookBeforeMethod("onNext", "com.coolapk.market.network.Result") {
                    val pictureWatermarkPosition = OwnSP.ownSP.getString("pictureWatermarkPosition", "-1")
                    if (pictureWatermarkPosition != "-1") {
                        instance.callMethod("uploadSetting", "picture_watermark_position", pictureWatermarkPosition, 1)
                        OwnSP.remove("pictureWatermarkPosition")
                    }
                }
        //回复临时关闭图片水印
        "com.coolapk.market.view.feed.ReplyActivity"
                .hookAfterMethod("initView") {
                    val viewBuilding = it.thisObject.getObjectField("binding")!!
                    val contentView = (viewBuilding.getObjectField("contentView") as LinearLayout).getChildAt(4) as LinearLayout
                    contentView.addView(CheckBox(CoolContext.activity).apply {
                        text = "临时关闭水印"
                        setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                            val pictureWatermarkPosition = CoolapkSP.coolapkSP.getString("picture_watermark_position", "0")
                            if (b) {
                                if (pictureWatermarkPosition != "0") {
                                    OwnSP.set("pictureWatermarkPosition", pictureWatermarkPosition!!)
                                    instance.callMethod("uploadSetting", "picture_watermark_position", "0", 1)
                                }
                            } else {
                                if (pictureWatermarkPosition != "0") {
                                    instance.callMethod("uploadSetting", "picture_watermark_position", OwnSP.ownSP.getString("pictureWatermarkPosition", "0"), 1)
                                    OwnSP.remove("pictureWatermarkPosition")
                                }
                            }
                        }
                    })
                }
        "com.coolapk.market.view.feed.ReplyActivity\$doPost$1"
                .hookBeforeMethod("onNext", "com.coolapk.market.network.Result") {
                    val pictureWatermarkPosition = OwnSP.ownSP.getString("pictureWatermarkPosition", "-1")
                    if (pictureWatermarkPosition != "-1") {
                        instance.callMethod("uploadSetting", "picture_watermark_position", pictureWatermarkPosition, 1)
                        OwnSP.remove("pictureWatermarkPosition")
                    }
                }
        //自定义水印
        if (OwnSP.ownSP.getBoolean("enableCustomWatermark", false)) {
            "com.coolapk.market.model.ImageUploadOption"
                    .hookBeforeMethod("create", String::class.java, String::class.java, String::class.java, Bundle::class.java) {
                        if (OwnSP.ownSP.getString("pictureWatermarkPosition", "-1") == "-1") {
                            val url = it.args[0] as String //file:///storage/emulated/0/Android/data/com.coolapk.market/cache/image_cache/xxxxxxxxxxxxxxxxxxxxxx
                            LogUtil.d(url)
                            val file = File(url.substring(url.indexOf("file://") + 7))
                            LogUtil.d(file.absolutePath)
                            val coolFileUtilsClass = XposedHelpers.findClass("com.coolapk.market.util.CoolFileUtils", CoolContext.classLoader)
                            var compressFormat = Bitmap.CompressFormat.JPEG
                            when (XposedHelpers.callStaticMethod(coolFileUtilsClass, "getImageFileType", file.absolutePath) as String) {
                                "jpg" -> compressFormat = Bitmap.CompressFormat.JPEG
                                "jpeg" -> compressFormat = Bitmap.CompressFormat.JPEG
                                "png" -> compressFormat = Bitmap.CompressFormat.PNG
                            }
                            val bitmap = doWaterMark(file).outputImage
                            val fileOutputStream = FileOutputStream(file)
                            bitmap.compress(compressFormat, 100, fileOutputStream)
                            fileOutputStream.flush()
                            fileOutputStream.close()
                        }
                    }
        }
    }
}

fun <T> doWaterMark(objects: T): Watermark {
    val watermarkBuilder: WatermarkBuilder = if (objects is File) {
        WatermarkBuilder.create(CoolContext.context, BitmapFactory.decodeFile(objects.absolutePath) as Bitmap)
    } else {
        WatermarkBuilder.create(CoolContext.context, objects as ImageView)
    }
    return if (OwnSP.ownSP.getBoolean("enablePictureWatermark", false)) {
        if (OwnSP.ownSP.getString("waterMarkPicturePath", "") == "") {
            LogUtil.toast("水印图片不可为空")
            watermarkBuilder.watermark
        } else {
            watermarkBuilder.loadWatermarkImage(WatermarkImage(BitmapFactory.decodeFile(OwnSP.ownSP.getString("waterMarkPicturePath", ""))).apply {
                setPositionX(OwnSP.ownSP.getString("waterMarkPositionX", "0")!!.toDouble())
                setPositionY(OwnSP.ownSP.getString("waterMarkPositionY", "0")!!.toDouble())
                setRotation(OwnSP.ownSP.getString("waterMarkRotation", "-30")!!.toDouble())
                setImageAlpha(OwnSP.ownSP.getString("waterMarkAlpha", "80")!!.toInt())
                size = OwnSP.ownSP.getString("waterMarkPictureSize", "0.2")!!.toDouble()
            })
                    .setTileMode(OwnSP.ownSP.getBoolean("enableTileWatermark", true))
                    .watermark
        }
    } else {
        watermarkBuilder.loadWatermarkText(WatermarkText(OwnSP.ownSP.getString("waterMarkText", if (CoolContext.loginSession.callMethod("isLogin") as Boolean) (CoolContext.loginSession.callMethod("getUserName") as String) else "水印文字")).apply {
            setPositionX(OwnSP.ownSP.getString("waterMarkPositionX", "0")!!.toDouble())
            setPositionY(OwnSP.ownSP.getString("waterMarkPositionY", "0")!!.toDouble())
            setRotation(OwnSP.ownSP.getString("waterMarkRotation", "-30")!!.toDouble())
            textAlpha = OwnSP.ownSP.getString("waterMarkAlpha", "80")!!.toInt()
            textSize = OwnSP.ownSP.getString("waterMarkTextSize", "20")!!.toDouble()
            textColor = Color.parseColor(OwnSP.ownSP.getString("waterMarkTextColor", "#000000"))
        })
                .setTileMode(OwnSP.ownSP.getBoolean("enableTileWatermark", true))
                .watermark
    }
}