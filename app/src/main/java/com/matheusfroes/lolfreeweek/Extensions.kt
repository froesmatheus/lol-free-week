package com.matheusfroes.lolfreeweek

import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by matheusfroes on 11/02/2017.
 */
fun nextDayOfWeek(day: Int): Calendar {
    val date = Calendar.getInstance()
    var diff = day - date.get(Calendar.DAY_OF_WEEK)
    if (diff <= 0) {
        diff += 7
    }
    date.add(Calendar.DAY_OF_MONTH, diff)
    date.set(Calendar.HOUR_OF_DAY, 0)
    date.set(Calendar.MINUTE, 0)
    date.set(Calendar.SECOND, 0)
    date.set(Calendar.MILLISECOND, 0)
    return date
}

fun getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
    val diffInMillies = date2.time - date1.time
    return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
}