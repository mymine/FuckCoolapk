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
import android.graphics.Color
import android.widget.CompoundButton
import android.widget.Switch
import com.fuckcoolapk.utils.*

class FuckSwitch(context: Context) : Switch(context) {
    var size: Float
        get() = textSize
        set(value) {
            textSize = value
        }
    var color = if (isNightMode(getContext())) "#ffffff" else "#000000"
        set(value) = setTextColor(Color.parseColor(value))
    var sharedPreferences = OwnSP.ownSP
    private val editor by lazy { sharedPreferences.edit() }
    var toastText = ""
    var defaultState = false
    var key = ""
        set(value) {
            isChecked = sharedPreferences.getBoolean(value, defaultState)
            setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                if (b and (toastText != "")) LogUtil.toast(toastText)
                editor.putBoolean(value, b)
                editor.apply()
            }
        }

    init {
        setPadding(dp2px(getContext(), 10f), dp2px(getContext(), 10f), dp2px(getContext(), 10f), dp2px(getContext(), 10f))
        setTextColor(Color.parseColor(color))
    }

    class Builder(private val mContext: Context = CoolContext.context, private val block: FuckSwitch.() -> Unit) {
        fun build() = FuckSwitch(mContext).apply(block)
    }

    class FastBuilder(private val mContext: Context = CoolContext.context, private val mText: String, private val mToastText: String? = null, private val mDefaultState: Boolean? = null, private val mKey: String) {
        fun build() = FuckSwitch(mContext).apply {
            text = mText
            mToastText?.let { toastText = it }
            mDefaultState?.let { defaultState = it }
            key = mKey
        }
    }
}