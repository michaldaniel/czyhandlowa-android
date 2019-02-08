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

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Observable
import com.google.firebase.analytics.FirebaseAnalytics
import com.kobakei.ratethisapp.RateThisApp
import eu.morningbird.czyhandlowa.R
import eu.morningbird.czyhandlowa.databinding.ActivityMainBinding
import eu.morningbird.czyhandlowa.model.MainActivityModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var currentDate: GregorianCalendar

    private lateinit var binding: ActivityMainBinding

    lateinit var view: View

    private lateinit var viewModel: MainActivityModel

    private var firebaseAnalytics: FirebaseAnalytics? = null

    private val onPropertyChanged = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(p0: Observable?, p1: Int) {
            runOnUiThread {
                updateView()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        view = binding.root
        setContentView(binding.root)

        RateThisApp.onCreate(this)
        RateThisApp.showRateDialogIfNeeded(this)

        initView()

    }

    override fun onResume() {
        viewModel.addOnPropertyChangedCallback(onPropertyChanged)
        val resumeDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            GregorianCalendar(resources.configuration.locales.get(0))
        } else {
            @Suppress("DEPRECATION")
            (GregorianCalendar(resources.configuration.locale))
        }
        if (currentDate.compareTo(resumeDate) != 0) {
            eventsCalendar.setCurrentDate(resumeDate.time)
            viewModel.loadData(resumeDate)
        }
        super.onResume()
    }

    override fun onPause() {
        viewModel.removeOnPropertyChangedCallback(onPropertyChanged)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }
        if (id == R.id.action_about) {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initView() {

        currentDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            GregorianCalendar(resources.configuration.locales.get(0))
        } else {
            @Suppress("DEPRECATION")
            (GregorianCalendar(resources.configuration.locale))
        }


        viewModel = MainActivityModel()
        viewModel.attach(this)
        binding.model = viewModel

        viewModel.addOnPropertyChangedCallback(onPropertyChanged)

        initCalendar(currentDate)

        if (!viewModel.isDataLoaded) {
            viewModel.loadData(currentDate)
        } else {
            viewModel.notifyChange()
        }

        setSupportActionBar(toolbar)

        modifyLooks()
    }

    private fun modifyLooks() {
        toolbar.title = null
    }

    private fun updateView() {
    }

    private fun initCalendar(calendar: GregorianCalendar) {
        eventsCalendar.setFirstDayOfWeek(Calendar.MONDAY)
        eventsCalendar.setListener(viewModel.compactCalendarViewListener)
        eventsCalendar.setCurrentDate(calendar.time)
    }


}
