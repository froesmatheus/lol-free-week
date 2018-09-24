package com.matheusfroes.lolfreeweek.extra

import android.app.*
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Toast
import androidx.work.*
import com.matheusfroes.lolfreeweek.CustomApplication
import com.matheusfroes.lolfreeweek.R
import com.matheusfroes.lolfreeweek.di.Injector
import com.matheusfroes.lolfreeweek.jobs.FetchFreeWeekChampionsWorker
import com.squareup.picasso.Picasso
import kotlinx.coroutines.experimental.async
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.CoroutineContext


/**
 * Created by matheusfroes on 11/02/2017.
 */

val Activity.app: CustomApplication get() = application as CustomApplication
val Fragment.app: CustomApplication get() = requireActivity().app

val Fragment.appCompatActivity: AppCompatActivity get() = activity as AppCompatActivity
val Fragment.appInjector: Injector get() = app.injector
val Activity.appInjector: Injector get() = app.injector


fun sendNotification(notificationId: Int, context: Context, title: String, message: String, intent: PendingIntent) {
    val notificationManager: NotificationManager =
            context.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager

    val channelName = "free_week_channel"
    val lightColor = Color.argb(255, 103, 58, 183)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelName, "Free Week alerts", NotificationManager.IMPORTANCE_HIGH)
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.lightColor = lightColor
        notificationManager.createNotificationChannel(channel)
    }
    val notification = NotificationCompat.Builder(context, channelName)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_small_icon)
            .setAutoCancel(true)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentIntent(intent)
            .setLights(lightColor, 500, 2000)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .build()

    notificationManager.notify(notificationId, notification)
}

fun createFreeWeekWorkRequest(): OneTimeWorkRequest {
    val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

    return OneTimeWorkRequestBuilder<FetchFreeWeekChampionsWorker>()
            .setInitialDelay(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.MINUTES)
            .build()
}

fun ImageView.loadImage(url: String) {
    Picasso
            .with(context)
            .load(url)
            .fit()
            .centerCrop()
            .into(this)
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

suspend fun <A, B> List<A>.parallelMap(
        context: CoroutineContext = networkContext,
        block: suspend (A) -> B
): List<B> {
    return map {
        // Use async to start a coroutine for each item
        async(context) {
            block(it)
        }
    }.map {
        // We now have a map of Deferred<T> so we await() each
        it.await()
    }
}