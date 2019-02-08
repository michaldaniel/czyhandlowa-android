package eu.morningbird.czyhandlowa.util

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

import java.util.*
import kotlin.collections.ArrayList


object ShoppingSunday {

    private const val NOT_SUNDAY = "Is not Sunday"

    @Throws(IllegalArgumentException::class)
    private fun getEaster(year: Int): GregorianCalendar {

        if (year <= 1582) {
            throw IllegalArgumentException("Algorithm invalid before April 1583")
        }

        val a = year % 19
        val b = year / 100
        val c = year % 100
        val d = b / 4
        val e = b % 4
        val f = (b + 8) / 25
        val g = (b - f + 1) / 3
        val h = (19 * a + b - d - g + 15) % 30
        val i = c / 4
        val k = c % 4
        val l = (32 + 2 * e + 2 * i - h - k) % 7
        val m = (a + 11 * h + 22 * l) / 451
        val n = h + l - 7 * m + 114
        val month = n / 31 - 1  // months indexed from 0
        val day = (n % 31) + 1

        return GregorianCalendar(year, month, day)
    }

    private fun getChristmas(year: Int): GregorianCalendar {
        return GregorianCalendar(year, Calendar.DECEMBER, 25)
    }

    private fun getIndependenceDay(year: Int): Calendar? {
        return GregorianCalendar(year, Calendar.NOVEMBER, 11)
    }

    @Throws(IllegalArgumentException::class)
    private fun getPentecost(year: Int): GregorianCalendar {
        val calendar = getEaster(year)
        calendar.add(Calendar.DATE, 49)
        return calendar
    }

    private fun getCurrentSunday(date: GregorianCalendar): GregorianCalendar {
        while (date.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            date.add(Calendar.DATE, 1)
        }
        return date
    }

    private fun getPreviousSunday(date: GregorianCalendar): GregorianCalendar {
        date.add(Calendar.DATE, -1)
        while (date.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            date.add(Calendar.DATE, -1)
        }
        return date
    }

    private fun getAllSundaysInAMonth(date: GregorianCalendar): List<GregorianCalendar> {
        val month = date.get(Calendar.MONTH)
        val calendar = GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), 1)
        val sundays = ArrayList<GregorianCalendar>()
        do {
            val day = calendar.get(Calendar.DAY_OF_WEEK)
            if (day == Calendar.SUNDAY) {
                sundays.add(
                    GregorianCalendar(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                )
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        } while (calendar.get(Calendar.MONTH) == month)
        return sundays
    }

    @Throws(IllegalArgumentException::class)
    private fun isFirst(sunday: GregorianCalendar): Boolean {
        if (sunday.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            throw IllegalArgumentException(NOT_SUNDAY)
        }
        val checkDate = GregorianCalendar(sunday.get(Calendar.YEAR), sunday.get(Calendar.MONTH), 1)
        while (checkDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            checkDate.add(Calendar.DATE, 1)
        }
        return checkDate.compareTo(sunday) == 0
    }

    @Throws(IllegalArgumentException::class)
    private fun isLast(sunday: GregorianCalendar): Boolean {
        if (sunday.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            throw IllegalArgumentException(NOT_SUNDAY)
        }
        val checkDate = GregorianCalendar(
            sunday.get(Calendar.YEAR),
            sunday.get(Calendar.MONTH),
            sunday.getActualMaximum(Calendar.DAY_OF_MONTH)
        )
        while (checkDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            checkDate.add(Calendar.DATE, -1)
        }
        return checkDate.compareTo(sunday) == 0
    }

    @Throws(IllegalArgumentException::class)
    private fun isNationalHoliday(sunday: GregorianCalendar): Boolean {
        if (sunday.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            throw IllegalArgumentException(NOT_SUNDAY)
        }
        if (sunday.compareTo(getChristmas(sunday.get(Calendar.YEAR))) == 0) return true
        if (sunday.compareTo(getPentecost(sunday.get(Calendar.YEAR))) == 0) return true
        if (sunday.compareTo(getEaster(sunday.get(Calendar.YEAR))) == 0) return true
        if (sunday.compareTo(getIndependenceDay(sunday.get(Calendar.YEAR))) == 0) return true

        return false
    }


    @Throws(IllegalArgumentException::class)
    private fun isBeforeChristmas(sunday: GregorianCalendar): Boolean {
        if (sunday.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            throw IllegalArgumentException(NOT_SUNDAY)
        }
        val christmas = getChristmas(sunday.get(Calendar.YEAR))
        val weekBefore = getChristmas(sunday.get(Calendar.YEAR))
        weekBefore.add(Calendar.DATE, -7)
        if (getPreviousSunday(christmas).compareTo(sunday) == 0) {
            return true
        }
        if (getPreviousSunday(weekBefore).compareTo(sunday) == 0) {
            return true
        }
        return false
    }

    @Throws(IllegalArgumentException::class)
    private fun isBeforeEaster(sunday: GregorianCalendar): Boolean {
        if (sunday.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            throw IllegalArgumentException(NOT_SUNDAY)
        }
        val easter = getEaster(sunday.get(Calendar.YEAR))
        return getPreviousSunday(easter).compareTo(sunday) == 0
    }

    @Throws(IllegalArgumentException::class)
    private fun checkForYear(sunday: GregorianCalendar): Boolean {
        if (sunday.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            throw IllegalArgumentException(NOT_SUNDAY)
        }
        if (sunday.get(Calendar.YEAR) < 2018) {
            return true
        }

        if (sunday.get(Calendar.YEAR) == 2018) {
            if (sunday.get(Calendar.MONTH) > Calendar.FEBRUARY) {
                if (isFirst(sunday)) {
                    return true
                }
                if (isLast(sunday)) {
                    return true
                }
                return false
            }
            return true
        }

        if (sunday.get(Calendar.YEAR) == 2019) {
            if (isLast(sunday)) {
                return true
            }
            return false
        }

        val month = sunday.get(Calendar.MONTH)
        if ((month == Calendar.JANUARY || month == Calendar.APRIL || month == Calendar.JUNE || month == Calendar.AUGUST) && isLast(
                sunday
            )
        ) {
            return true
        }

        return false
    }

    @Throws(IllegalArgumentException::class)
    private fun checkForHolidays(sunday: GregorianCalendar): Boolean {
        if (sunday.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            throw IllegalArgumentException(NOT_SUNDAY)
        }
        if (isBeforeChristmas(sunday)) {
            return true
        }
        if (isBeforeEaster(sunday)) {
            return true
        }
        return false
    }

    @Throws(IllegalArgumentException::class)
    private fun checkForExceptions(sunday: GregorianCalendar): Boolean {
        if (sunday.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            throw IllegalArgumentException(NOT_SUNDAY)
        }
        if (isNationalHoliday(sunday)) {
            return false
        }
        return true
    }


    fun areShopsOpen(date: GregorianCalendar): Boolean {
        val today = GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE))
        val sunday = getCurrentSunday(today)
        return (checkForYear(sunday) || checkForHolidays(sunday)) && checkForExceptions(sunday)
    }

    fun getOpenSundaysInMonth(date: GregorianCalendar): List<GregorianCalendar> {
        val sundays = getAllSundaysInAMonth(date)
        val openSundays = ArrayList<GregorianCalendar>()
        for (sunday in sundays) {
            if (areShopsOpen(sunday)) openSundays.add(sunday)
        }
        return openSundays
    }

    fun getClosedSundaysInMonth(date: GregorianCalendar): List<GregorianCalendar> {
        val sundays = getAllSundaysInAMonth(date)
        val closedSundays = ArrayList<GregorianCalendar>()
        for (sunday in sundays) {
            if (!areShopsOpen(sunday)) closedSundays.add(sunday)
        }
        return closedSundays
    }

}