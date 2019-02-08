package eu.morningbird.czyhandlowa.model

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

import android.graphics.Color
import android.view.View
import android.view.animation.AnimationUtils
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import eu.morningbird.czyhandlowa.BR
import eu.morningbird.czyhandlowa.R
import eu.morningbird.czyhandlowa.util.ShoppingSunday
import eu.morningbird.czyhandlowa.view.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.locks.ReentrantLock


@BindingAdapter("animation")
fun View.setAnimation(isAnimation: Boolean) {
    if (isAnimation) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.shake)
        this.startAnimation(animation)
    } else {
        this.clearAnimation()
    }
}

@BindingAdapter("events")
fun CompactCalendarView.setEvents(calendar: GregorianCalendar) {
    this.removeAllEvents()
    this.addEvents(MainActivityModel.getClosedSundaysEvents(calendar))
    this.addEvents(MainActivityModel.getOpenSundaysEvents(calendar))
}

@BindingAdapter("dateSelectedDayBackgroundColor")
fun CompactCalendarView.setDateSelectedDayBackgroundColor(calendar: GregorianCalendar) {
    this.setCurrentSelectedDayBackgroundColor(MainActivityModel.getDayBackgroundColor(calendar))
}

@BindingAdapter("dateCurrentDayBackgroundColor")
fun CompactCalendarView.setDateCurrentDayBackgroundColor(calendar: GregorianCalendar) {
    this.setCurrentDayBackgroundColor(MainActivityModel.getDayBackgroundColor(calendar))
}


class MainActivityModel : BaseObservable() {

    @Bindable
    var calendarDate: GregorianCalendar = GregorianCalendar()
        private set(value) {
            field = value
            if (isDataLoaded) notifyPropertyChanged(BR.calendarDate)
            if (isDataLoaded) notifyPropertyChanged(BR.month)
        }

    @Bindable
    var selectedDayDate: GregorianCalendar = GregorianCalendar()
        private set(value) {
            field = value
            if (isDataLoaded) notifyPropertyChanged(BR.selectedDayDate)
        }

    var compactCalendarViewListener = object : CompactCalendarView.CompactCalendarViewListener {
        override fun onDayClick(dateClicked: Date) {
            selectedDayDate.time = dateClicked
            notifyPropertyChanged(BR.selectedDayDate)
        }

        override fun onMonthScroll(firstDayOfNewMonth: Date) {
            calendarDate.time = firstDayOfNewMonth
            selectedDayDate.time = firstDayOfNewMonth
            notifyPropertyChanged(BR.calendarDate)
            notifyPropertyChanged(BR.selectedDayDate)
            notifyPropertyChanged(BR.month)

        }
    }

    private var context: MainActivity? = null


    @Volatile
    var isDataLoaded: Boolean = false
        private set

    private val lock = ReentrantLock()

    val month: String
        @Bindable get() {
            if (calendarDate.isSet(Calendar.MONTH)) return monthDateFormat.format(calendarDate.time).capitalize()
            return ""
        }


    private val monthDateFormat = SimpleDateFormat("LLLL", Locale.getDefault())


    @Bindable
    var shopsOpen: Boolean = false
        private set(value) {
            field = value
            if (isDataLoaded) notifyPropertyChanged(BR.shopsOpen)
        }

    fun attach(context: MainActivity) {
        this.context = context
    }

    fun calendarLeftButtonOnClick(view: View) {
        (view.context as MainActivity).eventsCalendar.scrollLeft()
    }

    fun calendarRightButtonOnClick(view: View) {
        (view.context as MainActivity).eventsCalendar.scrollRight()
    }

    fun loadData(calendar: GregorianCalendar) {
        val runnable = Runnable {
            lock.lock()
            try {
                shopsOpen = ShoppingSunday.areShopsOpen(calendar)
                calendarDate = calendar
                isDataLoaded = true
                notifyChange()
            } finally {
                lock.unlock()
            }
        }
        Thread(runnable).start()
    }

    companion object {
        fun getClosedSundaysEvents(calendar: GregorianCalendar): List<Event> {
            val closedSundays = ShoppingSunday.getClosedSundaysInMonth(calendar)
            val closedSundaysEvents = ArrayList<Event>()
            for (sunday in closedSundays) {
                closedSundaysEvents.add(Event(Color.DKGRAY, sunday.timeInMillis))
            }
            return closedSundaysEvents
        }

        fun getOpenSundaysEvents(calendar: GregorianCalendar): List<Event> {
            val openSundays = ShoppingSunday.getOpenSundaysInMonth(calendar)
            val openSundaysEvents = ArrayList<Event>()
            for (sunday in openSundays) {
                openSundaysEvents.add(Event(Color.RED, sunday.timeInMillis))
            }
            return openSundaysEvents
        }

        fun getDayBackgroundColor(calendar: GregorianCalendar): Int {
            return when {
                calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY -> Color.TRANSPARENT
                ShoppingSunday.areShopsOpen(calendar) -> Color.RED
                else -> Color.DKGRAY
            }
        }

    }
}



