package eu.morningbird.czyhandlowa.notification

/*
 *  This file is part of "Czy handlowa?" android application.
 *
 *  "Czy handlowa?" is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  "Czy handlowa?" is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with "Czy handlowa?". If not, see <http://www.gnu.org/licenses/>.
*/

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.legacy.content.WakefulBroadcastReceiver
import eu.morningbird.czyhandlowa.CzyHandlowaApplication
import eu.morningbird.czyhandlowa.R
import eu.morningbird.czyhandlowa.util.ShoppingSunday
import eu.morningbird.czyhandlowa.view.MainActivity
import java.util.*


class NotificationIntentService : JobIntentService() {

    @RequiresApi(26)
    private fun createNotificationChannel(id: String) {

        // Get the notification manager
        val notificationManager = getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        var notificationChannel: NotificationChannel? =
            notificationManager.getNotificationChannel(CzyHandlowaApplication.CHANNEL_ID)
        if (notificationChannel != null) {
            return
        }

        when (id) {
            CzyHandlowaApplication.CHANNEL_ID -> {
                // Create the NotificationChannel
                val name = getString(eu.morningbird.czyhandlowa.R.string.channel_name)
                val descriptionText = getString(eu.morningbird.czyhandlowa.R.string.channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                notificationChannel = NotificationChannel(CzyHandlowaApplication.CHANNEL_ID, name, importance)
                notificationChannel.description = descriptionText
            }
        }
        //Create channel
        notificationChannel?.let {
            notificationManager.createNotificationChannel(it)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.d(javaClass.simpleName, "onHandleWork, started handling a notification event")
        try {
            val action = intent.action
            if (ACTION_START == action) {
                processStartNotification()
            }
            if (ACTION_DELETE == action) {
                processDeleteNotification()
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent)
        }
    }

    private fun processDeleteNotification() {
        val manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(NOTIFICATION_ID)
    }

    private fun processStartNotification() {
        val calendar = Calendar.getInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(CzyHandlowaApplication.CHANNEL_ID)
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            startNotification()
        }
    }

    private fun startNotification() {
        val builder: NotificationCompat.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationCompat.Builder(this, CzyHandlowaApplication.CHANNEL_ID)
            } else {
                @Suppress("DEPRECATION")
                NotificationCompat.Builder(this)
            }

        val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            GregorianCalendar(resources.configuration.locales.get(0))
        } else {
            @Suppress("DEPRECATION")
            (GregorianCalendar(resources.configuration.locale))
        }
        if (ShoppingSunday.areShopsOpen(current)) {
            builder.setContentTitle(resources.getString(R.string.shops_open))
                .setAutoCancel(true)
                .setColor(resources.getColor(R.color.colorAccent, applicationContext.theme))
                .setContentText(resources.getString(R.string.shops_open_long))
                .setSmallIcon(R.drawable.ic_emoji_crab)
        } else {
            builder.setContentTitle(resources.getString(R.string.shops_closed))
                .setAutoCancel(true)
                .setColor(resources.getColor(R.color.colorPrimary, applicationContext.theme))
                .setContentText(resources.getString(R.string.shops_closed_long))
                .setSmallIcon(R.drawable.ic_emoji_frog_face)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_ID,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(pendingIntent)
        builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this))

        val manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {

        private const val NOTIFICATION_ID = 1000
        private const val ACTION_START = "ACTION_START"
        private const val ACTION_DELETE = "ACTION_DELETE"

        fun createIntentStartNotificationService(context: Context): Intent {
            val intent = Intent(context, NotificationIntentService::class.java)
            intent.action = ACTION_START
            return intent
        }

        fun createIntentDeleteNotification(context: Context): Intent {
            val intent = Intent(context, NotificationIntentService::class.java)
            intent.action = ACTION_DELETE
            return intent
        }
    }
}