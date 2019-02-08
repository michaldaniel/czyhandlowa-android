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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hotmail.or_dvir.easysettings.pojos.EasySettings
import eu.morningbird.czyhandlowa.CzyHandlowaApplication


class NotificationServiceStarterReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_TIMEZONE_CHANGED, Intent.ACTION_TIME_CHANGED, Intent.ACTION_BOOT_COMPLETED -> {
                if (EasySettings.retrieveSettingsSharedPrefs(context).getBoolean(
                        CzyHandlowaApplication.SETTINGS_DISPLAY_NOTIFICATIONS,
                        false
                    )
                ) {
                    NotificationEventReceiver.setupAlarm(context)
                } else {
                    NotificationEventReceiver.cancelAlarm(context)
                }
            }
        }

    }
}