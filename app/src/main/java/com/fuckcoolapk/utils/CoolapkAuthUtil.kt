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

package com.fuckcoolapk.utils

import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

@JvmOverloads
fun getAS2(UUID: String = java.util.UUID.randomUUID().toString()): String {
    //String UUID = "140457a3-af3f-407c-9e70-18b6548757b7";
    val calendar = Calendar.getInstance()
    val time = dateToStamp(calendar[Calendar.YEAR].toString() + "-" + (calendar[Calendar.MONTH] + 1) + "-" + calendar[Calendar.DATE] + " " + calendar[Calendar.HOUR_OF_DAY] + ":" + calendar[Calendar.MINUTE] + ":" + calendar[Calendar.SECOND]).substring(0, 10).toInt()
    //int time = (int) Calendar.getInstance(TimeZone.getTimeZone("UTC+8")).getTimeInMillis();
    val hexTime = "0x" + Integer.toHexString(time)
    val MD5Time = DigestUtils.md5Hex(time.toString())
    val a = "token://com.coolapk.market/c67ef5943784d09750dcfbb31020f0ab?$MD5Time$$UUID&com.coolapk.market"
    val MD5a = DigestUtils.md5Hex(Base64.encodeBase64(a.toByteArray(StandardCharsets.UTF_8)))
    return MD5a + UUID + hexTime
}

fun dateToStamp(time: String): String {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = simpleDateFormat.parse(time)
    val ts = date?.time
    return ts.toString()
}