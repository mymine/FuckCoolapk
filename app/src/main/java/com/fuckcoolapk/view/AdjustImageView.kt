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

package com.fuckcoolapk.view

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.widget.ImageView
import com.fuckcoolapk.utils.CoolContext
import com.fuckcoolapk.utils.DownloadUtil
import com.fuckcoolapk.utils.LogUtil
import java.io.File
import kotlin.math.ceil

typealias callback = (AdjustImageView) -> Unit

open class AdjustImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ImageView(context, attrs, defStyleAttr) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        drawable?.let {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = ceil(width.toDouble() * it.intrinsicHeight / it.intrinsicWidth).toInt()
            setMeasuredDimension(width, height)
        } ?: super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun setUrl(url: String, callback: callback) {
        val imageDirectory = context.cacheDir.path + "/image_manager_disk_cache/"
        val fileName = url.substring(url.indexOf("//") + 2).replace("/", "-")
        if (File(imageDirectory + fileName).isFile) {
            setImageBitmap(BitmapFactory.decodeFile(imageDirectory + fileName))
            callback(this)
        } else {
            Thread {
                DownloadUtil().download(url, imageDirectory, fileName, object : DownloadUtil.OnDownloadListener {
                    override fun onDownloadSuccess(file: File?) {
                        post {
                            setImageBitmap(BitmapFactory.decodeFile(file?.path))
                            callback(this@AdjustImageView)
                        }
                    }

                    override fun onDownloading(progress: Int) {}
                    override fun onDownloadFailed(e: Exception?) {
                        LogUtil.e(e)
                    }
                })
            }.start()
        }
    }

    class Builder(private val mContext: Context = CoolContext.context, private val block: AdjustImageView.() -> Unit) {
        fun build() = AdjustImageView(mContext).apply(block)
    }

    init {
        adjustViewBounds = true
        scaleType = ScaleType.FIT_XY
    }
}