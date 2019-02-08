package eu.morningbird.czyhandlowa.view

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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.hotmail.or_dvir.easysettings.events.SwitchSettingsClickEvent
import com.hotmail.or_dvir.easysettings.pojos.EasySettings
import eu.morningbird.czyhandlowa.CzyHandlowaApplication
import eu.morningbird.czyhandlowa.R
import eu.morningbird.czyhandlowa.notification.NotificationEventReceiver
import kotlinx.android.synthetic.main.activity_settings.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class SettingsActivity : AppCompatActivity() {

    private var firebaseAnalytics: FirebaseAnalytics? = null

    private lateinit var czyHandlowaApplication: CzyHandlowaApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        czyHandlowaApplication = application as CzyHandlowaApplication
        EasySettings.inflateSettingsLayout(this, settingsContainer, czyHandlowaApplication.settings)


    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onSwitchSettingsClicked(event: SwitchSettingsClickEvent) {
        val bundle = Bundle()
        bundle.putString(
            FirebaseAnalytics.Param.ITEM_ID,
            event.clickedSettingsObj.key + "_" + event.clickedSettingsObj.value.toString()
        )
        bundle.putString(
            FirebaseAnalytics.Param.ITEM_NAME,
            event.clickedSettingsObj.title + " set to " + event.clickedSettingsObj.value.toString()
        )
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "settings changed")
        firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

        when (event.clickedSettingsObj.key) {
            CzyHandlowaApplication.SETTINGS_DISPLAY_NOTIFICATIONS -> {
                if (event.clickedSettingsObj.value) {
                    //Switched on
                    NotificationEventReceiver.setupAlarm(applicationContext)
                } else {
                    //Switched off
                    NotificationEventReceiver.cancelAlarm(applicationContext)
                }
            }
        }
    }
}
