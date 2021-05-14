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
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.TextView
import com.fuckcoolapk.utils.CoolContext
import com.fuckcoolapk.utils.dp2px
import com.fuckcoolapk.utils.isNightMode
import com.fuckcoolapk.utils.sp2px

class FuckTextView(context: Context) : TextView(context) {

    var size: Float
        get() = textSize
        set(value) {
            textSize = value
        }
    var color = if (isNightMode(getContext())) "#ffffff" else "#000000"
        set(value) = setTextColor(Color.parseColor(value))
    var url = ""
        set(value) = setOnClickListener { CoolContext.activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(value))) }

    init {
        setPadding(dp2px(getContext(), 10f), dp2px(getContext(), 10f), dp2px(getContext(), 10f), dp2px(getContext(), 10f))
        setTextColor(Color.parseColor(color))
    }

    class Builder(private val mContext: Context = CoolContext.context, private val block: FuckTextView.() -> Unit) {
        fun build() = FuckTextView(mContext).apply(block)
    }

    class FastBuilder(private val mContext: Context = CoolContext.context, private val mText: String, private val mSize: Float? = null, private val mColor: String? = null, private val mUrl: String? = null, private val mOnClickListener: ((View) -> Unit)? = null) {
        fun build() = FuckTextView(mContext).apply {
            text = mText
            mSize?.let { size = it }
            mColor?.let { color = it }
            mUrl?.let { url = it }
            mOnClickListener?.let { setOnClickListener(it) }
        }
    }

    companion object {
        val titleSize = sp2px(CoolContext.context, 10f).toFloat()
        val title2Size = sp2px(CoolContext.context, 8f).toFloat()
        val textSize = sp2px(CoolContext.context, 6f).toFloat()
    }
}