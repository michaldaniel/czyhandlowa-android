package eu.morningbird.czyhandlowa

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

import android.app.Application
import com.hotmail.or_dvir.easysettings.pojos.EasySettings
import com.hotmail.or_dvir.easysettings.pojos.SettingsObject
import com.hotmail.or_dvir.easysettings.pojos.SwitchSettingsObject
import com.kobakei.ratethisapp.RateThisApp
import eu.morningbird.czyhandlowa.notification.NotificationEventReceiver


class CzyHandlowaApplication : Application() {

    var settings: ArrayList<SettingsObject<Any>>? = ArrayList()
    override fun onCreate() {
        super.onCreate()

        settings = EasySettings.createSettingsArray(
            SwitchSettingsObject.Builder(
                SETTINGS_DISPLAY_NOTIFICATIONS,
                getString(R.string.settings_display_notifications),
                true
            ).setSummary(getString(R.string.settings_description_display_notifications)).build()
        )
        EasySettings.initializeSettings(this, settings)

        val config = RateThisApp.Config()
        config.setTitle(R.string.rate_dialog_title)
        config.setMessage(R.string.rate_dialog_message)
        config.setYesButtonText(R.string.rate_dialog_rate)
        config.setNoButtonText(R.string.rate_dialog_thanks)
        config.setCancelButtonText(R.string.rate_dialog_cancel)
        RateThisApp.init(config)

        if (EasySettings.retrieveSettingsSharedPrefs(this).getBoolean(SETTINGS_DISPLAY_NOTIFICATIONS, false)) {
            NotificationEventReceiver.setupAlarm(applicationContext)
        } else {
            NotificationEventReceiver.cancelAlarm(applicationContext)
        }

    }

    companion object Settings {
        const val SETTINGS_DISPLAY_NOTIFICATIONS = "settings_display_notifications"
        const val CHANNEL_ID = "notifications_previous_day"
    }

}