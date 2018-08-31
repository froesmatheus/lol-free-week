package com.matheusfroes.lolfreeweek.extra

import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.matheusfroes.lolfreeweek.CustomApplication
import com.matheusfroes.lolfreeweek.R
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


/**
 * For Fragments, allows declarations like
 * ```
 * val myViewModel = viewModelProvider(myViewModelFactory)
 * ```
 */
inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
        provider: ViewModelProvider.Factory
) =
        ViewModelProviders.of(this, provider).get(VM::class.java)

/**
 * Like [Fragment.viewModelProvider] for Fragments that want a [ViewModel] scoped to the Activity.
 */
inline fun <reified VM : ViewModel> Fragment.activityViewModelProvider(
        provider: ViewModelProvider.Factory
) =
        ViewModelProviders.of(requireActivity(), provider).get(VM::class.java)

/**
 * For Actvities, allows declarations like
 * ```
 * val myViewModel = viewModelProvider(myViewModelFactory)
 * ```
 */
inline fun <reified VM : ViewModel> FragmentActivity.viewModelProvider(
        provider: ViewModelProvider.Factory
) =
        ViewModelProviders.of(this, provider).get(VM::class.java)





fun Context.toast(message: CharSequence): Toast = Toast
        .makeText(this, message, Toast.LENGTH_SHORT)
        .apply {
            show()
        }