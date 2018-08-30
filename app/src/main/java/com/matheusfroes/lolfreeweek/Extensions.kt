package com.matheusfroes.lolfreeweek

import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.matheusfroes.lolfreeweek.di.Injector
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by matheusfroes on 11/02/2017.
 */

val Activity.app: CustomApplication get() = application as CustomApplication
val Fragment.app: CustomApplication get() = requireActivity().app

val Fragment.appCompatActivity: AppCompatActivity get() = activity as AppCompatActivity
val Fragment.appInjector: Injector get() = app.injector
val Activity.appInjector: Injector get() = app.injector

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

fun sendNotification(notificationId: Int, context: Context, title: String, message: String, intent: PendingIntent) {
    val notificationManager: NotificationManager =
            context.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager

    val notification = Notification.Builder(context)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_small_icon)
            .setAutoCancel(true)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentIntent(intent)
            .setDefaults(Notification.DEFAULT_VIBRATE)

    notificationManager.notify(notificationId, notification.build())
}